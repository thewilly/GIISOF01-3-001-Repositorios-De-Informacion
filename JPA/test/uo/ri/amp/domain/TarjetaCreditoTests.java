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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import alb.util.date.DateUtil;
import uo.ri.model.TarjetaCredito;
import uo.ri.util.exception.BusinessException;

/**
 * The Class TarjetaCreditoTests.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class TarjetaCreditoTests {

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
     * A credit card with a past date cannot be used to pay.
     */
    @Test
    public void testNotValidAfterDate() {
	TarjetaCredito t = new TarjetaCredito("123", "VISA",
		DateUtil.yesterday());
	assertFalse(t.isValidNow());
    }

    /**
     * After paying with a card its accumulated increases.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testPagoTarjeta() throws BusinessException {
	TarjetaCredito t = new TarjetaCredito("123");
	t.pagar(10);

	assertTrue(t.getAcumulado() == 10.0);
    }

    /**
     * If validity date is changed to past and the card is used to pay an
     * exception is raised.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test(expected = BusinessException.class)
    public void testSetAndTryToPayAfterDate() throws BusinessException {
	TarjetaCredito t = new TarjetaCredito("123", "VISA",
		DateUtil.tomorrow());
	t.pagar(10);

	t.setValidez(DateUtil.yesterday());
	t.pagar(10);
    }

    /**
     * A credit card created with the basic constructor has one day validity and
     * is of UNKNOWN type.
     */
    @Test
    public void testSimpleConstructor() {
	TarjetaCredito t = new TarjetaCredito("123");
	Instant t0 = Instant.now();
	Date now = Date.from(t0);
	Date in24HoursTime = Date.from(t0.plus(1, ChronoUnit.DAYS));

	assertTrue(t.getValidez().after(now));
	assertTrue(t.getValidez().before(in24HoursTime)
		|| t.getValidez().equals(in24HoursTime));
	assertTrue(t.getTipo().equals("UNKNOWN"));
	assertTrue(t.getAcumulado() == 0.0);
	assertTrue(t.getNumero().equals("123"));
    }

    /**
     * If a card is used to pay after its valid date an exception is raised.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test(expected = BusinessException.class)
    public void testTryToPayAfterDate() throws BusinessException {
	TarjetaCredito t = new TarjetaCredito("123", "VISA",
		DateUtil.yesterday());
	t.pagar(10);
    }

}
