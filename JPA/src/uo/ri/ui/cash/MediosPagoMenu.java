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
import uo.ri.ui.cash.action.DeleteMedioPagoAction;
import uo.ri.ui.cash.action.ListMediosPagoFromClientAction;

/**
 * MediosPagoMenu.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class MediosPagoMenu extends BaseMenu {

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     */
    public static void main(String[] args) {
	new MainMenu().execute();
    }

    /**
     * Instantiates a new medios pago menu.
     */
    public MediosPagoMenu() {
	menuOptions = new Object[][] {
		{ "Caja de Taller > Gestión de medios de pago ", null },
		{ "Dar de alta un medio de pago a un cliente",
			AddMedioPagoMenu.class },
		{ "Dar de baja un medio de pago", DeleteMedioPagoAction.class },
		{ "Listar medios de pago de un cliente",
			ListMediosPagoFromClientAction.class }, };
    }

}
