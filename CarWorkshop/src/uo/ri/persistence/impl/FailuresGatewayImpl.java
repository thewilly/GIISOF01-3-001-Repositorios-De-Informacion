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
import java.util.List;

import alb.util.jdbc.Jdbc;
import uo.ri.common.BusinessException;
import uo.ri.conf.Conf;
import uo.ri.persistence.FailuresGateway;

/**
 * FailuresGatewayImpl.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class FailuresGatewayImpl implements FailuresGateway {

	private Connection connection;

	public void setConnection( Connection c ) {
		this.connection = c;
	}

	@Override
	public void checkAllFailuresAreFinished( List<Long> idsAveria ) throws BusinessException {
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = connection.prepareStatement( Conf.get( "SQL_VERIFICAR_ESTADO_AVERIA" ) );

			for (Long idAveria : idsAveria) {
				pst.setLong( 1, idAveria );

				rs = pst.executeQuery();
				if (rs.next() == false) {
					throw new BusinessException( "No existe la averia " + idAveria );
				}

				String status = rs.getString( 1 );
				if (!"TERMINADA".equalsIgnoreCase( status )) {
					throw new BusinessException( "No está terminada la avería " + idAveria );
				}
				rs.close();
			}
		} catch (SQLException e) {
			throw new BusinessException( e );
		} finally {
			Jdbc.close( rs, pst );
		}
	}

	@Override
	public double getCostForFailures( List<Long> idsAveria ) throws BusinessException {
		double totalFactura = 0.0;
		for (Long idAveria : idsAveria) {
			double importeManoObra = consultaImporteManoObra( idAveria );
			double importeRepuestos = consultaImporteRepuestos( idAveria );
			double totalAveria = importeManoObra + importeRepuestos;

			actualizarImporteAveria( idAveria, totalAveria );

			totalFactura += totalAveria;
		}
		return totalFactura;
	}

	private void actualizarImporteAveria( Long idAveria, double totalAveria ) {
		PreparedStatement pst = null;

		try {
			pst = connection.prepareStatement( Conf.get( "SQL_UPDATE_IMPORTE_AVERIA" ) );
			pst.setDouble( 1, totalAveria );
			pst.setLong( 2, idAveria );
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {
			Jdbc.close( connection );
			Jdbc.close( pst );
		}
	}

	private double consultaImporteManoObra( Long idAveria ) throws BusinessException {
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = connection.prepareStatement( Conf.get( "SQL_IMPORTE_MANO_OBRA" ) );
			pst.setLong( 1, idAveria );

			rs = pst.executeQuery();
			if (rs.next() == false) {
				throw new BusinessException( "La averia no existe o no se puede facturar" );
			}

			return rs.getDouble( 1 );

		} catch (BusinessException e) {
			throw e;
		} catch (SQLException e) {
			throw new BusinessException( e );
		} finally {
			Jdbc.close( rs, pst, connection );
		}
	}

	private double consultaImporteRepuestos( Long idAveria ) throws BusinessException {
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = connection.prepareStatement( Conf.get( "SQL_IMPORTE_REPUESTOS" ) );
			pst.setLong( 1, idAveria );

			rs = pst.executeQuery();
			if (rs.next() == false) {
				return 0.0; // La averia puede no tener repuestos
			}

			return rs.getDouble( 1 );

		} catch (SQLException e) {
			throw new BusinessException( e );
		} finally {
			Jdbc.close( rs, pst, connection );
		}
	}

	@Override
	public void setFailureStatus( List<Long> idsAveria, String status ) {

		PreparedStatement pst = null;
		try {
			pst = connection.prepareStatement( Conf.get( "SQL_ACTUALIZAR_ESTADO_AVERIA" ) );

			for (Long idAveria : idsAveria) {
				pst.setString( 1, status );
				pst.setLong( 2, idAveria );

				pst.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {
			Jdbc.close( connection );
			Jdbc.close( pst );
		}
	}

	@Override
	public void linkInvoiceWithFailures( long idFactura, List<Long> idsAveria ) {

		PreparedStatement pst = null;
		try {
			pst = connection.prepareStatement( Conf.get( "SQL_VINCULAR_AVERIA_FACTURA" ) );

			for (Long idAveria : idsAveria) {
				pst.setLong( 1, idFactura );
				pst.setLong( 2, idAveria );

				pst.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {
			Jdbc.close( connection );
			Jdbc.close( pst );
		}
	}

}
