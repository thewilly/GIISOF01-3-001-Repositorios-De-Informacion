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

import java.util.ArrayList;
import java.util.List;

import uo.ri.business.dto.VoucherSummary;
import uo.ri.business.impl.Command;
import uo.ri.business.repository.ClienteRepository;
import uo.ri.business.repository.MedioPagoRepository;
import uo.ri.conf.Factory;
import uo.ri.model.Cliente;
import uo.ri.util.exception.BusinessException;

/**
 * FindAllBonds.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class FindAllBonds implements Command<List<VoucherSummary>> {

	/** The clients repository. */
	private ClienteRepository clientsRepository = Factory.repository.forCliente();
	
	/** The payment method repository. */
	private MedioPagoRepository paymentMethodRepository = Factory.repository.forMedioPago();

	/* (non-Javadoc)
	 * @see uo.ri.business.impl.Command#execute()
	 */
	@Override
	public List<VoucherSummary> execute() throws BusinessException {
		List<Cliente> clients = clientsRepository.findAll();
		return getVouchers(clients);
	}

	/**
	 * This method traverses all the list of clients of the system, and adds it
	 * to the list of voucher summaries.
	 *
	 * @param clients the list of clients
	 * @return the list containing all the voucher summary list
	 */
	private List<VoucherSummary> getVouchers(List<Cliente> clients) {
		List<VoucherSummary> vouchers = new ArrayList<>();
		for (Cliente client : clients) {
			Object[] clientInformation = paymentMethodRepository.findAggregateVoucherDataByClientId(client.getId());
			VoucherSummary clientVoucher = new VoucherSummary();
			clientVoucher.name = client.getNombre();
			clientVoucher.surname = client.getApellidos();
			clientVoucher.dni = client.getDni();
			clientVoucher.emitted = (int) clientInformation[0];
			if (clientVoucher.emitted > 0) {
				clientVoucher.available = (double) clientInformation[1];
				clientVoucher.consumed = (double) clientInformation[2];
			} else {
				clientVoucher.available = 0.0;
				clientVoucher.consumed = 0.0;
			}
			clientVoucher.totalAmount = clientVoucher.available + clientVoucher.consumed;

			vouchers.add(clientVoucher);
		}
		return vouchers;
	}
}
