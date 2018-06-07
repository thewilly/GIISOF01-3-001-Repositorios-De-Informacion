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
package uo.ri.ui.cash.action;

import java.util.ArrayList;
import java.util.List;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.CashService;
import uo.ri.common.BusinessException;
import uo.ri.conf.ServicesFactory;
import uo.ri.ui.util.InvoicePrinter;

/**
 * CreateInvoiceForFailuresAction.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class CreateInvoiceForFailuresAction implements Action {

	@Override
	public void execute() throws BusinessException {
		List<Long> failuresIds = new ArrayList<Long>();

		// While there're more failures we continue asking for new id's.
		do {
			Long failureId = Console.readLong( "ID de averia" );
			failuresIds.add( failureId );
		} while (areMoreFailures());

		CashService cashService = ServicesFactory.getCashService();

		// Creating the invoice for the failures and printing the invoice.
		new InvoicePrinter( cashService.createInvoiceForFailures( failuresIds ) );
	}

	/**
	 * @return true if the're more failures.
	 */
	private boolean areMoreFailures() {
		return Console.readString( "¿Añadir más averias? (s/n) " ).equalsIgnoreCase( "s" );
	}

}
