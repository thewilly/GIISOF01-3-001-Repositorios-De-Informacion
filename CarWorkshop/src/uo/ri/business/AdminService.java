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
 * AdminService.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201805072244
 * @since 201805072244
 * @formatter Oviedo Computing Community
 */
public interface AdminService {

	/**
	 * For a given name and surname will call the corresponding persistence
	 * factory to send persist the new mechanic in the database.
	 * 
	 * @param name of the mechanic.
	 * @param suename of the mechanic.
	 * @throws BusinessException if any error occurs during the mechanic
	 *             creation or during persistence.
	 */
	void addMechanic( String name, String suename ) throws BusinessException;

	/**
	 * From a given mechanic id will remove it from the persistence database.
	 * 
	 * @param mechanicId is the id of the mechanic we want to remove from the
	 *            database.
	 * @throws BusinessException if any error occurs during the mechanic
	 *             deletion or during persistence.
	 */
	void removeMechanic( Long mechanicId ) throws BusinessException;

	/**
	 * For the given mechanic id will update the name and the surname with the
	 * ones provided.
	 * 
	 * @param mechanicId is the id of the mechanic we want to update its name
	 *            and surname.
	 * @param name is the new name we want to set as the mechanic name.
	 * @param surname is the new surname we want to set as the mechanic surname.
	 * @throws BusinessException if any error occurs during the mechanic update
	 *             or during persistence.
	 */
	void updateMechanic( Long mechanicId, String name, String surname )
			throws BusinessException;

	/**
	 * Return the a list containing all the mechanics data that is contained in
	 * the persistence layer.
	 * 
	 * @return a list a list containing all the mechanics data that is contained
	 *         in the persistence layer.
	 * @throws BusinessException if any error occurs during the execution of the
	 *             method.
	 */
	List<Map<String, Object>> findAllMechanics() throws BusinessException;

	/**
	 * Will automatically generate the corresponding bonuses for all registered
	 * clients. Those are the conditions to generate bonds.
	 * 
	 * For each client that has recommended 3 other clients (who have made some
	 * repair in the Workshop) a bonus of € 25 will be issued, with no limit on
	 * the number of bonuses that can be obtained. It is necessary that the
	 * client to whom a bond is to be issued has previously made some repair in
	 * the Workshop. The bonds of this type carry the description "By
	 * recommendation".
	 * 
	 * For every 3 breakdowns repaired in the Workshop (and charged) you will be
	 * issued a € 20 bonus. No limit on the number of bonuses you can get. The
	 * description of this type of bonus will be "For three breakdowns".
	 * 
	 * @throws BusinessException if any error occurs during the execution of the
	 *             method.
	 */
	void generateBonds() throws BusinessException;

}
