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
 * CashService.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public interface CashService {

	/**
	 * Creates an invoice for the given list of failures.
	 * 
	 * @param failuresIds is the list that contains the unique identifiers of
	 *            the failures to bill in the invoice.
	 * @return the invoice as a map.
	 * @throws BusinessException if any error during the executing of the
	 *             method.
	 */
	Map<String, Object> createInvoiceForFailures( List<Long> failuresIds ) throws BusinessException;

	/**
	 * Creates a payment method of bond type.
	 * 
	 * @param paymentMethodId is the id of the bond.
	 * @param bondDescription is the description of the bond.
	 * @param availableAmount is the available amount of the money that the bond
	 *            has.
	 * @throws BusinessException if any error during the executing of the
	 *             method.
	 */
	void createPMBond( Long paymentMethodId, String bondDescription, double availableAmount )
			throws BusinessException;

	/**
	 * Creates a payment method of card type.
	 * 
	 * @param paymentMethodId is the id for the card.
	 * @param cardKind is the kind of card.
	 * @param cardNumber is the number of the card.
	 * @param cardExpirationDate is the expiration date of the card after which
	 *            the card will not be valid.
	 * @throws BusinessException if any error during the executing of the
	 *             method.
	 */
	void createPMCard( Long paymentMethodId, String cardKind, String cardNumber,
			String cardExpirationDate )
			throws BusinessException;

	/**
	 * Finds all the payment method of a given client.
	 * 
	 * @param clientId is the unique DB identifier of the client to get the
	 *            payment methods.
	 * @return a list of maps where each map is a payment method of the given
	 *         client.
	 * @throws BusinessException if any error during the executing of the
	 *             method.
	 */
	List<Map<String, Object>> findAllPaymentMethodsByClientId( Long clientId )
			throws BusinessException;

	/**
	 * Removes a payment method by its unique DB identifier.
	 * 
	 * @param paymentMethodId is the unique DB identifier of the payment method
	 *            to remove from the system.
	 * @throws BusinessException if any error during the executing of the
	 *             method.
	 */
	void removePaymentMethodById( Long paymentMethodId ) throws BusinessException;
}
