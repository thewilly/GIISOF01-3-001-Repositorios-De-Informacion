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
import java.util.List;

import bin.alb.util.jdbc.Jdbc;
import uo.ri.common.BusinessException;
import uo.ri.conf.Conf;
import uo.ri.persistence.AveriasGateway;

public class AveriasGatewayImpl implements AveriasGateway {

	Connection conection = null;
	PreparedStatement pst = null;
	ResultSet rs = null;

	@Override
	public void setConnection( Connection conection ) {
		this.conection = conection;

	}

	@Override
	public void verificarEstadoAveria( List<Long> idsAveria ) throws BusinessException {
		try {

			pst = conection.prepareStatement( Conf.get( "SQL_VERIFICAR_ESTADO_AVERIA" ) );

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
			}

		} catch (SQLException e) {
			throw new BusinessException( "Error verificando el estado de una avería" );
		} finally {
			Jdbc.close( rs, pst );
		}

	}

	@Override
	public void actualizarEstadoAveria( List<Long> idsAveria, String status )
			throws BusinessException {
		try {
			pst = conection.prepareStatement( Conf.get( "SQL_ACTUALIZAR_ESTADO_AVERIA" ) );

			for (Long idAveria : idsAveria) {
				pst.setString( 1, status );
				pst.setLong( 2, idAveria );

				pst.executeUpdate();
			}

		} catch (SQLException e) {
			throw new BusinessException( "Error actualizando el estado de una avería" );
		} finally {
			Jdbc.close( pst );
		}

	}

	@Override
	public void actualizarImporteAveria( Long idAveria, double totalAveria )
			throws BusinessException {
		try {
			pst = conection.prepareStatement( Conf.get( "SQL_UPDATE_IMPORTE_AVERIA" ) );
			pst.setDouble( 1, totalAveria );
			pst.setLong( 2, idAveria );
			pst.executeUpdate();

		} catch (SQLException e) {
			throw new BusinessException( "Error actualizando el importe de una avería" );
		} finally {
			Jdbc.close( pst );
		}

	}

	@Override
	public double importeRepuestos( Long idAveria ) throws BusinessException {
		try {
			pst = conection.prepareStatement( Conf.get( "SQL_IMPORTE_REPUESTOS" ) );
			pst.setLong( 1, idAveria );

			rs = pst.executeQuery();
			if (rs.next() == false) {
				return 0.0;
			}

			return rs.getDouble( 1 );

		} catch (SQLException e) {
			throw new BusinessException( "Error consultando el importe de repuestos" );
		} finally {
			Jdbc.close( rs, pst );
		}
	}

	@Override
	public double importeManoObra( Long idAveria ) throws BusinessException {
		try {
			pst = conection.prepareStatement( Conf.get( "SQL_IMPORTE_MANO_OBRA" ) );
			pst.setLong( 1, idAveria );

			rs = pst.executeQuery();
			if (rs.next() == false) {
				throw new BusinessException( "La averia no existe o no se puede facturar" );
			}

			return rs.getDouble( 1 );

		} catch (SQLException e) {
			throw new BusinessException( "Error consultando el importe de mano de obra" );
		} finally {
			Jdbc.close( rs, pst );
		}
	}

	@Override
	public void insertBonoAveria( Long idAveria ) throws BusinessException {

		try {
			pst = conection.prepareStatement( Conf.get( "SQL_INSERT_BONO_AVERIA" ) );

			pst.setLong( 1, idAveria );

			pst.executeUpdate();

		} catch (SQLException e) {
			throw new BusinessException( "Error actualizando usada_bono en las averias" );
		} finally {
			Jdbc.close( pst );
		}

	}

}
