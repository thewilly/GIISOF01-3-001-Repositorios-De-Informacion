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
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uo.ri.amp.service.util.BaseServiceTests;
import uo.ri.amp.service.util.Fixture;
import uo.ri.amp.service.util.FixtureRepository;
import uo.ri.business.CashService;
import uo.ri.business.dto.CardDto;
import uo.ri.business.dto.VoucherDto;
import uo.ri.conf.Factory;
import uo.ri.model.Bono;
import uo.ri.model.Cliente;
import uo.ri.util.exception.BusinessException;

/**
 * The Class AddVoucherPaymentMeanTests.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class AddVoucherPaymentMeanTests extends BaseServiceTests {

	/** The c. */
	private Cliente c;
	
	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		c = FixtureRepository.registerNewClient();
	}
	
	/**
	 * Tear down.
	 */
	@After
	public void tearDown() {
	}

	/**
	 * No se puede añadir una tarjeta cuyo número ya existe en el sistema.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test(expected = BusinessException.class) 
	public void testAddNewCardRepeatedNumber() throws BusinessException {
		CardDto card = Fixture.newCardDto( c.getId() );
		CashService svc = Factory.service.forCash();
		try {
			svc.addCardPaymentMean(card);
		} catch (BusinessException bex) {
			fail("Not here!");
		}
		
		CardDto cardWithRepeatedNumber = Fixture.newCardDto( c.getId() );
		cardWithRepeatedNumber.cardNumber = card.cardNumber;
		svc.addCardPaymentMean( cardWithRepeatedNumber );   // must raise exception
	}

	/**
	 * No se puede añadir una tarjeta cuyo número ya existe en el sistema
	 * incluso si es de otro cliente.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test(expected = BusinessException.class) 
	public void testAddNewCardRepeatedNumberOtherClient() throws BusinessException {
		Cliente otherClient = FixtureRepository.registerNewClient();
		CardDto card = Fixture.newCardDto( c.getId() );
		CashService svc = Factory.service.forCash();
		try {
			svc.addCardPaymentMean(card);
		} catch (BusinessException bex) {
			fail("Not here!");
		}
		
		CardDto cardWithRepeatedNumber = Fixture.newCardDto( otherClient.getId() );
		cardWithRepeatedNumber.cardNumber = card.cardNumber;
		svc.addCardPaymentMean( cardWithRepeatedNumber );   // must raise exception
	}
	
	/**
	 * Se añade un bono no registrado previamente a un cliente.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testAddNewVoucher() throws BusinessException {
		VoucherDto voucher = Fixture.newVoucherDto( c.getId() );
		
		CashService svc = Factory.service.forCash();
		svc.addVoucherPaymentMean(voucher);
		
		List<Bono> bonos = FixtureRepository.findVouchersByClientId( c.getId() );
		Bono expected = bonos.get(0);
		
		assertTrue( expected.getCliente().getId().equals( c.getId() ) );
		assertTrue( expected.getCargos().size() == 0);
		assertTrue( expected.getAcumulado() == 0.0 );
		assertTrue( expected.getDisponible().equals( voucher.available ) );
		assertTrue( expected.getDescripcion().equals( voucher.description ) );
	}
	
	
	/**
	 * No se puede añadir una tarjeta a un cliente que no existe.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test(expected = BusinessException.class) 
	public void testAddNewVoucherToNonExisitngClient() throws BusinessException {
		VoucherDto voucher = Fixture.newVoucherDto( -1000L /*does not exist*/ );
		
		CashService svc = Factory.service.forCash();
		svc.addVoucherPaymentMean(voucher);  // must raise exception
	}

	/**
	 * El valor de acumulado se ignora al añadir nuevo bono.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testAddNewVoucherWithAccumulated() throws BusinessException {
		VoucherDto voucher = Fixture.newVoucherDto( c.getId() );
		voucher.accumulated = 1000.0; /* € */
		
		CashService svc = Factory.service.forCash();
		svc.addVoucherPaymentMean(voucher);
		
		List<Bono> bonos = FixtureRepository.findVouchersByClientId( c.getId() );
		Bono expected = bonos.get(0);
		
		assertTrue( expected.getAcumulado() == 0.0 );
	}

}
