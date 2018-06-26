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

import uo.ri.common.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.MechanicsGateway;

/**
 * 
 * AddMechanic.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201805072327
 * @since 201805072327
 * @formatter Oviedo Computing Community
 */
public class CerateMechanic {

    private String name = "";
    private String surname = "";

    /**
     * Allocates a mechanic with its name and surname.
     * 
     * @param name
     *            of the mechanic.
     * @param surname
     *            of the mechanic.
     */
    public CerateMechanic(String name, String surname) {
	this.name = name;
	this.surname = surname;
    }

    /**
     * Gets a connection from the JDBC. And for that given connection opens a
     * mechanics gateway to save the mechanic data.
     * 
     * @throws BusinessException
     *             if any error occurs during the execution of the method.
     */
    public void execute() {
	Connection connection = null;

	try {
	    // Getting the connection
	    connection = getConnection();

	    // Creating the gateway and setting the connection.
	    MechanicsGateway mechanicsGW = PersistenceFactory
		    .getMechanicsGateway();
	    mechanicsGW.setConnection(connection);

	    // Invoke the save method to persist the data.
	    mechanicsGW.save(this.name, this.surname);
	} catch (SQLException e) {
	    throw new RuntimeException(e);
	} finally {
	    // Closing the connection.
	    close(connection);
	}
    }
}
