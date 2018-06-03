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
package uo.ri.conf;

import uo.ri.persistence.FailuresGateway;
import uo.ri.persistence.InvoicesGateway;
import uo.ri.persistence.MechanicsGateway;
import uo.ri.persistence.PaymentMethodsGateway;
import uo.ri.persistence.impl.FailuresGatewayImpl;
import uo.ri.persistence.impl.InvoicesGatewayImpl;
import uo.ri.persistence.impl.MechaincsGatewayImpl;
import uo.ri.persistence.impl.PaymentMethodsGatewayImpl;

/**
 * PersistenceFactory.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class PersistenceFactory {

	/**
	 * @return a new gateway for the failures.
	 */
	public static FailuresGateway getFailuresGateway() {
		return new FailuresGatewayImpl();
	}

	/**
	 * @return a new gateway for the invoices.
	 */
	public static InvoicesGateway getInvoicesGateway() {
		return new InvoicesGatewayImpl();
	}

	/**
	 * @return a new gateway for the mechanics.
	 */
	public static MechanicsGateway getMechanicsGateway() {
		return new MechaincsGatewayImpl();
	}

	/**
	 * @return a new gateway for the payment methods.
	 */
	public static PaymentMethodsGateway getPaymentMethodsGateway() {
		return new PaymentMethodsGatewayImpl();
	}
}
