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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * The Class Intervencion.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
@Entity
@Table(name = "TIntervenciones", uniqueConstraints = { @UniqueConstraint(columnNames = "AVERIA_ID, MECANICO_ID") })
public class Intervencion {

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** The averia. */
	@ManyToOne
	private Averia averia;
	
	/** The mecanico. */
	@ManyToOne
	private Mecanico mecanico;
	
	/** The minutos. */
	private int minutos;

	/** The sustituciones. */
	@OneToMany(mappedBy = "intervencion")
	private Set<Sustitucion> sustituciones = new HashSet<>();

	/**
	 * Instantiates a new intervencion.
	 */
	Intervencion() {
	}

	/**
	 * Instantiates a new intervencion.
	 *
	 * @param mecanico the mecanico
	 * @param averia the averia
	 */
	public Intervencion(Mecanico mecanico, Averia averia) {
		super();
		Association.Intervenir.link(averia, this, mecanico);
	}

	/**
	 * Instantiates a new intervencion.
	 *
	 * @param m the m
	 * @param a the a
	 * @param minutes the minutes
	 */
	public Intervencion(Mecanico m, Averia a, int minutes) {
		this(m, a);
		this.minutos = minutes;
	}

	/**
	 * Gets the averia.
	 *
	 * @return the averia
	 */
	public Averia getAveria() {
		return averia;
	}

	/**
	 * Sets the averia.
	 *
	 * @param averia the averia
	 */
	void _setAveria(Averia averia) {
		this.averia = averia;
	}

	/**
	 * Gets the mecanico.
	 *
	 * @return the mecanico
	 */
	public Mecanico getMecanico() {
		return mecanico;
	}

	/**
	 * Sets the mecanico.
	 *
	 * @param mecanico the mecanico
	 */
	void _setMecanico(Mecanico mecanico) {
		this.mecanico = mecanico;
	}

	/**
	 * Gets the minutos.
	 *
	 * @return the minutos
	 */
	public int getMinutos() {
		return minutos;
	}

	/**
	 * Sets the minutos.
	 *
	 * @param minutos the new minutos
	 */
	public void setMinutos(int minutos) {
		this.minutos = minutos;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Intervencion [averia=" + averia + ", mecanico=" + mecanico + ", minutos=" + minutos + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((averia == null) ? 0 : averia.hashCode());
		result = prime * result + ((mecanico == null) ? 0 : mecanico.hashCode());
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
		Intervencion other = (Intervencion) obj;
		if (averia == null) {
			if (other.averia != null)
				return false;
		} else if (!averia.equals(other.averia))
			return false;
		if (mecanico == null) {
			if (other.mecanico != null)
				return false;
		} else if (!mecanico.equals(other.mecanico))
			return false;
		return true;
	}

	/**
	 * Gets the sustituciones.
	 *
	 * @return the sustituciones
	 */
	public Set<Sustitucion> getSustituciones() {
		return new HashSet<>(sustituciones);
	}

	/**
	 * Gets the sustituciones.
	 *
	 * @return the sets the
	 */
	Set<Sustitucion> _getSustituciones() {
		return sustituciones;
	}

	/**
	 * This method gets the total amount of money of the intervention by
	 * calculating the price of the minutes spent in it, and the price of all
	 * the substitutions that had to be used.
	 *
	 * @return the total amount of the calculated price
	 */
	public Double getImporte() {
		Double total = 0.0;
		total += getMinutos() * averia.getVehiculo().getTipo().getPrecioHora() / 60;
		for (Sustitucion s : getSustituciones()) {
			total += s.getImporte();
		}
		return total;
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
