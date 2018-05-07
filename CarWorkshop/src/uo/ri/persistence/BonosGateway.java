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

import java.sql.Connection;
import java.util.List;

import uo.ri.common.BusinessException;

public interface BonosGateway {

	void setConnection( Connection conection );

	/**
	 * Devuelve una lista con los ids de los vehículos de un cliente que se le
	 * pasa como parámetro
	 * 
	 * @param idCliente
	 * @return
	 * @throws BusinessException
	 */
	List<Long> getVehiculosByIdCliente( Long idCliente ) throws BusinessException;

	/**
	 * Devuelve una lista con los ids de las averias de un vehículo que se le
	 * pasa como parámetro
	 * 
	 * @param idVehiculo
	 * @return
	 * @throws BusinessException
	 */
	List<Long> getAveriasByIdVehiculo( Long idVehiculo ) throws BusinessException;

}
