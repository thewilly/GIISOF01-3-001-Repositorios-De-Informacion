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
package uo.ri.business.impl.admin;

import uo.ri.business.impl.Command;
import uo.ri.business.repository.MecanicoRepository;
import uo.ri.conf.Factory;
import uo.ri.model.Mecanico;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.Check;

/**
 * RemoveMechanic.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class RemoveMechanic implements Command<Void> {

	/** The mechanic id. */
	private Long mechanicId;

	/**
	 * Instantiates a new removes the mechanic.
	 *
	 * @param mechanicId the mechanic id
	 */
	public RemoveMechanic(Long mechanicId) {
		this.mechanicId = mechanicId;
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.impl.Command#execute()
	 */
	public Void execute() throws BusinessException {
		MecanicoRepository repository = Factory.repository.forMechanic();
		Mecanico mechanic = repository.findById(mechanicId);
		assertCanBeDeleted(mechanic);
		repository.remove(mechanic);
		return null;
	}

	/**
	 * This method checks if the mechanic can be deleted, by checking if it
	 * exists, if it doesn't have any kind of intervention nor any assignment.
	 *
	 * @param mechanic the mechanic to be deleted
	 * @throws BusinessException the business exception
	 */
	private void assertCanBeDeleted(Mecanico mechanic) throws BusinessException {
		Check.isNotNull(mechanic, "El mecánico no existe");
		Check.isTrue(mechanic.getIntervenciones().size() == 0, "El mecánico tiene interveciones");
		Check.isTrue(mechanic.getAsignadas().size() == 0, "El mecánico tiene interveciones");
	}

}
