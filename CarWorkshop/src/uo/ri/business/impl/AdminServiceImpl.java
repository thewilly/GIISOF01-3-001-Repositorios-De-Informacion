/*******************************************************************************
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package uo.ri.business.impl;

import java.util.List;
import java.util.Map;

import uo.ri.business.AdminService;
import uo.ri.business.impl.admin.AddMechanic;
import uo.ri.business.impl.admin.RemoveMechanic;
import uo.ri.business.impl.admin.FindAllMechanics;
import uo.ri.business.impl.admin.GenerateBonos;
import uo.ri.business.impl.admin.UpdateMechanic;
import uo.ri.common.BusinessException;

public class AdminServiceImpl implements AdminService {

	@Override
	public void addMechanic( String nombre, String apellidos ) throws BusinessException {
		AddMechanic action = new AddMechanic( nombre, apellidos );
		action.execute();

	}

	@Override
	public void removeMechanic( Long idMechanic ) throws BusinessException {
		RemoveMechanic d = new RemoveMechanic( idMechanic );
		d.execute();
	}

	@Override
	public void updateMechanic( Long idMechanic, String nombre, String apellidos )
			throws BusinessException {
		UpdateMechanic u = new UpdateMechanic( idMechanic, nombre, apellidos );
		u.execute();

	}

	@Override
	public List<Map<String, Object>> findAllMechanics() throws BusinessException {
		FindAllMechanics f = new FindAllMechanics();
		return f.execute();
	}

	@Override
	public void generateBonds() throws BusinessException {
		GenerateBonos g = new GenerateBonos();
		g.execute();

	}

}
