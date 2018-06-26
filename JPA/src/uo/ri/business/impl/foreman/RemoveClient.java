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

import java.util.Set;

import uo.ri.business.impl.Command;
import uo.ri.business.repository.ClienteRepository;
import uo.ri.business.repository.MedioPagoRepository;
import uo.ri.business.repository.RecomendacionRepository;
import uo.ri.conf.Factory;
import uo.ri.model.Association;
import uo.ri.model.Cliente;
import uo.ri.model.MedioPago;
import uo.ri.model.Recomendacion;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.Check;

/**
 * RemoveClient.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class RemoveClient implements Command<Void> {

    /** The client id. */
    private Long clientId;

    /** The clients repository. */
    private ClienteRepository clientsRepository = Factory.repository
	    .forCliente();

    /** The recommendations repository. */
    private RecomendacionRepository recommendationsRepository = Factory.repository
	    .forRecomendacion();

    /**
     * Instantiates a new removes the client.
     *
     * @param clientId
     *            the client id
     */
    public RemoveClient(Long clientId) {
	this.clientId = clientId;
    }

    /**
     * This method checks all the conditions that must be fulfilled for deleting
     * a client.
     *
     * @param c
     *            the client to be checked
     * @throws BusinessException
     *             the business exception
     */
    private void assertCanBeDeleted(Cliente c) throws BusinessException {
	Check.isNotNull(c, "El cliente no existe");
	Check.isTrue(c.getVehiculos().size() == 0,
		"El cliente no puede ser eliminado al tener vehículos registrados");
    }

    /**
     * This method deletes all the payment methods related to the client.
     *
     * @param cliente
     *            the client
     */
    private void deleteCashPayment(Cliente cliente) {
	MedioPagoRepository mp = Factory.repository.forMedioPago();
	Set<MedioPago> mediosPago = cliente.getMediosPago();
	for (MedioPago medioPago : mediosPago) {
	    Association.Pagar.unlink(cliente, medioPago);
	    mp.remove(medioPago);
	}
    }

    /**
     * This method deletes the recommendation that a client made to it.
     *
     * @param cliente
     *            the client
     */
    private void deleteRecommendationRecibed(Cliente cliente) {
	Recomendacion recomendacion = cliente.getRecomendacionRecibida();
	if (recomendacion != null) {
	    recomendacion.unlink();
	    recommendationsRepository.remove(recomendacion);
	}
    }

    /**
     * This method removes from the system the recommendations the client has
     * made.
     *
     * @param cliente
     *            the client
     */
    private void deleteRecommendations(Cliente cliente) {
	Set<Recomendacion> recomendaciones = cliente.getRecomendacionesHechas();
	if (recomendaciones.size() > 0) {
	    for (Recomendacion re : recomendaciones) {
		re.unlink();
		recommendationsRepository.remove(re);
	    }
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see uo.ri.business.impl.Command#execute()
     */
    @Override
    public Void execute() throws BusinessException {
	Cliente cliente = clientsRepository.findById(clientId);
	assertCanBeDeleted(cliente);
	deleteRecommendations(cliente);
	deleteRecommendationRecibed(cliente);
	deleteCashPayment(cliente);
	clientsRepository.remove(cliente);
	return null;
    }

}
