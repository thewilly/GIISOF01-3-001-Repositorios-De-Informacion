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

import bin.alb.util.jdbc.Jdbc;
import uo.ri.common.BusinessException;
import uo.ri.conf.Conf;
import uo.ri.persistence.PaymentMethodsGateway;

public class PaymentMethodsGatewayImp implements PaymentMethodsGateway {

	Connection conection = null;
	PreparedStatement pst = null;
	ResultSet rs = null;

	@Override
	public void createBonos( Long idCLiente, String codigo ) throws BusinessException {

		try {
			pst = conection.prepareStatement( Conf.get( "SQL_CREATE_BONOS" ) );

			pst.setString( 1, "TBonos" );
			pst.setInt( 2, 0 );
			pst.setString( 3, codigo );
			pst.setInt( 4, 20 );
			pst.setLong( 5, idCLiente );
			pst.setString( 6, "Por tres averias" );

			pst.executeUpdate();

		} catch (SQLException e) {
			throw new BusinessException( "Error creando los bonos" );
		} finally {
			Jdbc.close( pst );
		}

	}

	@Override
	public String getLastBonoCode() throws BusinessException {

		String codigo = "";

		try {
			pst = conection.prepareStatement( Conf.get( "SQL_GET_ALL_BONOS" ) );

			rs = pst.executeQuery();

			while (rs.next()) {
				codigo = rs.getString( "codigo" );
			}

			return codigo;

		} catch (SQLException e) {
			throw new BusinessException( "Error sacando el código del último bono" );
		} finally {
			Jdbc.close( rs, pst );
		}

	}

	@Override
	public void setConnection( Connection conection ) {
		this.conection = conection;

	}

}
