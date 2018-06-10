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
import static org.junit.Assert.fail;

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
 * The Class BonoPorFactura500Tests.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class BonoPorFactura500Tests {

	/** The i. */
	private Intervencion i;
	
	/** The a. */
	private Averia a;
	
	/** The cash. */
	private Metalico cash;

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		Cliente c = new Cliente("123", "n", "a");
		cash = new Metalico( c );
		Mecanico m = new Mecanico("123a");
		Vehiculo v = new Vehiculo("123-ABC");
		TipoVehiculo tv = new TipoVehiculo("v", 300 /* €/hour */);
		Association.Clasificar.link(tv, v);
		
		a = new Averia(v, "for test");
		a.assignTo( m );
		i = new Intervencion(m, a);
		i.setMinutos( 83 /* min */); // gives 500 € ii
	}

	/**
	 * Un factura por importe superior a 500 € pagada puede generar Bono500 y
	 * ser marcada como usada para Bono500.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testCanGenerateVoucher() throws BusinessException {
		a.markAsFinished();
		Factura f = new Factura(123L, Arrays.asList(a) );
		new Cargo(f, cash, f.getImporte());
		f.settle(); // factura pagada
		
		assertTrue( f.puedeGenerarBono500() );
		f.markAsBono500Used();
	}
	
	/**
	 * Una factura con importe inferior a 500 € sin pagar no puede generar bono
	 * y no pueder ser marcada como usada para ello.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testLess500CannotGenerateVoucher() throws BusinessException {
		i.setMinutos( 82 /* min */ ); // gives 499 €
		a.markAsFinished();
		Factura f = new Factura(123L, Arrays.asList(a) );
		
		assertTrue( f.puedeGenerarBono500() == false );
		try {
			f.markAsBono500Used();
			fail( "An exception must be thrown" );
		} catch (BusinessException be) {}

	}

	/**
	 * Una factura con importe inferior a 500 € pagada no puede generar bono y
	 * no puede ser marcada como usada para ello.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testLess500PaidGenerateVoucher() throws BusinessException {
		i.setMinutos( 82 /* min */ ); // gives 499 €
		a.markAsFinished();
		Factura f = new Factura(123L, Arrays.asList(a) );
		new Cargo(f, cash, f.getImporte());
		f.settle(); 	// paid
		
		assertTrue( f.puedeGenerarBono500() == false );
		try {
			f.markAsBono500Used();
			fail( "An exception must be thrown" );
		} catch (BusinessException be) {}
	}
	
	/**
	 * Una factura que ha generado bono ya no puede generar otro.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testMarkAsBono500Used() throws BusinessException {
		a.markAsFinished();
		Factura f = new Factura(123L, Arrays.asList(a) );
		new Cargo(f, cash, f.getImporte());
		f.settle(); 	// paid
		f.markAsBono500Used();

		assertTrue( f.puedeGenerarBono500() == false );
	}

	/**
	 * Un factura por importe superior a 500 € pero no pagada no puede generar
	 * bono500 y no puede ser marcada como usada para Bono500.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testMas500NoPagadaGenerateVoucher() throws BusinessException {
		a.markAsFinished();
		Factura f = new Factura(123L, Arrays.asList(a) ); // no pagada
		
		assertTrue( f.puedeGenerarBono500() == false );
		try {
			f.markAsBono500Used();
			fail( "An exception must be thrown" );
		} catch (BusinessException be) {}
	}

}
