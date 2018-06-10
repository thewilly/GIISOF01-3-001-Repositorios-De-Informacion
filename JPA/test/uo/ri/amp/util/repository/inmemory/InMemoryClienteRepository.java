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
package uo.ri.amp.util.repository.inmemory;

import java.util.List;
import java.util.stream.Collectors;

import uo.ri.business.repository.ClienteRepository;
import uo.ri.model.Cliente;

/**
 * The Class InMemoryClienteRepository.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class InMemoryClienteRepository 
		extends BaseMemoryRepository<Cliente> 
		implements ClienteRepository {

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.ClienteRepository#findByDni(java.lang.String)
	 */
	@Override
	public Cliente findByDni(String dni) {
		return entities.values().stream()
				.filter( c -> c.getDni().equals( dni ))
				.findFirst()
				.orElse( null );
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.ClienteRepository#findWithRecomendations()
	 */
	@Override
	public List<Cliente> findWithRecomendations() {
		return entities.values().stream()
				.filter( c -> c.getRecomendacionesHechas().size() >= 3)
				.flatMap( c -> c.getRecomendacionesHechas().stream() )
				.filter(r -> ! r.isUsada())
				.map(r -> r.getRecomendador())
				.distinct()
				.collect( Collectors.toList() );
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.ClienteRepository#findWithThreeUnusedBreakdowns()
	 */
	@Override
	public List<Cliente> findWithThreeUnusedBreakdowns() {
		return entities.values().stream()
				.flatMap(c -> c.getVehiculos().stream())
				.flatMap(v -> v.getAverias().stream())
				.filter( a -> a.isInvoiced() && ! a.isUsadaBono3() )
				.map(a -> a.getVehiculo().getCliente())
				.distinct()
				.collect( Collectors.toList() );
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.ClienteRepository#findRecomendedBy(java.lang.Long)
	 */
	@Override
	public List<Cliente> findRecomendedBy(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
