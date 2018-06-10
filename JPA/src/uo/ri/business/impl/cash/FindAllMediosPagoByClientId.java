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
package uo.ri.business.impl.cash;

import java.util.List;

import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.impl.Command;
import uo.ri.business.impl.util.DtoAssembler;
import uo.ri.business.repository.ClienteRepository;
import uo.ri.business.repository.MedioPagoRepository;
import uo.ri.conf.Factory;
import uo.ri.model.Cliente;
import uo.ri.model.MedioPago;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.Check;

/**
 * FindAllMediosPagoByClientId.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class FindAllMediosPagoByClientId implements Command<List<PaymentMeanDto>> {

	/** The client id. */
	private Long clientId;

	/**
	 * Instantiates a new find all medios pago by client id.
	 *
	 * @param clientId the client id
	 */
	public FindAllMediosPagoByClientId(Long clientId) {
		this.clientId = clientId;
	}

	/**
	 * This method checks that the client id corresponds to a real client in the
	 * system.
	 *
	 * @param clientId the id of the client
	 * @throws BusinessException the business exception
	 */
	private void assertExistenceClient(Long clientId) throws BusinessException {
		ClienteRepository cr = Factory.repository.forCliente();
		Cliente c = cr.findById(clientId);
		Check.isNotNull(c, "Ese cliente no existe");
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.impl.Command#execute()
	 */
	public List<PaymentMeanDto> execute() throws BusinessException {
		MedioPagoRepository mp = Factory.repository.forMedioPago();
		assertExistenceClient(clientId);
		List<MedioPago> list = mp.findPaymentMeansByClientId(clientId);
		return DtoAssembler.toPaymentMeanDtoList(list);
	}
}
