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
package uo.ri.business;

import java.util.List;
import java.util.Map;

import uo.ri.common.BusinessException;

/**
 * AdminService.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public interface AdminService {

	/**
	 * Creates a mechanic from the given name and surname.
	 * 
	 * @param name is the name of the mechanic.
	 * @param surname is the surname of the mechanic.
	 */
	void createMechanic( String name, String surname );

	/**
	 * @return all the bonds in the system as a list of maps where each map
	 *         represents a bond.
	 */
	List<Map<String, Object>> findAllBonds();

	/**
	 * @param clientId is the unique DB id of the client to query the bonds.
	 * @return the bonds in the system that matched the given criteria as a list
	 *         of maps where each map represents a bond.
	 * @throws BusinessException if any error during the executing of the
	 *             method.
	 */
	List<Map<String, Object>> findAllBondsByCliendId( long clientId ) throws BusinessException;

	/**
	 * @return all the mechanics in the system as a list of maps where each map
	 *         represents a mechanic.
	 */
	List<Map<String, Object>> findAllMechanics();

	/**
	 * Generates the bonds by recommendation.
	 */
	void generateBonosByRecomendation();

	/**
	 * Removes a mechanic by its given unique DB id.
	 * 
	 * @param mechanicId is the mechanic to remove given unique DB id.
	 * @throws BusinessException if any error during the executing of the
	 *             method.
	 */
	void removeMechanic( long mechanicId ) throws BusinessException;

	/**
	 * Updates the mechanic with the given mechanic id.
	 * 
	 * @param mechanicToUpdateId is the unique DB id of the mechanic to update.
	 * @param newMechanicName is the new name for the mechanic.
	 * @param newMechanicSurname is the new surname for the mechanic.
	 * @throws BusinessException if any error during the executing of the
	 *             method.
	 */
	void updateMechanic( long mechanicToUpdateId, String newMechanicName,
			String newMechanicSurname )
			throws BusinessException;
}
