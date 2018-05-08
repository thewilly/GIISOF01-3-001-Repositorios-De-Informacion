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
package uo.ri.business.impl.foreman;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static alb.util.jdbc.Jdbc.*;
import uo.ri.common.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.ClientsGateway;

/**
 * 
 * FindAllClients.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201805081957
 * @since 201805081957
 * @formatter Oviedo Computing Community
 */
public class FindAllClients {

	/**
	 * Executes the action of finding all the clients in the system and returns
	 * them as a list of maps being each map the client data.
	 * 
	 * @return a list of maps being each map the client data.
	 * @throws BusinessException if any error occurs during the execution of the
	 *             operations involved.
	 */
	public List<Map<String, Object>> execute() throws BusinessException {

		// Creating the all clients data and initializing it to avoid future null
		// pointers.
		List<Map<String, Object>> allClientsData = new ArrayList<Map<String, Object>>();
		Connection connection = null;

		try {

			// Getting the connection.
			connection = getConnection();

			// Creating the gateway and setting the connection.
			ClientsGateway clientsGW = PersistenceFactory.getClientsGateway();
			clientsGW.setConnection( connection );

			// Getting all clients data from the gateway.
			allClientsData = clientsGW.findAllClients();

		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {
			// Closing the connection
			close( connection );
		}
		// Returning all clients data.
		return allClientsData;
	}

}
