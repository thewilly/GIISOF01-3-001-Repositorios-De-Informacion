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
package uo.ri.business;

import java.util.List;
import java.util.Map;

import uo.ri.common.BusinessException;

/**
 * 
 * ForemanService.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201805082027
 * @since 201805082027
 * @formatter Oviedo Computing Community
 */
public interface ForemanService {

	/**
	 * Creates a simple client with the data provided. That is a client that has
	 * not been recommended.
	 * 
	 * @param dni of the new client.
	 * @param name of the new client.
	 * @param surname of the new client.
	 * @param zipCode of the new client.
	 * @param phoneNumber of the new client.
	 * @param email of the new client.
	 * @throws BusinessException if any error occurs during the creation of the
	 *             client.
	 */
	void createClient( String dni, String name, String surname, int zipCode, int phoneNumber,
			String email ) throws BusinessException;

	/**
	 * Creates a client that has been recommended by another client.
	 * 
	 * @param dni of the new client.
	 * @param name of the new client.
	 * @param surname of the new client.
	 * @param zipCode of the new client.
	 * @param phoneNumber of the new client.
	 * @param email of the new client.
	 * @param recommenderId is the id of the client that recommend the new
	 *            client.
	 * @throws BusinessException if any error occurs during the creation of the
	 *             client.
	 */
	void createRecommendedClient( String dni, String name, String surname, int zipCode,
			int phoneNumber, String email, long recommenderId ) throws BusinessException;

	/**
	 * Finds all the clients in the system
	 * 
	 * @return a list containing all the clients data.
	 * @throws BusinessException if any error occurs during the execution of the
	 *             operations.
	 */
	List<Map<String, Object>> findAllClients() throws BusinessException;

	/**
	 * Filters all the clients by its recommender id.
	 * 
	 * @param reocmmenderId is the id of the client that recommended the the
	 *            clients. Will be use a a pipe filter.
	 * @return a list containing all the clients data.
	 * @throws BusinessException if any error occurs during the execution of the
	 *             operations.
	 */
	List<Map<String, Object>> findAllClientsByRecomendator( Long reocmmenderId )
			throws BusinessException;

	/**
	 * Finds a client by its unique id.
	 * 
	 * @param clientId is the client id of the client to look for.
	 * @return an string containing all the client's data.
	 * @throws BusinessException if any error occurs during the execution of the
	 *             operations.
	 */
	String findByClientId( Long clientId ) throws BusinessException;

	/**
	 * Removes a client from the system by its client id.
	 * 
	 * @param clientId is the id of the client to remove.
	 * @throws BusinessException if any error occurs during the removal of the
	 *             client.
	 */
	void removeClient( Long clientId ) throws BusinessException;

	/**
	 * Updates the client whose client id is given as first parameter with the
	 * rest of the data provided.
	 * 
	 * @param clientId is the id of the client of update.
	 * @param dni is the new dni of the client.
	 * @param name is the new name of the client.
	 * @param surname is the new surname of the client.
	 * @param zipCode is the new zip code of the client.
	 * @param phoneNumber is the new phone number of the client.
	 * @param email is the new email of the client.
	 * @throws BusinessException if any error occurs during the execution of the
	 *             operations.
	 */
	void updateClient( long clientId, String dni, String name, String surname, int zipCode,
			int phoneNumber, String email ) throws BusinessException;

}
