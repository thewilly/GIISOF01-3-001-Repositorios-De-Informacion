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
package uo.ri.ui.cash.action;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.CashService;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.conf.Factory;
import uo.ri.ui.util.AbstractPrinter;
import uo.ri.util.exception.BusinessException;

/**
 * This class is the one that is called in the menu when an option that does not
 * lead you to another menu takes you. Just for the menu of the cash register.
 * In this case, the action of the class is to settle an invoice by its id, it
 * will be shown first the payment methods of the client associated with the
 * invoice, so all the charges can be made and divided.
 * 
 * @author uo250878
 *
 */
public class SettleInvoiceAction implements Action {

	/** The cs. */
	private CashService cs = Factory.service.forCash();

	/* (non-Javadoc)
	 * @see alb.util.menu.Action#execute()
	 */
	@Override
	public void execute() throws Exception {
		Long numeroFactura = Console.readLong("Número de factura");
		InvoiceDto facturaDto = cs.findInvoiceByNumber(numeroFactura);
		List<PaymentMeanDto> paymethods = possiblePaymentMethods(facturaDto);
		Map<Long, Double> cargos = getAllThePaymentDivision(paymethods);
		cs.settleInvoice(facturaDto.id, cargos);
		Console.println("Factura pagada");
	}

	/**
	 * This method takes all the possible payment methods id's of the client and
	 * adds then into a set.
	 * 
	 * @param paymethods
	 *            the possible payment methods of the client
	 * @return a set containing all the ids of the possible payment methods
	 */
	private Set<Long> getAllPossiblePaymentMethodIds(List<PaymentMeanDto> paymethods) {
		Set<Long> idsPaymentMethods = new HashSet<>();
		for (PaymentMeanDto dto : paymethods) {
			idsPaymentMethods.add(dto.id);
		}
		return idsPaymentMethods;
	}

	/**
	 * This method asks the user for all the charges division, and checks it is
	 * inside the ids of all payment methods the client has.
	 * 
	 * @param paymethods
	 *            all the possible payment methods the client has
	 * @return a map containing the id of the payment method and the quantity
	 *         for that
	 */
	private Map<Long, Double> getAllThePaymentDivision(List<PaymentMeanDto> paymethods) {
		Set<Long> idsPaymentMethods = getAllPossiblePaymentMethodIds(paymethods);
		Map<Long, Double> cargos = new HashMap<>();
		Console.println("\nSi no quiere añadir más ids de pago, pulse enter");
		boolean done = false;
		while (!done) {
			Long ans = Console.readLong("Id metodo de pago");
			if (ans == null) {
				done = true;
			} else {
				if (idsPaymentMethods.contains(ans)) {
					Double cantidad = Console.readDouble("Cantidad");
					if (cantidad != null) {
						if (cantidad > 0.0)
							cargos.put(ans, cantidad);
					}
				}
			}
		}
		return cargos;
	}

	/**
	 * This method uses the cash service, to obtain the possible payment
	 * methods, and shows them into the console.
	 *
	 * @param facturaDto the dto containing all the information of the invoice
	 * @return a list of all the possible payments of the client associated with
	 *         the invoice
	 * @throws BusinessException the business exception
	 */
	private List<PaymentMeanDto> possiblePaymentMethods(InvoiceDto facturaDto) throws BusinessException {
		List<PaymentMeanDto> paymethods = cs.findPaymentMeansForInvoice(facturaDto.id);
		AbstractPrinter.printInvoice(cs.findInvoiceByNumber(facturaDto.number));
		Console.println("\nPosibles metodos de pago:");
		AbstractPrinter.printPaymentMeans(paymethods);
		return paymethods;
	}

}
