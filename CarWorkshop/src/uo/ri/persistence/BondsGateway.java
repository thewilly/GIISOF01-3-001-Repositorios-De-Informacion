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
package uo.ri.persistence;

import java.util.List;

import uo.ri.common.BusinessException;

/**
 * 
 * BondsGateway.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201805082150
 * @since 201805082150
 * @formatter Oviedo Computing Community
 */
public interface BondsGateway extends RequestsConnection {

	/**
	 * Finds all the failures for a given vehicle id.
	 * 
	 * @param vehicleId is the unique vehicle id in the system to look for.
	 * @return a list containing all the id's of the failures associated with
	 *         that vehicle.
	 * @throws BusinessException if any error occurs during the execution of the
	 *             method.
	 */
	List<Long> findFailuresIdsByVehicleId( Long vehicleId ) throws BusinessException;

	/**
	 * Finds all the vehicles id's for a given client id.
	 * 
	 * @param clientId is the unique client id in the system of the client for
	 *            which we want to get all the associated cars.
	 * @return a list containing all the vehicles id's for the given client id.
	 * @throws BusinessException if any error occurs during the execution of the
	 *             method.
	 */
	List<Long> findVehiclesIdsByClientId( Long clientId ) throws BusinessException;
}
