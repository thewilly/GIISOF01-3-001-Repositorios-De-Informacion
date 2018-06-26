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

import uo.ri.business.repository.AveriaRepository;
import uo.ri.model.Averia;
import uo.ri.persistence.jpa.util.BaseRepository;
import uo.ri.persistence.jpa.util.Jpa;

/**
 * The Class AveriaJpaRepository.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class AveriaJpaRepository extends BaseRepository<Averia>
	implements AveriaRepository {

    /*
     * (non-Javadoc)
     * 
     * @see uo.ri.business.repository.AveriaRepository#findByIds(java.util.List)
     */
    @Override
    public List<Averia> findByIds(List<Long> idsAveria) {
	return Jpa.getManager()
		.createNamedQuery("Averia.findByIds", Averia.class)
		.setParameter(1, idsAveria).getResultList();

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * uo.ri.business.repository.AveriaRepository#findNoFacturadasByDni(java.
     * lang.String)
     */
    @Override
    public List<Averia> findNoFacturadasByDni(String dni) {
	return Jpa.getManager()
		.createNamedQuery("Averia.findNoFacturadasByDni", Averia.class)
		.setParameter(1, dni).getResultList();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * uo.ri.business.repository.AveriaRepository#findWithUnusedBono3ByClienteId
     * (java.lang.Long)
     */
    @Override
    public List<Averia> findWithUnusedBono3ByClienteId(Long id) {
	return Jpa.getManager()
		.createNamedQuery("Averia.findWithUnusedBono3ByClienteId",
			Averia.class)
		.setParameter(1, id).getResultList();
    }

}
