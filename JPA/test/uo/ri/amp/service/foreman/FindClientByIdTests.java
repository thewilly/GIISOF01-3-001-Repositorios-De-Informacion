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

import org.junit.Before;
import org.junit.Test;

import uo.ri.amp.service.util.BaseServiceTests;
import uo.ri.amp.service.util.FixtureRepository;
import uo.ri.business.ForemanService;
import uo.ri.business.dto.ClientDto;
import uo.ri.conf.Factory;
import uo.ri.model.Cliente;
import uo.ri.util.exception.BusinessException;

/**
 * The Class FindClientByIdTests.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class FindClientByIdTests extends BaseServiceTests {

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Se devuelve el Dto correcto para el cliente que existe.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testFindExisting() throws BusinessException {
		Cliente c = FixtureRepository.registerNewClient();
		
		ForemanService svc = Factory.service.forForeman();
		ClientDto dto = svc.findClientById( c.getId() );
		assertSameData(dto, c);
	}
	
	/**
	 * Si el cliente no existe se devuelve null, y no salta excepción.
	 *
	 * @throws BusinessException the business exception
	 */
	@Test
	public void testFindNonExisting() throws BusinessException {
		Long NON_EXISTING_ID = -12345L;
		
		ForemanService svc = Factory.service.forForeman();
		ClientDto dto = svc.findClientById( NON_EXISTING_ID );
		
		assertTrue( dto == null );
	}
	

}
