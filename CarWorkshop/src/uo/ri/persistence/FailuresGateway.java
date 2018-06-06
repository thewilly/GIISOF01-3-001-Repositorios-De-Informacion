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

import uo.ri.common.BusinessException;

/**
 * FailuresGateway.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public interface FailuresGateway {

	/**
	 * Checks that all the failures that are provided in the list are finished.
	 * 
	 * @param failuresIds is the list that contains all the id's of the failures
	 *            to be checked.
	 * @throws BusinessException if any error occurs during the execution of the
	 *             method.
	 */
	void checkAllFailuresAreFinished( List<Long> failuresIds ) throws BusinessException;

	/**
	 * Computes and returns the accumulated cost produced by the failures given.
	 * 
	 * @param failuresIds is the list of the failures to accumulate the costs.
	 * @return the accumulated costs for the failures.
	 * @throws BusinessException if any error occurs during the execution of the
	 *             method.
	 */
	double getCostForFailures( List<Long> failuresIds ) throws BusinessException;

	/**
	 * Links the given invoice with the given failures.
	 * 
	 * @param invoiceId is the invoice to link with the failures.
	 * @param failuresIds is the list of failures to link with the given
	 *            invoice.
	 */
	void linkInvoiceWithFailures( long invoiceId, List<Long> failuresIds );

	/**
	 * Sets the connection to operate the transaction.
	 * 
	 * @param connection is the connection to set.
	 */
	void setConnection( Connection connection );

	/**
	 * Sets the status for a list of failures id's.
	 * 
	 * @param failuresIds is the list of failures to change its status.
	 * @param newStatus is the new status for every failure in the list of
	 *            failures.
	 */
	void setFailureStatus( List<Long> failuresIds, String newStatus );
}
