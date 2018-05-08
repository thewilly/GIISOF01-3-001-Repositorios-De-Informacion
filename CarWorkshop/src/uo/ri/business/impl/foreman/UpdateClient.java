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

import static alb.util.jdbc.Jdbc.*;
import uo.ri.common.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.ClientsGateway;

/**
 * 
 * UpdateClient.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201805082023
 * @since 201805082023
 * @formatter Oviedo Computing Community
 */
public class UpdateClient {

	private Long clientId;
	private String dni, name, surname, email;
	private int phoneNumber, zipCode;

	/**
	 * Creates an update action that will update the client data for the given
	 * client id.
	 * 
	 * @param clientId is the id for whom we want to update the data.
	 * @param dni of the client.
	 * @param name of the client.
	 * @param surname of the client.
	 * @param zipCode of the client.
	 * @param phoneNumber of the client.
	 * @param email of the client.
	 */
	public UpdateClient( long clientId, String dni, String name, String surname, int zipCode,
			int phoneNumber, String email ) {
		this.dni = dni;
		this.name = name;
		this.surname = surname;
		this.zipCode = zipCode;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.clientId = clientId;
	}

	/**
	 * Executes the update in the data for the client.
	 * 
	 * @throws BusinessException if any error occurs during the execution of the
	 *             inner operations.
	 */
	public void execute() throws BusinessException {

		Connection connection = null;

		try {
			// Getting the connection.
			connection = getConnection();

			// Creating the gateway and setting the connection.
			ClientsGateway clientGW = PersistenceFactory.getClientsGateway();
			clientGW.setConnection( connection );

			// Updating the client's data
			clientGW.update( clientId, dni, name, surname, zipCode, phoneNumber, email );;

		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {
			// Closing the connection.
			close( connection );
		}

	}

}
