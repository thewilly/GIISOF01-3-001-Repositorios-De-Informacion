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

import uo.ri.business.dto.MechanicDto;
import uo.ri.business.impl.Command;
import uo.ri.business.impl.util.DtoAssembler;
import uo.ri.business.repository.MecanicoRepository;
import uo.ri.conf.Factory;
import uo.ri.model.Mecanico;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.Check;

/**
 * AddMechanic.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class CreateMechanic implements Command<Void> {

	/** The dto. */
	private MechanicDto dto;
	
	/** The repository. */
	private MecanicoRepository repository = Factory.repository.forMechanic();

	/**
	 * Instantiates a new creates the mechanic.
	 *
	 * @param mecanico the mecanico
	 */
	public CreateMechanic( MechanicDto mecanico ) {
		this.dto = mecanico;
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.impl.Command#execute()
	 */
	public Void execute() throws BusinessException {
		Mecanico mechanic = DtoAssembler.toEntity( dto );
		assertNotRepeatedDni( dto.dni );
		repository.add( mechanic );
		return null;
	}

	/**
	 * This method checks that the dni of the new mechanic doesn't already
	 * exists in the system.
	 *
	 * @param dni the dni of the new mechanic to be added
	 * @throws BusinessException if any error during the execution of the
	 *             method.
	 */
	private void assertNotRepeatedDni( String dni ) throws BusinessException {
		Check.isNull( repository.findByDni( dni ), "Ese dni ya existe" );
	}
}
