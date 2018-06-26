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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import uo.ri.business.dto.FailureDto;
import uo.ri.business.impl.Command;
import uo.ri.business.impl.util.DtoAssembler;
import uo.ri.business.repository.ClienteRepository;
import uo.ri.conf.Factory;
import uo.ri.model.Averia;
import uo.ri.model.Cliente;
import uo.ri.model.Vehiculo;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.Check;

/**
 * FindRepairsByClientId.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class FindRepairsByClientDNI implements Command<List<FailureDto>> {

    /** The client DNI. */
    private String clientDNI;

    /**
     * Instantiates a new find repairs by client DNI.
     *
     * @param clientDNI
     *            the client DNI
     */
    public FindRepairsByClientDNI(String clientDNI) {
	this.clientDNI = clientDNI;
    }

    /*
     * (non-Javadoc)
     * 
     * @see uo.ri.business.impl.Command#execute()
     */
    @Override
    public List<FailureDto> execute() throws BusinessException {
	ClienteRepository cp = Factory.repository.forCliente();
	Cliente cliente = cp.findByDni(clientDNI);
	Check.isNotNull(cliente, "El cliente no existe");
	List<Averia> averias = findAllBreakdownsFrom(cliente);
	return DtoAssembler.toBreakdownDtoList(averias);
    }

    /**
     * This method finds all the breakdowns of the client from all of its
     * vehicles.
     *
     * @param cliente
     *            the client to look
     * @return the list containing all the breakdowns
     */
    private List<Averia> findAllBreakdownsFrom(Cliente cliente) {
	List<Averia> averias = new ArrayList<>();
	Set<Vehiculo> vehiculos = cliente.getVehiculos();
	for (Vehiculo vehiculo : vehiculos) {
	    Set<Averia> aux = vehiculo.getAverias();
	    for (Averia averia : aux) {
		averias.add(averia);
	    }
	}
	return averias;
    }

}
