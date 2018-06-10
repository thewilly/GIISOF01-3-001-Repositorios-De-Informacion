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
package uo.ri.business.impl.admin;

import java.util.List;

import uo.ri.business.dto.VoucherDto;
import uo.ri.business.impl.Command;
import uo.ri.business.impl.util.DtoAssembler;
import uo.ri.business.repository.ClienteRepository;
import uo.ri.business.repository.MedioPagoRepository;
import uo.ri.conf.Factory;
import uo.ri.model.Bono;
import uo.ri.model.Cliente;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.Check;

/**
 * FindAllBondsByClientId.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class FindAllBondsByClientId implements Command<List<VoucherDto>> {

	/** The client id. */
	private long clientId;

	/**
	 * Instantiates a new find all bonds by client id.
	 *
	 * @param id the id
	 */
	public FindAllBondsByClientId(long id) {
		this.clientId = id;
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.impl.Command#execute()
	 */
	@Override
	public List<VoucherDto> execute() throws BusinessException {
		MedioPagoRepository repository = Factory.repository.forMedioPago();
		assertClientExists(clientId);
		List<Bono> list = repository.findVouchersByClientId(clientId);
		return DtoAssembler.toVoucherDtoList(list);
	}

	/**
	 * This method checks if the id passed to the constructor, corresponds to a
	 * client in the system for then listing all the vouchers.
	 *
	 * @param clientId the id of the client
	 * @throws BusinessException the business exception
	 */
	private void assertClientExists(long clientId) throws BusinessException {
		ClienteRepository repository = Factory.repository.forCliente();
		Cliente clientInDB = repository.findById(clientId);
		Check.isNotNull(clientInDB, "Ese cliente no existe en el sistema");
	}
}
