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
package uo.ri.ui.cash.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import alb.util.console.Console;
import alb.util.menu.Action;
import uo.ri.common.BusinessException;
import uo.ri.conf.ServicesFactory;

public class FacturarReparacionesAction implements Action {

	public FacturarReparacionesAction() {

	}

	@Override
	public void execute() throws BusinessException {
		List<Long> idsAveria = new ArrayList<Long>();

		do {
			Long id = Console.readLong( "ID de averia" );
			idsAveria.add( id );
		} while (masAverias());

		Map<String, Object> factura = ServicesFactory.getCashService()
				.generateInvoiceForFailures( idsAveria );

		mostrarFactura( (long) factura.get( "numeroFactura" ), (Date) factura.get( "fechaFactura" ),
				(double) factura.get( "totalFactura" ), (double) factura.get( "iva" ),
				(double) factura.get( "importe" ) );

	}

	private boolean masAverias() {
		return Console.readString( "¿Añadir más averias? (s/n) " ).equalsIgnoreCase( "s" );
	}

	private void mostrarFactura( long numeroFactura, Date fechaFactura, double totalFactura,
			double iva,
			double totalConIva ) {

		Console.printf( "Factura nº: %d\n", numeroFactura );
		Console.printf( "\tFecha: %1$td/%1$tm/%1$tY\n", fechaFactura );
		Console.printf( "\tTotal: %.2f €\n", totalFactura );
		Console.printf( "\tIva: %.1f %% \n", iva );
		Console.printf( "\tTotal con IVA: %.2f €\n", totalConIva );
	}

}
