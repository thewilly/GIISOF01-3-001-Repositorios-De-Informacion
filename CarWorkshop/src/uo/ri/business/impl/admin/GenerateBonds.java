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
package uo.ri.business.impl.admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static alb.util.jdbc.Jdbc.*;
import uo.ri.common.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.FailuresGateway;
import uo.ri.persistence.BondsGateway;
import uo.ri.persistence.ClientsGateway;
import uo.ri.persistence.PaymentMethodsGateway;

/**
 * 
 * GenerateBonos.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201805072358
 * @since 201805072358
 * @formatter Oviedo Computing Community
 */
public class GenerateBonds {

	private static final int NUMBER_OF_FAILURES_PER_BOND = 3;

	/**
	 * Generates a new bond in the table of PAYMENTS_METHOD and updates the
	 * relation of FAILURES to persist that the failure it is included in the
	 * generated bond.
	 * 
	 * @param failuresIds is a list containing all the failures id's for the
	 *            given client.
	 * @param clientId if the id of the client for whom the bonds are going to
	 *            be generated.
	 * @param connection to be used to connect to the database.
	 * @throws BusinessException if any error occurs during the execution of the
	 *             method.
	 */
	private void computeBondForEach3Failures( List<Long> failuresIds, Long clientId,
			Connection connection )
			throws BusinessException {

		// Creating the gateways and setting the connection.
		FailuresGateway failuresGW = PersistenceFactory.getFailuresGateway();
		PaymentMethodsGateway paymentMethodGW = PersistenceFactory.getPaymentMethodsGateway();
		failuresGW.setConnection( connection );
		paymentMethodGW.setConnection( connection );

		// Computing the number of failures for the client.
		int numberOfFailures = failuresIds.size()
				- ( failuresIds.size() % NUMBER_OF_FAILURES_PER_BOND );

		// Computing the corresponding number of bonds given the number of
		// failures per bond.
		int numberOfBonds = numberOfFailures / NUMBER_OF_FAILURES_PER_BOND;

		// For each failure, update the persist data to say it is used in the
		// bond.
		for (int i = 0; i < numberOfFailures; i++) {
			failuresGW.setFailureAsBondUsed( failuresIds.get( i ) );
		}

		// For the number of bonds to create we create and insert a bond in the
		// database with the computed code. The computed code its BXXXXX+1.
		for (int i = 0; i < numberOfBonds; i++) {
			paymentMethodGW.createBonos( clientId, "B" +
					( String.valueOf( Integer.valueOf(
							paymentMethodGW.getLastBonoCode().substring( 1 ) ) + 1 ) ) );
		}

	}

	/**
	 * Will automatically generate the corresponding bonuses for all registered
	 * clients. Those are the conditions to generate bonds.
	 * 
	 * For each client that has recommended 3 other clients (who have made some
	 * repair in the Workshop) a bonus of € 25 will be issued, with no limit on
	 * the number of bonuses that can be obtained. It is necessary that the
	 * client to whom a bond is to be issued has previously made some repair in
	 * the Workshop. The bonds of this type carry the description "By
	 * recommendation".
	 * 
	 * For every 3 breakdowns repaired in the Workshop (and charged) you will be
	 * issued a € 20 bonus. No limit on the number of bonuses you can get. The
	 * description of this type of bonus will be "For three breakdowns".
	 * 
	 * @throws BusinessException if any error occurs during the execution of the
	 *             method.
	 */
	public void execute() throws BusinessException {

		// Instantiate the connection.
		Connection connection = null;

		// Instantiate necessary list to generate bonds.
		List<Long> clientsIds, vehiclesIds, failuresIds, allFailuresforClient;

		try {
			// Getting the connection.
			connection = getConnection();

			// Creating the needed gateways and setting the connections.
			BondsGateway bondsGW = PersistenceFactory.getBondsGateway();
			ClientsGateway clientsGW = PersistenceFactory.getClientsGateway();
			bondsGW.setConnection( connection );
			clientsGW.setConnection( connection );

			// clientsIds will be a list with all the id's of the clients
			// registered in the system.
			clientsIds = clientsGW.findAllClientsIds();

			// For each client id in the previous list of id's
			for (Long clientId : clientsIds) {
				// New empty list to add all the failures id's.
				allFailuresforClient = new ArrayList<Long>();

				// Another list for all the vehicles id's of the client.
				vehiclesIds = bondsGW.findVehiclesIdsByClientId( clientId );

				// For each vehicle id of the client.
				for (Long vehicleId : vehiclesIds) {
					// Get all the failures of that vehicle.
					failuresIds = bondsGW.findFailuresIdsByVehicleId( vehicleId );

					// And for each failure of that vehicle.
					for (Long failureId : failuresIds) {
						// Added to all failures list.
						allFailuresforClient.add( failureId );
					}
				}

				// Finally we create the bond the the list of all failure of the
				// client.
				computeBondForEach3Failures( allFailuresforClient, clientId, connection );
			}

		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {
			// Closing the connection.
			close( connection );
		}
	}
}
