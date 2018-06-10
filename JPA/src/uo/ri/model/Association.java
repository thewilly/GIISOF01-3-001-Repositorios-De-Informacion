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
package uo.ri.model;

/**
 * The Class Association.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class Association {

	/**
	 * The Class Poseer.
	 *
	 * @author Guillermo Facundo Colunga
	 * @version 201806081225
	 */
	public static class Poseer {

		/**
		 * Link.
		 *
		 * @param cliente the cliente
		 * @param vehiculo the vehiculo
		 */
		public static void link(Cliente cliente, Vehiculo vehiculo) {
			vehiculo._setCliente(cliente);
			cliente._getVehiculos().add(vehiculo);
		}

		/**
		 * Unlink.
		 *
		 * @param cliente the cliente
		 * @param vehiculo the vehiculo
		 */
		public static void unlink(Cliente cliente, Vehiculo vehiculo) {
			cliente._getVehiculos().remove(vehiculo);
			vehiculo._setCliente(null);
		}
	}

	/**
	 * The Class Clasificar.
	 *
	 * @author Guillermo Facundo Colunga
	 * @version 201806081225
	 */
	public static class Clasificar {

		/**
		 * Link.
		 *
		 * @param tipoVehiculo the tipo vehiculo
		 * @param vehiculo the vehiculo
		 */
		public static void link(TipoVehiculo tipoVehiculo, Vehiculo vehiculo) {
			vehiculo._setTipo(tipoVehiculo);
			tipoVehiculo._getVehiculos().add(vehiculo);
		}

		/**
		 * Unlink.
		 *
		 * @param tipoVehiculo the tipo vehiculo
		 * @param vehiculo the vehiculo
		 */
		public static void unlink(TipoVehiculo tipoVehiculo, Vehiculo vehiculo) {
			tipoVehiculo._getVehiculos().remove(vehiculo);
			vehiculo._setTipo(null);
		}
	}

	/**
	 * The Class Pagar.
	 *
	 * @author Guillermo Facundo Colunga
	 * @version 201806081225
	 */
	public static class Pagar {

		/**
		 * Link.
		 *
		 * @param cliente the cliente
		 * @param mediopago the mediopago
		 */
		public static void link(Cliente cliente, MedioPago mediopago) {
			mediopago._setCliente(cliente);
			cliente._getMediosPago().add(mediopago);
		}

		/**
		 * Unlink.
		 *
		 * @param cliente the cliente
		 * @param mediopago the mediopago
		 */
		public static void unlink(Cliente cliente, MedioPago mediopago) {
			cliente._getMediosPago().remove(mediopago);
			mediopago._setCliente(null);
		}
	}

	/**
	 * The Class Averiar.
	 *
	 * @author Guillermo Facundo Colunga
	 * @version 201806081225
	 */
	public static class Averiar {

		/**
		 * Link.
		 *
		 * @param vehiculo the vehiculo
		 * @param averia the averia
		 */
		public static void link(Vehiculo vehiculo, Averia averia) {
			averia._setVehiculo(vehiculo);
			vehiculo._getAverias().add(averia);
		}

		/**
		 * Unlink.
		 *
		 * @param vehiculo the vehiculo
		 * @param averia the averia
		 */
		public static void unlink(Vehiculo vehiculo, Averia averia) {
			vehiculo._getAverias().remove(averia);
			averia._setVehiculo(null);
		}
	}

	/**
	 * The Class Facturar.
	 *
	 * @author Guillermo Facundo Colunga
	 * @version 201806081225
	 */
	public static class Facturar {

		/**
		 * Link.
		 *
		 * @param averia the averia
		 * @param factura the factura
		 */
		public static void link(Averia averia, Factura factura) {
			averia._setFactura(factura);
			factura._getAverias().add(averia);
		}

		/**
		 * Unlink.
		 *
		 * @param averia the averia
		 * @param factura the factura
		 */
		public static void unlink(Averia averia, Factura factura) {
			factura._getAverias().remove(averia);
			averia._setFactura(null);
		}
	}

	/**
	 * The Class Cargar.
	 *
	 * @author Guillermo Facundo Colunga
	 * @version 201806081225
	 */
	public static class Cargar {

		/**
		 * Link.
		 *
		 * @param medioPago the medio pago
		 * @param cargo the cargo
		 * @param factura the factura
		 */
		public static void link(MedioPago medioPago, Cargo cargo, Factura factura) {
			cargo._setMedioPago(medioPago);
			cargo._setFactura(factura);
			medioPago._getCargos().add(cargo);
			factura._getCargos().add(cargo);
		}

		/**
		 * Unlink.
		 *
		 * @param cargo the cargo
		 */
		public static void unlink(Cargo cargo) {
			cargo.getFactura()._getCargos().remove(cargo);
			cargo.getMedioPago()._getCargos().remove(cargo);
			cargo._setMedioPago(null);
			cargo._setFactura(null);
		}
	}

	/**
	 * The Class Asignar.
	 *
	 * @author Guillermo Facundo Colunga
	 * @version 201806081225
	 */
	public static class Asignar {

		/**
		 * Link.
		 *
		 * @param mecanico the mecanico
		 * @param averia the averia
		 */
		public static void link(Mecanico mecanico, Averia averia) {
			averia._setMecanico(mecanico);
			mecanico._getAsignadas().add(averia);
		}

		/**
		 * Unlink.
		 *
		 * @param mecanico the mecanico
		 * @param averia the averia
		 */
		public static void unlink(Mecanico mecanico, Averia averia) {
			mecanico._getAsignadas().remove(averia);
			averia._setMecanico(null);
		}
	}

	/**
	 * The Class Intervenir.
	 *
	 * @author Guillermo Facundo Colunga
	 * @version 201806081225
	 */
	public static class Intervenir {

		/**
		 * Link.
		 *
		 * @param averia the averia
		 * @param intervencion the intervencion
		 * @param mecanico the mecanico
		 */
		public static void link(Averia averia, Intervencion intervencion, Mecanico mecanico) {
			intervencion._setAveria(averia);
			intervencion._setMecanico(mecanico);
			averia._getIntervenciones().add(intervencion);
			mecanico._getIntervenciones().add(intervencion);
		}

		/**
		 * Unlink.
		 *
		 * @param intervencion the intervencion
		 */
		public static void unlink(Intervencion intervencion) {
			intervencion.getAveria()._getIntervenciones().remove(intervencion);
			intervencion.getMecanico()._getIntervenciones().remove(intervencion);
			intervencion._setAveria(null);
			intervencion._setMecanico(null);
		}
	}

	/**
	 * The Class Sustituir.
	 *
	 * @author Guillermo Facundo Colunga
	 * @version 201806081225
	 */
	public static class Sustituir {

		/**
		 * Link.
		 *
		 * @param repuesto the repuesto
		 * @param sustitucion the sustitucion
		 * @param intervencion the intervencion
		 */
		public static void link(Repuesto repuesto, Sustitucion sustitucion, Intervencion intervencion) {
			sustitucion._setRepuesto(repuesto);
			sustitucion._setIntervencion(intervencion);
			repuesto._getSustituciones().add(sustitucion);
			intervencion._getSustituciones().add(sustitucion);
		}

		/**
		 * Unlink.
		 *
		 * @param sustitucion the sustitucion
		 */
		public static void unlink(Sustitucion sustitucion) {
			sustitucion.getRepuesto()._getSustituciones().remove(sustitucion);
			sustitucion.getIntervencion()._getSustituciones().remove(sustitucion);
			sustitucion._setRepuesto(null);
			sustitucion._setIntervencion(null);
		}
	}

	/**
	 * The Class Recomendar.
	 *
	 * @author Guillermo Facundo Colunga
	 * @version 201806081225
	 */
	public static class Recomendar {
		
		/**
		 * Link.
		 *
		 * @param recomendacion the recomendacion
		 * @param recomendador the recomendador
		 * @param recomendado the recomendado
		 */
		public static void link(Recomendacion recomendacion, Cliente recomendador, Cliente recomendado) {
			recomendacion._setRecomendador(recomendador);
			recomendacion._setRecomendado(recomendado);
			recomendador._getRecomendados().add(recomendacion);
			recomendado._setRecomendador(recomendacion);
		}

		/**
		 * Unlink.
		 *
		 * @param recomendacion the recomendacion
		 */
		public static void unlink(Recomendacion recomendacion) {
			recomendacion.getRecomendado()._setRecomendador(null);
			recomendacion.getRecomendador()._getRecomendados().remove(recomendacion);
			recomendacion._setRecomendado(null);
			recomendacion._setRecomendador(null);

		}
	}

}
