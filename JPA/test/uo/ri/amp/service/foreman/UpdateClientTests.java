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
import uo.ri.amp.service.util.Fixture;
import uo.ri.amp.service.util.FixtureRepository;
import uo.ri.business.ForemanService;
import uo.ri.business.dto.ClientDto;
import uo.ri.business.impl.util.DtoAssembler;
import uo.ri.conf.Factory;
import uo.ri.model.Cliente;
import uo.ri.util.exception.BusinessException;

/**
 * The Class UpdateClientTests.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class UpdateClientTests extends BaseServiceTests {

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
     * El atributo dni no se actualiza aunque el dto lleve nuevo dni.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testDniDoesNotUpdate() throws BusinessException {
	Cliente c = FixtureRepository.registerNewClient();
	ClientDto dto = DtoAssembler.toDto(c);
	String previousDni = c.getDni();

	dto.dni = "m-" + dto.dni; // <-- must be ignored

	ForemanService svc = Factory.service.forForeman();
	svc.updateClient(dto);

	Cliente expected = FixtureRepository.findClientByDni(c.getDni());
	assertTrue(expected.getDni().equals(previousDni));
    }

    /**
     * Se actualiza el cliente con todos los campos del dto.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testUpdate() throws BusinessException {
	Cliente c = FixtureRepository.registerNewClient();
	ClientDto dto = DtoAssembler.toDto(c);

	dto.addressStreet = "m-" + dto.addressStreet;
	dto.addressCity = "m-" + dto.addressCity;
	dto.addressZipcode = "m-" + dto.addressZipcode;
	dto.email = "m-" + dto.email;
	dto.phone = "m-" + dto.phone;
	dto.name = "m-" + dto.name;
	dto.surname = "m-" + dto.surname;

	ForemanService svc = Factory.service.forForeman();
	svc.updateClient(dto);

	Cliente expected = FixtureRepository.findClientByDni(c.getDni());
	assertSameData(dto, expected);
    }

    /**
     * Si el cliente no existe salta una excepción.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test(expected = BusinessException.class)
    public void testUpdateClientDoesNotExist() throws BusinessException {
	ClientDto dto = Fixture.newClientDto();
	Long DOES_NOT_EXIST = -12345L;

	dto.id = DOES_NOT_EXIST;

	ForemanService svc = Factory.service.forForeman();
	svc.updateClient(dto);
    }

}
