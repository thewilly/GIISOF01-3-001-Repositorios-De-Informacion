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
package uo.ri.business.impl.cash;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alb.util.date.DateUtil;
import static alb.util.jdbc.Jdbc.*;
import alb.util.math.Round;
import uo.ri.common.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.FailuresGateway;
import uo.ri.persistence.InvoicesGateway;

/**
 * 
 * CreateInvoiceFor.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201805081340
 * @since 201805081340
 * @formatter Oviedo Computing Community
 */
public class CreateInvoiceForFailures {

    private List<Long> failuresIds;

    /**
     * Initializes the create invoice for the given list of id's.
     * 
     * @param failuresIds
     *            is the list that contains all the id's of the failures to be
     *            billed in the invoice.
     */
    public CreateInvoiceForFailures(List<Long> failuresIds) {
	this.failuresIds = failuresIds;
    }

    /**
     * 
     * @return
     * @throws BusinessException
     */
    public Map<String, Object> execute() throws BusinessException {
	Connection connection = null;

	// Invoice map will represent the invoice data.
	Map<String, Object> invoice = new HashMap<>();

	// Creating the gateways.
	FailuresGateway failuresGW = PersistenceFactory.getFailuresGateway();
	InvoicesGateway invoicesGW = PersistenceFactory.getInvoicesGateway();

	try {

	    // Getting the connection and setting the auto commit to false to
	    // ensure atomicity.
	    connection = getConnection();
	    connection.setAutoCommit(false);

	    // Setting the connection.
	    failuresGW.setConnection(connection);
	    invoicesGW.setConnection(connection);

	    // Checking that all the failures are finished.
	    failuresGW.checkAllFailuresAreFinished(failuresIds);

	    // Getting the invoice number from the gateway.
	    long invoiceNumber = invoicesGW.getLastInvoiceNumber();

	    // Getting the current date and setting it as the date of the
	    // invoice generation.
	    Date invoiceDate = DateUtil.today();

	    // Computing the invoice context.
	    double failuresCosts = failuresGW.getCostForFailures(failuresIds);
	    double taxes = invoicesGW.getTaxes(failuresCosts, invoiceDate);
	    double totalAmount = failuresCosts * (1 + taxes / 100);
	    totalAmount = Round.twoCents(totalAmount);
	    invoice.put("numFactura", invoiceNumber);
	    invoice.put("fechaFactura", invoiceDate);
	    invoice.put("iva", taxes);
	    invoice.put("importe", totalAmount);

	    // Saving the invoice.
	    long invoiceId = invoicesGW.save(invoice);

	    // Linking the invoice and the failures.
	    failuresGW.linkInvoiceWithFailures(invoiceId, failuresIds);

	    // Changing the status of the failures in the invoice to FACTURADA.
	    failuresGW.setFailureStatus(failuresIds, "FACTURADA");

	    // Finally we commit the changes in the database and return the
	    // generated invoice as a map.
	    connection.commit();
	    return invoice;
	} catch (SQLException e) {
	    try {
		// If any error we perform a roll back.
		connection.rollback();
	    } catch (SQLException ex) {
	    }
	    ;
	    throw new RuntimeException(e);
	} finally {
	    // Closing the connection.
	    close(connection);
	}

    }
}
