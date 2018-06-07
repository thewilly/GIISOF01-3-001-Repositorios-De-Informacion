package uo.ri.ui.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import alb.util.console.Console;

/**
 * BondsPrinter.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class BondsPrinter extends Printer {

	/**
	 * Initializes the bonds printer and prints the list of bonds given.
	 * 
	 * @param bondsToPrint
	 */
	public BondsPrinter( List<Map<String, Object>> bondsToPrint ) {
		super.listMaps = bondsToPrint;
		print();
	}

	/**
	 * 
	 */
	protected void print() {
		for (int i = 0; i < listMaps.size(); i++) {
			String dni = "";
			String name = "";
			int numeroTotal = 0;
			double importeTotal = 0.0;
			double consumidoTotal = 0.0;
			double disponibleTotal = 0.0;
			Iterator<Entry<String, Object>> it = listMaps.get( i ).entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Object> pair = (Map.Entry<String, Object>) it.next();
				if (pair.getKey().equals( "dni" )) {
					dni = (String) pair.getValue();
				} else if (pair.getKey().equals( "name" )) {
					name = (String) pair.getValue();
				} else if (pair.getKey().equals( "numeroTotal" )) {
					numeroTotal = (int) pair.getValue();
				} else if (pair.getKey().equals( "importeTotal" )) {
					importeTotal = (double) pair.getValue();
				} else if (pair.getKey().equals( "consumido" )) {
					consumidoTotal = (double) pair.getValue();
				} else if (pair.getKey().equals( "porConsumir" )) {
					disponibleTotal = (double) pair.getValue();
				}
			}
			Console.printf(
					"\tDni: %s Nombre: %s Número de bonos: %d Importe total: %.2f Consumido total: %.2f "
							+ "Disponible total: %.2f\n",
					dni, name, numeroTotal, importeTotal, consumidoTotal, disponibleTotal );
			it.remove();
		}
	}
}
