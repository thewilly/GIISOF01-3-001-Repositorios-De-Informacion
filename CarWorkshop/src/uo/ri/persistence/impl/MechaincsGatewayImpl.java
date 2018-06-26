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

import static alb.util.jdbc.Jdbc.*;
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
    private Connection connection;
    private PreparedStatement pst = null;
    private ResultSet rs = null;

    @Override
    public List<Map<String, Object>> findAll() {
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	try {

	    // Getting the query.
	    pst = connection
		    .prepareStatement(Conf.get("SQL_FIND_ALL_MECHANICS"));

	    // Executing the query.
	    rs = pst.executeQuery();

	    // If the result set has more results on it, load them in the list.
	    while (rs.next()) {
		list.add(mechanicResultSetToMap(rs));
	    }

	    // Finally return the list.
	    return list;
	} catch (SQLException e) {
	    throw new RuntimeException(e);
	} finally {

	    // Closing the result set the prepared statement and the connection.
	    close(rs, pst, connection);
	}
    }

    @Override
    public void remove(Long mechanicId) throws BusinessException {
	try {

	    // Getting the query.
	    pst = connection.prepareStatement(Conf.get("SQL_DELETE_MECHANIC"));

	    // Setting the parameters for the query.
	    pst.setLong(1, mechanicId);

	    // Executing the query.
	    pst.executeUpdate();
	} catch (SQLException e) {
	    throw new BusinessException(e);
	} finally {

	    // Closing the result set, the prepared statement and the
	    // connection.
	    close(rs, pst, connection);
	}

    }

    @Override
    public void save(String name, String surname) {
	try {

	    // Getting the query.
	    pst = connection.prepareStatement(Conf.get("SQL_ADD_MECHANIC"));

	    // Setting the parameters for the query.
	    pst.setString(1, name);
	    pst.setString(2, surname);

	    // Executing the update.
	    pst.executeUpdate();
	} catch (SQLException e) {
	    throw new RuntimeException();
	} finally {

	    // Closing the result set, the prepared statement and the
	    // connection.
	    close(rs, pst, connection);
	}
    }

    @Override
    public void setConnection(Connection connection) {
	this.connection = connection;
    }

    @Override
    public void update(Long mechanicId, String newName, String newSurname)
	    throws BusinessException {
	try {

	    // Getting the connection.
	    pst = connection.prepareStatement(Conf.get("SQL_UPDATE_MECHANIC"));

	    // Setting the parameters for the query.
	    pst.setString(1, newName);
	    pst.setString(2, newSurname);
	    pst.setLong(3, mechanicId);

	    // Executing the query.
	    pst.executeUpdate();
	} catch (SQLException e) {
	    throw new BusinessException(e);
	} finally {

	    // Closing the result set, the prepared statement and the
	    // connection.
	    close(rs, pst, connection);
	}
    }

    // PRIVATE MEMBERS

    /**
     * Transforms a mechanic result set to a map.
     * 
     * @param resultSet
     *            that contains the mechanic info and we want to cast to a map.
     * @return a map containing all the mechanic info.
     */
    private Map<String, Object> mechanicResultSetToMap(ResultSet resultSet) {
	Map<String, Object> mechanicMap = new HashMap<>();
	try {

	    // Setting the map entries.
	    mechanicMap.put("id", resultSet.getLong(1));
	    mechanicMap.put("name", resultSet.getString(2));
	    mechanicMap.put("surname", resultSet.getString(3));
	} catch (SQLException e) {
	    throw new RuntimeException(e);
	}

	// Returning the map.
	return mechanicMap;
    }
}