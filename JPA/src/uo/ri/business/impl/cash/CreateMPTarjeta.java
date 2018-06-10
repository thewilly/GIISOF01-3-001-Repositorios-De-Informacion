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
package uo.ri.business.impl.cash;

import java.util.Date;

import uo.ri.business.dto.CardDto;
import uo.ri.business.impl.Command;
import uo.ri.business.impl.util.DtoAssembler;
import uo.ri.business.repository.ClienteRepository;
import uo.ri.business.repository.MedioPagoRepository;
import uo.ri.conf.Factory;
import uo.ri.model.Association;
import uo.ri.model.Cliente;
import uo.ri.model.TarjetaCredito;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.Check;

/**
 * CreateMPTarjeta.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class CreateMPTarjeta implements Command<Void> {

	/** The card. */
	private CardDto card;
	
	/** The payment methods repository. */
	private MedioPagoRepository paymentMethodsRepository = Factory.repository.forMedioPago();
	
	/** The clients repository. */
	private ClienteRepository clientsRepository = Factory.repository.forCliente();

	/**
	 * Instantiates a new creates the MP tarjeta.
	 *
	 * @param card the card
	 */
	public CreateMPTarjeta(CardDto card) {
		this.card = card;
	}

	/**
	 * This method checks that the client id corresponds to a real client in the
	 * system.
	 *
	 * @param clientId the id of the client
	 * @throws BusinessException the business exception
	 */
	private void assertExistenceClient(Long clientId) throws BusinessException {
		Cliente c = clientsRepository.findById(clientId);
		Check.isNotNull(c, "Ese cliente no existe");
	}

	/**
	 * This method checks that the date the new credit card will have, is not
	 * expired.
	 *
	 * @param cardExpiration the expiration of the credit card
	 * @throws BusinessException the business exception
	 */
	private void assertNotExpiredDate(Date cardExpiration) throws BusinessException {
		Date today = new Date();
		Check.isTrue(today.before(cardExpiration), "La tarjeta tiene una fecha caducada");
	}

	/**
	 * This method checks that the credit card number is not repeated in the
	 * system.
	 *
	 * @param number the credit card number
	 * @throws BusinessException the business exception
	 */
	private void assertNotRepeatedCardNumber(String number) throws BusinessException {
		TarjetaCredito t = paymentMethodsRepository.findCreditCardByNumber(number);
		Check.isNull(t, "Ese número de tarjeta ya existe");
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.impl.Command#execute()
	 */
	public Void execute() throws BusinessException {
		TarjetaCredito t = DtoAssembler.toEntity(card);
		Cliente c = clientsRepository.findById(card.clientId);
		assertExistenceClient(card.clientId);
		assertNotRepeatedCardNumber(card.cardNumber);
		assertNotExpiredDate(card.cardExpirationDate);
		Association.Pagar.link(c, t);
		paymentMethodsRepository.add(t);
		return null;
	}

}
