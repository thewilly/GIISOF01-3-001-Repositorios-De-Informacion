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
package uo.ri.business;

import java.util.List;

import uo.ri.business.dto.ClientDto;
import uo.ri.util.exception.BusinessException;

/**
 * The Interface ForemanService.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public interface ForemanService {

    /**
     * Adds the client.
     *
     * @param client
     *            the client
     * @param idRecomendador
     *            the id recomendador
     * @throws BusinessException
     *             the business exception
     */
    void addClient(ClientDto client, Long idRecomendador)
	    throws BusinessException;

    /**
     * Delete client.
     *
     * @param id
     *            the id
     * @throws BusinessException
     *             the business exception
     */
    void deleteClient(Long id) throws BusinessException;

    /**
     * Find all clients.
     *
     * @return the list
     * @throws BusinessException
     *             the business exception
     */
    List<ClientDto> findAllClients() throws BusinessException;

    /**
     * Find client by id.
     *
     * @param id
     *            the id
     * @return the client dto
     * @throws BusinessException
     *             the business exception
     */
    ClientDto findClientById(Long id) throws BusinessException;

    /**
     * Find recomended clients by cliente id.
     *
     * @param id
     *            of the recommender client
     * @return the list of recommended clients, might be empty if there is none
     * @throws BusinessException
     *             the business exception
     */
    List<ClientDto> findRecomendedClientsByClienteId(Long id)
	    throws BusinessException;

    /**
     * Update client.
     *
     * @param dto
     *            the dto
     * @throws BusinessException
     *             the business exception
     */
    void updateClient(ClientDto dto) throws BusinessException;

}
