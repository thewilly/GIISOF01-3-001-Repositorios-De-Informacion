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

import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.impl.Command;
import uo.ri.business.impl.util.DtoAssembler;
import uo.ri.business.repository.AveriaRepository;
import uo.ri.business.repository.FacturaRepository;
import uo.ri.conf.Factory;
import uo.ri.model.Averia;
import uo.ri.model.Factura;
import uo.ri.util.exception.BusinessException;

/**
 * CreateInvoiceForFailures.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class CreateInvoiceForFailures implements Command<InvoiceDto> {

    /** The failures ids. */
    private List<Long> failuresIds;

    /**
     * Instantiates a new creates the invoice for failures.
     *
     * @param failuresIds
     *            the failures ids
     */
    public CreateInvoiceForFailures(List<Long> failuresIds) {
	this.failuresIds = failuresIds;
    }

    /*
     * (non-Javadoc)
     * 
     * @see uo.ri.business.impl.Command#execute()
     */
    @Override
    public InvoiceDto execute() throws BusinessException {
	FacturaRepository InvoiceRepository = Factory.repository.forFactura();
	AveriaRepository FailuresRepository = Factory.repository.forAveria();
	Long invoiceNumber = InvoiceRepository.getNextInvoiceNumber();
	List<Averia> failures = FailuresRepository.findByIds(failuresIds);
	Factura invoice = new Factura(invoiceNumber, failures);
	InvoiceRepository.add(invoice);
	return DtoAssembler.toDto(invoice);
    }

}
