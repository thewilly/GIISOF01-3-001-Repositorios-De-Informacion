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
package uo.ri.associations;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import alb.util.date.DateUtil;
import uo.ri.model.Association;
import uo.ri.model.Averia;
import uo.ri.model.Cargo;
import uo.ri.model.Cliente;
import uo.ri.model.Factura;
import uo.ri.model.Intervencion;
import uo.ri.model.Mecanico;
import uo.ri.model.Metalico;
import uo.ri.model.Repuesto;
import uo.ri.model.Sustitucion;
import uo.ri.model.TipoVehiculo;
import uo.ri.model.Vehiculo;
import uo.ri.util.exception.BusinessException;


/**
 * The Class PagarTest.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class PagarTest {
	
	/** The mecanico. */
	private Mecanico mecanico;
	
	/** The averia. */
	private Averia averia;
	
	/** The intervencion. */
	private Intervencion intervencion;
	
	/** The repuesto. */
	private Repuesto repuesto;
	
	/** The sustitucion. */
	private Sustitucion sustitucion;
	
	/** The vehiculo. */
	private Vehiculo vehiculo;
	
	/** The tipo vehiculo. */
	private TipoVehiculo tipoVehiculo;
	
	/** The cliente. */
	private Cliente cliente;
	
	/** The factura. */
	private Factura factura;
	
	/** The metalico. */
	private Metalico metalico;
	
	/** The cargo. */
	private Cargo cargo;

	/**
	 * Sets the up.
	 *
	 * @throws BusinessException the business exception
	 */
	@Before
	public void setUp() throws BusinessException {
		cliente = new Cliente("dni-cliente", "nombre", "apellidos");
		vehiculo = new Vehiculo("1234 GJI", "seat", "ibiza");
		Association.Poseer.link(cliente, vehiculo );

		tipoVehiculo = new TipoVehiculo("coche", 50.0);
		Association.Clasificar.link(tipoVehiculo, vehiculo);
		
		averia = new Averia(vehiculo, "falla la junta la trocla");
		mecanico = new Mecanico("dni-mecanico", "nombre", "apellidos");
		averia.assignTo(mecanico);
	
		intervencion = new Intervencion(mecanico, averia);
		intervencion.setMinutos(60);
		
		repuesto = new Repuesto("R1001", "junta la trocla", 100.0);
		sustitucion = new Sustitucion(repuesto, intervencion);
		sustitucion.setCantidad(2);
		
		averia.markAsFinished();

		factura = new Factura(0L, DateUtil.today());
		factura.addAveria(averia);
		
		metalico = new Metalico( cliente );
		cargo = new Cargo(factura, metalico, 100.0);
	}
	
	/**
	 * Test cargar add.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testCargarAdd() throws BusinessException {
		assertTrue( metalico.getCargos().contains( cargo ));
		assertTrue( factura.getCargos().contains( cargo ));
		
		assertTrue( cargo.getFactura() == factura );
		assertTrue( cargo.getMedioPago() == metalico );
		
		assertTrue( metalico.getAcumulado() == 100.0 );
	}

	/**
	 * Test cargar remove.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testCargarRemove() throws BusinessException {
		cargo.rewind();  // removes this charge: unlink from Invoice and PaymentMean
		
		assertTrue( ! metalico.getCargos().contains( cargo ));
		assertTrue( metalico.getCargos().size() == 0 );

		assertTrue( ! factura.getCargos().contains( cargo ));
		assertTrue( metalico.getCargos().size() == 0 );
		
		assertTrue( cargo.getFactura() == null );
		assertTrue( cargo.getMedioPago() == null );
	}

	/**
	 * Test pagar add.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testPagarAdd() throws BusinessException {
		assertTrue( cliente.getMediosPago().contains( metalico ));
		assertTrue( metalico.getCliente() == cliente );
	}

	/**
	 * Test pagar remove.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testPagarRemove() throws BusinessException {
		Association.Pagar.unlink(cliente, metalico);
		
		assertTrue( ! cliente.getMediosPago().contains( metalico ));
		assertTrue( cliente.getMediosPago().size() == 0 );
		assertTrue( metalico.getCliente() == null );
	}

}
