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

import java.util.List;

import uo.ri.business.dto.ClientDto;
import uo.ri.business.impl.Command;
import uo.ri.business.impl.util.DtoAssembler;
import uo.ri.business.repository.ClienteRepository;
import uo.ri.conf.Factory;
import uo.ri.model.Cliente;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.Check;

/**
 * The Class FindRecommendedClientsByClientId.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class FindRecommendedClientsByClientId implements Command<List<ClientDto>> {

	/** The recommender id. */
	private Long recommenderId;
	
	/** The clients repository. */
	private ClienteRepository clientsRepository = Factory.repository.forCliente();

	/**
	 * Instantiates a new find recommended clients by client id.
	 *
	 * @param clientId the client id
	 */
	public FindRecommendedClientsByClientId( Long clientId ) {
		this.recommenderId = clientId;
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.impl.Command#execute()
	 */
	@Override
	public List<ClientDto> execute() throws BusinessException {
		assertRecommenderExists();
		List<Cliente> recomendados = clientsRepository.findRecomendedBy( recommenderId );
		return DtoAssembler.toClientDtoList( recomendados );
	}

	/**
	 * This method checks if the id of the recommender passed in the constructor
	 * exists as a client in the system, or not.
	 *
	 * @throws BusinessException if any error during method execution.
	 */
	private void assertRecommenderExists() throws BusinessException {
		Cliente client = clientsRepository.findById( recommenderId );
		Check.isNotNull( client, "El cliente recomendador no existe" );
	}

}
