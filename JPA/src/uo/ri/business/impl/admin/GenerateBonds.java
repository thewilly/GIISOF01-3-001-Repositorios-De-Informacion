/* 
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package uo.ri.business.impl.admin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;

import alb.util.random.Random;
import uo.ri.business.impl.Command;
import uo.ri.business.repository.ClienteRepository;
import uo.ri.business.repository.MedioPagoRepository;
import uo.ri.conf.Factory;
import uo.ri.model.Association;
import uo.ri.model.Averia;
import uo.ri.model.Bono;
import uo.ri.model.Cliente;
import uo.ri.model.Factura;
import uo.ri.model.Recomendacion;
import uo.ri.model.Vehiculo;
import uo.ri.util.exception.BusinessException;

/**
 * GenerateVouchers.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class GenerateBonds implements Command<Integer> {

	/** The payment methods repository. */
	private MedioPagoRepository paymentMethodsRepository = Factory.repository.forMedioPago();

	/** The clients repository. */
	private ClienteRepository clientsRepository = Factory.repository.forCliente();

	/**
	 * This method creates a voucher and link it to the client.
	 *
	 * @param client the client who will have a new voucher
	 * @param description the description the voucher will have
	 * @param bondAmount the quantity of money the voucher will have
	 */
	private void createBond( Cliente client, String description, double bondAmount ) {
		Bono bond = new Bono( generateBondCode(), description, bondAmount );
		paymentMethodsRepository.add( bond );
		Association.Pagar.link( client, bond );
	}

	/**
	 * This method traverse all the list of clients and creates the necessary
	 * vouchers.
	 *
	 * @param clients the list of clients
	 * @return the number of vouchers created
	 * @throws BusinessException the business exception
	 */
	private int createBondsForClients( List<Cliente> clients ) throws BusinessException {
		int generatedBonds = 0, bondsForClient = 0, generatedBondsForClient = 0;
		for (Cliente client : clients) {
			bondsForClient = 0;
			generatedBondsForClient = 0;
			if (client.elegibleBonoPorRecomendaciones()) {
				bondsForClient = client.numberOfVoucherForRecomendations();
				generateBondsForRecommendations( bondsForClient, client );
				generatedBondsForClient += bondsForClient;
			}

			if (client.elegibleBonoPorAverias()) {
				bondsForClient = client.numberOfVoucherForBreakdowns();
				generateBondsForFailures( bondsForClient, client );
				generatedBondsForClient += bondsForClient;
			}

			if (client.elegibleBonoPorFactura500()) {
				bondsForClient = client.numberOfVoucherForInvoice();
				generateBondsForInvoices( bondsForClient, client );
				generatedBondsForClient += bondsForClient;
			}
			generatedBonds += generatedBondsForClient;
		}
		return generatedBonds;
	}

	/*
	 * (non-Javadoc)
	 * @see uo.ri.business.impl.Command#execute()
	 */
	@Override
	public Integer execute() throws BusinessException {
		List<Cliente> clients = clientsRepository.findAll();
		return createBondsForClients( clients );
	}

	/**
	 * This method generates a new voucher code that does not exists in the
	 * system.
	 *
	 * @return the new code to be added
	 */
	private String generateBondCode() {
		String code = "";
		Bono bond = null;
		do {
			try {
				byte[] hashArray = MessageDigest.getInstance( "MD5" ).digest( Random.string( 30 ).getBytes() );
				StringBuffer buffer =  new StringBuffer();
				for(int i = 0; i < hashArray.length; i++) {
					buffer.append(Integer.toString((hashArray[i] & 0xff) + 0x100, 16)
			                .substring(1));
				}
				code = "B-" + buffer.toString();
				
			} catch (NoSuchAlgorithmException e) {
				// Fall back method in case of error with the algorithm
				code = "B-" + Random.string( 5 ) + "-" + Random.integer( 0, 9999 );
			}
			bond = paymentMethodsRepository.findVoucherByCode( code );
		} while (bond != null);
		return code;
	}

	/**
	 * This method generates the corresponding vouchers by having 3 breakdowns.
	 *
	 * @param bondsToGenerate the number of vouchers to be created
	 * @param client the client who will have the new voucher
	 */
	private void generateBondsForFailures( int bondsToGenerate, Cliente client ) {
		for (int i = 0; i < bondsToGenerate; i++) {
			createBond( client, "Por tres averías", 20.0 );
			int count = 0;
			for (Vehiculo vehicle : client.getVehiculos()) {
				if (count < 3) {
					Set<Averia> failures = vehicle.getAverias();
					for (Averia failure : failures) {
						if (count < 3 && !failure.isUsadaBono3()) {
							failure.markAsBono3Used();
							count++;
						}
					}
				}
			}
		}
	}

	/**
	 * This method generates the corresponding vouchers by having an invoice
	 * over 500 euro.
	 *
	 * @param bondsToGenerate the number of vouchers to be created
	 * @param client the client who will have the new voucher
	 * @throws BusinessException the business exception
	 */
	private void generateBondsForInvoices( int bondsToGenerate, Cliente client )
			throws BusinessException {
		for (int i = 0; i < bondsToGenerate; i++) {
			createBond( client, "Por factura superior a 500€", 30.0 );
			int count = 0;
			for (Vehiculo vehicle : client.getVehiculos()) {
				if (count == 0) {
					Set<Averia> failures = vehicle.getAverias();
					for (Averia failure : failures) {
						Factura invoice = failure.getFactura();
						if (count == 0 && invoice != null && invoice.puedeGenerarBono500()) {
							invoice.markAsBono500Used();
							count++;
						}
					}
				}
			}
		}
	}

	/**
	 * This method generates the corresponding vouchers by recommendation.
	 *
	 * @param bondsToGenerate the number of vouchers to be created
	 * @param client the client who will have the new voucher
	 */
	private void generateBondsForRecommendations( int bondsToGenerate, Cliente client ) {
		for (int i = 0; i < bondsToGenerate; i++) {
			createBond( client, "Por recomendación", 25.0 );
			int count = 0;
			for (Recomendacion re : client.getRecomendacionesHechas()) {
				if (count < 3 && !re.isUsada()) {
					re.markAsUsadaBono();
					count++;
				}
			}
		}
	}

}
