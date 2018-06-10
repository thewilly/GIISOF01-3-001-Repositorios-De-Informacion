package uo.ri.ui.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import alb.util.console.Console;

/**
 * MechanicsPrinter.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class MechanicsPrinter extends AbstractPrinter {

	/**
	 * Allocates a [] object and initializes it so that it represents
	 * 
	 * @param mechanicsToPrint
	 */
	public MechanicsPrinter( List<Map<String, Object>> mechanicsToPrint ) {
		super.listMaps = mechanicsToPrint;
		print();
	}

	@Override
	protected void print() {
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
}
