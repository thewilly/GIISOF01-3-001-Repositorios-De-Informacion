/*******************************************************************************
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
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
package uo.ri.business.impl.cash;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import alb.util.date.DateUtil;
import static alb.util.jdbc.Jdbc.*;
import uo.ri.common.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.PaymentMethodsGateway;

/**
 * 
 * CreatePMCard.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032123
 * @since 201806032123
 * @formatter Oviedo Computing Community
 */
public class CreatePMCard {

	private Long cardId;
	private String cardKind, cardNumber, cardExpirationDate;

	/**
	 * Creates a card payment method.
	 * 
	 * @param cardId is the id of the card.
	 * @param cardKind is the kind of the card.
	 * @param cardNumber is the card number.
	 * @param cardExpirationDate is the date until which the card is valid.
	 */
	public CreatePMCard( Long cardId, String cardKind, String cardNumber, String cardExpirationDate ) {
		this.cardId = cardId;
		this.cardKind = cardKind;
		this.cardNumber = cardNumber;
		this.cardExpirationDate = cardExpirationDate;
	}

	/**
	 * Creates the card with the data provided.
	 * 
	 * @throws BusinessException if an error success during the execution of the
	 *             method.
	 */
	public void execute() throws BusinessException {
		
		Connection connection = null;
		
		try {
		
			// Getting the connection.
			connection = getConnection();
			
			// Creating the gateway and setting the connection.
			PaymentMethodsGateway paymentMethodsGW = PersistenceFactory.getPaymentMethodsGateway();
			paymentMethodsGW.setConnection( connection );

			// Parsing the date from the entered one.
			String OLD_FORMAT = "yyyy-MM-dd";
			DateFormat formatter = new SimpleDateFormat( OLD_FORMAT );
			Date d = null;
			try {
				d = formatter.parse( cardExpirationDate );
			} catch (ParseException e) {
				throw new BusinessException( e );
			}
			if (d.before( DateUtil.today() )) {
				throw new BusinessException( "Tarjeta no válida: la fecha de caducidad ha pasado." );
			}
			
			// Setting the date to the DB format.
			String NEW_FORMAT = "yyyy-MM-dd HH:mm:ss";
			( (SimpleDateFormat) formatter ).applyPattern( NEW_FORMAT );
			cardExpirationDate = formatter.format( d );
			Timestamp ts = Timestamp.valueOf( cardExpirationDate );

			// Creating the card in the gateway.
			paymentMethodsGW.createPMCard( cardId, cardKind, cardNumber, ts );
		} catch (SQLException e) {
			throw new RuntimeException();
		} finally {
			// Closing the connection.
			close( connection );
		}
	}

}
