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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The Class TipoVehiculo.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
@Entity
@Table(name = "TTiposVehiculo")
public class TipoVehiculo {

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** The nombre. */
	@Column(unique = true)
	private String nombre;
	
	/** The precio hora. */
	private double precioHora;

	/** The vehiculos. */
	@OneToMany(mappedBy = "tipo")
	private Set<Vehiculo> vehiculos = new HashSet<>();

	/**
	 * Instantiates a new tipo vehiculo.
	 */
	TipoVehiculo() {
	}

	/**
	 * Instantiates a new tipo vehiculo.
	 *
	 * @param nombre the nombre
	 */
	public TipoVehiculo(String nombre) {
		super();
		this.nombre = nombre;
	}

	/**
	 * Instantiates a new tipo vehiculo.
	 *
	 * @param nombre the nombre
	 * @param precioHora the precio hora
	 */
	public TipoVehiculo(String nombre, double precioHora) {
		this(nombre);
		this.precioHora = precioHora;
	}

	/**
	 * Gets the precio hora.
	 *
	 * @return the precio hora
	 */
	public double getPrecioHora() {
		return precioHora;
	}

	/**
	 * Sets the precio hora.
	 *
	 * @param precioHora the new precio hora
	 */
	public void setPrecioHora(double precioHora) {
		this.precioHora = precioHora;
	}

	/**
	 * Gets the nombre.
	 *
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
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
		TipoVehiculo other = (TipoVehiculo) obj;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TipoVehiculo [nombre=" + nombre + ", precioHora=" + precioHora + "]";
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

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

}
