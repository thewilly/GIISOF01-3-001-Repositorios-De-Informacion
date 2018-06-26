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

import uo.ri.model.types.FacturaStatus;
import uo.ri.util.exception.BusinessException;

/**
 * The Class Cargo.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
@Entity
@Table(name = "TCargos", uniqueConstraints = {
	@UniqueConstraint(columnNames = "FACTURA_ID, MEDIOPAGO_ID") })
public class Cargo {

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The factura. */
    @ManyToOne
    private Factura factura;

    /** The medio pago. */
    @ManyToOne
    private MedioPago medioPago;

    /** The importe. */
    private double importe = 0.0;

    /**
     * Instantiates a new cargo.
     */
    Cargo() {
    }

    /**
     * Instantiates a new cargo.
     *
     * @param factura
     *            the factura
     * @param medioPago
     *            the medio pago
     */
    public Cargo(Factura factura, MedioPago medioPago) {
	super();
	this.factura = factura;
	this.medioPago = medioPago;
	Association.Cargar.link(medioPago, this, factura);
    }

    /**
     * Instantiates a new cargo.
     *
     * @param factura
     *            the factura
     * @param medioPago
     *            the medio pago
     * @param importe
     *            the importe
     * @throws BusinessException
     *             the business exception
     */
    public Cargo(Factura factura, MedioPago medioPago, double importe)
	    throws BusinessException {
	this(factura, medioPago);
	checkIfCorrectPayment(medioPago, importe);
	medioPago.incrementarAcumulado(importe);
	this.importe = importe;
    }

    /**
     * Sets the factura.
     *
     * @param factura
     *            the factura
     */
    void _setFactura(Factura factura) {
	this.factura = factura;
    }

    /**
     * Sets the medio pago.
     *
     * @param medioPago
     *            the medio pago
     */
    void _setMedioPago(MedioPago medioPago) {
	this.medioPago = medioPago;
    }

    /**
     * This method checks if the payment method fulfills the condition for the
     * charge it will have. If it is a credit card, the date shouldn't be
     * expired, and the voucher should have enough money available.
     *
     * @param medioPago
     *            to be checked
     * @param importe
     *            the quantity to charge
     * @throws BusinessException
     *             the business exception
     */
    private void checkIfCorrectPayment(MedioPago medioPago, double importe)
	    throws BusinessException {
	if (medioPago.getClass().equals(Bono.class)) {
	    Bono b = (Bono) medioPago;
	    if (importe > b.getDisponible()) {
		throw new BusinessException(
			"No hay saldo suficiente en el bono");
	    }
	} else if (medioPago.getClass().equals(TarjetaCredito.class)) {
	    TarjetaCredito t = (TarjetaCredito) medioPago;
	    if (!t.isValidNow()) {
		throw new BusinessException("La tarjeta está caducada");
	    }
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (!(obj instanceof Cargo)) {
	    return false;
	}
	Cargo other = (Cargo) obj;
	if (id == null) {
	    if (other.id != null) {
		return false;
	    }
	} else if (!id.equals(other.id)) {
	    return false;
	}
	return true;
    }

    /**
     * Gets the factura.
     *
     * @return the factura
     */
    public Factura getFactura() {
	return factura;
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
	return importe;
    }

    /**
     * Gets the medio pago.
     *
     * @return the medio pago
     */
    public MedioPago getMedioPago() {
	return medioPago;
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
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	return result;
    }

    /**
     * Anula (retrocede) este cargo de la factura y el medio de pago Solo se
     * puede hacer si la factura no está abonada Decrementar el acumulado del
     * medio de pago Desenlazar el cargo de la factura y el medio de pago.
     *
     * @throws BusinessException
     *             the business exception
     */
    public void rewind() throws BusinessException {
	if (factura.getStatus() == FacturaStatus.ABONADA) {
	    throw new BusinessException("Factura ya está abonada.");
	}
	medioPago.decrementarAcumulado(importe);
	Association.Cargar.unlink(this);
    }
}
