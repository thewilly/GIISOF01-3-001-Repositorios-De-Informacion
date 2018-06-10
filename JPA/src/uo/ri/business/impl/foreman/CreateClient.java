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
import uo.ri.business.repository.MedioPagoRepository;
import uo.ri.business.repository.RecomendacionRepository;
import uo.ri.conf.Factory;
import uo.ri.model.Association;
import uo.ri.model.Cliente;
import uo.ri.model.Metalico;
import uo.ri.model.Recomendacion;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.Check;

/**
 * CreateClient.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class CreateClient implements Command<Void> {

	/** The client dto. */
	private ClientDto clientDto;
	
	/** The recommender id. */
	private Long recommenderId;
	
	/** The clients repository. */
	private ClienteRepository clientsRepository = Factory.repository.forCliente();
	
	/** The client. */
	private Cliente client;

	/**
	 * Instantiates a new creates the client.
	 *
	 * @param client the client
	 * @param recommenderId the recommender id
	 */
	public CreateClient(ClientDto client, Long recommenderId) {
		this.clientDto = client;
		this.recommenderId = recommenderId;
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.impl.Command#execute()
	 */
	@Override
	public Void execute() throws BusinessException {
		client = DtoAssembler.toEntity(clientDto);
		assertNotRepeatedClient();
		addRecomendationAndClient();
		MedioPagoRepository mp = Factory.repository.forMedioPago();
		Metalico metalico = new Metalico(client);
		mp.add(metalico);
		Association.Pagar.link(client, metalico);
		return null;
	}

	/**
	 * This method adds a new recommendation from the id, to the client. And
	 * checks if that id exists, if it does, the client will be added.
	 *
	 * @throws BusinessException the business exception
	 */
	private void addRecomendationAndClient() throws BusinessException {
		if (recommenderId != null) {
			Cliente recomendador = clientsRepository.findById(recommenderId);
			Check.isNotNull(recomendador, "No existe el cliente recomendador");
			clientsRepository.add(client);
			RecomendacionRepository rp = Factory.repository.forRecomendacion();
			Recomendacion r = new Recomendacion(recomendador, client);
			rp.add(r);
		} else {
			clientsRepository.add(client);
		}
	}

	/**
	 * This method checks that the client to be added has a correct dni, which
	 * is not in the system yet.
	 *
	 * @throws BusinessException the business exception
	 */
	private void assertNotRepeatedClient() throws BusinessException {
		Cliente aux = clientsRepository.findByDni(client.getDni());
		Check.isNull(aux, "Ya existe un cliente con ese dni");
	}

}
