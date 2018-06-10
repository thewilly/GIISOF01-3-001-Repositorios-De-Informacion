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
package uo.ri.business.impl;

import java.util.List;

import uo.ri.business.AdminService;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.dto.VoucherDto;
import uo.ri.business.dto.VoucherSummary;
import uo.ri.business.impl.admin.CreateMechanic;
import uo.ri.business.impl.admin.RemoveMechanic;
import uo.ri.business.impl.admin.FindAllBonds;
import uo.ri.business.impl.admin.FindAllMechanics;
import uo.ri.business.impl.admin.FindAllBondsByClientId;
import uo.ri.business.impl.admin.FindMechanicById;
import uo.ri.business.impl.admin.GenerateBonds;
import uo.ri.business.impl.admin.UpdateMechanic;
import uo.ri.conf.Factory;
import uo.ri.util.exception.BusinessException;

/**
 * AdminServiceImpl.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class AdminServiceImpl implements AdminService {

	/** The executor. */
	private CommandExecutor executor = Factory.executor.forExecutor();

	/* (non-Javadoc)
	 * @see uo.ri.business.AdminService#addMechanic(uo.ri.business.dto.MechanicDto)
	 */
	@Override
	public void addMechanic(MechanicDto mecanico) throws BusinessException {
		executor.execute(new CreateMechanic(mecanico));
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.AdminService#deleteMechanic(java.lang.Long)
	 */
	@Override
	public void deleteMechanic(Long idMecanico) throws BusinessException {
		executor.execute(new RemoveMechanic(idMecanico));
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.AdminService#findAllMechanics()
	 */
	@Override
	public List<MechanicDto> findAllMechanics() throws BusinessException {
		return executor.execute(new FindAllMechanics());
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.AdminService#findMechanicById(java.lang.Long)
	 */
	@Override
	public MechanicDto findMechanicById(Long id) throws BusinessException {
		return executor.execute(new FindMechanicById(id));
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.AdminService#findVouchersByClientId(java.lang.Long)
	 */
	@Override
	public List<VoucherDto> findVouchersByClientId(Long id) throws BusinessException {
		return executor.execute(new FindAllBondsByClientId(id));
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.AdminService#generateVouchers()
	 */
	@Override
	public int generateVouchers() throws BusinessException {
		return (executor.execute(new GenerateBonds()));
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.AdminService#getVoucherSummary()
	 */
	@Override
	public List<VoucherSummary> getVoucherSummary() throws BusinessException {
		return executor.execute(new FindAllBonds());
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.AdminService#updateMechanic(uo.ri.business.dto.MechanicDto)
	 */
	@Override
	public void updateMechanic(MechanicDto mecanico) throws BusinessException {
		executor.execute(new UpdateMechanic(mecanico));
	}

}
