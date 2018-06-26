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
package uo.ri.business.impl;

import java.util.List;
import java.util.Map;

import uo.ri.business.CashService;
import uo.ri.business.impl.cash.CreatePMBond;
import uo.ri.business.impl.cash.CreatePMCard;
import uo.ri.business.impl.cash.CreateInvoiceForFailures;
import uo.ri.business.impl.cash.RemovePaymentMehtodById;
import uo.ri.business.impl.cash.FindAllPaymentMethodsByClientId;
import uo.ri.common.BusinessException;

/**
 * CashServiceImpl.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class CashServiceImpl implements CashService {

    @Override
    public Map<String, Object> createInvoiceForFailures(List<Long> failuresIds)
	    throws BusinessException {
	CreateInvoiceForFailures createInvoiceForFailures = new CreateInvoiceForFailures(
		failuresIds);
	return createInvoiceForFailures.execute();
    }

    @Override
    public void createPMBond(Long paymentMethodId, String bondDescription,
	    double availableAmount) throws BusinessException {
	CreatePMBond createPMBond = new CreatePMBond(paymentMethodId,
		bondDescription, availableAmount);
	createPMBond.execute();
    }

    @Override
    public void createPMCard(Long paymentMethodId, String cardKind,
	    String cardNumber, String expirationDate) throws BusinessException {
	CreatePMCard createPMCard = new CreatePMCard(paymentMethodId, cardKind,
		cardNumber, expirationDate);
	createPMCard.execute();
    }

    @Override
    public List<Map<String, Object>> findAllPaymentMethodsByClientId(
	    Long clientId) throws BusinessException {
	FindAllPaymentMethodsByClientId findAllPaymentMethodsByClient = new FindAllPaymentMethodsByClientId(
		clientId);
	return findAllPaymentMethodsByClient.execute();
    }

    @Override
    public void removePaymentMethodById(Long paymentMethodId)
	    throws BusinessException {
	RemovePaymentMehtodById removePaymentMethodById = new RemovePaymentMehtodById(
		paymentMethodId);
	removePaymentMethodById.execute();
    }

}
