package uo.ri.ui.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import alb.util.console.Console;

/**
 * PaymentMethodsPrinter.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class PaymentMethodsPrinter extends Printer {

	/**
	 * Allocates a [] object and initializes it so that it represents 
	 * @param listMaps
	 */
	public PaymentMethodsPrinter( List<Map<String, Object>> listMaps ) {
		super.listMaps = listMaps;
		print();
	}

	@Override
	protected void print() {
		for (int i = 0; i < listMaps.size(); i++) {
			String tipo = "";
			double acumulado = 0;
			int id = 0;
			String aux = "";
			Iterator<Entry<String, Object>> it = listMaps.get( i ).entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Object> pair = (Map.Entry<String, Object>) it.next();
				if (pair.getKey().equals( "tipo" )) {
					tipo = (String) pair.getValue();
				} else if (pair.getKey().equals( "acumulado" )) {
					acumulado = (double) pair.getValue();
				} else if (pair.getKey().equals( "id" )) {
					id = (int) pair.getValue();
				}
			}
			it.remove();
			Iterator<Entry<String, Object>> it2 = listMaps.get( i ).entrySet().iterator();
			if (tipo != "") {
				while (it2.hasNext()) {
					Map.Entry<String, Object> pair2 = (Map.Entry<String, Object>) it2.next();
					if (pair2.getKey().length() > tipo.length()
							&& pair2.getKey().substring( 0, tipo.length() ).equals( tipo )) {
						aux += pair2.getKey().substring( tipo.length() ) + ": " + pair2.getValue()
								+ " ";
					}
				}
			}
			Console.printf( "\t ID: %d Tipo: %s Acumulado: %.2f %s\n", id, tipo, acumulado, aux );
			it2.remove();
		}
	}
}
