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

import uo.ri.business.repository.MecanicoRepository;
import uo.ri.model.Mecanico;
import uo.ri.persistence.jpa.util.Jpa;

/**
 * The Class MechanicJpaRepository.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class MechanicJpaRepository implements MecanicoRepository {

    /*
     * (non-Javadoc)
     * 
     * @see uo.ri.business.repository.Repository#add(java.lang.Object)
     */
    @Override
    public void add(Mecanico t) {
	Jpa.getManager().persist(t);
    }

    /*
     * (non-Javadoc)
     * 
     * @see uo.ri.business.repository.Repository#findAll()
     */
    @Override
    public List<Mecanico> findAll() {
	return Jpa.getManager()
		.createNamedQuery("Mecanico.findAll", Mecanico.class)
		.getResultList();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * uo.ri.business.repository.MecanicoRepository#findByDni(java.lang.String)
     */
    @Override
    public Mecanico findByDni(String dni) {
	return Jpa.getManager()
		.createNamedQuery("Mecanico.findByDni", Mecanico.class)
		.setParameter(1, dni).getResultList().stream().findFirst()
		.orElse(null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see uo.ri.business.repository.Repository#findById(java.lang.Long)
     */
    @Override
    public Mecanico findById(Long id) {
	return Jpa.getManager().find(Mecanico.class, id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see uo.ri.business.repository.Repository#remove(java.lang.Object)
     */
    @Override
    public void remove(Mecanico t) {
	Jpa.getManager().remove(t);
    }

}
