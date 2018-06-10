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
package uo.ri.business;

import java.util.List;

import uo.ri.business.dto.MechanicDto;
import uo.ri.business.dto.VoucherDto;
import uo.ri.business.dto.VoucherSummary;
import uo.ri.util.exception.BusinessException;

/**
 * The Interface AdminService.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public interface AdminService {

	/**
	 * Adds the mechanic.
	 *
	 * @param mecanico the mecanico
	 * @throws BusinessException the business exception
	 */
	void addMechanic(MechanicDto mecanico) throws BusinessException;

	/**
	 * Delete mechanic.
	 *
	 * @param idMecanico the id mecanico
	 * @throws BusinessException the business exception
	 */
	void deleteMechanic(Long idMecanico) throws BusinessException;

	/**
	 * Find all mechanics.
	 *
	 * @return the list
	 * @throws BusinessException the business exception
	 */
	List<MechanicDto> findAllMechanics() throws BusinessException;

	/**
	 * Find mechanic by id.
	 *
	 * @param id the id
	 * @return the mechanic dto
	 * @throws BusinessException the business exception
	 */
	MechanicDto findMechanicById(Long id) throws BusinessException;

	/**
	 * Find vouchers by client id.
	 *
	 * @param id the id
	 * @return the list
	 * @throws BusinessException the business exception
	 */
	List<VoucherDto> findVouchersByClientId(Long id) throws BusinessException;

	/**
	 * Generate vouchers.
	 *
	 * @return the int
	 * @throws BusinessException the business exception
	 */
	int generateVouchers() throws BusinessException;

	/**
	 * Gets the voucher summary.
	 *
	 * @return the voucher summary
	 * @throws BusinessException the business exception
	 */
	List<VoucherSummary> getVoucherSummary() throws BusinessException;

	/**
	 * Update mechanic.
	 *
	 * @param mecanico the mecanico
	 * @throws BusinessException the business exception
	 */
	void updateMechanic(MechanicDto mecanico) throws BusinessException;

}
