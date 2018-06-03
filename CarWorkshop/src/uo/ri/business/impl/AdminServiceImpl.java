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
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
package uo.ri.business.impl;

import java.util.List;
import java.util.Map;

import uo.ri.business.AdminService;
import uo.ri.business.impl.admin.CerateMechanic;
import uo.ri.business.impl.admin.RemoveMechanic;
import uo.ri.business.impl.admin.FindAllBonds;
import uo.ri.business.impl.admin.FindAllBondsByClientId;
import uo.ri.business.impl.admin.FindAllMechanics;
import uo.ri.business.impl.admin.GenerateBondByRecommendation;
import uo.ri.business.impl.admin.UpdateMechanic;
import uo.ri.common.BusinessException;

/**
 * AdminServiceImpl.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class AdminServiceImpl implements AdminService {

	@Override
	public void createMechanic( String name, String surname ) {
		CerateMechanic createMechanic = new CerateMechanic( name, surname );
		createMechanic.execute();
	}

	@Override
	public void removeMechanic( long mechanicId ) throws BusinessException {
		RemoveMechanic removeMechanic = new RemoveMechanic( mechanicId );
		removeMechanic.execute();
	}

	@Override
	public void updateMechanic( long mechanicId, String name, String surname ) throws BusinessException {
		UpdateMechanic updateMechanic = new UpdateMechanic( mechanicId, name, surname );
		updateMechanic.execute();
	}

	@Override
	public List<Map<String, Object>> findAllMechanics() {
		FindAllMechanics findAllMechanics = new FindAllMechanics();
		return findAllMechanics.execute();
	}

	@Override
	public List<Map<String, Object>> findAllBonds() {
		FindAllBonds findAllBonds = new FindAllBonds();
		return findAllBonds.execute();
	}

	@Override
	public List<Map<String, Object>> findAllBondsByCliendId( long clientId ) throws BusinessException {
		FindAllBondsByClientId findAllBondsByClientId = new FindAllBondsByClientId( clientId );
		return findAllBondsByClientId.execute();
	}

	@Override
	public void generateBonosByRecomendation() {
		GenerateBondByRecommendation generateBondsByRecommendation = new GenerateBondByRecommendation();
		generateBondsByRecommendation.execute();
	}

}
