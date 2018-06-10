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
package uo.ri.business.impl.cash;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.impl.Command;
import uo.ri.business.repository.CargoRepository;
import uo.ri.business.repository.FacturaRepository;
import uo.ri.business.repository.MedioPagoRepository;
import uo.ri.conf.Factory;
import uo.ri.model.Association;
import uo.ri.model.Cargo;
import uo.ri.model.Factura;
import uo.ri.model.MedioPago;
import uo.ri.model.types.FacturaStatus;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.Check;

/**
 * SettleInvoice.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class SettleInvoice implements Command<InvoiceDto> {

	/** The id. */
	private Long id;
	
	/** The cargos. */
	private Map<Long, Double> cargos;

	/**
	 * Instantiates a new settle invoice.
	 *
	 * @param id the id
	 * @param cargos the cargos
	 */
	public SettleInvoice(Long id, Map<Long, Double> cargos) {
		this.id = id;
		this.cargos = cargos;
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.impl.Command#execute()
	 */
	@Override
	public InvoiceDto execute() throws BusinessException {
		FacturaRepository fp = Factory.repository.forFactura();
		Factura factura = fp.findById(id);
		assertCheckCorrectInvoice(factura);
		chargesCreation(factura);
		Set<Cargo> cargos = factura.getCargos();
		if (correctAmount(cargos, factura)) {
			return DtoCreation(factura);
		} else {
			removeCharges(cargos);
			throw new BusinessException("Los cargos no igualan el importe");
		}
	}

	/**
	 * This method unlinks all the charges that had been created due to the
	 * amount was not correct.
	 *
	 * @param cargos the charges created
	 */
	private void removeCharges(Set<Cargo> cargos) {
		for (Cargo cargo : cargos) {
			Association.Cargar.unlink(cargo);
		}
	}

	/**
	 * This method gives the dto that will be returned, all the data from the
	 * invoice.
	 *
	 * @param fac the invoice
	 * @return thw invoice dto with all the information
	 */
	private InvoiceDto DtoCreation(Factura fac) {
		InvoiceDto dto = new InvoiceDto();
		dto.id = fac.getId();
		dto.date = fac.getFecha();
		dto.number = fac.getNumero();
		dto.status = fac.getStatus().toString();
		dto.taxes = fac.getIva();
		dto.total = fac.getImporte();
		return dto;
	}

	/**
	 * This method creates the charges the invoice will have.
	 *
	 * @param factura the invoice
	 * @throws BusinessException the business exception
	 */
	private void chargesCreation(Factura factura) throws BusinessException {
		Check.isFalse(cargos.isEmpty(), "No hay cargos");
		MedioPagoRepository mp = Factory.repository.forMedioPago();
		Iterator<Entry<Long, Double>> it = cargos.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Long, Double> pair = (Map.Entry<Long, Double>) it.next();
			MedioPago medioPago = mp.findById(pair.getKey());
			CargoRepository cargoRep = Factory.repository.forCargo();
			Cargo c = new Cargo(factura, medioPago, pair.getValue());
			cargoRep.add(c);
		}
		it.remove();
	}

	/**
	 * This method checks that all the charges sum up the total amount that must
	 * be payed in the invoice.
	 *
	 * @param cargos the charges
	 * @param f the invoice
	 * @return true if they sum up that quantity, false otherwise
	 * @throws BusinessException the business exception
	 */
	private boolean correctAmount(Set<Cargo> cargos, Factura f) throws BusinessException {
		double amount = 0.0;
		for (Cargo cargo : cargos) {
			amount += cargo.getImporte();
		}

		if (f.getImporte() == amount) {
			for (Cargo cargo : cargos) {
				cargo.getMedioPago().pagar(cargo.getImporte());
			}
			f.settle();
			return true;
		}
		return false;
	}

	/**
	 * This method checks if the invoice is correct to be used, by checking it
	 * exists and that it's not already invoiced.
	 *
	 * @param factura the invoice to be checked
	 * @throws BusinessException the business exception
	 */
	private void assertCheckCorrectInvoice(Factura factura) throws BusinessException {
		Check.isNotNull(factura, "La factura no existe");
		Check.isFalse(factura.getStatus() == FacturaStatus.ABONADA, "Ya está abonada");
	}
}
