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
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package uo.ri.ui.foreman;

import alb.util.menu.BaseMenu;
import uo.ri.ui.foreman.action.AddClientAction;
import uo.ri.ui.foreman.action.DeleteClientAction;
import uo.ri.ui.foreman.action.ListClientsAction;
import uo.ri.ui.foreman.action.ListClientsByRecomendatorAction;
import uo.ri.ui.foreman.action.ShowClientDetailAction;
import uo.ri.ui.foreman.action.UpdateClientAction;

public class ClientesMenu extends BaseMenu {

	public ClientesMenu() {
		menuOptions = new Object[][] {
				{ "Jefe de Taller > Gestión de Clientes", null },

				{ "Añadir cliente", AddClientAction.class },
				{ "Modificar datos de cliente", UpdateClientAction.class },
				{ "Eliminar cliente", DeleteClientAction.class },
				{ "Mostrar detalles de un cliente", ShowClientDetailAction.class },
				{ "Listar clientes", ListClientsAction.class },
				{ "Listar clientes recomendados por otro cliente",
						ListClientsByRecomendatorAction.class },
		};
	}

}