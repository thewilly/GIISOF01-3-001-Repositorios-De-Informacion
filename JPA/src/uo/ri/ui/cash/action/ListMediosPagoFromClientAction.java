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

import java.util.List;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.CashService;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.conf.Factory;
import uo.ri.ui.util.AbstractPrinter;
import uo.ri.util.exception.BusinessException;

/**
 * This class is the one that is called in the menu when an option that does not
 * lead you to another menu takes you. Just for the menu of the cash register.
 * In this case, the action of the class is listing all the ways of payment of a
 * specific client, by its id. The information will in this case be printed by
 * the Printer class.
 * 
 * @author uo250878
 *
 */
public class ListMediosPagoFromClientAction implements Action {

	/* (non-Javadoc)
	 * @see alb.util.menu.Action#execute()
	 */
	@Override
	public void execute() throws BusinessException {
		Long id = Console.readLong("Id cliente");
		CashService cs = Factory.service.forCash();
		List<PaymentMeanDto> list = cs.findPaymentMeansByClientId(id);
		Console.println("\nListado de medios de pago de cliente con id: " + id);
		AbstractPrinter.printPaymentMeans(list);
	}

}
