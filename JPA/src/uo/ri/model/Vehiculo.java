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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The Class Vehiculo.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
@Entity
@Table(name = "TVehiculos")
public class Vehiculo {

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** The marca. */
	private String marca;
	
	/** The matricula. */
	@Column(unique = true)
	private String matricula;
	
	/** The modelo. */
	private String modelo;

	/** The num averias. */
	@Column(name = "NUM_AVERIAS")
	private int numAverias = 0;

	/** The cliente. */
	@ManyToOne
	private Cliente cliente;
	
	/** The tipo. */
	@ManyToOne
	private TipoVehiculo tipo;
	
	/** The averias. */
	@OneToMany(mappedBy = "vehiculo")
	private Set<Averia> averias = new HashSet<>();

	/**
	 * Instantiates a new vehiculo.
	 */
	Vehiculo() {
	}

	/**
	 * Instantiates a new vehiculo.
	 *
	 * @param matricula the matricula
	 */
	public Vehiculo(String matricula) {
		super();
		this.matricula = matricula;
	}

	/**
	 * Instantiates a new vehiculo.
	 *
	 * @param matricula the matricula
	 * @param marca the marca
	 * @param modelo the modelo
	 */
	public Vehiculo(String matricula, String marca, String modelo) {
		this(matricula);
		this.marca = marca;
		this.modelo = modelo;
	}

	/**
	 * Gets the marca.
	 *
	 * @return the marca
	 */
	public String getMarca() {
		return marca;
	}

	/**
	 * Sets the marca.
	 *
	 * @param marca the new marca
	 */
	public void setMarca(String marca) {
		this.marca = marca;
	}

	/**
	 * Gets the modelo.
	 *
	 * @return the modelo
	 */
	public String getModelo() {
		return modelo;
	}

	/**
	 * Sets the modelo.
	 *
	 * @param modelo the new modelo
	 */
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	/**
	 * Gets the num averias.
	 *
	 * @return the num averias
	 */
	public int getNumAverias() {
		return numAverias;
	}

	/**
	 * Sets the num averias.
	 *
	 * @param numAverias the new num averias
	 */
	public void setNumAverias(int numAverias) {
		this.numAverias = numAverias;
	}

	/**
	 * Gets the matricula.
	 *
	 * @return the matricula
	 */
	public String getMatricula() {
		return matricula;
	}

	/**
	 * Gets the averias.
	 *
	 * @return the averias
	 */
	public Set<Averia> getAverias() {
		return new HashSet<>(averias);
	}

	/**
	 * Gets the averias.
	 *
	 * @return the sets the
	 */
	Set<Averia> _getAverias() {
		return averias;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Vehiculo [marca=" + marca + ", matricula=" + matricula + ", modelo=" + modelo + ", numAverias="
				+ numAverias + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((matricula == null) ? 0 : matricula.hashCode());
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
		Vehiculo other = (Vehiculo) obj;
		if (matricula == null) {
			if (other.matricula != null)
				return false;
		} else if (!matricula.equals(other.matricula))
			return false;
		return true;
	}

	/**
	 * Gets the cliente.
	 *
	 * @return the cliente
	 */
	public Cliente getCliente() {
		return cliente;
	}

	/**
	 * Sets the cliente.
	 *
	 * @param cliente the cliente
	 */
	void _setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	/**
	 * Gets the tipo.
	 *
	 * @return the tipo
	 */
	public TipoVehiculo getTipo() {
		return tipo;
	}

	/**
	 * Sets the tipo.
	 *
	 * @param tipo the tipo
	 */
	void _setTipo(TipoVehiculo tipo) {
		this.tipo = tipo;
	}

	/**
	 * Incrementar averias.
	 */
	public void incrementarAverias() {
		this.numAverias++;
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
