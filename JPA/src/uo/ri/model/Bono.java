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

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import uo.ri.util.exception.BusinessException;

/**
 * The Class Bono.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
@Entity
@Table(name = "TBonos")
@DiscriminatorValue("TBonos")
public class Bono extends MedioPago {

	/** The disponible. */
	private double disponible = 0.0;
	
	/** The descripcion. */
	private String descripcion;
	
	/** The codigo. */
	@Column(unique = true)
	private String codigo;

	/**
	 * Instantiates a new bono.
	 */
	Bono() {
	}

	/**
	 * Instantiates a new bono.
	 *
	 * @param codigo the codigo
	 */
	public Bono(String codigo) {
		super();
		this.codigo = codigo;
		this.disponible = 0.0;
		this.descripcion = "";
	}

	/**
	 * Instantiates a new bono.
	 *
	 * @param codigo the codigo
	 * @param disponible the disponible
	 */
	public Bono(String codigo, double disponible) {
		this(codigo);
		this.disponible = disponible;
		this.descripcion = "";
	}

	/**
	 * Instantiates a new bono.
	 *
	 * @param code the code
	 * @param description the description
	 * @param quantity the quantity
	 */
	public Bono(String code, String description, double quantity) {
		this.codigo = code;
		this.descripcion = description;
		this.disponible = quantity;
	}

	/* (non-Javadoc)
	 * @see uo.ri.model.MedioPago#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bono other = (Bono) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	/**
	 * Gets the codigo.
	 *
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * Gets the descripcion.
	 *
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Gets the disponible.
	 *
	 * @return the disponible
	 */
	public Double getDisponible() {
		return disponible;
	}

	/* (non-Javadoc)
	 * @see uo.ri.model.MedioPago#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	/**
	 * This method add a charge in the accumulated money of the payment method,
	 * if there is not enough money, and exception will be raised.
	 *
	 * @param cantidad the quantity to be payed and discounted from the voucher
	 * @throws BusinessException the business exception
	 */
	@Override
	public void pagar(double cantidad) throws BusinessException {
		if (cantidad <= getDisponible()) {
			super.acumulado += cantidad;
			this.disponible -= cantidad;
		} else {
			throw new BusinessException("No hay saldo suficiente en el bono");
		}
	}

	/**
	 * Sets the descripcion.
	 *
	 * @param descripcion the new descripcion
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Sets the disponible.
	 *
	 * @param disponible the new disponible
	 */
	public void setDisponible(double disponible) {
		this.disponible = disponible;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Bono [disponible=" + disponible + ", descripcion=" + descripcion + ", codigo=" + codigo + ", acumulado="
				+ acumulado + "]";
	}

}
