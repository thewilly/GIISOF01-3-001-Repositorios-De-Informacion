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
package uo.ri.business.impl.admin;

import static alb.util.jdbc.Jdbc.close;
import static alb.util.jdbc.Jdbc.getConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uo.ri.common.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.MechanicsGateway;

/**
 * 
 * FindAllMechanics.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201805072354
 * @since 201805072354
 * @formatter Oviedo Computing Community
 */
public class FindAllMechanics {

    /**
     * Return the a list containing all the mechanics data that is contained in
     * the persistence layer.
     * 
     * @return a list a list containing all the mechanics data that is contained
     *         in the persistence layer.
     * @throws BusinessException
     *             if any error occurs during the execution of the method.
     */
    public List<Map<String, Object>> execute() {

	// Instantiate the result object and initialize it to avoid future null
	// pointers.
	List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

	Connection connection = null;

	try {
	    // Getting the connection.
	    connection = getConnection();

	    // Creating the gateway and setting the connection.
	    MechanicsGateway mechanicsGW = PersistenceFactory
		    .getMechanicsGateway();
	    mechanicsGW.setConnection(connection);

	    // Invoke the findAllMechanics method from the gateway to get all
	    // the mechanics.
	    result = mechanicsGW.findAll();

	} catch (SQLException e) {
	    throw new RuntimeException(e);
	} finally {
	    // Closing the connection.
	    close(connection);
	}

	return result;
    }
}
