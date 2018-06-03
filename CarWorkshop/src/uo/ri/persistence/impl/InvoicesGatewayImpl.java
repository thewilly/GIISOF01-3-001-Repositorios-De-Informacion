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
import java.util.Date;
import java.util.Map;

import alb.util.date.DateUtil;
import alb.util.jdbc.Jdbc;
import uo.ri.conf.Conf;
import uo.ri.persistence.InvoicesGateway;

/**
 * InvoicesGatewayImpl.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class InvoicesGatewayImpl implements InvoicesGateway {

	private Connection connection;

	@Override
	public void setConnection( Connection c ) {
		this.connection = c;
	}

	@Override
	public Long getLastInvoiceNumber() {
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = connection.prepareStatement( Conf.get( "SQL_ULTIMO_NUMERO_FACTURA" ) );
			rs = pst.executeQuery();

			if (rs.next()) {
				return rs.getLong( 1 ) + 1; // +1, el siguiente
			} else { // todavía no hay ninguna
				return 1L;
			}
		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {
			Jdbc.close( rs, pst, connection );
		}
	}

	@Override
	public Long save( Map<String, Object> map ) {
		PreparedStatement pst = null;
		try {
			pst = connection.prepareStatement( Conf.get( "SQL_INSERTAR_FACTURA" ) );
			pst.setLong( 1, (long) map.get( "numFactura" ) );
			pst.setDate( 2, (java.sql.Date) map.get( "fechaFactura" ) );
			pst.setDouble( 3, (double) map.get( "iva" ) );

			pst.setDouble( 4, (double) map.get( "importe" ) );
			pst.setString( 5, "SIN_ABONAR" );

			pst.executeUpdate();

			return getGeneratedKey( (long) map.get( "numFactura" ) ); // Id de
																		// la
																		// nueva
																		// factura
			// generada
		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {
			Jdbc.close( connection );
			Jdbc.close( pst );
		}
	}

	private long getGeneratedKey( long numeroFactura ) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = connection.prepareStatement( Conf.get( "SQL_RECUPERAR_CLAVE_GENERADA" ) );
			pst.setLong( 1, numeroFactura );
			rs = pst.executeQuery();
			rs.next();

			return rs.getLong( 1 );

		} finally {
			Jdbc.close( rs, pst, connection );
		}
	}

	@Override
	public double getTaxes( double totalFactura, Date fechaFactura ) {
		return DateUtil.fromString( "1/7/2012" ).before( fechaFactura ) ? 21.0 : 18.0;
	}

}
