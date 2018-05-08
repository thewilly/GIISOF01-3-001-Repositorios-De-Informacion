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
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package uo.ri.conf;

import uo.ri.persistence.FailuresGateway;
import uo.ri.persistence.BondsGateway;
import uo.ri.persistence.ClientsGateway;
import uo.ri.persistence.InvoicesGateway;
import uo.ri.persistence.MechanicsGateway;
import uo.ri.persistence.PaymentMethodsGateway;
import uo.ri.persistence.impl.FailuresGatewayImpl;
import uo.ri.persistence.impl.BondsGatewayImpl;
import uo.ri.persistence.impl.ClientsGatewayImpl;
import uo.ri.persistence.impl.InvoicesGatewayImpl;
import uo.ri.persistence.impl.MechanicsGatewayImpl;
import uo.ri.persistence.impl.PaymentMethodsGatewayImp;

/**
 * 
 * PersistenceFactory.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201805082121
 * @since 201805082121
 * @formatter Oviedo Computing Community
 */
public class PersistenceFactory {

	/**
	 * @return new bonds gateway implementation.
	 */
	public static BondsGateway getBondsGateway() {
		return new BondsGatewayImpl();
	}

	/**
	 * @return new bonds gateway implementation.
	 */
	public static ClientsGateway getClientsGateway() {
		return new ClientsGatewayImpl();
	}

	/**
	 * @return new bonds gateway implementation.
	 */
	public static FailuresGateway getFailuresGateway() {
		return new FailuresGatewayImpl();
	}

	/**
	 * @return new bonds gateway implementation.
	 */
	public static InvoicesGateway getInvoicesGateway() {
		return new InvoicesGatewayImpl();
	}

	/**
	 * @return new bonds gateway implementation.
	 */
	public static MechanicsGateway getMechanicsGateway() {
		return new MechanicsGatewayImpl();
	}

	/**
	 * @return new bonds gateway implementation.
	 */
	public static PaymentMethodsGateway getPaymentMethodsGateway() {
		return new PaymentMethodsGatewayImp();
	}

	private PersistenceFactory() {}

}
