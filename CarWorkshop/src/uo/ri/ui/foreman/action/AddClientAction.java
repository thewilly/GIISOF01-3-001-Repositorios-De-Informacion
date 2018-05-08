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
package uo.ri.ui.foreman.action;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.conf.ServicesFactory;
import uo.ri.common.BusinessException;

public class AddClientAction implements Action {

	@Override
	public void execute() throws BusinessException {

		String nombre = Console.readString( "Nombre" );
		String apellidos = Console.readString( "Apellidos" );
		String dni = Console.readString( "Dni" );
		int zipcode = Console.readInt( "Codigo Postal(número)" );
		int telefono = Console.readInt( "Telefono(número)" );
		String correo = Console.readString( "Correo" );

		String recomendacion = Console
				.readString( "¿Viene usted recomendado por otro cliente?  [s|n]" );

		if (recomendacion.equals( "s" )) {
			long idRecomendador = Console.readLong( "Id del recomendador" );
			ServicesFactory.getForemanService().createRecommendedClient( dni, nombre, apellidos,
					zipcode, telefono,
					correo, idRecomendador );
		} else if (recomendacion.equals( "n" )) {
			ServicesFactory.getForemanService().createClient( dni, nombre, apellidos, zipcode,
					telefono, correo );
		}

		Console.println( "Nuevo cliente añadido" );

	}

}
