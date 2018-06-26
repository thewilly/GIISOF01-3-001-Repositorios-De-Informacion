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
package uo.ri.business.impl;

import java.util.List;

import uo.ri.business.ForemanService;
import uo.ri.business.dto.ClientDto;
import uo.ri.business.impl.foreman.CreateClient;
import uo.ri.business.impl.foreman.RemoveClient;
import uo.ri.business.impl.foreman.FindAllClients;
import uo.ri.business.impl.foreman.FindClientById;
import uo.ri.business.impl.foreman.FindRecommendedClientsByClientId;
import uo.ri.business.impl.foreman.UpdateClient;
import uo.ri.conf.Factory;
import uo.ri.util.exception.BusinessException;

/**
 * ForemanServiceImpl.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class ForemanServiceImpl implements ForemanService {

    /** The executor. */
    private CommandExecutor executor = Factory.executor.forExecutor();

    /*
     * (non-Javadoc)
     * 
     * @see
     * uo.ri.business.ForemanService#addClient(uo.ri.business.dto.ClientDto,
     * java.lang.Long)
     */
    @Override
    public void addClient(ClientDto client, Long idRecomendator)
	    throws BusinessException {
	executor.execute(new CreateClient(client, idRecomendator));
    }

    /*
     * (non-Javadoc)
     * 
     * @see uo.ri.business.ForemanService#deleteClient(java.lang.Long)
     */
    @Override
    public void deleteClient(Long id) throws BusinessException {
	executor.execute(new RemoveClient(id));
    }

    /*
     * (non-Javadoc)
     * 
     * @see uo.ri.business.ForemanService#findAllClients()
     */
    @Override
    public List<ClientDto> findAllClients() throws BusinessException {
	return executor.execute(new FindAllClients());
    }

    /*
     * (non-Javadoc)
     * 
     * @see uo.ri.business.ForemanService#findClientById(java.lang.Long)
     */
    @Override
    public ClientDto findClientById(Long id) throws BusinessException {
	return executor.execute(new FindClientById(id));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * uo.ri.business.ForemanService#findRecomendedClientsByClienteId(java.lang.
     * Long)
     */
    @Override
    public List<ClientDto> findRecomendedClientsByClienteId(Long id)
	    throws BusinessException {
	return executor.execute(new FindRecommendedClientsByClientId(id));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * uo.ri.business.ForemanService#updateClient(uo.ri.business.dto.ClientDto)
     */
    @Override
    public void updateClient(ClientDto dto) throws BusinessException {
	executor.execute(new UpdateClient(dto));
    }

}
