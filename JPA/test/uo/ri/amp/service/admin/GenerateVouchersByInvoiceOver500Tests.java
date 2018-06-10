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
import static uo.ri.amp.service.util.FixtureRepository.registerNewInvoiceForClientWithAmount;
import static uo.ri.amp.service.util.FixtureRepository.registerNewSettledInvoiceForAmount;

import org.junit.Before;
import org.junit.Test;

import uo.ri.amp.service.util.BaseServiceTests;
import uo.ri.business.AdminService;
import uo.ri.conf.Factory;
import uo.ri.model.Bono;
import uo.ri.model.Cliente;
import uo.ri.model.Factura;
import uo.ri.util.exception.BusinessException;

/**
 * The Class GenerateVouchersByInvoiceOver500Tests.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class GenerateVouchersByInvoiceOver500Tests extends BaseServiceTests {

	/**
	 * Assert right voucher.
	 *
	 * @param c the c
	 * @param expected the expected
	 */
	private void assertRightVoucher(Cliente c, Bono expected) {
		assertTrue( expected.getAcumulado() == 0.0 );
		assertTrue( expected.getCargos().size() == 0.0 );
		assertTrue( expected.getCliente().equals( c ) );
		assertTrue( expected.getDescripcion().equals("Por factura superior a 500€") );
		assertTrue( expected.getDisponible() == 30.0 /*€*/ );
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
	 * Con varias facturas: - f1: pagada, 550€, genera bono - f2: pagada, 450€,
	 * no genera bono - f3: no pagada, 450€, no genera bono - f4: no pagada,
	 * 550€, no genera bono - f5: pagada, 550€, ya usada, no genera bono.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testJustOneVoucherGenerated() throws BusinessException {
		Cliente c = registerNewClient();
		Factura f1 = registerNewSettledInvoiceForAmount(c, 550);
		/*f2*/ registerNewSettledInvoiceForAmount(c, 450);
		/*f3*/ registerNewInvoiceForClientWithAmount(c, 450);
		/*f4*/ registerNewInvoiceForClientWithAmount(c, 550);
		Factura f5 = registerNewSettledInvoiceForAmount(c, 550);
		
		f5.markAsBono500Used();
		
		AdminService svc = Factory.service.forAdmin();
		int qty = svc.generateVouchers();
		
		assertTrue( qty == 1);
		assertTrue( f1.isBono500Used() );
	
		Bono expected = getFirstVoucher(c);
		assertRightVoucher(c, expected);
	}

	/**
	 * Varios clientes con facturas que pueden generar bonos c1 y c3 generan
	 * bono.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testSeveralClientes() throws BusinessException {
		Cliente c1 = registerNewClient();
		Factura f1 = registerNewSettledInvoiceForAmount( c1, 550 );
		
		Cliente c2 = registerNewClient();
		Factura f2 = registerNewInvoiceForClientWithAmount(c2, 550 );
		
		Cliente c3 = registerNewClient();
		Factura f3 = registerNewSettledInvoiceForAmount( c3, 550 );

		AdminService svc = Factory.service.forAdmin();
		int qty = svc.generateVouchers();
		
		assertTrue( qty == 2);
		assertTrue( f1.isBono500Used() );
		assertTrue( ! f2.isBono500Used() );
		assertTrue( f3.isBono500Used() );
	
		Bono expected = getFirstVoucher(c1);
		assertRightVoucher(c1, expected);		

		expected = getFirstVoucher(c3);
		assertRightVoucher(c3, expected);		
	}

	/**
	 * Se genera bono con factura de más de 500 €, pagada - La factura queda
	 * marcada como usada para bono500 - El bono generado tiene descripcion
	 * correcta - El bono generado es por 30 €.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testValidInvoice() throws BusinessException {
		Cliente c = registerNewClient();
		Factura f = registerNewSettledInvoiceForAmount(c, 550);
		
		AdminService svc = Factory.service.forAdmin();
		int qty = svc.generateVouchers();
		
		assertTrue( qty == 1);
		assertTrue( f.isBono500Used() );
		
		Bono expected = getFirstVoucher(c);
		assertRightVoucher(c, expected);
	}

	/**
	 * No se genera bono por factura ya usada para bono500.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testWithAlreadyUsedInvoiceOver500() throws BusinessException {
		Cliente c = registerNewClient();
		Factura f = registerNewSettledInvoiceForAmount(c, 550);
		
		f.markAsBono500Used();
		
		AdminService svc = Factory.service.forAdmin();
		int qty = svc.generateVouchers();
		
		assertTrue( qty == 0);
	}
	
	/**
	 * No se genera bono por factura sin pagar.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testWithNotSettledInvoice() throws BusinessException {
		Cliente c = registerNewClient();
		registerNewInvoiceForClientWithAmount( c, 550 );
		
		AdminService svc = Factory.service.forAdmin();
		int qty = svc.generateVouchers();
		
		assertTrue( qty == 0);
	}
	
	/**
	 * No se genera bono por factura pagada de menos de 500 euros.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testWitSettledInvoiceUnder500() throws BusinessException {
		Cliente c = registerNewClient();
		registerNewSettledInvoiceForAmount(c, 450);
		
		AdminService svc = Factory.service.forAdmin();
		int qty = svc.generateVouchers();
		
		assertTrue( qty == 0);
	}

}
