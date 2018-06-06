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

	/**
	 * Creates a payment method of bond type.
	 * 
	 * @param paymentMethodId is the id of the bond.
	 * @param availableAmount is the available amount of the money that the bond
	 *            has.
	 * @param bondDescription is the description of the bond.
	 * @throws BusinessException if any error during the executing of the
	 *             method.
	 */
	void createBond( long paymentMethodId, double availableAmount, String bondDescription ) throws BusinessException;

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
	void createPMCard( Long paymentMethodId, String cardKind, String cardNumber, Timestamp cardExpirationDate )
			throws BusinessException;

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
	List<Map<String, Object>> findAllBondsByClientId( long clientId ) throws BusinessException;

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
	List<Map<String, Object>> findAllPaymentMethodsByClientId( Long clientId ) throws BusinessException;

	/**
	 * 
	 * @throws BusinessException
	 */
	void generateBondsByRecomendation() throws BusinessException;

	/**
	 * Removes a payment method by its unique DB identifier.
	 * 
	 * @param paymentMethodId is the unique DB identifier of the payment method
	 *            to remove from the system.
	 * @throws BusinessException if any error during the executing of the
	 *             method.
	 */
	void removePaymentMethodById( Long paymentMethodId ) throws BusinessException;

	/**
	 * Sets the connection to operate the transaction.
	 * 
	 * @param connection is the connection to set.
	 */
	void setConnection( Connection connection );
}
