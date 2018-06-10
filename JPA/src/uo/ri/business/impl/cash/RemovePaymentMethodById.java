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

import uo.ri.business.impl.Command;
import uo.ri.business.repository.MedioPagoRepository;
import uo.ri.conf.Factory;
import uo.ri.model.MedioPago;
import uo.ri.model.Metalico;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.Check;

/**
 * DeleteMedioPago.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class RemovePaymentMethodById implements Command<Void> {

	/** The payment method id. */
	private Long paymentMethodId;

	/**
	 * Instantiates a new removes the payment method by id.
	 *
	 * @param paymentMethodId the payment method id
	 */
	public RemovePaymentMethodById(Long paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.impl.Command#execute()
	 */
	public Void execute() throws BusinessException {
		MedioPagoRepository mp = Factory.repository.forMedioPago();
		MedioPago medioPago = mp.findById(paymentMethodId);
		assertCanBeDeleted(medioPago);
		mp.remove(medioPago);
		return null;
	}

	/**
	 * This method do all the possible checks that should not be allowed to be
	 * deleted from the database, due to errors on the petition or conditions of
	 * the application.
	 *
	 * @param medioPago the payment method to be deleted
	 * @throws BusinessException the business exception
	 */
	private void assertCanBeDeleted(MedioPago medioPago) throws BusinessException {
		Check.isNotNull(medioPago, "Ese medio de pago no existe");
		Check.isFalse(medioPago.getClass().equals(Metalico.class),
				"El medio de pago no puede ser eliminado, por ser metálico");
		Check.isTrue(medioPago.getCargos().isEmpty(), "Ese medio de pago tiene cargos, y no puede ser eliminado");
	}

}
