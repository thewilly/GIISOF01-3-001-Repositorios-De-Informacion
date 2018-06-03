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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alb.util.jdbc.Jdbc;
import uo.ri.common.BusinessException;
import uo.ri.conf.Conf;
import uo.ri.persistence.MechanicsGateway;

/**
 * MechaincsGatewayImpl.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class MechaincsGatewayImpl implements MechanicsGateway {
	private Connection c;
	private PreparedStatement pst = null;
	private ResultSet rs = null;

	@Override
	public void setConnection( Connection c ) {
		this.c = c;
	}

	private Map<String, Object> load( ResultSet rs ) {
		Map<String, Object> map = new HashMap<>();
		try {
			map.put( "id", rs.getLong( 1 ) );
			map.put( "name", rs.getString( 2 ) );
			map.put( "surname", rs.getString( 3 ) );
		} catch (SQLException e) {
			throw new RuntimeException( e );
		}
		return map;
	}

	@Override
	public List<Map<String, Object>> findAll() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			pst = c.prepareStatement( Conf.get( "SQL_FIND_ALL_MECHANICS" ) );
			rs = pst.executeQuery();
			while (rs.next()) {
				list.add( load( rs ) );
			}
			return list;
		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {
			Jdbc.close( rs, pst, c );
		}
	}

	@Override
	public void update( Long id, String name, String surname ) throws BusinessException {
		try {
			pst = c.prepareStatement( Conf.get( "SQL_UPDATE_MECHANIC" ) );
			pst.setString( 1, name );
			pst.setString( 2, surname );
			pst.setLong( 3, id );
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new BusinessException( e );
		} finally {
			Jdbc.close( rs, pst, c );
		}
	}

	@Override
	public void save( String name, String surname ) {
		try {
			pst = c.prepareStatement( Conf.get( "SQL_ADD_MECHANIC" ) );
			pst.setString( 1, name );
			pst.setString( 2, surname );
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException();
		} finally {
			Jdbc.close( rs, pst, c );
		}
	}

	@Override
	public void remove( Long idMecanico ) throws BusinessException {
		try {
			pst = c.prepareStatement( Conf.get( "SQL_DELETE_MECHANIC" ) );
			pst.setLong( 1, idMecanico );
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new BusinessException( e );
		} finally {
			Jdbc.close( rs, pst, c );
		}

	}

}
