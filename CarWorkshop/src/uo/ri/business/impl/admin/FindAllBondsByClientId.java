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
package uo.ri.business.impl.admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static alb.util.jdbc.Jdbc.*;
import uo.ri.common.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.PaymentMethodsGateway;

/**
 * 
 * FindAllBondsByClientId.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806021346
 * @since 201806021346
 * @formatter Oviedo Computing Community
 */
public class FindAllBondsByClientId {

    private long clientId;

    /**
     * Allocates the object and initializes it with the id of the client to find
     * its bonds.
     * 
     * @param clientId
     *            is the id of the client for which we want to find the bonds.
     */
    public FindAllBondsByClientId(long clientId) {
	this.clientId = clientId;
    }

    /**
     * Will find all bonds in the system for the given clientId and will return
     * them.
     * 
     * @return a list of maps where each map represents a bond.
     * @throws BusinessException
     *             if any error occurs during the execution of the method.
     */
    public List<Map<String, Object>> execute() throws BusinessException {

	Connection connection = null;

	try {

	    // Getting the connection.
	    connection = getConnection();

	    // Creating the gateway and setting the connection.
	    PaymentMethodsGateway paymentMethodsGW = PersistenceFactory
		    .getPaymentMethodsGateway();
	    paymentMethodsGW.setConnection(connection);

	    // Returning the found bonds for the given client id.
	    return paymentMethodsGW.findAllBondsByClientId(clientId);

	} catch (SQLException e) {
	    throw new RuntimeException(e);
	} finally {
	    // Closing the connection.
	    close(connection);
	}
    }

}
