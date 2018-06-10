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
package uo.ri.amp.util.repository.inmemory;

import java.util.List;
import java.util.stream.Collectors;

import uo.ri.business.repository.MedioPagoRepository;
import uo.ri.model.Bono;
import uo.ri.model.MedioPago;
import uo.ri.model.TarjetaCredito;

/**
 * The Class InMemoryMediosPagoRepository.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class InMemoryMediosPagoRepository 
		extends BaseMemoryRepository<MedioPago> 
		implements MedioPagoRepository {

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.MedioPagoRepository#findPaymentMeansByClientId(java.lang.Long)
	 */
	@Override
	public List<MedioPago> findPaymentMeansByClientId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.MedioPagoRepository#findPaymentMeansByInvoiceId(java.lang.Long)
	 */
	@Override
	public List<MedioPago> findPaymentMeansByInvoiceId(Long idFactura) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.MedioPagoRepository#findByClientId(java.lang.Long)
	 */
	@Override
	public List<MedioPago> findByClientId(Long id) {
		return entities.values().stream()
				.filter(m -> m.getCliente().getId().equals( id ))
				.collect( Collectors.toList() );
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.MedioPagoRepository#findAggregateVoucherDataByClientId(java.lang.Long)
	 */
	@Override
	public Object[] findAggregateVoucherDataByClientId(Long id) {
		List<Bono> bs = findVouchersByClientId(id);
		
		double available = bs.stream()
				.map(b -> b.getDisponible())
				.collect(Collectors.summingDouble( a -> a));
		
		double consumed = bs.stream()
				.map(b -> b.getAcumulado())
				.collect(Collectors.summingDouble( a -> a));
		
		Object[] res = new Object[]{
			bs.size(),
			available,
			consumed
		};
		return res;
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.MedioPagoRepository#findCreditCardByNumber(java.lang.String)
	 */
	@Override
	public TarjetaCredito findCreditCardByNumber(String pan) {
		return entities.values().stream()
				.filter( m -> m instanceof TarjetaCredito)
				.map( m -> (TarjetaCredito) m)
				.filter(tc -> tc.getNumero().equals( pan ))
				.findFirst()
				.orElse( null );
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.MedioPagoRepository#findVouchersByClientId(java.lang.Long)
	 */
	@Override
	public List<Bono> findVouchersByClientId(Long id) {
		return entities.values().stream()
				.filter( m -> m instanceof Bono)
				.map( m -> (Bono) m)
				.filter(b -> b.getCliente().getId().equals( id ))
				.collect( Collectors.toList() );
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.MedioPagoRepository#findVoucherByCode(java.lang.String)
	 */
	@Override
	public Bono findVoucherByCode(String code) {
		return entities.values().stream()
				.filter( m -> m instanceof Bono)
				.map( m -> (Bono) m)
				.filter(b -> b.getCodigo().equals( code ))
				.findFirst()
				.orElse( null );
	}

}
