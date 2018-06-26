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
import static uo.ri.amp.service.util.FixtureRepository.registerNewInvoiceForAmount;

import org.junit.Before;
import org.junit.Test;

import uo.ri.amp.service.util.BaseServiceTests;
import uo.ri.business.CashService;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.conf.Factory;
import uo.ri.model.Factura;
import uo.ri.util.exception.BusinessException;

/**
 * The Class FindInvoiceByNumberTests.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class FindInvoiceByNumberTests extends BaseServiceTests {

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Se devuelve dto de la factura si el id existe.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testFindExistingInvoice() throws BusinessException {
	Factura f = registerNewInvoiceForAmount(100 /* € */ );

	CashService svc = Factory.service.forCash();
	InvoiceDto expected = svc.findInvoiceByNumber(f.getNumero());

	assertTrue(expected != null);
	assertTrue(expected.number == f.getNumero());
	assertTrue(expected.date.equals(f.getFecha()));
	assertTrue(expected.status.equals(f.getStatus().toString()));
	assertTrue(expected.total == f.getImporte());
	assertTrue(expected.taxes == f.getIva());
    }

    /**
     * Si la factura no existe devuelve null y no se lanza excepcion.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testInvoiceDoesnoExist() throws BusinessException {
	Long DOES_NOT_EXIST = -12345L;
	CashService svc = Factory.service.forCash();
	InvoiceDto expected = svc.findInvoiceByNumber(DOES_NOT_EXIST);

	assertTrue(expected == null);
    }

}
