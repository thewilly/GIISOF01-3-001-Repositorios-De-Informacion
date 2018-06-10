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
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import uo.ri.amp.service.util.BaseServiceTests;
import uo.ri.amp.service.util.Fixture;
import uo.ri.amp.service.util.FixtureRepository;
import uo.ri.business.ForemanService;
import uo.ri.business.dto.ClientDto;
import uo.ri.conf.Factory;
import uo.ri.model.Cliente;
import uo.ri.model.MedioPago;
import uo.ri.model.Recomendacion;
import uo.ri.util.exception.BusinessException;

/**
 * The Class AddClientTests.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class AddClientTests extends BaseServiceTests {

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Al cliente añadido se le ha asignado su medio de pago en metálico .
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testAddClient() throws BusinessException {
		ClientDto client = Fixture.newClientDto();
		Long WITHOUT_RECOMENDATION = null;
		
		ForemanService svc = Factory.service.forForeman();
		svc.addClient(client, WITHOUT_RECOMENDATION );
		
		Cliente expected = FixtureRepository.findClientByDni( client.dni );
		assertSameData(client, expected);

		assertTrue( expected.getMediosPago().size() == 1 );
		MedioPago mp = getMetalico( expected );
		assertTrue( mp != null);
		assertTrue( mp.getAcumulado() == 0.0 );
		assertTrue( mp.getCargos().size() == 0 );
		assertTrue( mp.getCliente().equals( expected ));
	}

	/**
	 * No se puede añadir un cliente con dni de otro ya existente.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testAddRepeatedClient() throws BusinessException {
		Long WITHOUT_RECOMENDATION = null;
		Cliente c = FixtureRepository.registerNewClient();
		ClientDto client = Fixture.newClientDto();
		client.dni = c.getDni(); // <---repeated dni
		
		ForemanService svc = Factory.service.forForeman();
		try {
			svc.addClient(client, WITHOUT_RECOMENDATION );
			fail("An exception must be thrown");
		} catch (BusinessException be) {
			assertMsg( be, "Ya existe un cliente con ese dni");
		}
	}

	/**
	 * Si el cliente recomendador no existe salta excepcion.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testAddWithNonExisitngRecomender() throws BusinessException {
		Long NON_EXISTING_RECOMENDER_ID = -1000L;
		ClientDto client = Fixture.newClientDto();
		
		ForemanService svc = Factory.service.forForeman();
		try {
			svc.addClient(client, NON_EXISTING_RECOMENDER_ID );
			fail("An exception must be thrown");
		} catch (BusinessException be) {
			assertMsg( be, "No existe el cliente recomendador");
		}
	}
	
	/**
	 * Al cliente añadido por recomendacion se le registra la recomendación.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testAddWithRecomendation() throws BusinessException {
		Cliente recomender = FixtureRepository.registerNewClient();
		ClientDto client = Fixture.newClientDto();
		
		ForemanService svc = Factory.service.forForeman();
		svc.addClient(client, recomender.getId() );
		
		Cliente expected = FixtureRepository.findClientByDni( client.dni );
		assertSameData(client, expected);
		
		Recomendacion r = expected.getRecomendacionRecibida();
		assertTrue( r != null );
		assertTrue( r.getRecomendador().equals( recomender ));
		assertTrue( r.getRecomendado().equals( expected ));
		
		assertTrue( recomender.getRecomendacionesHechas().size() == 1);
		assertTrue( recomender.getRecomendacionesHechas().contains( r ));
	}
	
}
