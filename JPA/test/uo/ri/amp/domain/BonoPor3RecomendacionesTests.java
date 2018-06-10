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

import alb.util.random.Random;
import uo.ri.model.Association;
import uo.ri.model.Averia;
import uo.ri.model.Cliente;
import uo.ri.model.Mecanico;
import uo.ri.model.Recomendacion;
import uo.ri.model.Vehiculo;
import uo.ri.util.exception.BusinessException;

/**
 * The Class BonoPor3RecomendacionesTests.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class BonoPor3RecomendacionesTests {

	/** The c. */
	private Cliente c;
	
	/** The cr 1. */
	private Cliente cr1; // cliente recomendado 1
	
	/** The cr 2. */
	private Cliente cr2; // cliente recomendado 2
	
	/** The cr 3. */
	private Cliente cr3; // cliente recomendado 3
	
	/** The cr 4. */
	private Cliente cr4; // cliente recomendado 4
	
	/** The m. */
	private Mecanico m;

	/**
	 * Adds the averia.
	 *
	 * @param v2 the v 2
	 * @param m2 the m 2
	 * @return the averia
	 * @throws BusinessException the business exception
	 */
	private Averia addAveria(Vehiculo v2, Mecanico m2) throws BusinessException {
		sleep( 10 /*msec*/ );
		Averia a = new Averia( v2 );
		a.assignTo( m2 );
		a.markAsFinished();
		return a;
	}

	/**
	 * Adds the vehiculo.
	 *
	 * @param c2 the c 2
	 * @return the vehiculo
	 */
	private Vehiculo addVehiculo(Cliente c2) {
		Vehiculo v = new Vehiculo( Random.string( 7 ));
		Association.Poseer.link(c2, v);
		return v;
	}
	
	/**
	 * Adds the vehiculo con averia.
	 *
	 * @param c2 the c 2
	 * @param m2 the m 2
	 * @return the vehiculo
	 * @throws BusinessException the business exception
	 */
	private Vehiculo addVehiculoConAveria(Cliente c2, Mecanico m2) throws BusinessException {
		Vehiculo v = addVehiculo(c2);
		addAveria(v, m2);
		return v;
	}

	/**
	 * Recomendar.
	 *
	 * @param recomendador the recomendador
	 * @param recomendado the recomendado
	 * @return the recomendacion
	 */
	private Recomendacion recomendar(Cliente recomendador, Cliente recomendado) {
		return new Recomendacion(recomendador, recomendado);
	}
	
	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		c = new Cliente("123a", "n", "a");
		cr1 = new Cliente("234b", "nr1", "ar1");
		cr2 = new Cliente("345c", "nr2", "ar2");
		cr3 = new Cliente("456d", "nr3", "ar3");
		cr4 = new Cliente("567e", "nr4", "ar4");
		m = new Mecanico("678f");
	}

	/**
	 * Sleep.
	 *
	 * @param millis the millis
	 */
	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// dont't care if this occurs
		}
	}
	
	/**
	 * Un cliente con reparaciones y 2 recomendaciones que han hecho
	 * reparaciones y 1 que no tiene reparaciones no puede ser elegible para
	 * bono por recomendaciones.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testCliente3Recomendados1SinReparaciones() throws BusinessException {
		recomendar(c, cr1);
		recomendar(c, cr2);
		addVehiculoConAveria(c, m);
		addVehiculoConAveria(cr1, m);
		addVehiculoConAveria(cr2, m);
		addVehiculo(cr3);	// <-- Sin averia
		
		assertTrue( c.elegibleBonoPorRecomendaciones() == false );
	}
	
	/**
	 * Un cliente con reparaciones y 3 recomendaciones que han hecho
	 * reparaciones sí es elegible para bono por recomendaciones.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testCliente3RecomendadosConReparaciones() throws BusinessException {
		recomendar(c, cr1);
		recomendar(c, cr2);
		recomendar(c, cr3);
		addVehiculoConAveria(c, m);
		addVehiculoConAveria(cr1, m);
		addVehiculoConAveria(cr2, m);
		addVehiculoConAveria(cr3, m);
		
		assertTrue( c.elegibleBonoPorRecomendaciones() );
	}

	/**
	 * Un cliente con reparaciones y 3 recomendaciones que han hecho
	 * reparaciones y una recomendacion ya usada para bono generado no es
	 * elegible para bono por recomendaciones.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testCliente3RecomendadosConReparacionesUnaUsada() throws BusinessException {
		Recomendacion r1 = recomendar(c, cr1);
		recomendar(c, cr2);
		recomendar(c, cr3);
		addVehiculoConAveria(c, m);
		addVehiculoConAveria(cr1, m);
		addVehiculoConAveria(cr2, m);
		addVehiculoConAveria(cr3, m);
		
		assertTrue( c.elegibleBonoPorRecomendaciones() );
		r1.markAsUsadaBono();
		
		assertTrue( c.elegibleBonoPorRecomendaciones() == false);
	}

	/**
	 * Un cliente con reparaciones y 3 recomendaciones no puede ser elegible si
	 * sus recomendados no han hecho reparaciones.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testCliente3RecomendadosSinReparaciones() throws BusinessException {
		recomendar(c, cr1);
		recomendar(c, cr2);
		recomendar(c, cr3);
		addVehiculoConAveria(c, m);
		addVehiculo(cr1);	// <-- Sin averia
		addVehiculo(cr2);	// <-- Sin averia
		addVehiculo(cr3);	// <-- Sin averia
		
		assertTrue( c.elegibleBonoPorRecomendaciones() == false );
	}

	/**
	 * Un cliente con reparaciones y 4 recomendaciones, solo 3 han hecho
	 * reparaciones y una recomendacion ya usada para bono generado no es
	 * elegible para bono por recomendaciones.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testCliente4Recomendados3ConReparacionesUnaUsada() throws BusinessException {
		Recomendacion r1 = recomendar(c, cr1);
		recomendar(c, cr2);
		recomendar(c, cr3);
		recomendar(c, cr4);
		addVehiculoConAveria(c, m);
		addVehiculoConAveria(cr1, m);
		addVehiculoConAveria(cr2, m);
		addVehiculoConAveria(cr3, m);
		addVehiculo(cr4);	// sin reparaciones
		
		assertTrue( c.elegibleBonoPorRecomendaciones() );
		r1.markAsUsadaBono(); 
		
		assertTrue( c.elegibleBonoPorRecomendaciones() == false);
	}
	
	/**
	 * Un cliente con vehiculo pero sin averias no puede ser elegible para bono
	 * por recomendaciones.
	 */
	@Test
	public void testClienteConVehiculo() {
		addVehiculo( c );
		
		assertTrue( c.elegibleBonoPorRecomendaciones() == false );
	}

	/**
	 * Un cliente con vehiculo y averia pero sin recomendados no puede ser
	 * elejible para bono por recomendaciones.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testClienteConVehiculoYAveria() throws BusinessException {
		addVehiculoConAveria(c, m);
		
		assertTrue( c.elegibleBonoPorRecomendaciones() == false );
	}
	
	/**
	 * Un cliente recien registrado no puede tener derecho a bono por
	 * recomendaciones.
	 */
	@Test
	public void testClienteNuevo() {
		assertTrue( c.elegibleBonoPorRecomendaciones() == false );
	}

	/**
	 * Un cliente con 3 recomendados que han hecho reparaciones, pero sin
	 * reparaciones realizadas por él no puede ser elegible para bono por
	 * recomendaciones.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testClienteSinReparaciones3Recomendados() throws BusinessException {
		recomendar(c, cr1);
		recomendar(c, cr2);
		recomendar(c, cr3);
		addVehiculo( c );
		addVehiculoConAveria(cr1, m);
		addVehiculoConAveria(cr2, m);
		addVehiculoConAveria(cr3, m);
		
		assertTrue( c.elegibleBonoPorRecomendaciones() == false );
	}

}
