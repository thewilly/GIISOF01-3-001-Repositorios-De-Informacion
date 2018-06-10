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
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import uo.ri.model.types.AveriaStatus;
import uo.ri.model.types.FacturaStatus;
import uo.ri.util.exception.BusinessException;

/**
 * The Class Averia.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
@Entity
@Table(name = "TAverias", uniqueConstraints = { @UniqueConstraint(columnNames = "FECHA, VEHICULO_ID") })
public class Averia {

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** The descripcion. */
	private String descripcion;
	
	/** The fecha. */
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;
	
	/** The importe. */
	private double importe = 0.0;
	
	/** The status. */
	@Enumerated(EnumType.STRING)
	private AveriaStatus status = AveriaStatus.ABIERTA;

	/** The vehiculo. */
	@ManyToOne
	private Vehiculo vehiculo;
	
	/** The mecanico. */
	@ManyToOne
	private Mecanico mecanico;
	
	/** The factura. */
	@ManyToOne
	private Factura factura;
	
	/** The intervenciones. */
	@OneToMany(mappedBy = "averia")
	private Set<Intervencion> intervenciones = new HashSet<>();

	/** The usada bono 3. */
	private boolean usadaBono3 = false;

	/**
	 * Instantiates a new averia.
	 */
	Averia() {
	}

	/**
	 * Instantiates a new averia.
	 *
	 * @param vehiculo the vehiculo
	 */
	public Averia(Vehiculo vehiculo) {
		super();
		this.fecha = new Date();
		this.vehiculo = vehiculo;
		Association.Averiar.link(vehiculo, this);
		vehiculo.incrementarAverias();
	}

	/**
	 * Instantiates a new averia.
	 *
	 * @param vehiculo the vehiculo
	 * @param descripcion the descripcion
	 */
	public Averia(Vehiculo vehiculo, String descripcion) {
		this(vehiculo);
		this.descripcion = descripcion;
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
	 * Sets the descripcion.
	 *
	 * @param descripcion the new descripcion
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Gets the vehiculo.
	 *
	 * @return the vehiculo
	 */
	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	/**
	 * Sets the vehiculo.
	 *
	 * @param vehiculo the vehiculo
	 */
	void _setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

	/**
	 * Gets the fecha.
	 *
	 * @return the fecha
	 */
	public Date getFecha() {
		return new Date(fecha.getTime());
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
	 * Gets the status.
	 *
	 * @return the status
	 */
	public AveriaStatus getStatus() {
		return status;
	}

	/**
	 * Gets the intervenciones.
	 *
	 * @return the intervenciones
	 */
	public Set<Intervencion> getIntervenciones() {
		return new HashSet<>(intervenciones);
	}

	/**
	 * Gets the intervenciones.
	 *
	 * @return the sets the
	 */
	Set<Intervencion> _getIntervenciones() {
		return intervenciones;
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
	 * Sets the factura.
	 *
	 * @param factura the factura
	 */
	void _setFactura(Factura factura) {
		this.factura = factura;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Averia [descripcion=" + descripcion + ", fecha=" + fecha + ", importe=" + importe + ", status=" + status
				+ ", vehiculo=" + vehiculo + ", mecanico=" + mecanico + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + ((vehiculo == null) ? 0 : vehiculo.hashCode());
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
		Averia other = (Averia) obj;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		if (vehiculo == null) {
			if (other.vehiculo != null)
				return false;
		} else if (!vehiculo.equals(other.vehiculo))
			return false;
		return true;
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
	 * Asigna la averia al mecanico.
	 *
	 * @param mecanico the mecanico
	 * @throws BusinessException the business exception
	 */
	public void assignTo(Mecanico mecanico) throws BusinessException {
		if (getStatus() != AveriaStatus.ABIERTA) {
			throw new BusinessException("Avería no está en estado abierto.");
		}
		Association.Asignar.link(mecanico, this);
		this.status = AveriaStatus.ASIGNADA;
	}

	/**
	 * El mecánico da por finalizada esta avería, entonces se calcula el
	 * importe.
	 *
	 * @throws BusinessException the business exception
	 */
	public void markAsFinished() throws BusinessException {
		if (getStatus() != AveriaStatus.ASIGNADA) {
			throw new BusinessException("Avería no está asignada.");
		}
		calcularImporte();
		Association.Asignar.unlink(mecanico, this);
		this.status = AveriaStatus.TERMINADA;
	}

	/**
	 * This method calculates the total amount of all the interventions of the
	 * breakdown.
	 */
	void calcularImporte() {
		Double total = 0.0;
		for (Intervencion i : intervenciones)
			total += i.getImporte();
		importe = total;
	}

	/**
	 * Una averia en estado TERMINADA se puede asignar a otro mecánico (el
	 * primero no ha podido terminar la reparación), pero debe ser pasada a
	 * ABIERTA primero.
	 *
	 * @throws BusinessException the business exception
	 */
	public void reopen() throws BusinessException {
		if (getStatus() != AveriaStatus.TERMINADA) {
			throw new BusinessException("Avería no está terminada.");
		}
		this.status = AveriaStatus.ABIERTA;
	}

	/**
	 * This method breaks the assignment with the mechanic that is has been
	 * assign to the breakdown with itself.
	 *
	 * @throws BusinessException the business exception
	 */
	public void desassign() throws BusinessException {
		if (getStatus() != AveriaStatus.ASIGNADA) {
			throw new BusinessException("Avería no está asignada.");
		}
		Association.Asignar.unlink(mecanico, this);
		this.status = AveriaStatus.ABIERTA;
	}

	/**
	 * Una avería ya facturada se elimina de la factura.
	 *
	 * @throws BusinessException the business exception
	 */
	public void markBackToFinished() throws BusinessException {
		if (getStatus() != AveriaStatus.FACTURADA) {
			throw new BusinessException("Avería no está facturada");
		}
		this.status = AveriaStatus.TERMINADA;
	}

	/**
	 * Se marca la avería como facturada.
	 *
	 * @throws BusinessException the business exception
	 */
	public void markAsInvoiced() throws BusinessException {
		if (factura == null) {
			throw new BusinessException("Factura inexistente");
		}
		this.status = AveriaStatus.FACTURADA;
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
	 * This method checks if the current breakdown is suitable for been used for
	 * creating a voucher. it should be invoiced, payed, and not used other
	 * time.
	 * 
	 * @return true, if it fulfills all the conditions, false otherwise
	 */
	public boolean esElegibleParaBono3() {
		if (getStatus() != AveriaStatus.FACTURADA) {
			return false;
		} else if (getStatus() == AveriaStatus.FACTURADA && factura.getStatus() == FacturaStatus.SIN_ABONAR) {
			return false;
		} else if (getStatus() == AveriaStatus.FACTURADA && factura.getStatus() == FacturaStatus.ABONADA
				&& isUsadaBono3()) {
			return false;
		}
		return true;
	}

	/**
	 * This method changes the state of the breakdown to used for creating a
	 * voucher.
	 */
	public void markAsBono3Used() {
		if (!isUsadaBono3()) {
			this.usadaBono3 = true;
		}
	}

	/**
	 * Checks if is usada bono 3.
	 *
	 * @return true, if is usada bono 3
	 */
	public boolean isUsadaBono3() {
		return usadaBono3;
	}

	/**
	 * This method checks if the breakdown is already invoiced.
	 *
	 * @return true if it is, false otherwise
	 */
	public boolean isInvoiced() {
		return this.status == AveriaStatus.FACTURADA;
	}
}
