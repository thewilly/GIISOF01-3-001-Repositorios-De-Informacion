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
 * AddClientAction.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class AddClientAction implements Action {

    /*
     * (non-Javadoc)
     * 
     * @see alb.util.menu.Action#execute()
     */
    @Override
    public void execute() throws Exception {
	ClientDto dto = gettingDtoInfo();
	Long idRecomendador = getIdRecomendador();

	ForemanService foreman = Factory.service.forForeman();
	foreman.addClient(dto, idRecomendador);
	Console.println("Cliente añadido correctamente");
    }

    /**
     * This method is the one in charge of asking the user if there will be a
     * recommender, and then returning that value.
     *
     * @return the value of the recommender's id, or null if it has none
     */
    private Long getIdRecomendador() {
	String ans = "";
	do {
	    Console.println("\n¿Viene recomendado? Escribe SI o NO:");
	    ans = Console.readString("Respuesta");
	} while (validAnswer(ans));

	if (ans.equalsIgnoreCase("si")) {
	    return Console.readLong("Id recomendador");
	}
	return null;
    }

    /**
     * This method gets all the information of the dto from the console.
     *
     * @return the dto
     */
    private ClientDto gettingDtoInfo() {
	ClientDto dto = new ClientDto();
	dto.name = Console.readString("Nombre");
	dto.surname = Console.readString("Apellidos");
	dto.dni = Console.readString("DNI");
	dto.email = Console.readString("E-Mail");
	dto.phone = Console.readString("Teléfono");
	dto.addressStreet = Console.readString("Calle");
	dto.addressCity = Console.readString("Ciudad");
	dto.addressZipcode = Console.readString("Código postal");
	return dto;
    }

    /**
     * This method checks if the answer is yes or no, if not, it will be asked
     * again.
     *
     * @param ans
     *            the answer of th user
     * @return false if the answer was yes or no, true otherwise
     */
    private boolean validAnswer(String ans) {
	if (ans.equalsIgnoreCase("no") || ans.equalsIgnoreCase("si")) {
	    return false;
	}
	return true;
    }

}
