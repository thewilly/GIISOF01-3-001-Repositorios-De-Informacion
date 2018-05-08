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
import java.util.Date;
import java.util.List;

import bin.alb.util.jdbc.Jdbc;
import uo.ri.common.BusinessException;
import uo.ri.conf.Conf;
import uo.ri.persistence.InvoicesGateway;

public class InvoicesGatewayImpl implements InvoicesGateway {

	Connection conection = null;
	PreparedStatement pst = null;
	ResultSet rs = null;

	@Override
	public void setConnection( Connection conection ) {
		this.conection = conection;

	}

	@Override
	public void linkInvoiceWithFailures( long idFactura, List<Long> idsAveria )
			throws BusinessException {
		try {
			pst = conection.prepareStatement( Conf.get( "SQL_VINCULAR_AVERIA_FACTURA" ) );

			for (Long idAveria : idsAveria) {
				pst.setLong( 1, idFactura );
				pst.setLong( 2, idAveria );

				pst.executeUpdate();
			}

		} catch (SQLException e) {
			throw new BusinessException( "Error vinculando una avería a una factura" );
		} finally {
			Jdbc.close( pst );
		}

	}

	@Override
	public void save( long numeroFactura, Date fechaFactura, double iva, double totalConIva )
			throws BusinessException {
		try {
			pst = conection.prepareStatement( Conf.get( "SQL_INSERTAR_FACTURA" ) );
			pst.setLong( 1, numeroFactura );
			pst.setDate( 2, new java.sql.Date( fechaFactura.getTime() ) );
			pst.setDouble( 3, iva );
			pst.setDouble( 4, totalConIva );
			pst.setString( 5, "SIN_ABONAR" );

			pst.executeUpdate();

		} catch (SQLException e) {
			throw new BusinessException( "Error insertando una factura" );
		} finally {
			Jdbc.close( pst );
		}

	}

	@Override
	public long getGeneratedKey( long numeroFactura ) throws BusinessException {
		try {
			pst = conection.prepareStatement( Conf.get( "SQL_RECUPERAR_CLAVE_GENERADA" ) );
			pst.setLong( 1, numeroFactura );
			rs = pst.executeQuery();
			rs.next();

			return rs.getLong( 1 );

		} catch (SQLException e) {
			throw new BusinessException( "Error recuperando la clave generada" );
		} finally {
			Jdbc.close( rs, pst );
		}
	}

	@Override
	public Long ultimoNumeroFactura() throws BusinessException {
		try {
			pst = conection.prepareStatement( Conf.get( "SQL_ULTIMO_NUMERO_FACTURA" ) );
			rs = pst.executeQuery();

			if (rs.next()) {
				return rs.getLong( 1 ) + 1;
			} else {
				return 1L;
			}

		} catch (SQLException e) {
			throw new BusinessException( "Error devolviendo el último numero de factura" );
		} finally {
			Jdbc.close( rs, pst );
		}
	}

}
