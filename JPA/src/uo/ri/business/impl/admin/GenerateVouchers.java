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
public class GenerateVouchers implements Command<Integer> {

	/** The payment methods repository. */
	private MedioPagoRepository paymentMethodsRepository = Factory.repository.forMedioPago();
	
	/** The clients repository. */
	private ClienteRepository clientsRepository = Factory.repository.forCliente();

	/* (non-Javadoc)
	 * @see uo.ri.business.impl.Command#execute()
	 */
	@Override
	public Integer execute() throws BusinessException {
		List<Cliente> clients = clientsRepository.findAll();
		return createVouchers(clients);
	}

	/**
	 * This method traverse all the list of clients and creates the necessary
	 * vouchers.
	 *
	 * @param clients the list of clients
	 * @return the number of vouchers created
	 * @throws BusinessException the business exception
	 */
	private int createVouchers(List<Cliente> clients) throws BusinessException {
		int vouchersCreated = 0;
		for (Cliente cliente : clients) {
			vouchersCreated += generateVouchers(cliente);
		}
		return vouchersCreated;
	}

	/**
	 * This method generates all the possible vouchers each client could have.
	 *
	 * @param cliente the client
	 * @return the number of vouchers created
	 * @throws BusinessException the business exception
	 */
	private int generateVouchers(Cliente cliente) throws BusinessException {
		int vouchersClient = 0;
		int vouchersCreated = 0;
		if (cliente.elegibleBonoPorRecomendaciones()) {
			vouchersClient = cliente.numberOfVoucherForRecomendations();
			generateVouchersByRecomendation(vouchersClient, cliente);
			vouchersCreated += vouchersClient;
		}

		if (cliente.elegibleBonoPorAverias()) {
			vouchersClient = cliente.numberOfVoucherForBreakdowns();
			generateVouchersByBreakdowns(vouchersClient, cliente);
			vouchersCreated += vouchersClient;
		}

		if (cliente.elegibleBonoPorFactura500()) {
			vouchersClient = cliente.numberOfVoucherForInvoice();
			generateVouchersByInvoice(vouchersClient, cliente);
			vouchersCreated += vouchersClient;
		}
		return vouchersCreated;
	}

	/**
	 * This method generates the corresponding vouchers by having an invoice
	 * over 500 euro.
	 *
	 * @param vouchersClient the number of vouchers to be created
	 * @param cliente the client who will have the new voucher
	 * @throws BusinessException the business exception
	 */
	private void generateVouchersByInvoice(int vouchersClient, Cliente cliente) throws BusinessException {
		for (int i = 0; i < vouchersClient; i++) {
			voucherCreation(cliente, "Por factura superior a 500€", 30.0);
			int count = 0;
			for (Vehiculo vehiculo : cliente.getVehiculos()) {
				if (count == 0) {
					Set<Averia> averias = vehiculo.getAverias();
					for (Averia averia : averias) {
						Factura factura = averia.getFactura();
						if (count == 0 && factura != null && factura.puedeGenerarBono500()) {
							factura.markAsBono500Used();
							count++;
						}

					}
				}
			}
		}
	}

	/**
	 * This method generates the corresponding vouchers by having 3 breakdowns.
	 *
	 * @param vouchersClient the number of vouchers to be created
	 * @param cliente the client who will have the new voucher
	 */
	private void generateVouchersByBreakdowns(int vouchersClient, Cliente cliente) {
		for (int i = 0; i < vouchersClient; i++) {
			voucherCreation(cliente, "Por tres averías", 20.0);
			int count = 0;
			for (Vehiculo vehiculo : cliente.getVehiculos()) {
				if (count < 3) {
					markBreadowns(count, vehiculo);
				}
			}
		}
	}

	/**
	 * This method marks 3 breakdowns as used.
	 *
	 * @param count the count it is used outside
	 * @param vehiculo the vehicle
	 */
	private void markBreadowns(int count, Vehiculo vehiculo) {
		Set<Averia> averias = vehiculo.getAverias();
		for (Averia averia : averias) {
			if (count < 3 && !averia.isUsadaBono3()) {
				averia.markAsBono3Used();
				count++;
			}
		}
	}

	/**
	 * This method generates the corresponding vouchers by recommendation.
	 *
	 * @param vouchersClient the number of vouchers to be created
	 * @param cliente the client who will have the new voucher
	 */
	private void generateVouchersByRecomendation(int vouchersClient, Cliente cliente) {
		for (int i = 0; i < vouchersClient; i++) {
			voucherCreation(cliente, "Por recomendación", 25.0);
			int count = 0;
			for (Recomendacion re : cliente.getRecomendacionesHechas()) {
				if (count < 3 && !re.isUsada()) {
					re.markAsUsadaBono();
					count++;
				}
			}
		}
	}

	/**
	 * This method creates a voucher and link it to the client.
	 *
	 * @param cliente the client who will have a new voucher
	 * @param description the description the voucher will have
	 * @param money the queaantity of money the voucher will have
	 */
	private void voucherCreation(Cliente cliente, String description, double money) {
		Bono b = new Bono(createCodeOfBono(), description, money);
		paymentMethodsRepository.add(b);
		Association.Pagar.link(cliente, b);
	}

	/**
	 * This method generates a new voucher code that does not exists in the
	 * system.
	 *
	 * @return the new code to be added
	 */
	private String createCodeOfBono() {
		String code = "";
		Bono b = null;
		do {
			code = "V-" + Random.string(5) + "-" + Random.integer(1000, 9999);
			b = paymentMethodsRepository.findVoucherByCode(code);
		} while (b != null);
		return code;
	}

}
