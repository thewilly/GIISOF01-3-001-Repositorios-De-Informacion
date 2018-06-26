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
package uo.ri.business.impl;

import java.util.List;
import java.util.Map;

import uo.ri.business.CashService;
import uo.ri.business.dto.FailureDto;
import uo.ri.business.dto.CardDto;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.dto.VoucherDto;
import uo.ri.business.impl.cash.CreateMPBond;
import uo.ri.business.impl.cash.CreateMPTarjeta;
import uo.ri.business.impl.cash.CreateInvoiceForFailures;
import uo.ri.business.impl.cash.RemovePaymentMethodById;
import uo.ri.business.impl.cash.FindAllMediosPagoByClientId;
import uo.ri.business.impl.cash.FindInvoiceByInvoiceNumber;
import uo.ri.business.impl.cash.FindPayMethodsByInvoiceId;
import uo.ri.business.impl.cash.FindRepairsByClientDNI;
import uo.ri.business.impl.cash.SettleInvoice;
import uo.ri.conf.Factory;
import uo.ri.util.exception.BusinessException;

/**
 * CashServiceImpl.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class CashServiceImpl implements CashService {

    /** The executor. */
    private CommandExecutor executor = Factory.executor.forExecutor();

    /*
     * (non-Javadoc)
     * 
     * @see
     * uo.ri.business.CashService#addCardPaymentMean(uo.ri.business.dto.CardDto)
     */
    @Override
    public void addCardPaymentMean(CardDto card) throws BusinessException {
	executor.execute(new CreateMPTarjeta(card));
    }

    /*
     * (non-Javadoc)
     * 
     * @see uo.ri.business.CashService#addVoucherPaymentMean(uo.ri.business.dto.
     * VoucherDto)
     */
    @Override
    public void addVoucherPaymentMean(VoucherDto voucher)
	    throws BusinessException {
	executor.execute(new CreateMPBond(voucher));
    }

    /*
     * (non-Javadoc)
     * 
     * @see uo.ri.business.CashService#createInvoiceFor(java.util.List)
     */
    @Override
    public InvoiceDto createInvoiceFor(List<Long> idsAveria)
	    throws BusinessException {
	return executor.execute(new CreateInvoiceForFailures(idsAveria));
    }

    /*
     * (non-Javadoc)
     * 
     * @see uo.ri.business.CashService#deletePaymentMean(java.lang.Long)
     */
    @Override
    public void deletePaymentMean(Long id) throws BusinessException {
	executor.execute(new RemovePaymentMethodById(id));
    }

    /*
     * (non-Javadoc)
     * 
     * @see uo.ri.business.CashService#findInvoiceByNumber(java.lang.Long)
     */
    @Override
    public InvoiceDto findInvoiceByNumber(Long numeroFactura)
	    throws BusinessException {
	return executor.execute(new FindInvoiceByInvoiceNumber(numeroFactura));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * uo.ri.business.CashService#findPaymentMeansByClientId(java.lang.Long)
     */
    @Override
    public List<PaymentMeanDto> findPaymentMeansByClientId(Long id)
	    throws BusinessException {
	return executor.execute(new FindAllMediosPagoByClientId(id));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * uo.ri.business.CashService#findPaymentMeansForInvoice(java.lang.Long)
     */
    @Override
    public List<PaymentMeanDto> findPaymentMeansForInvoice(Long idFactura)
	    throws BusinessException {
	return executor.execute(new FindPayMethodsByInvoiceId(idFactura));
    }

    /*
     * (non-Javadoc)
     * 
     * @see uo.ri.business.CashService#findRepairsByClient(java.lang.String)
     */
    @Override
    public List<FailureDto> findRepairsByClient(String dni)
	    throws BusinessException {
	return executor.execute(new FindRepairsByClientDNI(dni));
    }

    /*
     * (non-Javadoc)
     * 
     * @see uo.ri.business.CashService#settleInvoice(java.lang.Long,
     * java.util.Map)
     */
    @Override
    public InvoiceDto settleInvoice(Long idInvoiceDto, Map<Long, Double> cargos)
	    throws BusinessException {
	return executor.execute(new SettleInvoice(idInvoiceDto, cargos));
    }

}
