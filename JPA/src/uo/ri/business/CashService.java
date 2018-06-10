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
package uo.ri.business;

import java.util.List;
import java.util.Map;

import uo.ri.business.dto.FailureDto;
import uo.ri.business.dto.CardDto;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.dto.VoucherDto;
import uo.ri.util.exception.BusinessException;

/**
 * The Interface CashService.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public interface CashService {

	/**
	 * Creates the invoice for.
	 *
	 * @param idsAveria a list if breakdown ids to be included in the invoice
	 * @return a dto with the new generated invoice
	 * @throws BusinessException the business exception
	 */
	InvoiceDto createInvoiceFor(List<Long> idsAveria) throws BusinessException;

	/**
	 * Find invoice by number.
	 *
	 * @param numeroFactura the numero factura
	 * @return the invoice dto
	 * @throws BusinessException the business exception
	 */
	InvoiceDto findInvoiceByNumber(Long numeroFactura) throws BusinessException;

	/**
	 * Find payment means for invoice.
	 *
	 * @param idFactura the id factura
	 * @return a list of payment mean dtos owned by the first breakdown's
	 *         vehicle's owner
	 * @throws BusinessException the business exception
	 */
	List<PaymentMeanDto> findPaymentMeansForInvoice(Long idFactura) throws BusinessException;

	/**
	 * Settles the invoice (liquida la factura) with the charges specified in
	 * the cargos Map. The map's key is the payment mean id and the value is the
	 * amount to be charged to this payment mean.
	 *
	 * @param idFactura the id factura
	 * @param cargos the cargos
	 * @return the invoice dto
	 * @throws BusinessException the business exception
	 */
	InvoiceDto settleInvoice(Long idFactura, Map<Long, Double> cargos) throws BusinessException;

	/**
	 * Find repairs by client.
	 *
	 * @param dni the dni
	 * @return the list
	 * @throws BusinessException the business exception
	 */
	List<FailureDto> findRepairsByClient(String dni) throws BusinessException;

	/**
	 * Find payment means by client id.
	 *
	 * @param id the id
	 * @return the list
	 * @throws BusinessException the business exception
	 */
	List<PaymentMeanDto> findPaymentMeansByClientId(Long id) throws BusinessException;

	/**
	 * Adds the card payment mean.
	 *
	 * @param card the card
	 * @throws BusinessException the business exception
	 */
	void addCardPaymentMean(CardDto card) throws BusinessException;

	/**
	 * Adds the voucher payment mean.
	 *
	 * @param voucher the voucher
	 * @throws BusinessException the business exception
	 */
	void addVoucherPaymentMean(VoucherDto voucher) throws BusinessException;

	/**
	 * Delete payment mean.
	 *
	 * @param id the id
	 * @throws BusinessException the business exception
	 */
	void deletePaymentMean(Long id) throws BusinessException;
}
