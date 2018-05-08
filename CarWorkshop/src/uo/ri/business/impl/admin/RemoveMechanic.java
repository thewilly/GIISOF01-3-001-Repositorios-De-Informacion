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
package uo.ri.business.impl.admin;

import java.sql.Connection;
import java.sql.SQLException;

import static alb.util.jdbc.Jdbc.*;
import uo.ri.common.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.MechanicsGateway;

/**
 * 
 * DeleteMechanic.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201805072347
 * @since 201805072347
 * @formatter Oviedo Computing Community
 */
public class RemoveMechanic {

	private long mechanicId;

	/**
	 * For the given id of an existing mechanic will remove it from the
	 * persistence.
	 * 
	 * @param mechanicId is the id of the mechanic we want to remove.
	 */
	public RemoveMechanic( long mechanicId ) {
		this.mechanicId = mechanicId;
	}

	/**
	 * Will execute the removal of the mechanic whose id was set in the
	 * constructor.
	 * 
	 * @throws BusinessException if any error occurs during the execution of the
	 *             method.
	 */
	public void execute() throws BusinessException {

		Connection connection = null;

		try {
			// Getting the connection.
			connection = getConnection();

			// Creating the gateway and setting the connection.
			MechanicsGateway mechanicsGW = PersistenceFactory.getMechanicsGateway();
			mechanicsGW.setConnection( connection );

			// Invoke the save method to persist the data.
			mechanicsGW.remove( mechanicId );

		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {
			// Closing the connection.
			close( connection );
		}
	}

}
