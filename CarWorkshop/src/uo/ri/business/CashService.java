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
 * CashService.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201805072312
 * @since 201805072312
 * @formatter Oviedo Computing Community
 */
public interface CashService {

	/**
	 * For a given list of the failures id's will generate the corresponding
	 * invoice and return its data in to a map.
	 * 
	 * @param failuresIds is a list containing all the failures that are going
	 *            to be billed in the invoice.
	 * @return a <tt>Map</tt> containing all the invoice data.
	 * @throws BusinessException if any error occurs during the generation of
	 *             the invoices.
	 */
	Map<String, Object> generateInvoiceForFailures( List<Long> failuresIds )
			throws BusinessException;

}
