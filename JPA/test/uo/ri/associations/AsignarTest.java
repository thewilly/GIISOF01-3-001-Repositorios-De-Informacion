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

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import uo.ri.model.Association;
import uo.ri.model.Averia;
import uo.ri.model.Cliente;
import uo.ri.model.Mecanico;
import uo.ri.model.TipoVehiculo;
import uo.ri.model.Vehiculo;
import uo.ri.util.exception.BusinessException;

/**
 * The Class AsignarTest.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class AsignarTest {

    /** The mecanico. */
    private Mecanico mecanico;

    /** The averia. */
    private Averia averia;

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
	vehiculo = new Vehiculo("1234 GJI", "seat", "ibiza");
	Association.Poseer.link(cliente, vehiculo);

	tipoVehiculo = new TipoVehiculo("coche", 50.0);
	Association.Clasificar.link(tipoVehiculo, vehiculo);

	averia = new Averia(vehiculo, "falla la junta la trocla");

	mecanico = new Mecanico("dni-mecanico", "nombre", "apellidos");
	averia.assignTo(mecanico);
    }

    /**
     * Test asignar linked.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testAsignarLinked() throws BusinessException {
	assertTrue(mecanico.getAsignadas().contains(averia));
	assertTrue(averia.getMecanico() == mecanico);
    }

    /**
     * Test asignar unlink.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testAsignarUnlink() throws BusinessException {
	averia.desassign();

	assertTrue(!mecanico.getAsignadas().contains(averia));
	assertTrue(mecanico.getAsignadas().size() == 0);
	assertTrue(averia.getMecanico() == null);
    }

    /**
     * Test safe return.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testSafeReturn() throws BusinessException {
	Set<Averia> asignadas = mecanico.getAsignadas();
	asignadas.remove(averia);

	assertTrue(asignadas.size() == 0);
	assertTrue(
		"Se debe retornar copia de la coleccion o hacerla de solo lectura",
		mecanico.getAsignadas().size() == 1);
    }

}
