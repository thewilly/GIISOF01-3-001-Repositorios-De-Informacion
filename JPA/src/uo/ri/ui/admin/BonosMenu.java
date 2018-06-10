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
package uo.ri.ui.admin;

import alb.util.menu.BaseMenu;
import uo.ri.ui.admin.action.GenerateVouchersAutomaticAction;
import uo.ri.ui.admin.action.ListBonosAction;
import uo.ri.ui.admin.action.ListBonosClientsAction;

/**
 * This class is the one in charge of giving the user options to do in the
 * application. It will redirect to the corresponding class it is selected. in
 * this case the menu is just for bonus in the system and the operations to do
 * with them.
 * 
 * @author uo250878
 *
 */
public class BonosMenu extends BaseMenu {

	/**
	 * Instantiates a new bonos menu.
	 */
	public BonosMenu() {
		menuOptions = new Object[][] { { "Administrador > Gestión de bonos", null },

				{ "Generacion automática de bonos", GenerateVouchersAutomaticAction.class },
				{ "Listado de bonos de un cliente", ListBonosClientsAction.class },
				{ "Listado de todos los bonos", ListBonosAction.class }, };
	}

}