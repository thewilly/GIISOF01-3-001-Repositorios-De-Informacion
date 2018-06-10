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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * This class is the relationship between a client and another client. The
 * purpose is to register the recommendations that a client can do for earning a
 * voucher of 25 euro. This is why, we can see two clients, recomendador and
 * recomendado, and a boolean attribute for knowing if this recommendation has
 * been used to generate a voucher.
 * 
 * @author uo250878
 *
 */
@Entity
@Table(name = "TRecomendaciones", uniqueConstraints = {
		@UniqueConstraint(columnNames = "RECOMENDADOR_ID, RECOMENDADO_ID") })
public class Recomendacion {

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** The recomendador. */
	private Cliente recomendador;
	
	/** The recomendado. */
	private Cliente recomendado;
	
	/** The usada. */
	private boolean usada;

	/**
	 * Instantiates a new recomendacion.
	 */
	Recomendacion() {
	}

	/**
	 * Instantiates a new recomendacion.
	 *
	 * @param recomendador the recomendador
	 * @param recomendado the recomendado
	 */
	public Recomendacion(Cliente recomendador, Cliente recomendado) {
		super();
		this.recomendador = recomendador;
		this.recomendado = recomendado;
		this.usada = false;
		Association.Recomendar.link(this, recomendador, recomendado);
	}

	/**
	 * Gets the recomendador.
	 *
	 * @return the recomendador
	 */
	public Cliente getRecomendador() {
		return recomendador;
	}

	/**
	 * Gets the recomendado.
	 *
	 * @return the recomendado
	 */
	public Cliente getRecomendado() {
		return recomendado;
	}

	/**
	 * This method marks the usada attribute as true, if and only if it is not
	 * already true.
	 */
	public void markAsUsadaBono() {
		if (!isUsada()) {
			this.usada = true;
		}
	}

	/**
	 * Checks if is usada.
	 *
	 * @return true, if is usada
	 */
	public boolean isUsada() {
		return usada;
	}

	/**
	 * Sets the recomendador.
	 *
	 * @param recomendador the recomendador
	 */
	void _setRecomendador(Cliente recomendador) {
		this.recomendador = recomendador;
	}

	/**
	 * Sets the recomendado.
	 *
	 * @param recomendado the recomendado
	 */
	void _setRecomendado(Cliente recomendado) {
		this.recomendado = recomendado;
	}

	/**
	 * This method unlinks the recommendation.
	 */
	public void unlink() {
		Association.Recomendar.unlink(this);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((recomendado == null) ? 0 : recomendado.hashCode());
		result = prime * result + ((recomendador == null) ? 0 : recomendador.hashCode());
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
		Recomendacion other = (Recomendacion) obj;
		if (recomendado == null) {
			if (other.recomendado != null)
				return false;
		} else if (!recomendado.equals(other.recomendado))
			return false;
		if (recomendador == null) {
			if (other.recomendador != null)
				return false;
		} else if (!recomendador.equals(other.recomendador))
			return false;
		return true;
	}

}
