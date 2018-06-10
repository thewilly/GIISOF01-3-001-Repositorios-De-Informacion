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
package uo.ri.business.repository;

/**
 * A factory for creating Repository objects.
 */
public interface RepositoryFactory {

	/**
	 * For mechanic.
	 *
	 * @return the mecanico repository
	 */
	MecanicoRepository forMechanic();

	/**
	 * For averia.
	 *
	 * @return the averia repository
	 */
	AveriaRepository forAveria();

	/**
	 * For medio pago.
	 *
	 * @return the medio pago repository
	 */
	MedioPagoRepository forMedioPago();

	/**
	 * For factura.
	 *
	 * @return the factura repository
	 */
	FacturaRepository forFactura();

	/**
	 * For cliente.
	 *
	 * @return the cliente repository
	 */
	ClienteRepository forCliente();

	/**
	 * For repuesto.
	 *
	 * @return the repuesto repository
	 */
	RepuestoRepository forRepuesto();

	/**
	 * For recomendacion.
	 *
	 * @return the recomendacion repository
	 */
	RecomendacionRepository forRecomendacion();

	/**
	 * For cargo.
	 *
	 * @return the cargo repository
	 */
	CargoRepository forCargo();

	/**
	 * For vehiculo.
	 *
	 * @return the vehiculo repository
	 */
	VehiculoRepository forVehiculo();

}
