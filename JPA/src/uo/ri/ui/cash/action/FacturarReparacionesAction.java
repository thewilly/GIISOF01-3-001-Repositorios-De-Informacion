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

import java.util.ArrayList;
import java.util.List;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.CashService;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.conf.Factory;
import uo.ri.ui.util.AbstractPrinter;
import uo.ri.ui.util.InvoicePrinter;
import uo.ri.util.exception.BusinessException;

/**
 * FacturarReparacionesAction.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class FacturarReparacionesAction implements Action {

    /*
     * (non-Javadoc)
     * 
     * @see alb.util.menu.Action#execute()
     */
    @Override
    public void execute() throws BusinessException {
	List<Long> idsAveria = new ArrayList<Long>();

	// pedir las averias a incluir en la factura
	do {
	    Long id = Console.readLong("ID de averia");
	    idsAveria.add(id);
	} while (masAverias());

	CashService cs = Factory.service.forCash();
	InvoiceDto factura = cs.createInvoiceFor(idsAveria);

	new AbstractPrinter(new InvoicePrinter(factura)).print();
    }

    /**
     * Mas averias.
     *
     * @return true, if successful
     */
    private boolean masAverias() {
	return Console.readString("¿Añadir más averias? (s/n) ")
		.equalsIgnoreCase("s");
    }

}
