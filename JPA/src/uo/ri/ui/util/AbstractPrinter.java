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
package uo.ri.ui.util;

import java.util.List;

import alb.util.console.Console;
import uo.ri.business.dto.FailureDto;
import uo.ri.business.dto.ClientDto;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.dto.VoucherDto;
import uo.ri.business.dto.VoucherSummary;

/**
 * Printer.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class AbstractPrinter {

	/**
	 * Prints the client.
	 *
	 * @param client the client to print
	 */
	public static void printClient( ClientDto client ) {
		if (client != null) {
			Console.printf( "\t%s %s %s %s %s %s %s %s\n", client.name, client.surname, client.dni,
					client.email, client.phone,
					client.addressStreet, client.addressCity, client.addressZipcode );
		}
	}

	/**
	 * Prints the invoice.
	 *
	 * @param invoice the invoice
	 */
	public static void printInvoice( InvoiceDto invoice ) {
		double importeConIVa = invoice.total;
		double iva = invoice.taxes;
		double importeSinIva = importeConIVa / ( 1 + iva / 100 );

		Console.printf( "Factura nº: %d\n", invoice.number );
		Console.printf( "\tFecha: %1$td/%1$tm/%1$tY\n", invoice.date );
		Console.printf( "\tTotal: %.2f €\n", importeSinIva );
		Console.printf( "\tIva: %.1f %% \n", invoice.taxes );
		Console.printf( "\tTotal con IVA: %.2f €\n", invoice.total );
		Console.printf( "\tEstado: %s\n", invoice.status );
	}

	/**
	 * Prints the mechanic.
	 *
	 * @param mechanic the mechanic
	 */
	public static void printMechanic( MechanicDto mechanic ) {
		Console.printf( "\t%d %-10.10s %-25.25s %-25.25s\n", mechanic.id, mechanic.dni,
				mechanic.name, mechanic.surname );
	}

	/**
	 * Prints the payment mean.
	 *
	 * @param paymentMethod the payment method
	 */
	private static void printPaymentMean( PaymentMeanDto paymentMethod ) {
		Console.printf( "\t%s \t%-8.12s \t%s \n", paymentMethod.id,
				paymentMethod.getClass().getSimpleName(), paymentMethod.accumulated );
	}

	/**
	 * Prints the payment means.
	 *
	 * @param paymentMethods the payment methods
	 */
	public static void printPaymentMeans( List<PaymentMeanDto> paymentMethods ) {
		Console.println();
		Console.println( "Medios de pago disponibles" );

		Console.printf( "\t%s \t%-8.8s \t%s \n", "ID", "Tipo", "Acumulado" );
		for (PaymentMeanDto paymentMethod : paymentMethods) {
			printPaymentMean( paymentMethod );
		}
	}

	/**
	 * Prints the repairing.
	 *
	 * @param failure the failure
	 */
	public static void printRepairing( FailureDto failure ) {
		Console.printf( "\t%d \t%-40.40s \t%td/%<tm/%<tY \t%-12.12s \t%.2f\n", failure.id,
				failure.description, failure.date,
				failure.status, failure.total );
	}

	/**
	 * Prints the bond.
	 *
	 * @param bond the bond
	 */
	public static void printBond( VoucherDto bond ) {
		Console.printf( "\t%s %.2f %.2f %s\n", bond.code, bond.available, bond.accumulated,
				bond.description );
	}

	/**
	 * Prints the bonds.
	 *
	 * @param bonds the bonds
	 */
	public static void printBonds( List<VoucherDto> bonds ) {
		double total = 0.0;
		double totalConsumido = 0.0;
		double totalDisponible = 0.0;
		for (VoucherDto bond : bonds) {
			totalConsumido += bond.accumulated;
			totalDisponible += bond.available;
			AbstractPrinter.printBond( bond );
		}
		total = totalConsumido + totalDisponible;
		Console.printf( "\t%d %.2f %.2f %.2f", bonds.size(), total, totalConsumido,
				totalDisponible );
	}

	/**
	 * Prints the bonds summary.
	 *
	 * @param bonds the bonds
	 */
	public static void printBondsSummary( List<VoucherSummary> bonds ) {
		for (VoucherSummary bond : bonds) {
			Console.printf( "\t%s %s %s %d %.2f %.2f %.2f\n", bond.dni, bond.name, bond.surname,
					bond.emitted, bond.available,
					bond.consumed, bond.totalAmount );
		}
	}

}
