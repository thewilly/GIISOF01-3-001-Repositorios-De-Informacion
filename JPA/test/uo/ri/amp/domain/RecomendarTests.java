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

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import uo.ri.model.Cliente;
import uo.ri.model.Recomendacion;
import uo.ri.util.exception.BusinessException;

/**
 * The Class RecomendarTests.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class RecomendarTests {
	
	/** The recomendador. */
	private Cliente recomendador;
	
	/** The recomendado. */
	private Cliente recomendado;

	/**
	 * Sets the up.
	 *
	 * @throws BusinessException the business exception
	 */
	@Before
	public void setUp() throws BusinessException {
		recomendador = new Cliente("123a", "recomendador", "a");
		recomendado = new Cliente("234b", "recomendado", "ap");
	}

	/**
	 * Un cliente recomienda a otro (link).
	 */
	@Test
	public void testRecomendarLink() {
		Recomendacion r = new Recomendacion(recomendador, recomendado);
		
		assertTrue( r.getRecomendado() == recomendado );
		assertTrue( r.getRecomendador() == recomendador );
		
		assertTrue( recomendador.getRecomendacionesHechas().contains( r ) );
		assertTrue( recomendador.getRecomendacionRecibida() == null );
		
		assertTrue( recomendado.getRecomendacionRecibida() == r );
		assertTrue( recomendado.getRecomendacionesHechas().isEmpty() );
	}
	
	/**
	 * Recomendar.unlink
	 */
	@Test
	public void testRecomendarUnlink() {
		Recomendacion r = new Recomendacion(recomendador, recomendado);
		r.unlink();
		
		assertTrue( r.getRecomendado() == null );
		assertTrue( r.getRecomendador() == null );
		
		assertTrue( recomendador.getRecomendacionesHechas().isEmpty() );
		assertTrue( recomendador.getRecomendacionRecibida() == null );
		
		assertTrue( recomendado.getRecomendacionRecibida() == null );
		assertTrue( recomendado.getRecomendacionesHechas().isEmpty() );		
	}
	
	/**
	 * Safe return.
	 */
	@Test
	public void testSafeReturn() {
		Recomendacion r = new Recomendacion(recomendador, recomendado);
		Set<Recomendacion> rs = recomendador.getRecomendacionesHechas();
		
		rs.clear();
		assertTrue( rs.isEmpty() );
		assertTrue( "Se debe retornar copia de la coleccion o hacerla de solo lectura",
				recomendador.getRecomendacionesHechas().size() == 1 );
		assertTrue( recomendador.getRecomendacionesHechas().contains( r ));
	}


}
