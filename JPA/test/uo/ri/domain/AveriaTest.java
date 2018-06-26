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
package uo.ri.domain;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uo.ri.model.Association;
import uo.ri.model.Averia;
import uo.ri.model.Cliente;
import uo.ri.model.Factura;
import uo.ri.model.Intervencion;
import uo.ri.model.Mecanico;
import uo.ri.model.Repuesto;
import uo.ri.model.Sustitucion;
import uo.ri.model.TipoVehiculo;
import uo.ri.model.Vehiculo;
import uo.ri.model.types.FacturaStatus;
import uo.ri.util.exception.BusinessException;

/**
 * The Class AveriaTest.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class AveriaTest {

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

    /**
     * Sets the up.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Before
    public void setUp() throws BusinessException {
	cliente = new Cliente("dni-cliente", "nombre", "apellidos");
	vehiculo = new Vehiculo("1234 GJI", "ibiza", "seat");
	Association.Poseer.link(cliente, vehiculo);

	tipoVehiculo = new TipoVehiculo("coche", 50.0 /* â‚¬/hora */);
	Association.Clasificar.link(tipoVehiculo, vehiculo);

	averia = new Averia(vehiculo, "falla la junta la trocla");
	mecanico = new Mecanico("dni-mecanico", "nombre", "apellidos");
	averia.assignTo(mecanico);

	intervencion = new Intervencion(mecanico, averia);
	intervencion.setMinutos(60);

	repuesto = new Repuesto("R1001", "junta la trocla", 100.0 /* â‚¬ */);
	sustitucion = new Sustitucion(repuesto, intervencion);
	sustitucion.setCantidad(2);

	averia.markAsFinished(); // changes status & compute price
    }

    /**
     * No se puede añadir a una factura una averia no terminada.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test(expected = BusinessException.class)
    public void testAveriaNoTerminadaException() throws BusinessException {
	averia.reopen();
	List<Averia> averias = new ArrayList<>();
	averias.add(averia);
	new Factura(0L, averias); // debe saltar excepcion: averia no terminada
    }

    /**
     * Una factura creada y con averias asignadas está en estado SIN_ABONAR.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testFacturaCreadaSinAbonar() throws BusinessException {
	List<Averia> averias = new ArrayList<>();
	averias.add(averia);
	Factura factura = new Factura(0L, averias);

	assertTrue(factura.getStatus() == FacturaStatus.SIN_ABONAR);
    }

    /**
     * El importe de la averia de referencia es 250.0
     */
    @Test
    public void testImporteAveria() {
	assertTrue(averia.getImporte() == 250.0);
    }

    /**
     * Calculo del importe de averia con intervenciones de varios mecanicos.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testImporteAveriaConDosIntervenciones()
	    throws BusinessException {
	averia.reopen();
	Mecanico otro = new Mecanico("1", "a", "n");
	averia.assignTo(otro);
	Intervencion i = new Intervencion(otro, averia);
	i.setMinutos(30);

	averia.markAsFinished();

	assertTrue(averia.getImporte() == 275.0);
    }

    /**
     * Calculo correcto de importe de averia al quitar intervenciones El
     * (re)cálculo se hace al pasar la factura a TERMINADA .
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testImporteAveriaQuitandoIntervencione()
	    throws BusinessException {
	averia.reopen();
	Mecanico otro = new Mecanico("1", "a", "n");
	averia.assignTo(otro);
	Intervencion i = new Intervencion(otro, averia);
	i.setMinutos(30);

	Association.Intervenir.unlink(intervencion);
	averia.markAsFinished();

	assertTrue(averia.getImporte() == 25.0);
    }

    /**
     * La fecha de averia se devuelve clonada.
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testSafeGetFechaClonada() {
	Date d = averia.getFecha();

	d.setYear(0);

	assertTrue(averia.getFecha().getYear() == new Date().getYear());
    }

    /**
     * Una averia no puede ser marcada como facturada si no tiene factura
     * asignada.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test(expected = BusinessException.class)
    public void testSinFacturaNoMarcarFacturada() throws BusinessException {
	averia.markAsInvoiced(); // Lanza excepción "No factura asignada"
    }

}
