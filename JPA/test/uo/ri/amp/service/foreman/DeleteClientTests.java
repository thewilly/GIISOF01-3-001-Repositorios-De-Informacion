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
package uo.ri.amp.service.foreman;

import static org.junit.Assert.assertTrue;
import static uo.ri.amp.service.util.FixtureRepository.findClientByDni;
import static uo.ri.amp.service.util.FixtureRepository.registerNewClient;
import static uo.ri.amp.service.util.FixtureRepository.registerNewClientWithVehicle;
import static uo.ri.amp.service.util.FixtureRepository.registerNewRecommendedClient;

import org.junit.Before;
import org.junit.Test;

import uo.ri.amp.service.util.BaseServiceTests;
import uo.ri.business.ForemanService;
import uo.ri.conf.Factory;
import uo.ri.model.Cliente;
import uo.ri.util.exception.BusinessException;

/**
 * The Class DeleteClientTests.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class DeleteClientTests extends BaseServiceTests {

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Se borra un cliente sin vehiculos y sin recomendaciones hechas o
     * recibidas.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testDeleteClient() throws BusinessException {
	Cliente client = registerNewClient();

	ForemanService svc = Factory.service.forForeman();
	svc.deleteClient(client.getId());

	Cliente expected = findClientByDni(client.getDni());
	assertTrue(expected == null);
    }

    /**
     * Al tratar de borrar un cliente que no existe salta excepcion.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testDeleteNonExistingClient() throws BusinessException {
	Long NON_EXISTING_ID = -12345L;

	ForemanService svc = Factory.service.forForeman();
	try {
	    svc.deleteClient(NON_EXISTING_ID);
	} catch (BusinessException be) {
	    assertMsg(be, "El cliente no existe");
	}
    }

    /**
     * Al borrar un cliente se borra la recomendación recibida.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testDeleteRecommendation() throws BusinessException {
	Cliente recommender = registerNewClient();
	Cliente recommended = registerNewRecommendedClient(recommender);
	assertTrue(recommender.getRecomendacionesHechas().size() == 1);

	ForemanService svc = Factory.service.forForeman();
	svc.deleteClient(recommended.getId());

	Cliente expected = findClientByDni(recommended.getDni());
	assertTrue(expected == null);
	assertTrue(recommender.getRecomendacionesHechas().size() == 0);
    }

    /**
     * Al borrar un cliente se borran las recomendaciones que haya hecho.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testDeleteRecommendationsMade() throws BusinessException {
	Cliente recommender = registerNewClient();
	Cliente recommended1 = registerNewRecommendedClient(recommender);
	Cliente recommended2 = registerNewRecommendedClient(recommender);
	assertTrue(recommended1.getRecomendacionRecibida() != null);
	assertTrue(recommended2.getRecomendacionRecibida() != null);

	ForemanService svc = Factory.service.forForeman();
	svc.deleteClient(recommender.getId());

	Cliente expected = findClientByDni(recommender.getDni());
	assertTrue(expected == null);
	assertTrue(recommended1.getRecomendacionRecibida() == null);
	assertTrue(recommended2.getRecomendacionRecibida() == null);
    }

    /**
     * Salta excepcion al borrar un cliente que tenga vehiculos registrados.
     */
    @Test
    public void testWithVehicleCannotBeDeleted() {
	Cliente client = registerNewClientWithVehicle();

	ForemanService svc = Factory.service.forForeman();
	try {
	    svc.deleteClient(client.getId());
	} catch (BusinessException be) {
	    assertMsg(be,
		    "El cliente no puede ser eliminado al tener vehículos registrados");
	}
    }

}
