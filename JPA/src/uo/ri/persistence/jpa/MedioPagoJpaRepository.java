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

import uo.ri.business.repository.MedioPagoRepository;
import uo.ri.model.Bono;
import uo.ri.model.MedioPago;
import uo.ri.model.TarjetaCredito;
import uo.ri.persistence.jpa.util.BaseRepository;
import uo.ri.persistence.jpa.util.Jpa;

/**
 * The Class MedioPagoJpaRepository.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class MedioPagoJpaRepository extends BaseRepository<MedioPago> implements MedioPagoRepository {

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.MedioPagoRepository#findPaymentMeansByClientId(java.lang.Long)
	 */
	@Override
	public List<MedioPago> findPaymentMeansByClientId(Long id) {
		return Jpa.getManager().createNamedQuery("MedioPago.findForClient", MedioPago.class).setParameter(1, id)
				.getResultList();
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.MedioPagoRepository#findCreditCardByNumber(java.lang.String)
	 */
	@Override
	public TarjetaCredito findCreditCardByNumber(String number) {
		return Jpa.getManager().createNamedQuery("Tarjeta.findByCardNumber", TarjetaCredito.class)
				.setParameter(1, number).getResultList().stream().findFirst().orElse(null);
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.MedioPagoRepository#findVouchersByClientId(java.lang.Long)
	 */
	@Override
	public List<Bono> findVouchersByClientId(Long id) {
		return Jpa.getManager().createNamedQuery("Bono.findBonoFromClient", Bono.class).setParameter(1, id)
				.getResultList();
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.MedioPagoRepository#findVoucherByCode(java.lang.String)
	 */
	@Override
	public Bono findVoucherByCode(String code) {
		return Jpa.getManager().createNamedQuery("Bono.findVoucherByCode", Bono.class).setParameter(1, code)
				.getResultList().stream().findFirst().orElse(null);
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.MedioPagoRepository#findPaymentMeansByInvoiceId(java.lang.Long)
	 */
	@Override
	public List<MedioPago> findPaymentMeansByInvoiceId(Long idFactura) {
		return Jpa.getManager().createNamedQuery("MedioPago.findPaymentMeansByInvoiceId", MedioPago.class)
				.setParameter(1, idFactura).getResultList();
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.MedioPagoRepository#findAggregateVoucherDataByClientId(java.lang.Long)
	 */
	@Override
	public Object[] findAggregateVoucherDataByClientId(Long id) {
		Object[] array = new Object[3];
		array[0] = findVouchersByClientId(id).size();
		array[1] = Jpa.getManager().createNamedQuery("Bono.findAvailableVouchersByClientId", Double.class)
				.setParameter(1, id).getSingleResult();
		array[2] = Jpa.getManager().createNamedQuery("Bono.findSpentVouchersByClientId", Double.class)
				.setParameter(1, id).getSingleResult();
		return array;
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.MedioPagoRepository#findByClientId(java.lang.Long)
	 */
	@Override
	public List<MedioPago> findByClientId(Long id) {
		return null;
		// este metodo esta repetido, misma funcion en el
		// findPaymentMeansByClientId
	}

}
