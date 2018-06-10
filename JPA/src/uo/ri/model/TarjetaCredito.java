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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import alb.util.date.DateUtil;
import uo.ri.util.exception.BusinessException;

/**
 * The Class TarjetaCredito.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
@Entity
@Table(name = "TTarjetasCredito")
@DiscriminatorValue("TTarjetasCredito")
public class TarjetaCredito extends MedioPago {

	/** The numero. */
	@Column(unique = true)
	private String numero;
	
	/** The tipo. */
	private String tipo;
	
	/** The validez. */
	private Date validez;

	/**
	 * Instantiates a new tarjeta credito.
	 */
	TarjetaCredito() {
	}

	/**
	 * Instantiates a new tarjeta credito.
	 *
	 * @param numero the numero
	 */
	public TarjetaCredito(String numero) {
		super();
		this.numero = numero;
		this.validez = DateUtil.tomorrow();
		this.tipo = "UNKNOWN";
	}

	/**
	 * Instantiates a new tarjeta credito.
	 *
	 * @param num the num
	 * @param type the type
	 * @param val the val
	 */
	public TarjetaCredito(String num, String type, Date val) {
		this.numero = num;
		this.tipo = type;
		this.validez = (Date) val.clone();
	}

	/**
	 * Gets the tipo.
	 *
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * Sets the tipo.
	 *
	 * @param tipo the new tipo
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * Gets the validez.
	 *
	 * @return the validez
	 */
	public Date getValidez() {
		return (Date) validez.clone();
	}

	/**
	 * Sets the validez.
	 *
	 * @param validez the new validez
	 */
	public void setValidez(Date validez) {
		this.validez = validez;
	}

	/**
	 * Gets the numero.
	 *
	 * @return the numero
	 */
	public String getNumero() {
		return numero;
	}

	/* (non-Javadoc)
	 * @see uo.ri.model.MedioPago#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		return result;
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
		TarjetaCredito other = (TarjetaCredito) obj;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TarjetaCredito [numero=" + numero + ", tipo=" + tipo + ", validez=" + validez + ", acumulado="
				+ acumulado + "]";
	}

	/**
	 * This method checks if the validity of the credit card is still right.
	 *
	 * @return true if the validity of the card is okay, false otherwise
	 */
	public boolean isValidNow() {
		return validez.after(DateUtil.today());
	}

	/**
	 * This method add to the accumulated money of the credit card the quantity
	 * specified as a parameter. If the validity of the credit card is not right
	 * anymore, an exception will be raised
	 *
	 * @param cantidad the quantity to be payed
	 * @throws BusinessException the business exception
	 */
	@Override
	public void pagar(double cantidad) throws BusinessException {
		if (isValidNow()) {
			super.acumulado += cantidad;
		} else {
			throw new BusinessException("La tarjeta está caducada");
		}
	}

}
