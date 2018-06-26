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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import uo.ri.util.exception.BusinessException;

/**
 * The Class MedioPago.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "TMediosPago")
public abstract class MedioPago {

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The acumulado. */
    protected double acumulado = 0.0;

    /** The cliente. */
    @ManyToOne
    private Cliente cliente;

    /** The cargos. */
    @OneToMany(mappedBy = "medioPago")
    private Set<Cargo> cargos = new HashSet<>();

    /**
     * Gets the cargos.
     *
     * @return the sets the
     */
    Set<Cargo> _getCargos() {
	return cargos;
    }

    /**
     * Sets the cliente.
     *
     * @param cliente
     *            the cliente
     */
    void _setCliente(Cliente cliente) {
	this.cliente = cliente;
    }

    /**
     * Decrementar acumulado.
     *
     * @param importe
     *            the importe
     */
    void decrementarAcumulado(Double importe) {
	acumulado -= importe;
    }

    /*
     * (non-Javadoc)
     * 
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
	MedioPago other = (MedioPago) obj;
	if (cliente == null) {
	    if (other.cliente != null)
		return false;
	} else if (!cliente.equals(other.cliente))
	    return false;
	return true;
    }

    /**
     * Gets the acumulado.
     *
     * @return the acumulado
     */
    public double getAcumulado() {
	return acumulado;
    }

    /**
     * Gets the cargos.
     *
     * @return the cargos
     */
    public Set<Cargo> getCargos() {
	return new HashSet<>(cargos);
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
     * Gets the id.
     *
     * @return the id
     */
    public Long getId() {
	return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
	return result;
    }

    /**
     * Incrementar acumulado.
     *
     * @param importe
     *            the importe
     * @throws BusinessException
     *             the business exception
     */
    void incrementarAcumulado(Double importe) throws BusinessException {
	pagar(importe);
    }

    /**
     * This method increases the acumulado attribute with the amount parameter.
     * It is redefined in all the children with the specific implementation
     * depending in each one.
     *
     * @param amount
     *            the amount to be payed with the payment method
     * @throws BusinessException
     *             the business exception
     */
    public abstract void pagar(double amount) throws BusinessException;

}
