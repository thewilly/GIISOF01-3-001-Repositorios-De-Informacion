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

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import uo.ri.model.Association;
import uo.ri.model.Averia;
import uo.ri.model.Cargo;
import uo.ri.model.Cliente;
import uo.ri.model.Factura;
import uo.ri.model.Intervencion;
import uo.ri.model.Mecanico;
import uo.ri.model.Metalico;
import uo.ri.model.TipoVehiculo;
import uo.ri.model.Vehiculo;
import uo.ri.util.exception.BusinessException;

/**
 * The Class AveriaTests.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class AveriaTests {

	/** The c. */
	private Cliente c;
	
	/** The v. */
	private Vehiculo v;
	
	/** The m. */
	private Mecanico m;
	
	/** The a. */
	private Averia a;

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		c = new Cliente("123a", "n", "a");
		m = new Mecanico("123a");
		v = new Vehiculo("123-ABC");
		TipoVehiculo tv = new TipoVehiculo("tv", 30 /* €/hour */);
		
		Association.Poseer.link(c,  v);
		Association.Clasificar.link(tv, v);
		
		a = new Averia(v, "for test");
		a.assignTo(m);
		Intervencion i = new Intervencion(m, a);
		i.setMinutos(10 /* min */);
		a.markAsFinished();
	}

	/**
	 * Una averia facturada pero no pagada no puede ser elegida para Bono3.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testAveriaFacturadaNoPagadaNoElegibleBono3() throws BusinessException {
		new Factura(123L, Arrays.asList( a ));  // Pasa a facturada
		
		assertTrue( a.esElegibleParaBono3() == false );
	}


	/**
	 * Una averia facturada y pagada puede ser elegida para Bono3.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testAveriaFacturadaPagadaElegibleBono3() throws BusinessException {
		Factura f = new Factura(123L, Arrays.asList( a ));  // Averia facturada
		Metalico m = new Metalico(c);
		new Cargo(f, m, f.getImporte());
		f.settle();  // Factura liquidada
		
		assertTrue( a.esElegibleParaBono3() );
	}


	/**
	 * Una averia no facturada no puede ser elegida para Bono3.
	 */
	@Test
	public void testAveriaNoFacturadaNoElegibleBono3() {
		assertTrue( a.esElegibleParaBono3() == false );
	}


	/**
	 * Una averia facturada, pagada y ya usada para Bono3 no puede ser elegida
	 * para Bono3 otra vez.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testAveriaYaUsadaNoElegibleBono3() throws BusinessException {
		Factura f = new Factura(123L, Arrays.asList( a ));  // Averia facturada
		Metalico m = new Metalico(c);
		new Cargo(f, m, f.getImporte());
		f.settle();  // Factura liquidada
		
		assertTrue( a.esElegibleParaBono3() );
		a.markAsBono3Used();
		assertTrue( a.esElegibleParaBono3() == false );
	}

}
