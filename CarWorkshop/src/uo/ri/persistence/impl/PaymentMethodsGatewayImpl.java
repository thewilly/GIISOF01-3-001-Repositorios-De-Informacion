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

import static alb.util.jdbc.Jdbc.*;
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
	public void createBond( long paymentMethodId, double availableAmount, String bondDescription )
			throws BusinessException {
		try {
			pst = connection.prepareStatement( Conf.get( "SQL_CREATE_BONO" ) );
			pst.setString( 1, "TBonos" );
			pst.setDouble( 2, 0 );
			pst.setDouble( 3, availableAmount );
			pst.setLong( 4, paymentMethodId );
			pst.setString( 5, bondDescription );
			pst.setString( 6, getLastGeneratedBondCode() );
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new BusinessException( e );
		} finally {
			close( pst );
		}
	}

	@Override
	public void createPMCard( Long paymentMethodId, String cardKind, String cardNumber,
			Timestamp cardExpirationDate )
			throws BusinessException {
		try {
			if (!isCardNumberInDB( cardNumber )) {
				pst = connection.prepareStatement( Conf.get( "SQL_CREATE_TARJETA_CREDITO" ) );
				pst.setString( 1, "TTarjetasCredito" );
				pst.setString( 2, cardKind );
				pst.setString( 3, cardNumber );
				pst.setTimestamp( 4, cardExpirationDate );
				pst.setLong( 5, paymentMethodId );
				pst.setDouble( 6, 0 );
				pst.executeUpdate();
			} else {
				throw new BusinessException( "El número de la tarjeta ya existe" );
			}
		} catch (SQLException e) {
			throw new BusinessException( e );
		} finally {
			close( rs, pst, connection );
		}
	}

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
			close( rs, pst, connection );
		}
	}

	@Override
	public List<Map<String, Object>> findAllBondsByClientId( long clientId )
			throws BusinessException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			pst = connection.prepareStatement( Conf.get( "SQL_FIND_BONOS_CLIENTE" ) );
			pst.setLong( 1, clientId );
			rs = pst.executeQuery();
			while (rs.next()) {
				list.add( bondResultSetToMap( rs ) );
			}
			list.add( findBondSummaryByClient( clientId ) );
			return list;
		} catch (SQLException e) {
			throw new BusinessException( e );
		} finally {
			close( rs, pst, connection );
		}
	}

	@Override
	public List<Map<String, Object>> findAllPaymentMethodsByClientId( Long clientId )
			throws BusinessException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			pst = connection.prepareStatement( Conf.get( "SQL_FIND_ALL_MEDIOS_PAGO" ) );
			pst.setLong( 1, clientId );
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
			close( rs, pst, connection );
		}
	}

	@Override
	public void generateBondsByRecomendation() throws BusinessException {
		try {
			pst = connection.prepareStatement( Conf.get( "SQL_GET_RECOMENDADORES" ) );
			rs = pst.executeQuery();
			Map<Long, Integer> map = new HashMap<Long, Integer>();
			while (rs.next()) {
				map.put( rs.getLong( 1 ), rs.getInt( 2 ) );
			}
			map = filterRecomendatorsBynumberOfRecommendations( map, 3 );
			createBonoForClient( map, 3 );
		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {
			close( rs, pst, connection );
		}
	}

	@Override
	public void removePaymentMethodById( Long paymentMethodId ) throws BusinessException {
		try {
			if (!isPaymentMethodCash( paymentMethodId )) {
				pst = connection.prepareStatement( Conf.get( "SQL_DELETE_MEDIO_PAGO" ) );
				pst.setLong( 1, paymentMethodId );
				pst.executeUpdate();
			} else {
				throw new BusinessException( "No se puede dar de baja un pago en metálico" );
			}
		} catch (SQLException e) {
			throw new BusinessException( e );
		} finally {
			close( rs, pst, connection );
		}
	}

	@Override
	public void setConnection( Connection connection ) {
		this.connection = connection;
	}

	// PRIVATE MEMBERS

	/**
	 * Creates the corresponding number of bonds for the given map and number of
	 * recommendations per bond.
	 * 
	 * @param recomenderAndRecomendationsMap is the map that contains the
	 *            recommender and the recommendations done.
	 * @throws BusinessException if any error during the method execution.
	 */
	private void createBonoForClient( Map<Long, Integer> recomenderAndRecomendationsMap,
			int numberOfRecommendationsPerBond ) throws BusinessException {
		Iterator<Entry<Long, Integer>> it = recomenderAndRecomendationsMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Long, Integer> pair = (Map.Entry<Long, Integer>) it.next();
			long id = pair.getKey();
			int numRecomendations = pair.getValue();
			int numBonos = numRecomendations / numberOfRecommendationsPerBond;
			for (int i = 0; i < numBonos; i++) {
				createBond( id, 25, "Por recomendación" );
			}
			if (numRecomendations > 0) {
				updateClientsBono( id, numRecomendations );
			}
		}
	}

	/**
	 * Gets the latest generated bond code in DB. Then adds 10 and return the
	 * new bond code.
	 * 
	 * @param lastGeneratedBondCode the last bond code of the database.
	 * @return the new generated bond code. Which is the last one + 10.
	 */
	private String createCodeOfBono( String lastGeneratedBondCode ) {
		String original = lastGeneratedBondCode;
		String value = "";
		int aux = Integer.parseInt( original.substring( 1 ) );
		aux += 10;
		value = "B" + aux;
		return value;
	}

	/**
	 * Gets the latest generated bond code in DB.
	 * 
	 * @return the latest generated bond code in DB.
	 */
	private String getLastGeneratedBondCode() {
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
			close( rs, p );
		}
		return createCodeOfBono( code );
	}

	/**
	 * From a given bond represented by a result set will transform it to a map.
	 * 
	 * @param resultSet the result set.
	 * @return the map of each bonus of the client.
	 */
	private Map<String, Object> bondResultSetToMap( ResultSet resultSet ) {
		Map<String, Object> bondMap = new HashMap<>();
		try {
			bondMap.put( "codigo", resultSet.getString( 1 ) );
			bondMap.put( "acumulado", resultSet.getDouble( 2 ) );
			bondMap.put( "disponible", resultSet.getDouble( 3 ) );
			bondMap.put( "descripcion", resultSet.getString( 4 ) );
		} catch (SQLException e) {
			throw new RuntimeException( e );
		}
		return bondMap;
	}

	/**
	 * Checks if the given payment method is cash.
	 * 
	 * @param paymentMethodId is the unique identifier of the payment method to
	 *            check.
	 * @return true if the payment method is cash, false otherwise.
	 */
	private boolean isPaymentMethodCash( Long paymentMethodId ) {
		try {
			pst = connection.prepareStatement( Conf.get( "SQL_CHECK_IF_CASH" ) );
			pst.setLong( 1, paymentMethodId );
			rs = pst.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {
			close( rs, pst );
		}
	}

	/**
	 * Checks if the given card number is already registered in the DB.
	 * 
	 * @param cardNumber is the number of the credit card.
	 * @return true if the number already exists, false otherwise.
	 */
	private boolean isCardNumberInDB( String cardNumber ) {
		Connection c = connection;
		PreparedStatement p = null;
		ResultSet r = null;

		try {
			p = c.prepareStatement( Conf.get( "SQL_CHECK_NUM_TARJETA_EXISTS" ) );
			p.setString( 1, cardNumber );
			r = p.executeQuery();
			return r.next();
		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {
			close( r, p );
		}
	}

	/**
	 * This method select those ones who recommended people whose number of
	 * recommendations has been 3, or a multiple of 3, and add them into a map.
	 * 
	 * @param recommendatorsAndNumberOfRecommendations map of clients
	 * @return the map with all the id's of the clients who have recommended 3
	 *         or more people
	 */
	private Map<Long, Integer> filterRecomendatorsBynumberOfRecommendations(
			Map<Long, Integer> recommendatorsAndNumberOfRecommendations,
			int numberOfRecommendations ) {
		Map<Long, Integer> filteredMap = new HashMap<Long, Integer>();
		Iterator<Entry<Long, Integer>> it = recommendatorsAndNumberOfRecommendations.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<Long, Integer> pair = (Map.Entry<Long, Integer>) it.next();
			long id = pair.getKey();
			int numRecomendations = pair.getValue();
			if (numRecomendations % numberOfRecommendations == 0) {
				filteredMap.put( id, numRecomendations );
			} else {
				int rest = numRecomendations / numberOfRecommendations;
				filteredMap.put( id, ( rest * numberOfRecommendations ) );
			}
		}
		return filteredMap;
	}

	// PRIVATE MEMBERS

	/**
	 * Finds the bond summary for a given client.
	 * 
	 * @param clientId is the unique id for a client.
	 * @return a map with the summary of all the bonus for the given client.
	 */
	private Map<String, Object> findBondSummaryByClient( long clientId ) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			pst = connection.prepareStatement( Conf.get( "SQL_RESUMEN_BONOS_CLIENTE" ) );
			pst.setLong( 1, clientId );
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
			close( rs, pst );
		}
	}

	/**
	 * Updated the client recommendation table so it is marked as used.
	 * 
	 * @param clientId is the unique id of the client who has recommend.
	 * @param recommendedPeople the number of people that had been recommended.
	 */
	private void updateClientsBono( long clientId, int recommendedPeople ) {
		PreparedStatement pst = null;
		try {
			pst = connection.prepareStatement( Conf.get( "SQL_UPDATE_CLIENTS_BONO" ) );
			pst.setLong( 1, clientId );
			pst.setInt( 2, recommendedPeople );
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {
			close( pst );
		}
	}
}
