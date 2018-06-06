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
import java.util.Date;
import java.util.Map;

/**
 * InvoicesGateway.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public interface InvoicesGateway {

	/**
	 * Gets the last invoice number generated.
	 * 
	 * @return the last invoice number generated.
	 */
	Long getLastInvoiceNumber();

	/**
	 * Gets the taxes for the amount and date.
	 * 
	 * @param invoiceAmount is the amount of the invoice.
	 * @param invoiceDate is the date of the invoice.
	 * @return the taxes for the given data.
	 */
	double getTaxes( double invoiceAmount, Date invoiceDate );

	/**
	 * Saves the given invoice.
	 * 
	 * @param invoice is the context representation of an invoice as a map.
	 * @return the generated id of the invoice.
	 */
	Long save( Map<String, Object> invoice );

	/**
	 * Sets the connection for the transactions.
	 * 
	 * @param cconnection is the connection to use in the transactions.
	 */
	void setConnection( Connection cconnection );
}
