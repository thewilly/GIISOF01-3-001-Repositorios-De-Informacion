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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * The Class Sustitucion.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
@Entity
@Table(name = "TSustituciones", uniqueConstraints = { @UniqueConstraint(columnNames = "REPUESTO_ID, INTERVENCION_ID") })
public class Sustitucion {

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** The repuesto. */
	@ManyToOne
	private Repuesto repuesto;
	
	/** The intervencion. */
	@ManyToOne
	private Intervencion intervencion;
	
	/** The cantidad. */
	private int cantidad;

	/**
	 * Instantiates a new sustitucion.
	 */
	Sustitucion() {
	}

	/**
	 * Instantiates a new sustitucion.
	 *
	 * @param repuesto the repuesto
	 * @param intervencion the intervencion
	 */
	public Sustitucion(Repuesto repuesto, Intervencion intervencion) {
		super();
		Association.Sustituir.link(repuesto, this, intervencion);
	}

	/**
	 * Sets the intervencion.
	 *
	 * @param intervencion the intervencion
	 */
	void _setIntervencion(Intervencion intervencion) {
		this.intervencion = intervencion;
	}

	/**
	 * Sets the repuesto.
	 *
	 * @param repuesto the repuesto
	 */
	void _setRepuesto(Repuesto repuesto) {
		this.repuesto = repuesto;
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
		Sustitucion other = (Sustitucion) obj;
		if (intervencion == null) {
			if (other.intervencion != null)
				return false;
		} else if (!intervencion.equals(other.intervencion))
			return false;
		if (repuesto == null) {
			if (other.repuesto != null)
				return false;
		} else if (!repuesto.equals(other.repuesto))
			return false;
		return true;
	}

	/**
	 * Gets the cantidad.
	 *
	 * @return the cantidad
	 */
	public int getCantidad() {
		return cantidad;
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
	 * Gets the importe.
	 *
	 * @return the importe
	 */
	public Double getImporte() {
		return getCantidad() * repuesto.getPrecio();
	}

	/**
	 * Gets the intervencion.
	 *
	 * @return the intervencion
	 */
	public Intervencion getIntervencion() {
		return intervencion;
	}

	/**
	 * Gets the repuesto.
	 *
	 * @return the repuesto
	 */
	public Repuesto getRepuesto() {
		return repuesto;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((intervencion == null) ? 0 : intervencion.hashCode());
		result = prime * result + ((repuesto == null) ? 0 : repuesto.hashCode());
		return result;
	}

	/**
	 * Sets the cantidad.
	 *
	 * @param cantidad the new cantidad
	 */
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Sustitucion [repuesto=" + repuesto + ", intervencion=" + intervencion + ", cantidad=" + cantidad + "]";
	}
}
