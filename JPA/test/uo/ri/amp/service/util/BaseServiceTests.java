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
package uo.ri.amp.service.util;

import static org.junit.Assert.assertTrue;
import static uo.ri.amp.service.util.FixtureRepository.findVouchersByClientId;

import java.util.List;

import uo.ri.amp.util.repository.inmemory.InMemoryCommandExecutorFactory;
import uo.ri.amp.util.repository.inmemory.InMemoryRepositoryFactory;
import uo.ri.business.dto.ClientDto;
import uo.ri.business.impl.BusinessServiceFactory;
import uo.ri.conf.Factory;
import uo.ri.model.Bono;
import uo.ri.model.Cliente;
import uo.ri.model.MedioPago;
import uo.ri.util.exception.BusinessException;

/**
 * The Class BaseServiceTests.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public abstract class BaseServiceTests {

	/**
	 * Does the dependency injection for all Business layer tests.
	 */
	public BaseServiceTests() {
		Factory.service = new BusinessServiceFactory();
		Factory.executor = new InMemoryCommandExecutorFactory();
		Factory.repository = new InMemoryRepositoryFactory();
	}

	/**
	 * Asserts whether the exception message is the expected.
	 *
	 * @param be the be
	 * @param msg the msg
	 */
	protected void assertMsg(BusinessException be, String msg) {
		assertTrue(be.getMessage().equals(msg));
	}

	/**
	 * Assert same data.
	 *
	 * @param dto the dto
	 * @param entity the entity
	 */
	protected void assertSameData(ClientDto dto, Cliente entity) {
		assertTrue(entity.getAddress().getStreet().equals(dto.addressStreet));
		assertTrue(entity.getAddress().getCity().equals(dto.addressCity));
		assertTrue(entity.getAddress().getZipcode().equals(dto.addressZipcode));
		assertTrue(entity.getEmail().equals(dto.email));
		assertTrue(entity.getPhone().equals(dto.phone));
		assertTrue(entity.getNombre().equals(dto.name));
		assertTrue(entity.getApellidos().equals(dto.surname));
	}

	/**
	 * Gets the first voucher.
	 *
	 * @param c the c
	 * @return the first voucher
	 */
	protected Bono getFirstVoucher(Cliente c) {
		List<Bono> bns = findVouchersByClientId(c.getId());
		assertTrue(bns.size() == 1);
		Bono expected = bns.get(0);
		return expected;
	}

	/**
	 * Gets the metalico.
	 *
	 * @param c the c
	 * @return the metalico
	 */
	protected MedioPago getMetalico(Cliente c) {
		return c.getMediosPago().stream().findFirst().orElse(null);
	}

}
