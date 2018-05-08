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
 * CreateClientWithRecomender.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201805081953
 * @since 201805081953
 * @formatter Oviedo Computing Community
 */
public class CreateRecommendedClient {

	private Long recommenderId;
	private String dni, name, surname, email;
	private int phoneNumber, zipCode;

	/**
	 * Create the action to create a client with a recommender.
	 * 
	 * @param dni of the client.
	 * @param name of the client.
	 * @param surname of the client.
	 * @param zipCode of the client.
	 * @param phone of the client.
	 * @param email of the client.
	 * @param recomenderId is the recommender unique identifier in the system.
	 */
	public CreateRecommendedClient( String dni, String name, String surname, int zipCode,
			int phone, String email, long recomenderId ) {
		this.dni = dni;
		this.name = name;
		this.surname = surname;
		this.zipCode = zipCode;
		this.phoneNumber = phone;
		this.email = email;
		this.recommenderId = recomenderId;
	}

	/**
	 * Executes the action.
	 * 
	 * @throws BusinessException if any error occurs during the creation of the
	 *             client.
	 */
	public void execute() throws BusinessException {

		Connection connection = null;

		try {
			// Getting the connection.
			connection = getConnection();

			// Creating the gateway and setting the connection.
			ClientsGateway clientsGW = PersistenceFactory.getClientsGateway();
			clientsGW.setConnection( connection );

			// Saving the object in persistence.
			clientsGW.createWithRecommender( dni, name, surname, zipCode, phoneNumber, email,
					recommenderId );

		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {
			// Closing the connection.
			close( connection );
		}
	}

}
