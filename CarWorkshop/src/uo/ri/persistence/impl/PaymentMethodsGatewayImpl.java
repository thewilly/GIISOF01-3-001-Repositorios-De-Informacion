/*******************************************************************************
 * MIT License
 * 
 * Copyright (c) 2018 Guillermo Facundo Colunga
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
package uo.ri.persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import alb.util.jdbc.Jdbc;
import uo.ri.common.BusinessException;
import uo.ri.conf.Conf;
import uo.ri.persistence.PaymentMethodsGateway;

/**
 * PaymentMethodsGatewayImpl.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class PaymentMethodsGatewayImpl implements PaymentMethodsGateway {

	private Connection connection;
	private PreparedStatement pst = null;
	private ResultSet rs = null;

	@Override
	public void setConnection( Connection c ) {
		this.connection = c;
	}

	/**
	 * This method finds all the bonus of the system, and returns the
	 * information within the dni and the name of the owners
	 * 
	 * @return a list of maps with all the information of all the bonus in the
	 *         database
	 */
	@Override
	public List<Map<String, Object>> findAllBonds() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			pst = connection.prepareStatement( Conf.get( "SQL_FIND_ALL_BONOS" ) );
			rs = pst.executeQuery();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put( "dni", rs.getString( 1 ) );
				map.put( "name", rs.getString( 2 ) );
				map.put( "numeroTotal", rs.getInt( 3 ) );
				map.put( "importeTotal", rs.getDouble( 4 ) );
				map.put( "consumido", rs.getDouble( 5 ) );
				map.put( "porConsumir", rs.getDouble( 6 ) );
				list.add( map );
			}
			return list;
		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {
			Jdbc.close( rs, pst, connection );
		}
	}

	/**
	 * This method finds all the bonus of the client and returns them into a
	 * list of maps form. The last map of this list will be a summary of the
	 * bonus
	 * 
	 * @param id the id of the client to search for the bonus
	 * @return the list of maps with all the information about the bonus of the
	 *         client
	 * @throws BusinessException
	 */
	@Override
	public List<Map<String, Object>> findAllBondsByClientId( long id ) throws BusinessException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			pst = connection.prepareStatement( Conf.get( "SQL_FIND_BONOS_CLIENTE" ) );
			pst.setLong( 1, id );
			rs = pst.executeQuery();
			while (rs.next()) {
				list.add( loadBono( rs ) );
			}
			list.add( summaryBonoCliente( id ) );
			return list;
		} catch (SQLException e) {
			throw new BusinessException( e );
		} finally {
			Jdbc.close( rs, pst, connection );
		}
	}

	/**
	 * This method gets the result set of the query to obtain all the bonus of
	 * the client and transform it to a map
	 * 
	 * @param rs the result set
	 * @return the map of each bonus of the client
	 */
	private Map<String, Object> loadBono( ResultSet rs ) {
		Map<String, Object> map = new HashMap<>();
		try {
			map.put( "codigo", rs.getString( 1 ) );
			map.put( "acumulado", rs.getDouble( 2 ) );
			map.put( "disponible", rs.getDouble( 3 ) );
			map.put( "descripcion", rs.getString( 4 ) );
		} catch (SQLException e) {
			throw new RuntimeException( e );
		}
		return map;
	}

	/**
	 * This method gives back a map with the summary of the information of the
	 * bonus of the client
	 * 
	 * @param id the id of the client
	 * @return a map with the summary of all the bonus of the client
	 */
	private Map<String, Object> summaryBonoCliente( long id ) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			pst = connection.prepareStatement( Conf.get( "SQL_RESUMEN_BONOS_CLIENTE" ) );
			pst.setLong( 1, id );
			rs = pst.executeQuery();
			while (rs.next()) {
				map.put( "bonos", rs.getInt( 1 ) );
				map.put( "total", rs.getDouble( 2 ) );
				map.put( "porConsumir", rs.getDouble( 3 ) );
				map.put( "consum", rs.getDouble( 4 ) );
			}
			return map;
		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {
			Jdbc.close( rs, pst );
		}
	}

	/**
	 * This method generate all the bonus from a client by recommendation. The
	 * client must have had a breakdown payed at least.
	 * 
	 * @throws BusinessException
	 */
	@Override
	public void generateBonosByRecomendation() throws BusinessException {
		try {
			pst = connection.prepareStatement( Conf.get( "SQL_GET_RECOMENDADORES" ) );
			rs = pst.executeQuery();
			Map<Long, Integer> map = new HashMap<Long, Integer>();
			while (rs.next()) {
				map.put( rs.getLong( 1 ), rs.getInt( 2 ) );
			}
			map = selectRecomendator( map );
			createBonoForClient( map );
		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {
			Jdbc.close( rs, pst, connection );
		}
	}

	/**
	 * This method select those ones who recommended people whose number of
	 * recommendations has been 3, or a multiple of 3, and add them into a map
	 * 
	 * @param map map of clients
	 * @return the map with all the id's of the clients who have recommended 3
	 *         or more people
	 */
	private Map<Long, Integer> selectRecomendator( Map<Long, Integer> map ) {
		Map<Long, Integer> mapAux = new HashMap<Long, Integer>();
		Iterator<Entry<Long, Integer>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Long, Integer> pair = (Map.Entry<Long, Integer>) it.next();
			long id = pair.getKey();
			int numRecomendations = pair.getValue();
			if (numRecomendations % 3 == 0) {
				mapAux.put( id, numRecomendations );
			} else {
				int rest = numRecomendations / 3;
				mapAux.put( id, ( rest * 3 ) );
			}
		}
		return mapAux;
	}

	/**
	 * This method creates a bonus for a each client
	 * 
	 * @param map
	 * @throws BusinessException
	 */
	private void createBonoForClient( Map<Long, Integer> map ) throws BusinessException {
		Iterator<Entry<Long, Integer>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Long, Integer> pair = (Map.Entry<Long, Integer>) it.next();
			long id = pair.getKey();
			int numRecomendations = pair.getValue();
			int numBonos = numRecomendations / 3;
			for (int i = 0; i < numBonos; i++) {
				createBond( id, 25, "Por recomendación" );
			}
			if (numRecomendations > 0) {
				updateClientsBono( id, numRecomendations );
			}
		}
	}

	/**
	 * This method update the usado_bono field of the tclients table, so it is
	 * marked as used
	 * 
	 * @param id the id of the client who has recommended
	 * @param num the number of people that had been recommended
	 */
	private void updateClientsBono( long id, int num ) {
		Connection c = connection;
		PreparedStatement p = null;
		try {
			p = c.prepareStatement( Conf.get( "SQL_UPDATE_CLIENTS_BONO" ) );
			p.setLong( 1, id );
			p.setInt( 2, num );
			p.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {
			Jdbc.close( p );
		}
	}

	/**
	 * This method creates a bonus for the specified id of the client, with the
	 * quantity of money and the description defined by the parameters
	 * 
	 * @param id the id of the client whom the bonus will be assigned
	 * @param disponible the quantity of money to put
	 * @param descripcion the description of the bonus
	 * @throws BusinessException
	 */
	public void createBond( long id, double disponible, String descripcion )
			throws BusinessException {
		try {
			pst = connection.prepareStatement( Conf.get( "SQL_CREATE_BONO" ) );
			pst.setString( 1, "TBonos" );
			pst.setDouble( 2, 0 );
			pst.setDouble( 3, disponible );
			pst.setLong( 4, id );
			pst.setString( 5, descripcion );
			pst.setString( 6, getGeneratedKey() );
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new BusinessException( e );
		} finally {
			Jdbc.close( pst );
		}
	}

	/**
	 * This method looks for the last code of a bonus in the database and
	 * generates the new one
	 * 
	 * @return the new code, which is the last one + 10
	 */
	private String getGeneratedKey() {
		String code = "";
		PreparedStatement p = null;
		try {
			p = connection.prepareStatement( Conf.get( "SQL_ULTIMO_CODIGO_BONO" ) );
			rs = p.executeQuery();
			while (rs.next()) {
				code = rs.getString( 1 );
			}
		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {
			Jdbc.close( rs, p );
		}
		return createCodeOfBono( code );
	}

	/**
	 * This method takes the last generated code of a bonus in the database, and
	 * adds 10 to it
	 * 
	 * @param code the last code of the database
	 * @return the new code to be added
	 */
	private String createCodeOfBono( String code ) {
		String original = code;
		String value = "";
		int aux = Integer.parseInt( original.substring( 1 ) );
		aux += 10;
		value = "B" + aux;
		return value;
	}

	/**
	 * This method finds and returns a list of all the possible ways of payment
	 * the client has registered in the system
	 * 
	 * @param id the id of the client
	 * @return a list of maps with all the ways of payment of the client
	 *         specified
	 * @throws BusinessException
	 */
	@Override
	public List<Map<String, Object>> findAllPaymentMethodsByClientId( Long id )
			throws BusinessException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			pst = connection.prepareStatement( Conf.get( "SQL_FIND_ALL_MEDIOS_PAGO" ) );
			pst.setLong( 1, id );
			rs = pst.executeQuery();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put( "tipo", rs.getString( 1 ) );
				map.put( "acumulado", rs.getDouble( 2 ) );
				map.put( "id", rs.getInt( 3 ) );
				map.put( "TBonoCodigo", rs.getString( 4 ) );
				map.put( "TBonoDisponible", rs.getDouble( 5 ) );
				map.put( "TTarjetasCreditoNumero", rs.getString( 6 ) );
				map.put( "TBonoDescripcion", rs.getString( 7 ) );
				list.add( map );
			}
			return list;
		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {
			Jdbc.close( rs, pst, connection );
		}
	}

	/**
	 * This method deletes a way of payment defined by the @param. This one,
	 * must have been not used in any payment, or it will not be removed.If this
	 * one corresponds to a cash type, it will throw an exception.
	 * 
	 * @param id the id of the way of payment to be deleted
	 * @throws BusinessException
	 */
	@Override
	public void removePaymentMethodById( Long id ) throws BusinessException {
		try {
			if (!medioPagoIdIsCash( id )) {
				pst = connection.prepareStatement( Conf.get( "SQL_DELETE_MEDIO_PAGO" ) );
				pst.setLong( 1, id );
				pst.executeUpdate();
			} else {
				throw new BusinessException( "No se puede dar de baja un pago en metálico" );
			}
		} catch (SQLException e) {
			throw new BusinessException( e );
		} finally {
			Jdbc.close( rs, pst, connection );
		}
	}

	/**
	 * This method checks if the id of the type of pay is cash, or not in the
	 * database
	 * 
	 * @param id the id of the type of pay that is being checked
	 * @return true if the type of pay is cash, false otherwise
	 */
	private boolean medioPagoIdIsCash( Long id ) {
		try {
			pst = connection.prepareStatement( Conf.get( "SQL_CHECK_IF_CASH" ) );
			pst.setLong( 1, id );
			rs = pst.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {
			Jdbc.close( rs, pst );
		}
	}

	/**
	 * This method adds a new credit card to a defined client, with the
	 * corresponding information this new way of payment needs
	 * 
	 * @param id the id of the client
	 * @param tipo type of the credit card(visa, master card,...)
	 * @param numero the number of the credit card
	 * @param fecha the date in which the validity of the credit card expires
	 * @throws BusinessException
	 */
	@Override
	public void createPMCard( Long id, String tipo, String numero, Timestamp fecha )
			throws BusinessException {
		try {
			if (!numTarjetaExists( numero )) {
				pst = connection.prepareStatement( Conf.get( "SQL_CREATE_TARJETA_CREDITO" ) );
				pst.setString( 1, "TTarjetasCredito" );
				pst.setString( 2, tipo );
				pst.setString( 3, numero );
				pst.setTimestamp( 4, fecha );
				pst.setLong( 5, id );
				pst.setDouble( 6, 0 );
				pst.executeUpdate();
			} else {
				throw new BusinessException( "El número de la tarjeta ya existe" );
			}
		} catch (SQLException e) {
			throw new BusinessException( e );
		} finally {
			Jdbc.close( rs, pst, connection );
		}
	}

	/**
	 * This method checks if the number passed as a parameter(number of the
	 * credit card) already exists in the database, and returns true if it does,
	 * false otherwise
	 * 
	 * @param num the number of the credit card
	 * @return true if the number already exists, false otherwise
	 */
	private boolean numTarjetaExists( String num ) {
		Connection c = connection;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			p = c.prepareStatement( Conf.get( "SQL_CHECK_NUM_TARJETA_EXISTS" ) );
			p.setString( 1, num );
			r = p.executeQuery();
			return r.next();
		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {
			Jdbc.close( r, p );
		}
	}
}
