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

import org.junit.Before;
import org.junit.Test;

import uo.ri.amp.service.util.BaseServiceTests;
import uo.ri.amp.service.util.Fixture;
import uo.ri.amp.service.util.FixtureRepository;
import uo.ri.business.CashService;
import uo.ri.business.dto.CardDto;
import uo.ri.conf.Factory;
import uo.ri.model.Cliente;
import uo.ri.model.TarjetaCredito;
import uo.ri.util.exception.BusinessException;

/**
 * The Class AddCardPaymentMeanTests.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class AddCardPaymentMeanTests extends BaseServiceTests {

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
	 * Se añade una tarjeta no registrada previamente a un cliente.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testAddNewCard() throws BusinessException {
		CardDto card = Fixture.newCardDto( c.getId() );
		
		CashService svc = Factory.service.forCash();
		svc.addCardPaymentMean(card);
		
		TarjetaCredito expected = FixtureRepository.findCardByNumber( card.cardNumber );
		
		assertTrue( expected.getCliente().getId().equals( c.getId() ) );
		assertTrue( expected.getCargos().size() == 0);
		assertTrue( expected.getAcumulado() == 0.0 );
		assertTrue( expected.getNumero().equals( card.cardNumber ) );
		assertTrue( expected.getTipo().equals( card.cardType ) );
		assertTrue( expected.getValidez().equals( card.cardExpiration ) );
	}
	
	/**
	 * El valor de acumulado se ignora al añadir nueva tarjeta.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testAddNewCardWithAccumulated() throws BusinessException {
		CardDto card = Fixture.newCardDto( c.getId() );
		card.accumulated = 1000.0; /* € */
		
		CashService svc = Factory.service.forCash();
		svc.addCardPaymentMean(card);
		
		TarjetaCredito expected = FixtureRepository.findCardByNumber( card.cardNumber );
		
		assertTrue( expected.getAcumulado() == 0.0 );
	}
	
	/**
	 * No se puede añadir una tarjeta a un cliente que no existe.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test(expected = BusinessException.class) 
	public void testAddNewCardNonExisitngClient() throws BusinessException {
		CardDto card = Fixture.newCardDto( -1000L /*does not exist*/ );
		
		CashService svc = Factory.service.forCash();
		svc.addCardPaymentMean(card);  // must raise exception
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

}
