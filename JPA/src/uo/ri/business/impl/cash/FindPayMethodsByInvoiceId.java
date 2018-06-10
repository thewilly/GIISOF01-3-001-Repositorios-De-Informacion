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
package uo.ri.business.impl.cash;

import java.util.List;
import java.util.Set;

import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.impl.Command;
import uo.ri.business.repository.FacturaRepository;
import uo.ri.conf.Factory;
import uo.ri.model.Averia;
import uo.ri.model.Cliente;
import uo.ri.model.Factura;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.Check;

/**
 * FindPayMethodsByInvoiceId.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class FindPayMethodsByInvoiceId implements Command<List<PaymentMeanDto>> {

	/** The invoice id. */
	private Long invoiceId;
	
	/** The invoices repository. */
	private FacturaRepository invoicesRepository = Factory.repository.forFactura();

	/**
	 * Instantiates a new find pay methods by invoice id.
	 *
	 * @param invoiceId the invoice id
	 */
	public FindPayMethodsByInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.impl.Command#execute()
	 */
	@Override
	public List<PaymentMeanDto> execute() throws BusinessException {
		Factura factura = invoicesRepository.findById(invoiceId);
		assertInvoiceExists(factura);
		Set<Averia> averias = factura.getAverias();
		Cliente cliente = getClientFromBreakdowns(averias);
		List<PaymentMeanDto> mediosPago = Factory.service.forCash().findPaymentMeansByClientId(cliente.getId());
		return mediosPago;
	}

	/**
	 * This method gets the client from all the breakdowns the invoice has
	 * associated to it.
	 *
	 * @param averias the breakdowns
	 * @return the client associated to the first breakdown
	 * @throws BusinessException the business exception
	 */
	private Cliente getClientFromBreakdowns(Set<Averia> averias) throws BusinessException {
		Check.isFalse(averias.isEmpty(), "La factura no tiene averias asociadas");
		for (Averia averia : averias) {
			return averia.getVehiculo().getCliente();
		}
		return null;
	}

	/**
	 * This method checks that the invoice exists in the system.
	 *
	 * @param factura the invoice
	 * @throws BusinessException the business exception
	 */
	private void assertInvoiceExists(Factura factura) throws BusinessException {
		Check.isNotNull(factura, "La factura no existe");
	}

}
