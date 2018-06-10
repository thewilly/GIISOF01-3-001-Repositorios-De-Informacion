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

import uo.ri.business.repository.AveriaRepository;
import uo.ri.business.repository.CargoRepository;
import uo.ri.business.repository.ClienteRepository;
import uo.ri.business.repository.FacturaRepository;
import uo.ri.business.repository.MecanicoRepository;
import uo.ri.business.repository.MedioPagoRepository;
import uo.ri.business.repository.RecomendacionRepository;
import uo.ri.business.repository.RepositoryFactory;
import uo.ri.business.repository.RepuestoRepository;
import uo.ri.business.repository.VehiculoRepository;

/**
 * A factory for creating JpaRepository objects.
 */
public class JpaRepositoryFactory implements RepositoryFactory {

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.RepositoryFactory#forAveria()
	 */
	@Override
	public AveriaRepository forAveria() {
		return new AveriaJpaRepository();
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.RepositoryFactory#forCargo()
	 */
	@Override
	public CargoRepository forCargo() {
		return new CargoJpaRepository();
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.RepositoryFactory#forCliente()
	 */
	@Override
	public ClienteRepository forCliente() {
		return new ClienteJpaRepository();
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.RepositoryFactory#forFactura()
	 */
	@Override
	public FacturaRepository forFactura() {
		return new FacturaJpaRepository();
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.RepositoryFactory#forMechanic()
	 */
	@Override
	public MecanicoRepository forMechanic() {
		return new MechanicJpaRepository();
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.RepositoryFactory#forMedioPago()
	 */
	@Override
	public MedioPagoRepository forMedioPago() {
		return new MedioPagoJpaRepository();
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.RepositoryFactory#forRecomendacion()
	 */
	@Override
	public RecomendacionRepository forRecomendacion() {
		return new RecomendacionJpaRepository();
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.RepositoryFactory#forRepuesto()
	 */
	@Override
	public RepuestoRepository forRepuesto() {
		return new RepuestoJpaRepository();
	}

	/* (non-Javadoc)
	 * @see uo.ri.business.repository.RepositoryFactory#forVehiculo()
	 */
	@Override
	public VehiculoRepository forVehiculo() {
		return new VehiculoJpaRepository();
	}

}
