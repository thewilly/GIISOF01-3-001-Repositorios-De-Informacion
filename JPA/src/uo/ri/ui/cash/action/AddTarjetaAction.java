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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.CashService;
import uo.ri.business.dto.CardDto;
import uo.ri.conf.Factory;
import uo.ri.util.exception.BusinessException;

/**
 * AddTarjetaAction.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class AddTarjetaAction implements Action {

    /*
     * (non-Javadoc)
     * 
     * @see alb.util.menu.Action#execute()
     */
    @Override
    public void execute() throws BusinessException {
	Long id = Console.readLong("Id cliente");
	String tipo = Console.readString("Tipo");
	String numero = Console.readString("Número");
	String fecha = Console.readString("Fecha validez (yyyy-mm-dd)");

	String FORMAT = "yyyy-MM-dd";
	DateFormat formatter = new SimpleDateFormat(FORMAT);
	Date d = null;
	try {
	    d = formatter.parse(fecha);
	} catch (ParseException e) {
	    throw new BusinessException(e);
	}

	CardDto dto = new CardDto();
	dto.clientId = id;
	dto.cardNumber = numero;
	dto.cardExpirationDate = d;
	dto.cardType = tipo;

	CashService cs = Factory.service.forCash();
	cs.addCardPaymentMean(dto);

	Console.println("Nuevo tarjeta añadida a " + id);
    }

}
