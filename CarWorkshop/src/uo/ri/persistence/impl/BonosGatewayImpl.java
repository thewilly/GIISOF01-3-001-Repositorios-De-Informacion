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
import java.util.List;

import bin.alb.util.jdbc.Jdbc;
import uo.ri.common.BusinessException;
import uo.ri.conf.Conf;
import uo.ri.persistence.BonosGateway;

public class BonosGatewayImpl implements BonosGateway {

	Connection conection = null;
	PreparedStatement pst = null;
	ResultSet rs = null;

	@Override
	public void setConnection( Connection conection ) {
		this.conection = conection;

	}

	@Override
	public List<Long> getVehiculosByIdCliente( Long idCliente ) throws BusinessException {

		List<Long> ids = new ArrayList<Long>();

		try {
			pst = conection.prepareStatement( Conf.get( "SQL_FIND_VEHICULOS_BY_ID_CLIENTE" ) );

			pst.setLong( 1, idCliente );

			rs = pst.executeQuery();
			while (rs.next()) {
				ids.add( rs.getLong( "id" ) );
			}

		} catch (SQLException e) {
			throw new BusinessException( "Error sacando los vehiculos de los clientes" );
		} finally {
			Jdbc.close( rs, pst );
		}

		return ids;
	}

	@Override
	public List<Long> getAveriasByIdVehiculo( Long idVehiculo ) throws BusinessException {
		List<Long> ids = new ArrayList<Long>();

		try {
			pst = conection.prepareStatement( Conf.get( "SQL_FIND_AVERIAS_BY_ID_VEHICULO" ) );

			pst.setLong( 1, idVehiculo );

			rs = pst.executeQuery();
			while (rs.next()) {
				ids.add( rs.getLong( "id" ) );
			}

		} catch (SQLException e) {
			throw new BusinessException( "Error sacando las averias de los vehículos" );
		} finally {
			Jdbc.close( rs, pst );
		}

		return ids;
	}

}
