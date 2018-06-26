package uo.ri.ui.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import alb.util.console.Console;

public class BondsPerClientPrinter extends BondsPrinter {

    public BondsPerClientPrinter(List<Map<String, Object>> bondsToPrint) {
	super(bondsToPrint);
	// TODO Auto-generated constructor stub
    }

    @Override
    protected void print() {
	for (int i = 0; i < listMaps.size(); i++) {
	    if (i < listMaps.size() - 1) {
		String code = "";
		double acumulado = 0;
		double disponible = 0;
		String descripcion = "";
		Iterator<Entry<String, Object>> it = listMaps.get(i).entrySet()
			.iterator();
		while (it.hasNext()) {
		    Map.Entry<String, Object> pair = (Map.Entry<String, Object>) it
			    .next();
		    if (pair.getKey().equals("codigo")) {
			code = (String) pair.getValue();
		    } else if (pair.getKey().equals("acumulado")) {
			acumulado = (double) pair.getValue();
		    } else if (pair.getKey().equals("disponible")) {
			disponible = (double) pair.getValue();
		    } else if (pair.getKey().equals("descripcion")) {
			descripcion = (String) pair.getValue();
		    }
		}
		Console.printf(
			"\tCódigo: %s Acumulado: %.2f Disponible: %.2f Descripción: %s\n",
			code, acumulado, disponible, descripcion);
		it.remove();
	    } else {
		int numeroTotal = 0;
		double importeTotal = 0.0;
		double consumidoTotal = 0.0;
		double disponibleTotal = 0.0;
		Iterator<Entry<String, Object>> it = listMaps
			.get(listMaps.size() - 1).entrySet().iterator();
		while (it.hasNext()) {
		    Map.Entry<String, Object> pair = (Map.Entry<String, Object>) it
			    .next();
		    if (pair.getKey().equals("bonos")) {
			numeroTotal = (int) pair.getValue();
		    } else if (pair.getKey().equals("total")) {
			importeTotal = (double) pair.getValue();
		    } else if (pair.getKey().equals("consum")) {
			consumidoTotal = (double) pair.getValue();
		    } else if (pair.getKey().equals("porConsumir")) {
			disponibleTotal = (double) pair.getValue();
		    }
		}
		Console.printf(
			"\tNúmero de bonos: %d Importe total: %.2f Consumido total: %.2f Disponible total: "
				+ "%.2f\n",
			numeroTotal, importeTotal, consumidoTotal,
			disponibleTotal);
		it.remove();
	    }
	}
    }

}
