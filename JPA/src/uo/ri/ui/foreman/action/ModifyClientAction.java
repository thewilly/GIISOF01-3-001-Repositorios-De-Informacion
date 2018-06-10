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
package uo.ri.ui.foreman.action;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.business.ForemanService;
import uo.ri.business.dto.ClientDto;
import uo.ri.conf.Factory;

/**
 * This class is the one that is called in the menu when an option that does not
 * lead you to another menu takes you. Just for the menu of the foreman. In this
 * case, the action of the class is to modify/update a client and so it reads
 * from the console and creates a dto to transport the information, and then the
 * operation is done thanks to the foreman service.
 * 
 * @author uo250878
 *
 */
public class ModifyClientAction implements Action {

	/* (non-Javadoc)
	 * @see alb.util.menu.Action#execute()
	 */
	@Override
	public void execute() throws Exception {
		ClientDto dto = new ClientDto();
		dto.id = Console.readLong("Id cliente");
		dto.name = Console.readString("Nombre");
		dto.surname = Console.readString("Apellidos");
		dto.dni = Console.readString("DNI");
		dto.email = Console.readString("E-Mail");
		dto.phone = Console.readString("Teléfono");
		dto.addressStreet = Console.readString("Calle");
		dto.addressCity = Console.readString("Ciudad");
		dto.addressZipcode = Console.readString("Código postal");

		ForemanService foreman = Factory.service.forForeman();
		foreman.updateClient(dto);
		Console.println("Datos actualizados correctamente");

	}

}
