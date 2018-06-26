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
package uo.ri.business.impl.foreman;

import uo.ri.business.dto.ClientDto;
import uo.ri.business.impl.Command;
import uo.ri.business.impl.util.DtoAssembler;
import uo.ri.business.repository.ClienteRepository;
import uo.ri.conf.Factory;
import uo.ri.model.Cliente;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.Check;

/**
 * UpdateClient.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class UpdateClient implements Command<Void> {

    /** The client dto. */
    private ClientDto clientDto;

    /** The clients repository. */
    private ClienteRepository clientsRepository = Factory.repository
	    .forCliente();

    /**
     * Instantiates a new update client.
     *
     * @param dto
     *            the dto
     */
    public UpdateClient(ClientDto dto) {
	this.clientDto = dto;
    }

    /*
     * (non-Javadoc)
     * 
     * @see uo.ri.business.impl.Command#execute()
     */
    @Override
    public Void execute() throws BusinessException {
	Cliente clientInDB = clientsRepository.findById(clientDto.id);
	Check.isNotNull(clientInDB, "El cliente no existe");
	Cliente updatedClient = DtoAssembler.toEntity(clientDto);
	clientInDB.setNombre(updatedClient.getNombre());
	clientInDB.setApellidos(updatedClient.getApellidos());
	clientInDB.setEmail(updatedClient.getEmail());
	clientInDB.setPhone(updatedClient.getPhone());
	clientInDB.setAddress(updatedClient.getAddress());
	return null;
    }
}
