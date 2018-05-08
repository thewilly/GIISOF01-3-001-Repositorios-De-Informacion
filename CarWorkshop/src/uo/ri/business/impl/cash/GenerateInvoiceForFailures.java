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
public class GenerateInvoiceForFailures {

	private Connection connection;
	private List<Long> failuresIds;

	/**
	 * For a given list of the failures id's will generate the corresponding
	 * invoice and return its data in to a map.
	 * 
	 * @param failuresIds is a list containing all the failures that are going
	 *            to be billed in the invoice.
	 */
	public GenerateInvoiceForFailures( List<Long> failuresIds ) {
		this.failuresIds = failuresIds;
	}

	/**
	 * Changes the status of each failure who's id is on the list provided to
	 * the status provided.
	 * 
	 * @param failuresIds are the id's of the failures to change the status.
	 * @param statusToSet is the new status of the failures.
	 * @throws BusinessException if any error occurs during the execution of the
	 *             method.
	 */
	private void changeFailuresStatuses( List<Long> failuresIds, String statusToSet )
			throws BusinessException {

		FailuresGateway failuresGW = PersistenceFactory.getFailuresGateway();
		failuresGW.setConnection( connection );

		failuresGW.updateFailuresStatus( failuresIds, statusToSet );
	}

	/**
	 * Gets the taxes for a given date.
	 * 
	 * @param invoiceDate is the date where the invoice was created.
	 * @return 18.0 if the date is before 1/7/2012. Otherwise 21.0.
	 */
	private double computeTaxes( Date invoiceDate ) {
		return DateUtil.fromString( "1/7/2012" ).before( invoiceDate ) ? 21.0 : 18.0;
	}

	/**
	 * Computes the total costs for all the given failures.
	 * 
	 * @param failuresIds are the different failures id's to compute the costs.
	 * @return the total cost of the invoice without taxes.
	 * @throws BusinessException if any error occurs during the execution of the
	 *             method.
	 */
	protected double computeTotalAmmountForFailures( List<Long> failuresIds )
			throws BusinessException {

		double totalCostForInvoice = 0.0, humanCosts, replacementCosts, totalCostsForFailure;
		for (Long failureId : failuresIds) {
			humanCosts = getHumanCostsForFailure( failureId );
			replacementCosts = getReplamentCostsForFailure( failureId );
			totalCostsForFailure = humanCosts + replacementCosts;

			updateAmountForFailure( failureId, totalCostsForFailure );

			totalCostForInvoice += totalCostsForFailure;
		}
		return totalCostForInvoice;
	}

	/**
	 * Creates an invoice for the given data.
	 * 
	 * @param invoiceNumber is the number that will identify the invoice.
	 * @param invoiceDate is the creation date of the invoice.
	 * @param taxes are the amount of taxes applied
	 * @param totalAmountWithTaxes is the total amount of the invoice, that is
	 *            including taxes.
	 * @return the generated key for the created invoice.
	 * @throws BusinessException if any error occurs during the generation of
	 *             the invoices.
	 */
	private long createInvoice( long invoiceNumber, Date invoiceDate, double taxes,
			double totalAmountWithTaxes )
			throws BusinessException {

		// Creating the gateway and setting the connection.
		InvoicesGateway invoicesGW = PersistenceFactory.getInvoicesGateway();
		invoicesGW.setConnection( connection );

		// Persisting the invoice.
		invoicesGW.save( invoiceNumber, invoiceDate, taxes, totalAmountWithTaxes );

		// The id of the new invoice
		return getGeneratedKey( invoiceNumber );

	}

	/**
	 * For a given list of the failures id's will generate the corresponding
	 * invoice and return its data in to a map.
	 * 
	 * @return a <tt>Map</tt> containing all the invoice data.
	 * @throws BusinessException if any error occurs during the generation of
	 *             the invoices.
	 */
	public Map<String, Object> execute() throws BusinessException {
		Map<String, Object> invoiceData = new HashMap<String, Object>();
		try {

			// Creating the connection and setting to false the auto-commit in
			// order to wait all the operations to finish before committing the
			// operation.
			connection = getConnection();
			connection.setAutoCommit( false );

			// Verifies that every failure in the invoice is finished.
			verifyFailuresAreFinished( failuresIds );

			// Generating the invoice number
			long invoiceNumber = generateNewInvoiceNumber();

			// Set the current date as the invoice creation date.
			Date invoiceCreationDate = DateUtil.today();

			// Compute the total amount for failures to bill in the invoice.
			// That is, without taxes.
			double partialAmount = computeTotalAmmountForFailures( failuresIds );

			// Computing the taxes for the invoice.
			double taxes = computeTaxes( invoiceCreationDate );

			// Computing the total amount to bill in the invoice.
			double totalAmount = partialAmount * ( 1 + taxes / 100 );
			totalAmount = Round.twoCents( totalAmount );

			// Creating the invoice and getting the generated key of the
			// invoice.
			long invoiceGeneratedKey = createInvoice( invoiceNumber, invoiceCreationDate, taxes,
					totalAmount );

			// Linking invoice with each failure inside.
			linkInvoiceWithFailures( invoiceGeneratedKey, failuresIds );

			// Changing the status of the failures to billed.
			changeFailuresStatuses( failuresIds, "FACTURADA" );

			// Adding the information to the invoice data map.
			invoiceData.put( "numeroFactura", invoiceNumber );
			invoiceData.put( "fechaFactura", invoiceCreationDate );
			invoiceData.put( "totalFactura", partialAmount );
			invoiceData.put( "iva", taxes );
			invoiceData.put( "importe", totalAmount );

			// Committing the changes done.
			connection.commit();
		} catch (SQLException e) {
			try {
				// In case of an error we perform a roll back.
				connection.rollback();
			} catch (SQLException ex) {
			} ;
			throw new RuntimeException( e );
		} catch (BusinessException e) {
			try {
				// In case of an error we perform a roll back.
				connection.rollback();
			} catch (SQLException ex) {
			} ;
			throw e;
		} finally {
			// Closing the connection.
			close( connection );
		}
		// We return the invoice data.
		return invoiceData;
	}

	/**
	 * Generates a new invoice number.
	 * 
	 * @return the new invoice number generated.
	 * @throws BusinessException if any error occurs during the execution of the
	 *             method.
	 */
	private Long generateNewInvoiceNumber() throws BusinessException {

		InvoicesGateway invoicesGW = PersistenceFactory.getInvoicesGateway();
		invoicesGW.setConnection( connection );

		return invoicesGW.ultimoNumeroFactura();
	}

	/**
	 * Gets the last generated key.
	 * 
	 * @param invoiceNumber is the invoice identifier.
	 * @return the generated key for that invoice number.
	 * @throws BusinessException if any error occurs during the execution of the
	 *             method.
	 */
	private long getGeneratedKey( long invoiceNumber ) throws BusinessException {

		InvoicesGateway invoicesGW = PersistenceFactory.getInvoicesGateway();
		invoicesGW.setConnection( connection );

		return invoicesGW.getGeneratedKey( invoiceNumber );
	}

	/**
	 * Gets the total costs of human labor for the given failure.
	 * 
	 * @param failureId is the failure identifier for which we want to check the
	 *            human labor costs.
	 * @return the human labor costs for the given failure.
	 * @throws BusinessException if any error occurs during the execution of the
	 *             method.
	 */
	private double getHumanCostsForFailure( Long failureId ) throws BusinessException {

		FailuresGateway failuresGW = PersistenceFactory.getFailuresGateway();
		failuresGW.setConnection( connection );

		return failuresGW.getHumanCostsForFailure( failureId );

	}

	/**
	 * Gets the total costs of the replacements used to fix the failure.
	 * 
	 * @param failureId id the failure id to checks the costs of replacements.
	 * @return the total costs of replacements used in this failure.
	 * @throws BusinessException if any error occurs during the execution of the
	 *             method.
	 */
	private double getReplamentCostsForFailure( Long failureId ) throws BusinessException {

		FailuresGateway failuresGW = PersistenceFactory.getFailuresGateway();
		failuresGW.setConnection( connection );

		return failuresGW.getReplacementCostsForFailure( failureId );
	}

	/**
	 * Links the each failure with the invoice where the are billed.
	 * 
	 * @param invoiceId is the invoice where the failures are included.
	 * @param failuresIds are the id's of the failures included in the invoice.
	 * @throws BusinessException if any error occurs during the execution of the
	 *             method.
	 */
	private void linkInvoiceWithFailures( long invoiceId, List<Long> failuresIds )
			throws BusinessException {

		InvoicesGateway invoicesGW = PersistenceFactory.getInvoicesGateway();
		invoicesGW.setConnection( connection );

		invoicesGW.linkInvoiceWithFailures( invoiceId, failuresIds );
	}

	/**
	 * Updates the amount of a given failure.
	 * 
	 * @param failureId is the id of the failure to update.
	 * @param failureTotal is the new amount of the failure.
	 * @throws BusinessException if any error occurs during the execution of the
	 *             method.
	 */
	private void updateAmountForFailure( Long failureId, double failureTotal )
			throws BusinessException {

		FailuresGateway failuresGW = PersistenceFactory.getFailuresGateway();
		failuresGW.setConnection( connection );

		failuresGW.updateAmountForFailure( failureId, failureTotal );

	}

	/**
	 * Verifies that all the failures who's id's are in the list provided are
	 * finished.
	 * 
	 * @param failuresIds is a list containing all the id's of failures to check
	 *            that are finished.
	 * @throws BusinessException if any error occurs during the execution of the
	 *             method.
	 */
	private void verifyFailuresAreFinished( List<Long> failuresIds ) throws BusinessException {

		FailuresGateway failuresGW = PersistenceFactory.getFailuresGateway();
		failuresGW.setConnection( connection );

		failuresGW.verifyFailuresAreFinished( failuresIds );
	}
}
