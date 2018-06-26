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
 * The Class LiquidarFacturaTests.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class LiquidarFacturaTests {

    /** The a. */
    private Averia a;

    /** The cash. */
    private Metalico cash;

    /** The i. */
    private Intervencion i;

    /** The m. */
    private Mecanico m;

    /** The v. */
    private Vehiculo v;

    /**
     * Crear averia terminada.
     *
     * @param m
     *            the m
     * @param v
     *            the v
     * @param min
     *            the min
     * @return the averia
     * @throws BusinessException
     *             the business exception
     */
    private Averia crearAveriaTerminada(Mecanico m, Vehiculo v, int min)
	    throws BusinessException {
	a = new Averia(v, "for test");
	a.assignTo(m);
	i = new Intervencion(m, a);
	i.setMinutos(min);
	a.markAsFinished();
	return a;
    }

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
	Cliente c = new Cliente("123", "n", "a");
	cash = new Metalico(c);
	m = new Mecanico("123a");
	v = new Vehiculo("123-ABC");
	TipoVehiculo tv = new TipoVehiculo("v", 300 /* €/hour */);
	Association.Clasificar.link(tv, v);

	a = crearAveriaTerminada(m, v, 83 /* min */); // gives 500 € ii
    }

    /**
     * No se puede liquidar una factura sin averías.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test(expected = BusinessException.class)
    public void testFacturaSinAverias() throws BusinessException {
	Factura f = new Factura(123L);
	f.settle();
    }

    /**
     * Se puede liquidar una factura con importe cero sin cargos.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testImporteCero() throws BusinessException {
	Averia a = crearAveriaTerminada(m, v, 0 /* mins */); // 0€ importe
	Factura f = new Factura(123L, Arrays.asList(a));
	f.settle();

	assertTrue(f.isSettled());
    }

    /**
     * No se puede marcar como liquidada una factura que no esté totalmente
     * pagada con margen de +-0,01 €.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test(expected = BusinessException.class)
    public void testNoDelTodoPagada() throws BusinessException {
	Factura f = new Factura(123L, Arrays.asList(a));
	double importe = f.getImporte() - 0.011 /* € */;
	new Cargo(f, cash, importe);
	f.settle();
    }

    /**
     * Se puede marcar como liquidada una factura completamente pagada dentro
     * del margen +-0,01 €.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testPagada() throws BusinessException {
	Factura f = new Factura(123L, Arrays.asList(a));
	new Cargo(f, cash, f.getImporte());
	f.settle();

	assertTrue(f.isSettled());
    }

    /**
     * Se puede marcar como liquidada una factura completamente pagada dentro
     * del margen +-0,01 €.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testPagadaEnMargenNegativo() throws BusinessException {
	Factura f = new Factura(123L, Arrays.asList(a));
	double importe = f.getImporte() - 0.009 /* € */;
	new Cargo(f, cash, importe);
	f.settle();

	assertTrue(f.isSettled());
    }

    /**
     * Se puede marcar como liquidada una factura completamente pagada con
     * margen de +-0,01 €.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testPagadaEnMargenPositivo() throws BusinessException {
	Factura f = new Factura(123L, Arrays.asList(a));
	double importe = f.getImporte() + 0.009 /* € */;
	new Cargo(f, cash, importe);
	f.settle();

	assertTrue(f.isSettled());
    }

    /**
     * No se puede marcar como liquidada una factura sobrepagada con margen de
     * +-0,01 €.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test(expected = BusinessException.class)
    public void testSobrePagada() throws BusinessException {
	Factura f = new Factura(123L, Arrays.asList(a));
	double importe = f.getImporte() + 0.011 /* € */;
	new Cargo(f, cash, importe);
	f.settle();
    }
}
