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
package uo.ri.amp.service.admin;

import static org.junit.Assert.assertTrue;
import static uo.ri.amp.service.util.FixtureRepository.registerNewClient;
import static uo.ri.amp.service.util.FixtureRepository.registerNewVoucherWithAvailable;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uo.ri.amp.service.util.BaseServiceTests;
import uo.ri.business.AdminService;
import uo.ri.business.dto.VoucherSummary;
import uo.ri.conf.Factory;
import uo.ri.model.Association;
import uo.ri.model.Bono;
import uo.ri.model.Cliente;
import uo.ri.util.exception.BusinessException;

/**
 * The Class GenerateVoucherSummaryTests.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class GenerateVoucherSummaryTests extends BaseServiceTests {

	/**
	 * Assert right summary.
	 *
	 * @param vs the vs
	 * @param c the c
	 * @param emitted the emitted
	 * @param total the total
	 * @param consumed the consumed
	 */
	private void assertRightSummary(VoucherSummary vs, Cliente c, 
			int emitted, double total, double consumed) {
		assertTrue( vs.dni.equals( c.getDni() ) );
		assertTrue( vs.name.equals( c.getNombre() ) );
		assertTrue( vs.surname.equals( c.getApellidos() ) );
		assertTrue( vs.emitted == emitted );
		assertTrue( vs.totalAmount == total );
		assertTrue( vs.consumed ==  consumed );
		assertTrue( vs.available ==  total - consumed );
	}

	/**
	 * Find client index.
	 *
	 * @param data the data
	 * @param dni the dni
	 * @return the int
	 */
	private int findClientIndex(Object[][] data, String dni) {
		for (int i = 0; i < data.length; i++) {
			Cliente c = (Cliente) data[i][0];
			if ( c.getDni().equals( dni ) ) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Register client with vouchers.
	 *
	 * @param emitted the emitted
	 * @param total the total
	 * @param consumed the consumed
	 * @return the cliente
	 * @throws BusinessException the business exception
	 */
	private Cliente registerClientWithVouchers(
			int emitted, double total, double consumed) 
			throws BusinessException {
		
		double initialAvailable = total / emitted;
		double toBeConsumed = consumed / emitted;

		Cliente c = registerNewClient();
		for(int i = 0; i < emitted; i++) {
			Bono b = registerNewVoucherWithAvailable( initialAvailable );
			Association.Pagar.link(c, b);
			b.pagar( toBeConsumed );
		}
		return c;
	}

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
	}
	
	/**
	 * One client with no vouchers produces one record with all zeroes.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testClientNoVouchers() throws BusinessException {
		int EMIITED = 0;
		double TOTAL = 0;
		double CONSUMED = 0;

		Cliente c = registerClientWithVouchers(EMIITED, TOTAL, CONSUMED);
		
		AdminService svc = Factory.service.forAdmin();
		List<VoucherSummary> summary = svc.getVoucherSummary();
		
		assertTrue( summary.size() == 1 );
		VoucherSummary vs = summary.get(0);
		assertRightSummary(vs, c, EMIITED, TOTAL, CONSUMED);
	}
	
	/**
	 * An empty list is returned if there is no client registered on the system
	 * .
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testEmptyListReturned() throws BusinessException {
		AdminService svc = Factory.service.forAdmin();
		List<VoucherSummary> summary = svc.getVoucherSummary();
		
		assertTrue( summary != null );
		assertTrue( summary.size() == 0 );
	}

	/**
	 * Only one client with vouchers registered produces the right summary.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testRightListReturned() throws BusinessException {
		int EMIITED = 10;
		double TOTAL = 1000;
		double CONSUMED = 650;

		Cliente c = registerClientWithVouchers(EMIITED, TOTAL, CONSUMED);
		
		AdminService svc = Factory.service.forAdmin();
		List<VoucherSummary> summary = svc.getVoucherSummary();
		
		assertTrue( summary.size() == 1 );
		VoucherSummary vs = summary.get(0);
		assertRightSummary(vs, c, EMIITED, TOTAL, CONSUMED);
	}
	
	/**
	 * Three client with vouchers registered produces the right summary.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testThreeClientWithVouchers() throws BusinessException {
		Object[][] data = new Object[][]{
			{registerClientWithVouchers(5, 100, 50), 5, 100.0, 50.0},
			{registerClientWithVouchers(2, 250, 65), 2, 250.0, 65.0},
			{registerClientWithVouchers(10, 750, 100), 10, 750.0, 100.0}
		};

		AdminService svc = Factory.service.forAdmin();
		List<VoucherSummary> summary = svc.getVoucherSummary();
		
		assertTrue( summary.size() == 3 );
		for(VoucherSummary vs : summary) {
			int i = findClientIndex(data, vs.dni);
			Cliente c = (Cliente) data[i][0];
			int emitted = (int) data[i][1];
			double total = (double) data[i][2];
			double consumed = (double) data[i][3];
			
			assertRightSummary(vs, c, emitted, total, consumed);
		}
	}

}
