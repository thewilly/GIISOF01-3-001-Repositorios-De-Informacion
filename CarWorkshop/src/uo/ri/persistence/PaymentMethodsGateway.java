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
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import uo.ri.common.BusinessException;

/**
 * PaymentMethodsGateway.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public interface PaymentMethodsGateway {

	void setConnection( Connection c );

	List<Map<String, Object>> findAllBonds();

	List<Map<String, Object>> findAllBondsByClientId( long id ) throws BusinessException;

	void generateBonosByRecomendation() throws BusinessException;

	List<Map<String, Object>> findAllPaymentMethodsByClientId( Long id ) throws BusinessException;

	void removePaymentMethodById( Long id ) throws BusinessException;

	void createPMCard( Long id, String tipo, String numero, Timestamp fecha )
			throws BusinessException;

	void createBond( long id, double disponible, String descripcion ) throws BusinessException;
}
