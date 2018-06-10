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
 * This class has the function of printing in the console everything we obtain
 * from the system with the other classes.
 * 
 * @author uo250878
 *
 */
public class Printer {

	/**
	 * Prints the invoice.
	 *
	 * @param invoice the invoice
	 */
	public static void printInvoice(InvoiceDto invoice) {
		double importeConIVa = invoice.total;
		double iva = invoice.taxes;
		double importeSinIva = importeConIVa / (1 + iva / 100);

		Console.printf("Factura nº: %d\n", invoice.number);
		Console.printf("\tFecha: %1$td/%1$tm/%1$tY\n", invoice.date);
		Console.printf("\tTotal: %.2f €\n", importeSinIva);
		Console.printf("\tIva: %.1f %% \n", invoice.taxes);
		Console.printf("\tTotal con IVA: %.2f €\n", invoice.total);
		Console.printf("\tEstado: %s\n", invoice.status);
	}

	/**
	 * Prints the payment means.
	 *
	 * @param medios the medios
	 */
	public static void printPaymentMeans(List<PaymentMeanDto> medios) {
		Console.println();
		Console.println("Medios de pago disponibles");

		Console.printf("\t%s \t%-8.8s \t%s \n", "ID", "Tipo", "Acumulado");
		for (PaymentMeanDto medio : medios) {
			printPaymentMean(medio);
		}
	}

	/**
	 * Prints the payment mean.
	 *
	 * @param medio the medio
	 */
	private static void printPaymentMean(PaymentMeanDto medio) {
		Console.printf("\t%s \t%-8.12s \t%s \n", medio.id, medio.getClass().getSimpleName(), medio.accumulated);
	}

	/**
	 * Prints the repairing.
	 *
	 * @param rep the rep
	 */
	public static void printRepairing(FailureDto rep) {
		Console.printf("\t%d \t%-40.40s \t%td/%<tm/%<tY \t%-12.12s \t%.2f\n", rep.id, rep.description, rep.date,
				rep.status, rep.total);
	}

	/**
	 * Prints the mechanic.
	 *
	 * @param m the m
	 */
	public static void printMechanic(MechanicDto m) {
		Console.printf("\t%d %-10.10s %-25.25s %-25.25s\n", m.id, m.dni, m.name, m.surname);
	}

	/**
	 * This method prints all the vouchers' information, and then a
	 * recapitulation of the number of vouchers, the total consumed, available,
	 * and the quantity of all of them.
	 *
	 * @param list the list of vouchers
	 */
	public static void printVouchers(List<VoucherDto> list) {
		double total = 0.0;
		double totalConsumido = 0.0;
		double totalDisponible = 0.0;
		for (VoucherDto vou : list) {
			totalConsumido += vou.accumulated;
			totalDisponible += vou.available;
			Printer.printVoucher(vou);
		}
		total = totalConsumido + totalDisponible;
		Console.printf("\t%d %.2f %.2f %.2f", list.size(), total, totalConsumido, totalDisponible);
	}

	/**
	 * This method print all the information of the specific voucherdto.
	 *
	 * @param vou the dto to be printed
	 */
	public static void printVoucher(VoucherDto vou) {
		Console.printf("\t%s %.2f %.2f %s\n", vou.code, vou.available, vou.accumulated, vou.description);
	}

	/**
	 * This method prints all the information of a list of voucher summary,
	 * traversing the list and showing in console all the attributes of the dto.
	 *
	 * @param list the list of voucher summary
	 */
	public static void printVoucherSummary(List<VoucherSummary> list) {
		for (VoucherSummary vou : list) {
			Console.printf("\t%s %s %s %d %.2f %.2f %.2f\n", vou.dni, vou.name, vou.surname, vou.emitted, vou.available,
					vou.consumed, vou.totalAmount);
		}
	}

	/**
	 * This method prints all the information of a client.
	 *
	 * @param dto the dto with the information
	 */
	public static void printClient(ClientDto dto) {
		if (dto != null) {
			Console.printf("\t%s %s %s %s %s %s %s %s\n", dto.name, dto.surname, dto.dni, dto.email, dto.phone,
					dto.addressStreet, dto.addressCity, dto.addressZipcode);
		}

	}

}
