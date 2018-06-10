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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import alb.util.date.DateUtil;
import alb.util.math.Round;
import uo.ri.model.types.AveriaStatus;
import uo.ri.model.types.FacturaStatus;
import uo.ri.util.exception.BusinessException;

/**
 * The Class Factura.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
@Entity
@Table(name = "TFacturas")
public class Factura {

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** The numero. */
	@Column(unique = true)
	private Long numero;
	
	/** The fecha. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;
	
	/** The importe. */
	private Double importe;
	
	/** The iva. */
	private Double iva;
	
	/** The status. */
	@Enumerated(EnumType.STRING)
	private FacturaStatus status = FacturaStatus.SIN_ABONAR;

	/** The averias. */
	@OneToMany(mappedBy = "factura")
	private Set<Averia> averias = new HashSet<>();
	
	/** The cargos. */
	@OneToMany(mappedBy = "factura")
	private Set<Cargo> cargos = new HashSet<>();

	/** The usada bono 500. */
	private boolean usadaBono500 = false;

	/**
	 * Instantiates a new factura.
	 */
	Factura() {
	}

	/**
	 * Instantiates a new factura.
	 *
	 * @param num the num
	 * @param today the today
	 */
	public Factura(long num, Date today) {
		this(num);
		this.fecha = today;
	}

	/**
	 * Instantiates a new factura.
	 *
	 * @param num the num
	 * @param fecha the fecha
	 * @param averias the averias
	 * @throws BusinessException the business exception
	 */
	public Factura(long num, Date fecha, List<Averia> averias) throws BusinessException {
		this(num, fecha);
		addAverias(averias);
	}

	/**
	 * Instantiates a new factura.
	 *
	 * @param num the num
	 * @param averias the averias
	 * @throws BusinessException the business exception
	 */
	public Factura(long num, List<Averia> averias) throws BusinessException {
		this(num);
		addAverias(averias);
	}

	/**
	 * Instantiates a new factura.
	 *
	 * @param numero the numero
	 */
	public Factura(Long numero) {
		super();
		this.numero = numero;
		this.fecha = new Date();
	}

	/**
	 * Gets the averias.
	 *
	 * @return the sets the
	 */
	Set<Averia> _getAverias() {
		return averias;
	}

	/**
	 * Gets the cargos.
	 *
	 * @return the sets the
	 */
	Set<Cargo> _getCargos() {
		return cargos;
	}

	/**
	 * Añade la averia a la factura.
	 *
	 * @param averia the averia
	 * @throws BusinessException the business exception
	 */
	public void addAveria(Averia averia) throws BusinessException {
		if (getStatus() != FacturaStatus.SIN_ABONAR) {
			throw new BusinessException("Factura ya está abonada.");
		}
		if (averia.getStatus() != AveriaStatus.TERMINADA) {
			throw new BusinessException("Avería no está terminada.");
		}
		Association.Facturar.link(averia, this);
		averia.markAsInvoiced();
		calcularImporte();
	}

	/**
	 * Añade la lista de averias a la factura.
	 *
	 * @param av the av
	 * @throws BusinessException the business exception
	 */
	private void addAverias(List<Averia> av) throws BusinessException {
		for (Averia a : av) {
			addAveria(a);
		}
	}

	/**
	 * Calcula el importe de la avería y su IVA, teniendo en cuenta la fecha de
	 * factura.
	 */
	void calcularImporte() {
		Double total = 0.0;
		for (Averia a : getAverias()) {
			total += a.getImporte();
		}
		importe = total + total * getIva() / 100;
		importe = Round.twoCents(importe);
	}

	/**
	 * This method checks if all the charges of the invoice sum up the import or
	 * a number less than 0.01€
	 * 
	 * @return true if the charges are in the interval +-0.01, false otherwise
	 */
	private boolean chargesOnInterval() {
		Double total = 0.0;
		for (Cargo cargo : cargos) {
			total += cargo.getImporte();
		}
		if (total <= getImporte() + 0.01 && total >= getImporte() - 0.01) {
			return true;
		}
		return false;
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
		Factura other = (Factura) obj;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
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
	 * Gets the cargos.
	 *
	 * @return the cargos
	 */
	public Set<Cargo> getCargos() {
		return new HashSet<>(cargos);
	}

	/**
	 * Gets the fecha.
	 *
	 * @return the fecha
	 */
	public Date getFecha() {
		if (fecha == null)
			return fecha = new Date();
		return new Date(fecha.getTime());
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
	public double getImporte() {
		calcularImporte();
		return importe;
	}

	/**
	 * Gets the iva.
	 *
	 * @return the iva
	 */
	public double getIva() {
		if (iva == null)
			obtenerIVA();
		return iva;
	}

	/**
	 * Gets the numero.
	 *
	 * @return the numero
	 */
	public Long getNumero() {
		return numero;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public FacturaStatus getStatus() {
		return status;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		return result;
	}

	/**
	 * Checks if is bono 500 used.
	 *
	 * @return true, if is bono 500 used
	 */
	public boolean isBono500Used() {
		return usadaBono500;
	}

	/**
	 * This method checks if the invoice is settled or not.
	 *
	 * @return true if it is settled, false otherwise
	 */
	public boolean isSettled() {
		return getStatus() == FacturaStatus.ABONADA;
	}

	/**
	 * This method checks that the invoice can be used to generate a voucher,
	 * and if it can, it is marked as used, otherwise, an exception will be
	 * thrown.
	 *
	 * @throws BusinessException the business exception
	 */
	public void markAsBono500Used() throws BusinessException {
		if (!puedeGenerarBono500()) {
			throw new BusinessException("Esta factura no puede ser usada para generar un bono");
		}
		this.usadaBono500 = true;
	}

	/**
	 * Obtiene el IVA dependiendo de la fecha de la factura. Si la fecha es
	 * anterior al 1/7/2012 se aplica 18%, sino 21%.
	 */
	void obtenerIVA() {
		iva = getFecha().before(DateUtil.fromString("1/7/2012")) ? 18.0 : 21.0;
	}

	/**
	 * This method checks if the invoice can generate a special voucher. The
	 * condition is that it has been payed, and the money payed was 500 euro or
	 * more, and the invoice could not have been used for another voucher.
	 * 
	 * @return true, if the conditions has been fulfilled, false otherwise
	 */
	public boolean puedeGenerarBono500() {
		if (getStatus() == FacturaStatus.ABONADA && getImporte() >= 500 && !isBono500Used()) {
			return true;
		}
		return false;
	}

	/**
	 * Elimina una averia de la factura, solo si está SIN_ABONAR y recalcula el
	 * importe.
	 *
	 * @param averia the averia
	 * @throws BusinessException the business exception
	 */
	public void removeAveria(Averia averia) throws BusinessException {
		if (getStatus() != FacturaStatus.SIN_ABONAR) {
			throw new BusinessException("Factura ya está abonada.");
		}
		Association.Facturar.unlink(averia, this);
		averia.markBackToFinished();
		calcularImporte();
	}

	/**
	 * Sets the fecha.
	 *
	 * @param today the new fecha
	 */
	public void setFecha(Date today) {
		this.fecha = today;
	}

	/**
	 * This method settles the invoice if it has breakdowns, and if it totally
	 * payed, otherwise, an exception will be raised.
	 *
	 * @throws BusinessException the business exception
	 */
	public void settle() throws BusinessException {
		if (averias.isEmpty()) {
			throw new BusinessException("No hay averias registradas");
		} else if (!chargesOnInterval()) {
			throw new BusinessException("Los cargos no igualan el importe");
		}
		this.status = FacturaStatus.ABONADA;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Factura [numero=" + numero + ", fecha=" + fecha + ", importe=" + importe + ", iva=" + iva + ", status="
				+ status + ", averias=" + averias + "]";
	}
}
