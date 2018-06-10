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
package uo.ri.persistence.jpa;

import java.util.List;

import uo.ri.business.repository.ClienteRepository;
import uo.ri.model.Cliente;
import uo.ri.persistence.jpa.util.BaseRepository;
import uo.ri.persistence.jpa.util.Jpa;

/**
 * The Class ClienteJpaRepository.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class ClienteJpaRepository extends BaseRepository<Cliente> implements ClienteRepository {

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.ClienteRepository#findByDni(java.lang.String)
	 */
	@Override
	public Cliente findByDni(String dni) {
		return Jpa.getManager().createNamedQuery("Cliente.findByDni", Cliente.class).setParameter(1, dni)
				.getResultList().stream().findFirst().orElse(null);
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.ClienteRepository#findWithRecomendations()
	 */
	@Override
	public List<Cliente> findWithRecomendations() {
		return Jpa.getManager().createNamedQuery("Cliente.findWithRecomendations", Cliente.class).getResultList();
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.ClienteRepository#findWithThreeUnusedBreakdowns()
	 */
	@Override
	public List<Cliente> findWithThreeUnusedBreakdowns() {
		return Jpa.getManager().createNamedQuery("Cliente.findWithThreeUnusedBreakdowns", Cliente.class)
				.getResultList();
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.ClienteRepository#findRecomendedBy(java.lang.Long)
	 */
	@Override
	public List<Cliente> findRecomendedBy(Long id) {
		return Jpa.getManager().createNamedQuery("Cliente.findRecommendedBy", Cliente.class).setParameter(1, id)
				.getResultList();
	}

}
