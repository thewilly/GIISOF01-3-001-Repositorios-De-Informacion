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
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package uo.ri.persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bin.alb.util.jdbc.Jdbc;
import uo.ri.common.BusinessException;
import uo.ri.conf.Conf;
import uo.ri.persistence.ClientesGateway;

public class ClientesGatewayImpl implements ClientesGateway {

	Connection conection = null;
	PreparedStatement pst = null;
	ResultSet rs = null;

	@Override
	public void setConnection( Connection conection ) {
		this.conection = conection;

	}

	@Override
	public void save( String dni, String nombre, String apellidos, int zipcode, int telefono,
			String correo )
			throws BusinessException {

		if (!existDni( dni )) {

			try {
				pst = conection.prepareStatement( Conf.get( "SQL_INSERT_CLIENT" ) );

				pst.setString( 1, dni );
				pst.setString( 2, nombre );
				pst.setString( 3, apellidos );
				pst.setInt( 4, zipcode );
				pst.setInt( 5, telefono );
				pst.setString( 6, correo );

				pst.executeUpdate();

				insertMedioPago( getIdCliente( dni ) );

			} catch (SQLException e) {
				throw new BusinessException( "Error añadiendo un cliente sin recomendación" );
			} finally {
				Jdbc.close( pst );
			}
		} else {
			throw new BusinessException( "El dni que se desea registrar ya existe" );
		}

	}

	@Override
	public void saveWithRecomendator( String dni, String nombre, String apellidos, int zipcode,
			int telefono,
			String correo, Long idRecomendador ) throws BusinessException {

		if (!existDni( dni )) {
			if (existRecomendador( idRecomendador )) {
				if (facturasPagadas( idRecomendador ) > 0) {

					try {
						pst = conection.prepareStatement(
								Conf.get( "SQL_INSERT_CLIENT_WITH_RECOMENDATOR" ) );

						pst.setString( 1, dni );
						pst.setString( 2, nombre );
						pst.setString( 3, apellidos );
						pst.setInt( 4, zipcode );
						pst.setInt( 5, telefono );
						pst.setString( 6, correo );
						pst.setLong( 7, idRecomendador );

						pst.executeUpdate();

						insertMedioPago( getIdCliente( dni ) );

					} catch (SQLException e) {
						throw new BusinessException(
								"Error añadiendo un cliente con recomendación" );
					} finally {
						Jdbc.close( pst );
					}
				} else {
					throw new BusinessException(
							"El recomendador necesita tener al menos 1 factura pagada" );
				}
			} else {
				throw new BusinessException( "El id del recomendador no existe" );
			}
		} else {
			throw new BusinessException( "El dni que se desea registrar ya existe" );
		}

	}

	@Override
	public void delete( long idClient ) throws BusinessException {

		if (existIdCliente( idClient )) {

			if (getNumberOfVehiculos( idClient ) == 0) {

				try {
					deleteMedioPago( idClient );

					pst = conection.prepareStatement( Conf.get( "SQL_DELETE_CLIENT" ) );

					pst.setLong( 1, idClient );

					pst.executeUpdate();

				} catch (SQLException e) {
					throw new BusinessException( "Error eliminando un cliente" );
				} finally {
					Jdbc.close( pst );
				}
			} else {
				throw new BusinessException(
						"No ha sido posible eliminar dicho cliente ya que dispone de vehículos registrados" );
			}
		} else {
			throw new BusinessException( "El id del cliente introducido no existe" );
		}

	}

	@Override
	public void update( long idClient, String dni, String nombre, String apellidos, int zipcode,
			int telefono,
			String correo ) throws BusinessException {

		if (existIdCliente( idClient )) {

			try {

				pst = conection.prepareStatement( Conf.get( "SQL_UPDATE_CLIENT" ) );
				pst.setString( 1, nombre );
				pst.setString( 2, apellidos );
				pst.setString( 3, dni );
				pst.setInt( 4, zipcode );
				pst.setInt( 5, telefono );
				pst.setString( 6, correo );
				pst.setLong( 7, idClient );

				pst.executeUpdate();

			} catch (SQLException e) {
				throw new BusinessException( "Error actualizando un cliente" );
			} finally {
				Jdbc.close( pst );
			}
		} else {
			throw new BusinessException( "El id del cliente introducido no existe" );
		}

	}

	@Override
	public String showClient( long idClient ) throws BusinessException {

		if (existIdCliente( idClient )) {

			String client = "";

			try {

				pst = conection.prepareStatement( Conf.get( "SQL_SHOW_CLIENT" ) );
				pst.setLong( 1, idClient );

				rs = pst.executeQuery();

				while (rs.next()) {
					client = "Dni: ";
					client += rs.getString( "dni" );
					client += "\t Nombre: ";
					client += rs.getString( "nombre" );
					client += "\t Apellidos: ";
					client += rs.getString( "apellidos" );
					client += "\t Zipcode: ";
					client += rs.getInt( "zipcode" );
					client += "\t Telefono ";
					client += rs.getInt( "telefono" );
					client += "\t Correo: ";
					client += rs.getString( "email" );
				}

			} catch (SQLException e) {
				throw new BusinessException( "Error mostrando un cliente" );
			} finally {
				Jdbc.close( rs, pst );
			}
			return client;
		} else {
			throw new BusinessException( "El id del cliente introducido no existe" );
		}

	}

	@Override
	public List<Map<String, Object>> findAllClients() throws BusinessException {

		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();

		try {

			pst = conection.prepareStatement( Conf.get( "SQL_FIND_ALL_CLIENTS" ) );

			rs = pst.executeQuery();
			while (rs.next()) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put( "id", rs.getInt( "id" ) );
				m.put( "nombre", rs.getString( "nombre" ) );
				m.put( "apellidos", rs.getString( "apellidos" ) );
				map.add( m );

			}

		} catch (SQLException e) {
			throw new BusinessException( "Error buscando todos los clientes" );
		} finally {
			Jdbc.close( rs, pst );
		}

		return map;
	}

	@Override
	public List<Long> findAllClientsId() throws BusinessException {

		List<Long> ids = new ArrayList<Long>();

		try {

			pst = conection.prepareStatement( Conf.get( "SQL_FIND_ALL_CLIENTS_ID" ) );

			rs = pst.executeQuery();
			while (rs.next()) {
				ids.add( rs.getLong( "id" ) );
			}

		} catch (SQLException e) {
			throw new BusinessException( "Error buscando todos los ids de los clientes" );
		} finally {
			Jdbc.close( rs, pst );
		}
		return ids;
	}

	@Override
	public List<Map<String, Object>> findAllClientsByRecomendator( long idRecomendator )
			throws BusinessException {

		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();

		try {

			pst = conection.prepareStatement( Conf.get( "SQL_FIND_ALL_CLIENTS_BY_RECOMENDATOR" ) );

			pst.setLong( 1, idRecomendator );

			rs = pst.executeQuery();
			while (rs.next()) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put( "id", rs.getInt( "id" ) );
				m.put( "nombre", rs.getString( "nombre" ) );
				m.put( "apellidos", rs.getString( "apellidos" ) );
				map.add( m );

			}

		} catch (SQLException e) {
			throw new BusinessException( "Error buscando todos los clientes recomendados" );
		} finally {
			Jdbc.close( rs, pst );
		}

		return map;
	}

	private List<String> findAllClientsDni() throws BusinessException {

		List<String> map = new ArrayList<String>();

		try {

			pst = conection.prepareStatement( Conf.get( "SQL_FIND_ALL_CLIENTS_DNI" ) );

			rs = pst.executeQuery();
			while (rs.next()) {
				map.add( rs.getString( "dni" ) );

			}

		} catch (SQLException e) {
			throw new BusinessException( "Error buscando todos los clientes recomendados" );
		} finally {
			Jdbc.close( rs, pst );
		}

		return map;
	}

	private void insertMedioPago( Long idClient ) throws BusinessException {

		String dType = "TMetalico";
		int acumulado = 0;

		try {

			pst = conection.prepareStatement( Conf.get( "SQL_INSERT_MEDIOPAGO" ) );

			pst.setString( 1, dType );
			pst.setInt( 2, acumulado );
			pst.setLong( 3, idClient );

			pst.executeUpdate();

		} catch (SQLException e) {
			throw new BusinessException(
					"Error insertando un medio de pago de tipo metálico a un nuevo cliente" );
		} finally {
			Jdbc.close( pst );
		}

	}

	private void deleteMedioPago( Long idClient ) throws BusinessException {

		long idMedioPago = getIDMedioPago( idClient );

		try {

			pst = conection.prepareStatement( Conf.get( "SQL_DELETE_MEDIOPAGO" ) );

			pst.setLong( 1, idMedioPago );

			pst.executeUpdate();

		} catch (SQLException e) {
			throw new BusinessException(
					"Error eliminando el medio de pago de tipo metálico del cliente" );
		} finally {
			Jdbc.close( pst );
		}

	}

	private int getNumberOfVehiculos( Long idClient ) throws BusinessException {

		int cont = 0;

		try {

			pst = conection.prepareStatement( Conf.get( "SQL_FIND_VEHICULOS_BY_ID_CLIENTE" ) );

			pst.setLong( 1, idClient );

			rs = pst.executeQuery();
			while (rs.next()) {
				cont++;
			}

		} catch (SQLException e) {
			throw new BusinessException( "Error buscando los vehiculos de un cliente" );
		} finally {
			Jdbc.close( rs, pst );
		}
		return cont;
	}

	private long getIdCliente( String dni ) throws BusinessException {

		long idCLient = 0;

		try {

			pst = conection.prepareStatement( Conf.get( "SQL_FIND_ID_CLIENTE" ) );

			pst.setString( 1, dni );

			rs = pst.executeQuery();
			while (rs.next()) {
				idCLient = rs.getLong( "id" );
			}

			return idCLient;

		} catch (SQLException e) {
			throw new BusinessException( "Error buscando los vehiculos de un cliente" );
		} finally {
			Jdbc.close( rs, pst );
		}

	}

	private long getIDMedioPago( Long idCliente ) throws BusinessException {

		long idMedioPago = 0;

		try {

			pst = conection.prepareStatement( Conf.get( "SQL_FIND_ID_MEDIOPAGO" ) );

			pst.setLong( 1, idCliente );

			rs = pst.executeQuery();
			while (rs.next()) {
				idMedioPago = rs.getLong( "id" );
			}

			return idMedioPago;

		} catch (SQLException e) {
			throw new BusinessException( "Error buscando los vehiculos de un cliente" );
		} finally {
			Jdbc.close( rs, pst );
		}

	}

	private boolean existRecomendador( Long idRecomendador ) throws BusinessException {

		boolean exist = false;

		try {

			pst = conection.prepareStatement( Conf.get( "SQL_FIND_ALL_CLIENTS_ID" ) );

			rs = pst.executeQuery();
			while (rs.next()) {
				if (rs.getLong( "id" ) == idRecomendador) {
					exist = true;
				}
			}

			return exist;

		} catch (SQLException e) {
			throw new BusinessException( "Error buscando todos los id de clientes" );
		} finally {
			Jdbc.close( rs, pst );
		}
	}

	/**
	 * Devuelve el número de facturas pagadas de un cliente (en este caso un
	 * recomendador)
	 * 
	 * @param idRecomendador
	 * @return
	 * @throws BusinessException
	 */
	private int facturasPagadas( Long idRecomendador ) throws BusinessException {

		List<Long> idsClientes = new ArrayList<Long>();
		List<Long> idsMediospago = null;

		int numFacturas = 0;

		try {

			pst = conection.prepareStatement( Conf.get( "SQL_FIND_ALL_CLIENTS_ID" ) );

			rs = pst.executeQuery();
			while (rs.next()) {
				idsClientes.add( rs.getLong( "id" ) );
			}

			idsMediospago = new ArrayList<Long>();

			pst = conection.prepareStatement( Conf.get( "SQL_FIND_ALL_MEDIOSPAGO_BY_ID_CLIENTE" ) );

			pst.setLong( 1, idRecomendador );

			rs = pst.executeQuery();
			while (rs.next()) {
				idsMediospago.add( rs.getLong( "id" ) );
			}

			for (Long medioPago : idsMediospago) {

				pst = conection.prepareStatement(
						Conf.get( "SQL_FIND_ALL_FACTURAS_ABONADAS_BY_MEDIOSPAGO_ID" ) );

				pst.setLong( 1, medioPago );

				rs = pst.executeQuery();
				while (rs.next()) {
					numFacturas += rs.getInt( "total" );
				}

			}

			return numFacturas;

		} catch (SQLException e) {
			throw new BusinessException(
					"Error buscando el número de factuas pagadas de un cliente" );
		} finally {
			Jdbc.close( rs, pst );
		}

	}

	/**
	 * Determina si el dni que se le pasa como parámetro ya existe.
	 * 
	 * @param dni
	 * @return True si existe, false en caso contrario
	 * @throws BusinessException
	 */
	private boolean existDni( String dni ) throws BusinessException {

		List<String> clientes = findAllClientsDni();
		for (String s : clientes) {
			if (s.equals( dni )) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determina si el id de un cliente que se le pasa por parámetro ya existe.
	 * 
	 * @param idCliente
	 * @return True si existe, false en caso contrario
	 * @throws BusinessException
	 */
	private boolean existIdCliente( long idCliente ) throws BusinessException {

		List<Long> clientes = findAllClientsId();
		for (Long l : clientes) {
			if (l == idCliente) {
				return true;
			}
		}
		return false;
	}

}
