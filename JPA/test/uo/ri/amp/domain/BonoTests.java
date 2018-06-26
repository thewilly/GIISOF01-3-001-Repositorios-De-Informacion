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

import org.junit.Test;

import alb.util.random.Random;
import uo.ri.model.Bono;
import uo.ri.util.exception.BusinessException;

/**
 * The Class BonoTests.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class BonoTests {

    /**
     * Generate new code.
     *
     * @return the string
     */
    private String generateNewCode() {
	return "V-" + Random.string(5) + "-" + Random.integer(1000, 9999);
    }

    /**
     * A voucher cannot be charged with an amount greater than its available.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test(expected = BusinessException.class)
    public void testCannotBeCharged() throws BusinessException {
	Bono b = new Bono("123", "For test", 10.0);
	b.pagar(11.0); // raises exception
    }

    /**
     * Any new payment mean has 0 accumulated.
     */
    @Test
    public void testNewBono() {
	Bono b = new Bono("123", 100.0);

	assertTrue(b.getDescripcion().equals(""));
	assertTrue(b.getCodigo().equals("123"));
	assertTrue(b.getAcumulado() == 0.0);
	assertTrue(b.getDisponible() == 100.0);
    }

    /**
     * After paying with a voucher its accumulated increases and its available
     * decreases.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testPagoBono() throws BusinessException {
	String code = generateNewCode();
	Bono b = new Bono(code, "For test", 100);
	b.pagar(10);

	assertTrue(b.getDescripcion().equals("For test"));
	assertTrue(b.getCodigo().equals(code));
	assertTrue(b.getAcumulado() == 10.0);
	assertTrue(b.getDisponible() == 90.0);
    }

}
