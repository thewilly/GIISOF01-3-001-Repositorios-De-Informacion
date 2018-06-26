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
package uo.ri.business.repository;

import java.util.List;

import uo.ri.model.Bono;
import uo.ri.model.MedioPago;
import uo.ri.model.TarjetaCredito;

/**
 * The Interface MedioPagoRepository.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public interface MedioPagoRepository extends Repository<MedioPago> {

    /**
     * Returns an Object[] with three values - Object[0] an integer with the
     * number of vouchers the client has - Object[1] a double with the total
     * amount available in all the client's vouchers - Object[2] a double with
     * the amount already consumed.
     *
     * @param id
     *            the id
     * @return the object[]
     */
    Object[] findAggregateVoucherDataByClientId(Long id);

    /**
     * Find by client id.
     *
     * @param id
     *            the id
     * @return the list
     */
    List<MedioPago> findByClientId(Long id);

    /**
     * Find credit card by number.
     *
     * @param number
     *            the number
     * @return the tarjeta credito
     */
    TarjetaCredito findCreditCardByNumber(String number);

    /**
     * Find payment means by client id.
     *
     * @param id
     *            the id
     * @return the list
     */
    List<MedioPago> findPaymentMeansByClientId(Long id);

    /**
     * Find payment means by invoice id.
     *
     * @param idFactura
     *            the id factura
     * @return the list
     */
    List<MedioPago> findPaymentMeansByInvoiceId(Long idFactura);

    /**
     * Find voucher by code.
     *
     * @param code
     *            the code
     * @return the bono
     */
    Bono findVoucherByCode(String code);

    /**
     * Find vouchers by client id.
     *
     * @param id
     *            the id
     * @return the list
     */
    List<Bono> findVouchersByClientId(Long id);
}
