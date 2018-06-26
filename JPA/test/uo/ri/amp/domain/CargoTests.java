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
package uo.ri.amp.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import alb.util.date.DateUtil;
import uo.ri.model.Bono;
import uo.ri.model.Cargo;
import uo.ri.model.Cliente;
import uo.ri.model.Factura;
import uo.ri.model.Metalico;
import uo.ri.model.TarjetaCredito;
import uo.ri.util.exception.BusinessException;

/**
 * The Class CargoTests.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class CargoTests {

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
     * A credit card cannot be charged if its expiration date is over.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test(expected = BusinessException.class)
    public void tesChargeExpiredCard() throws BusinessException {
	TarjetaCredito t = new TarjetaCredito("123", "TTT",
		DateUtil.yesterday());
	Factura f = new Factura(123L);

	new Cargo(f, t, 100.0); // Card validity date expired exception
    }

    /**
     * A charge to a voucher increases the accumulated and decreases the
     * available.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testCargoBono() throws BusinessException {
	Bono b = new Bono("123", "For test", 150.0);
	Factura f = new Factura(123L);

	new Cargo(f, b, 100.0);

	assertTrue(b.getAcumulado() == 100.0);
	assertTrue(b.getDisponible() == 50.0);
    }

    /**
     * A charge to cash increases the accumulated.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testCargoMetalico() throws BusinessException {
	Metalico m = new Metalico(new Cliente("123", "n", "a"));
	Factura f = new Factura(123L);

	new Cargo(f, m, 100.0);

	assertTrue(m.getAcumulado() == 100.0);
    }

    /**
     * A charge to a card increases the accumulated.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testCargoTarjeta() throws BusinessException {
	TarjetaCredito t = new TarjetaCredito("123");
	Factura f = new Factura(123L);

	new Cargo(f, t, 100.0);

	assertTrue(t.getAcumulado() == 100.0);
    }

    /**
     * A voucher cannot be charged if it has no available money.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test(expected = BusinessException.class)
    public void testEmptyVoucherCannotBeCharged() throws BusinessException {
	Bono b = new Bono("123", "For test", 150.0);
	Factura f = new Factura(123L);

	new Cargo(f, b, 151.0); // Not enough money exception
    }

}
