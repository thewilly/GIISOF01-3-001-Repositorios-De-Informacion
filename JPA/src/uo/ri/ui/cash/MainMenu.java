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
package uo.ri.ui.cash;

import alb.util.menu.BaseMenu;
import alb.util.menu.NotYetImplementedAction;
import uo.ri.business.impl.BusinessServiceFactory;
import uo.ri.conf.Factory;
import uo.ri.persistence.jpa.JpaRepositoryFactory;
import uo.ri.persistence.jpa.executor.JpaExecutorFactory;
import uo.ri.ui.cash.action.FacturarReparacionesAction;
import uo.ri.ui.cash.action.FindInvoiceByNumberAction;
import uo.ri.ui.cash.action.ReparacionesNoFacturadasUnClienteAction;
import uo.ri.ui.cash.action.SettleInvoiceAction;

/**
 * MainMenu.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class MainMenu extends BaseMenu {

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
	new MainMenu().config().execute();
    }

    /**
     * Instantiates a new main menu.
     */
    public MainMenu() {
	menuOptions = new Object[][] { { "Caja de Taller", null },
		{ "Buscar reparaciones no facturadas de un cliente",
			ReparacionesNoFacturadasUnClienteAction.class },
		{ "Buscar reparación por matrícula",
			NotYetImplementedAction.class },
		{ "Facturar reparaciones", FacturarReparacionesAction.class },
		{ "Liquidar factura", SettleInvoiceAction.class },
		{ "Buscar factura", FindInvoiceByNumberAction.class },
		{ "Gestión de medios de pago", MediosPagoMenu.class }, };
    }

    /**
     * Configures the main components of the application.
     *
     * @return this
     */
    private MainMenu config() {
	Factory.service = new BusinessServiceFactory();
	Factory.repository = new JpaRepositoryFactory();
	Factory.executor = new JpaExecutorFactory();

	return this;
    }

}
