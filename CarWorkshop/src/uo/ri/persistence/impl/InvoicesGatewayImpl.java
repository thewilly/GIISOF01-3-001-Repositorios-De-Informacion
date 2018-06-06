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
import static alb.util.jdbc.Jdbc.*;
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

	// PUBLIC MEMBERS

	@Override
	public Long getLastInvoiceNumber() {
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {

			// Getting the query.
			pst = connection.prepareStatement( Conf.get( "SQL_ULTIMO_NUMERO_FACTURA" ) );

			// Executing the query.
			rs = pst.executeQuery();

			// If there is a last invoice number in the result set we return the
			// last one + 1. Else the default one.
			if (rs.next()) {
				return rs.getLong( 1 ) + 1;
			} else {
				return 1L;
			}
		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {

			// Closing the result set, the prepared statement and the
			// connection.
			close( rs, pst, connection );
		}
	}

	@Override
	public double getTaxes( double invoiceAmount, Date invoiceDate ) {
		return DateUtil.fromString( "1/7/2012" ).before( invoiceDate ) ? 21.0 : 18.0;
	}

	@Override
	public Long save( Map<String, Object> invoice ) {
		PreparedStatement pst = null;

		try {

			// Getting the query.
			pst = connection.prepareStatement( Conf.get( "SQL_INSERTAR_FACTURA" ) );

			// Setting the parameters in the query.
			pst.setLong( 1, (long) invoice.get( "numFactura" ) );
			pst.setDate( 2, (java.sql.Date) invoice.get( "fechaFactura" ) );
			pst.setDouble( 3, (double) invoice.get( "iva" ) );
			pst.setDouble( 4, (double) invoice.get( "importe" ) );
			pst.setString( 5, "SIN_ABONAR" );

			// Executing the query.
			pst.executeUpdate();

			// Then we compute and return the key generated for the created
			// invoice.
			return getGeneratedKey( (long) invoice.get( "numFactura" ) );
		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {

			// Closing the connection and the prepared statement,
			close( connection );
			close( pst );
		}
	}

	@Override
	public void setConnection( Connection c ) {
		this.connection = c;
	}

	// PRIVATE MEMBERS

	private long getGeneratedKey( long invoiceNumber ) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {

			// Getting the query from the configuration.
			pst = connection.prepareStatement( Conf.get( "SQL_RECUPERAR_CLAVE_GENERADA" ) );

			// Setting the parameters of the query.
			pst.setLong( 1, invoiceNumber );

			// Executing the query.
			rs = pst.executeQuery();

			// Openning the result set and returning the first long in it.
			rs.next();
			return rs.getLong( 1 );

		} finally {

			// Closing the result set, the prepared statement and the
			// connection.
			close( rs, pst, connection );
		}
	}
}