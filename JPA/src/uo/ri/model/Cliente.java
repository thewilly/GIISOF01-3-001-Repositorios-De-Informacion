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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import uo.ri.model.types.Address;

/**
 * The Class Cliente.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
@Entity
@Table(name = "TClientes")
public class Cliente {

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** The dni. */
	@Column(unique = true)
	private String dni;
	
	/** The nombre. */
	private String nombre;
	
	/** The apellidos. */
	private String apellidos;
	
	/** The email. */
	private String email;
	
	/** The phone. */
	private String phone;
	
	/** The address. */
	private Address address;

	/** The vehiculos. */
	@OneToMany(mappedBy = "cliente")
	private Set<Vehiculo> vehiculos = new HashSet<>();
	
	/** The medios pago. */
	@OneToMany(mappedBy = "cliente")
	private Set<MedioPago> mediosPago = new HashSet<>();

	/** The recomendador. */
	@OneToOne
	private Recomendacion recomendador;
	
	/** The recomendados. */
	@OneToMany(mappedBy = "recomendador")
	private Set<Recomendacion> recomendados = new HashSet<>();

	/**
	 * Instantiates a new cliente.
	 */
	Cliente() {
	}

	/**
	 * Instantiates a new cliente.
	 *
	 * @param dni the dni
	 */
	public Cliente(String dni) {
		super();
		this.dni = dni;
	}

	/**
	 * Instantiates a new cliente.
	 *
	 * @param DNI the dni
	 * @param nombre the nombre
	 * @param apellidos the apellidos
	 */
	public Cliente(String DNI, String nombre, String apellidos) {
		this(DNI);
		this.nombre = nombre;
		this.apellidos = apellidos;
	}

	/**
	 * Gets the nombre.
	 *
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Sets the nombre.
	 *
	 * @param nombre the new nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Gets the apellidos.
	 *
	 * @return the apellidos
	 */
	public String getApellidos() {
		return apellidos;
	}

	/**
	 * Sets the apellidos.
	 *
	 * @param apellidos the new apellidos
	 */
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * Gets the dni.
	 *
	 * @return the dni
	 */
	public String getDni() {
		return dni;
	}

	/**
	 * Gets the medios pago.
	 *
	 * @return the medios pago
	 */
	public Set<MedioPago> getMediosPago() {
		return new HashSet<>(mediosPago);
	}

	/**
	 * Gets the medios pago.
	 *
	 * @return the sets the
	 */
	Set<MedioPago> _getMediosPago() {
		return mediosPago;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Cliente [dni=" + dni + ", nombre=" + nombre + ", apellidos=" + apellidos + ", address=" + address + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dni == null) ? 0 : dni.hashCode());
		return result;
	}

	/**
	 * Gets the vehiculos.
	 *
	 * @return the vehiculos
	 */
	public Set<Vehiculo> getVehiculos() {
		return new HashSet<>(vehiculos);
	}

	/**
	 * Gets the vehiculos.
	 *
	 * @return the sets the
	 */
	Set<Vehiculo> _getVehiculos() {
		return vehiculos;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		if (dni == null) {
			if (other.dni != null)
				return false;
		} else if (!dni.equals(other.dni))
			return false;
		return true;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * This method returns a list containing all the breakdowns of the vehicles
	 * of the current client, that can be chosen for a special voucher.
	 *
	 * @return the list containing all the breakdowns
	 */
	public List<Averia> getAveriasBono3NoUsadas() {
		List<Averia> averias = new ArrayList<>();
		for (Vehiculo vehiculo : vehiculos) {
			for (Averia averia : vehiculo.getAverias()) {
				if (averia.esElegibleParaBono3()) {
					averias.add(averia);
				}
			}
		}
		return averias;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Gets the phone.
	 *
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Gets the recomendados.
	 *
	 * @return the sets the
	 */
	Set<Recomendacion> _getRecomendados() {
		return recomendados;
	}

	/**
	 * Gets the recomendador.
	 *
	 * @return the recomendacion
	 */
	Recomendacion _getRecomendador() {
		return recomendador;
	}

	/**
	 * Sets the recomendador.
	 *
	 * @param recomendador the recomendador
	 */
	void _setRecomendador(Recomendacion recomendador) {
		this.recomendador = recomendador;
	}

	/**
	 * Gets the recomendaciones hechas.
	 *
	 * @return the recomendaciones hechas
	 */
	public Set<Recomendacion> getRecomendacionesHechas() {
		return new HashSet<>(recomendados);
	}

	/**
	 * Gets the recomendacion recibida.
	 *
	 * @return the recomendacion recibida
	 */
	public Recomendacion getRecomendacionRecibida() {
		return recomendador;
	}

	/**
	 * Sets the email.
	 *
	 * @param string the new email
	 */
	public void setEmail(String string) {
		this.email = string;
	}

	/**
	 * Sets the phone.
	 *
	 * @param string the new phone
	 */
	public void setPhone(String string) {
		this.phone = string;
	}

	/**
	 * This method checks if the client is proper to have a voucher for it
	 * recommendations. For this, it has to have vehicles in the garage, as well
	 * as breakdowns and at least 3 recommendations.
	 * 
	 * @return true if all the conditions are fulfilled, false otherwise
	 */
	public boolean elegibleBonoPorRecomendaciones() {
		if (vehiculos.size() == 0 || !itHasBreakdowns() || !itHasRecomendations()) {
			return false;
		}
		return true;
	}

	/**
	 * This method checks if the client has recommended at least 3 clients that
	 * must have done any kind of reparation in the garage.
	 *
	 * @return true if it has recommended 3 other clients at least with the
	 *         conditions explained, false otherwise
	 */
	private boolean itHasRecomendations() {
		int count = 0;
		if (recomendados.size() >= 3) {
			for (Recomendacion recomendacion : recomendados) {
				if (recomendacion.getRecomendado().vehiculos.size() > 0
						&& recomendacion.getRecomendado().itHasBreakdowns() && !recomendacion.isUsada()) {
					count++;
				}
			}
		}

		if (count >= 3) {
			return true;
		}
		return false;
	}

	/**
	 * This method checks if the client has any vehicle with at least one
	 * breakdown.
	 *
	 * @return true if any vehicle has any breakdown, false otherwise
	 */
	private boolean itHasBreakdowns() {
		for (Vehiculo vehiculo : vehiculos) {
			if (vehiculo.getNumAverias() > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method returns the total number of vouchers depending on the number
	 * of recommendations not used for creating another.
	 *
	 * @return the number of vouchers
	 */
	public int numberOfVoucherForRecomendations() {
		int recommendations = 0;
		for (Recomendacion recomendacion : recomendados) {
			if (!recomendacion.isUsada()) {
				recommendations++;
			}
		}
		if (recommendations < 3) {
			return 0;
		}
		return recommendations / 3;
	}

	/**
	 * This method checks if the client has at least 3 breakdowns that have been
	 * payed.
	 *
	 * @return true if it has 3 breakdowns at least, false otherwise
	 */
	public boolean elegibleBonoPorAverias() {
		if (numberOfVoucherForBreakdowns() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * This method returns the total number of vouchers depending on the number
	 * of breakdowns that have been payed and not used for creating another.
	 *
	 * @return the number of vouchers
	 */
	public int numberOfVoucherForBreakdowns() {
		int numAv = 0;
		for (Vehiculo vehiculo : vehiculos) {
			Set<Averia> averias = vehiculo.getAverias();
			for (Averia averia : averias) {
				if (averia.getFactura() != null && averia.getFactura().isSettled() && !averia.isUsadaBono3()) {
					numAv++;
				}
			}
		}
		if (numAv < 3) {
			return 0;
		}
		return numAv / 3;
	}

	/**
	 * This method checks if the client can have a new voucher for having at
	 * least one invoice which total amount is over 500 euro.
	 *
	 * @return true if it is fulfilled, false otherwise
	 */
	public boolean elegibleBonoPorFactura500() {
		if (numberOfVoucherForInvoice() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * This method returns the number of vouchers that can be created depending
	 * on the number of invoice that have a total amount that is over 500 euro.
	 *
	 * @return the number of vouchers
	 */
	public int numberOfVoucherForInvoice() {
		int num = 0;
		List<Long> ids = new ArrayList<>();
		for (Vehiculo vehiculo : vehiculos) {
			Set<Averia> averias = vehiculo.getAverias();
			for (Averia averia : averias) {
				if (averia.getFactura() != null && averia.getFactura().puedeGenerarBono500()) {
					Long id = averia.getFactura().getId();
					if (!ids.contains(id)) {
						num++;
						ids.add(averia.getFactura().getId());
					}
				}
			}
		}
		return num;
	}

}
