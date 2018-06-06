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
package uo.ri.persistence;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import uo.ri.common.BusinessException;

/**
 * MechanicsGateway.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public interface MechanicsGateway {

	/**
	 * @return all the mechanics in the system.
	 */
	List<Map<String, Object>> findAll();

	/**
	 * Removes a mechanic by its given unique DB id.
	 * 
	 * @param mechanicId is the mechanic to remove given unique DB id.
	 * @throws BusinessException if any error during the executing of the
	 *             method.
	 */
	void remove( Long mechanicId ) throws BusinessException;

	/**
	 * Creates a mechanic from the given name and surname.
	 * 
	 * @param name is the name of the mechanic.
	 * @param surname is the surname of the mechanic.
	 */
	void save( String name, String surname );

	/**
	 * Sets the connection for the transactions.
	 * 
	 * @param connection is the connection to use.
	 */
	void setConnection( Connection connection );

	/**
	 * Updates the mechanic with the given mechanic id.
	 * 
	 * @param mechanicToUpdateId is the unique DB id of the mechanic to update.
	 * @param newMechanicName is the new name for the mechanic.
	 * @param newMechanicSurname is the new surname for the mechanic.
	 * @throws BusinessException if any error during the executing of the
	 *             method.
	 */
	void update( Long mechanicToUpdateId, String newMechanicName, String newMechanicSurname ) throws BusinessException;
}
