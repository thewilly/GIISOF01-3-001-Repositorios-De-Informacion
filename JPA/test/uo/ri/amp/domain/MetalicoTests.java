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

import uo.ri.model.Cliente;
import uo.ri.model.Metalico;
import uo.ri.util.exception.BusinessException;

/**
 * The Class MetalicoTests.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class MetalicoTests {

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * 
	 * A new cash object has no accumulated.
	 */
	@Test
	public void testConstructor() {
		Cliente c = new Cliente("123", "nombre", "apellidos");
		Metalico m = new Metalico( c );

		assertTrue( m.getCliente().equals( c ) );
		assertTrue( m.getAcumulado() == 0.0 );
	}

	/**
	 * After paying with cash its accumulated increases.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testPagoMetalico() throws BusinessException {
		Cliente c = new Cliente("123", "nombre", "apellidos");
		Metalico m = new Metalico( c );
		m.pagar( 10 );
		
		assertTrue( m.getAcumulado() == 10.0 );
	}

}
