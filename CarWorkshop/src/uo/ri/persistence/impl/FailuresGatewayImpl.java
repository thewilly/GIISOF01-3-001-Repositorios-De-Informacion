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
package uo.ri.persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static alb.util.jdbc.Jdbc.*;
import uo.ri.common.BusinessException;
import uo.ri.conf.Conf;
import uo.ri.persistence.FailuresGateway;

/**
 * FailuresGatewayImpl.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class FailuresGatewayImpl implements FailuresGateway {

	private Connection connection;

	
	// PUBLIC MEMBERS
	
	
	@Override
	public void checkAllFailuresAreFinished( List<Long> failuresIds ) throws BusinessException {
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {

			// Preparing the statement with the stored query to check the status
			// of the failures.
			pst = connection.prepareStatement( Conf.get( "SQL_VERIFICAR_ESTADO_AVERIA" ) );

			// For each failure...
			for (Long failureId : failuresIds) {

				// Set the parameter as the id of the current failure
				pst.setLong( 1, failureId );

				// Execute the query
				rs = pst.executeQuery();

				// If the result is empty means that the failure id is not in
				// the system.
				if (rs.next() == false) {
					throw new BusinessException( "No existe la averia " + failureId );
				}

				// Checking that the failure is "TERMINADA". Else throw an
				// exception.
				if (!"TERMINADA".equalsIgnoreCase( rs.getString( 1 ) )) {
					throw new BusinessException( "No está terminada la avería " + failureId );
				}

				// Closing the result set.
				rs.close();
			}
		} catch (SQLException e) {
			throw new BusinessException( e );
		} finally {
			// Closing result set and prepared statement.
			close( rs, pst );
		}
	}

	@Override
	public double getCostForFailures( List<Long> failuresIds ) throws BusinessException {
		double invoiceTotalAmount = 0.0;

		// For each failure in the list of failures...
		for (Long failureId : failuresIds) {

			// Compute the hand labor costs.
			double handWorkCosts = getHandWorkCosts( failureId );

			// Compute the replacement pieces costs.
			double replacementCosts = getReplacementCosts( failureId );

			// Compute the total amount of the failure.
			double failureTotalAmount = handWorkCosts + replacementCosts;

			// Update the costs of the failure in the DB.
			updateFailureCosts( failureId, failureTotalAmount );

			// Finally add the cost of the failure to the invoice.
			invoiceTotalAmount += failureTotalAmount;
		}

		// Return the invoice total costs.
		return invoiceTotalAmount;
	}

	@Override
	public void linkInvoiceWithFailures( long invoiceId, List<Long> failuresIds ) {
	
		PreparedStatement pst = null;
	
		try {
	
			// Getting the query.
			pst = connection.prepareStatement( Conf.get( "SQL_VINCULAR_AVERIA_FACTURA" ) );
	
			// For each failure in the list of failures...
			for (Long failureId : failuresIds) {
	
				// We set the parameters for the query.
				pst.setLong( 1, invoiceId );
				pst.setLong( 2, failureId );
	
				// And execute the query.
				pst.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {
	
			// Closing the connection and the prepared statement.
			close( connection );
			close( pst );
		}
	}

	@Override
	public void setConnection( Connection connection ) {
		this.connection = connection;
	}

	@Override
	public void setFailureStatus( List<Long> failuresIds, String newStatus ) {
	
		PreparedStatement pst = null;
	
		try {
	
			// Getting the query
			pst = connection.prepareStatement( Conf.get( "SQL_ACTUALIZAR_ESTADO_AVERIA" ) );
	
			// For each failure in the list of failures...
			for (Long failureId : failuresIds) {
	
				// Setting the parameters of the query.
				pst.setString( 1, newStatus );
				pst.setLong( 2, failureId );
	
				// Executing the query.
				pst.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {
	
			// Closing the connection and the prepared statement.
			close( connection );
			close( pst );
		}
	}
	
	
	// PRIVATE MEMBERS
	

	/**
	 * Computes and gets the costs derived from the hand work of mechanics.
	 * 
	 * @param failureId is the failureId to compute the hand work costs.
	 * @return the costs derived from the hand work of mechanics.
	 * @throws BusinessException is any error during the execution of the
	 *             method.
	 */
	private double getHandWorkCosts( Long failureId ) throws BusinessException {
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {

			// Preparing the query.
			pst = connection.prepareStatement( Conf.get( "SQL_IMPORTE_MANO_OBRA" ) );

			// Adding the parameter to the query.
			pst.setLong( 1, failureId );

			// Executing the query.
			rs = pst.executeQuery();

			// If the result set is empty we throw the exception.
			if (rs.next() == false) {
				throw new BusinessException( "La averia no existe o no se puede facturar" );
			}

			// Then we return the hand work costs.
			return rs.getDouble( 1 );

		} catch (BusinessException exception) {
			throw exception;
		} catch (SQLException exception) {
			throw new BusinessException( exception );
		} finally {

			// We close the result set the prepared statement and the
			// connection.
			close( rs, pst, connection );
		}
	}

	/**
	 * Computes and gets the replacement costs for the given failure.
	 * 
	 * @param failureId is the unique id of the failure to compute the
	 *            replacement costs.
	 * @return the replacement costs for the given failure.
	 * @throws BusinessException is any error during the execution of the
	 *             method.
	 */
	private double getReplacementCosts( Long failureId ) throws BusinessException {
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {

			// Preparing the query.
			pst = connection.prepareStatement( Conf.get( "SQL_IMPORTE_REPUESTOS" ) );

			// Setting the parameters of the query.
			pst.setLong( 1, failureId );

			// Executing the query.
			rs = pst.executeQuery();

			// If there are no replacements we return 0.0 that is the empty
			// replacement value.
			if (rs.next() == false) {
				return 0.0;
			}

			// Else we return the price of the replacements.
			return rs.getDouble( 1 );

		} catch (SQLException e) {
			throw new BusinessException( e );
		} finally {

			// We close the result set the prepared statement and the
			// connection.
			close( rs, pst, connection );
		}
	}

	/**
	 * Updates the amount billed for a given failure.
	 * 
	 * @param failureId is the unique identifier of the failure to update.
	 * @param failureTotalAmount is the total amount to update in the failure.
	 */
	private void updateFailureCosts( Long failureId, double failureTotalAmount ) {

		PreparedStatement pst = null;

		try {

			// Getting the query.
			pst = connection.prepareStatement( Conf.get( "SQL_UPDATE_IMPORTE_AVERIA" ) );

			// Setting the parameters of the query.
			pst.setDouble( 1, failureTotalAmount );
			pst.setLong( 2, failureId );

			// Executing the update.
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {

			// Closing the connection and the prepared statement.
			close( connection );
			close( pst );
		}
	}
}