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
package uo.ri.amp.service.cash;

import static org.junit.Assert.assertTrue;
import static uo.ri.amp.service.util.FixtureRepository.findCardByNumber;
import static uo.ri.amp.service.util.FixtureRepository.findCashByClientId;
import static uo.ri.amp.service.util.FixtureRepository.registerNewClientWithCash;
import static uo.ri.amp.service.util.FixtureRepository.registerNewCreditCardForClient;
import static uo.ri.amp.service.util.FixtureRepository.registerNewInvoiceWithChargesToCard;

import org.junit.Before;
import org.junit.Test;

import uo.ri.amp.service.util.BaseServiceTests;
import uo.ri.business.CashService;
import uo.ri.conf.Factory;
import uo.ri.model.Cliente;
import uo.ri.model.Metalico;
import uo.ri.model.TarjetaCredito;
import uo.ri.util.exception.BusinessException;

/**
 * The Class DeletePaymentMeanTests.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class DeletePaymentMeanTests extends BaseServiceTests {
	
	/** The c. */
	private Cliente c;
	
	/** The tc. */
	private TarjetaCredito tc;
	
	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		c = registerNewClientWithCash();
		tc = registerNewCreditCardForClient( c );
	}
	
	/**
	 * No se puede borrar si tiene cargos.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test(expected = BusinessException.class)
	public void testInvalidDeleteForCharges() throws BusinessException {
		registerNewInvoiceWithChargesToCard( tc );
		
		CashService svc = Factory.service.forCash();
		svc.deletePaymentMean( tc.getId() );
	}
	
	/**
	 * No se puede borrar si es de tipo metálico.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test(expected = BusinessException.class)
	public void testInvalidDeleteIfCash() throws BusinessException {
		Metalico m = findCashByClientId( c.getId() );
		
		CashService svc = Factory.service.forCash();
		svc.deletePaymentMean( m.getId() );
	}
	
	/**
	 * Si no existe el id salta excepcion.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test(expected = BusinessException.class)
	public void testInvalidDeleteNoId() throws BusinessException {
		CashService svc = Factory.service.forCash();
		svc.deletePaymentMean( -1000L );
	}
	
	/**
	 * Se puede borrar un medio de pago, que exista, que no sea metálico y que
	 * no tenga cargos.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testValidDelete() throws BusinessException {
		TarjetaCredito expected = findCardByNumber( tc.getNumero() );
		assertTrue( expected != null );
		
		CashService svc = Factory.service.forCash();
		svc.deletePaymentMean( tc.getId() );

		String number = tc.getNumero();
		expected = findCardByNumber( number); 
		assertTrue( expected == null );
	}

}
