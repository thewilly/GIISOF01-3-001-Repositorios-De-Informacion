package uo.ri.ui.util;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import alb.util.console.Console;

public class InvoicePrinter extends Printer {

	public InvoicePrinter( Map<String, Object> invoiceToPrint ) {
		super.map = invoiceToPrint;
		print();
	}
	
	protected void print() {
		long numF = 0;
		Date fecha = null;
		double total = 0.0;
		double iva = 0.0;
		double totalIva = 0.0;

		Iterator<Entry<String, Object>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> pair = (Map.Entry<String, Object>) it.next();
			if (pair.getKey().equals( "numFactura" )) {
				numF = (long) pair.getValue();
			} else if (pair.getKey().equals( "fechaFactura" )) {
				fecha = (Date) pair.getValue();
			} else if (pair.getKey().equals( "totalFactura" )) {
				total = (double) pair.getValue();
			} else if (pair.getKey().equals( "iva" )) {
				iva = (double) pair.getValue();
			} else if (pair.getKey().equals( "importe" )) {
				totalIva = (double) pair.getValue();
			}
		}
		mostrarFactura( numF, fecha, total, iva, totalIva );
		it.remove(); // avoids a ConcurrentModificationException
	}
	
	/**
	 * This method shows the invoice with the parameters given
	 * 
	 * @param numeroFactura
	 * @param fechaFactura
	 * @param totalFactura
	 * @param iva
	 * @param totalConIva
	 */
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
