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
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
package uo.ri.ui.util;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import alb.util.console.Console;

/**
 * This class is in charge of printing all kinds of maps of data that the system
 * needs to be shown in console.
 * 
 * @author uo250878
 *
 */
public class Printer {

	private Map<String, Object> map;
	private List<Map<String, Object>> listMaps;

	public Printer( Map<String, Object> map ) {
		this.map = map;
	}

	public Printer( List<Map<String, Object>> listMaps ) {
		this.listMaps = listMaps;
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

	/**
	 * This method go over the map field of the class and shows the invoice
	 */
	public void printInvoice() {
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
	 * This method shows the information of the mechanic with the given format
	 */
	public void printMechanics() {
		for (int i = 0; i < listMaps.size(); i++) {
			long id = 0;
			String name = "";
			String surname = "";
			Iterator<Entry<String, Object>> it = listMaps.get( i ).entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Object> pair = (Map.Entry<String, Object>) it.next();
				if (pair.getKey().equals( "id" )) {
					id = (long) pair.getValue();
				} else if (pair.getKey().equals( "name" )) {
					name = (String) pair.getValue();
				} else if (pair.getKey().equals( "surname" )) {
					surname = (String) pair.getValue();
				}
			}
			Console.printf( "\t%d %s %s\n", id, name, surname );
			it.remove();
		}
	}

	/**
	 * This method shows the information of all the bonos in the system
	 */
	public void printBonos() {
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

	/**
	 * This shows the information of the bonos of a client
	 */
	public void printBonosFromClient() {
		for (int i = 0; i < listMaps.size(); i++) {
			if (i < listMaps.size() - 1) {
				String code = "";
				double acumulado = 0;
				double disponible = 0;
				String descripcion = "";
				Iterator<Entry<String, Object>> it = listMaps.get( i ).entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<String, Object> pair = (Map.Entry<String, Object>) it.next();
					if (pair.getKey().equals( "codigo" )) {
						code = (String) pair.getValue();
					} else if (pair.getKey().equals( "acumulado" )) {
						acumulado = (double) pair.getValue();
					} else if (pair.getKey().equals( "disponible" )) {
						disponible = (double) pair.getValue();
					} else if (pair.getKey().equals( "descripcion" )) {
						descripcion = (String) pair.getValue();
					}
				}
				Console.printf( "\tCódigo: %s Acumulado: %.2f Disponible: %.2f Descripción: %s\n",
						code, acumulado,
						disponible, descripcion );
				it.remove();
			} else {
				int numeroTotal = 0;
				double importeTotal = 0.0;
				double consumidoTotal = 0.0;
				double disponibleTotal = 0.0;
				Iterator<Entry<String, Object>> it = listMaps.get( listMaps.size() - 1 ).entrySet()
						.iterator();
				while (it.hasNext()) {
					Map.Entry<String, Object> pair = (Map.Entry<String, Object>) it.next();
					if (pair.getKey().equals( "bonos" )) {
						numeroTotal = (int) pair.getValue();
					} else if (pair.getKey().equals( "total" )) {
						importeTotal = (double) pair.getValue();
					} else if (pair.getKey().equals( "consum" )) {
						consumidoTotal = (double) pair.getValue();
					} else if (pair.getKey().equals( "porConsumir" )) {
						disponibleTotal = (double) pair.getValue();
					}
				}
				Console.printf(
						"\tNúmero de bonos: %d Importe total: %.2f Consumido total: %.2f Disponible total: "
								+ "%.2f\n",
						numeroTotal, importeTotal, consumidoTotal, disponibleTotal );
				it.remove();
			}
		}
	}

	/**
	 * This method shows the payment ways that a client has registered
	 */
	public void printMediosPagoFromClient() {
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
