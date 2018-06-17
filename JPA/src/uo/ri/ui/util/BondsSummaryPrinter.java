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
import uo.ri.business.dto.VoucherSummary;

/**
 * The Class BondsSummaryPrinter.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class BondsSummaryPrinter implements Printer {
	
	/** The bonds. */
	private List<VoucherSummary> bonds;

	/**
	 * Instantiates a new bonds summary printer.
	 *
	 * @param bonds the bonds
	 */
	public BondsSummaryPrinter( List<VoucherSummary> bonds ) {
		this.bonds=bonds;
	}
	
	/* (non-Javadoc)
	 * @see uo.ri.ui.util.Printer#print()
	 */
	@Override
	public void print() {
		for (VoucherSummary bond : bonds) {
			Console.printf( "\t%s %s %s %d %.2f %.2f %.2f\n", bond.dni, bond.name, bond.surname,
					bond.emitted, bond.available,
					bond.consumed, bond.totalAmount );
		}
	}

}
