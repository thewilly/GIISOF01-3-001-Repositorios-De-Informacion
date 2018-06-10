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
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The Class Repuesto.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
@Entity
@Table(name = "TRepuestos")
public class Repuesto {

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	/** The codigo. */
	@Column(unique = true)
	private String codigo;
	
	/** The descripcion. */
	private String descripcion;
	
	/** The precio. */
	private double precio;

	/** The sustituciones. */
	@OneToMany(mappedBy = "repuesto")
	private Set<Sustitucion> sustituciones = new HashSet<>();

	/**
	 * Instantiates a new repuesto.
	 */
	Repuesto() {
	}

	/**
	 * Instantiates a new repuesto.
	 *
	 * @param codigo the codigo
	 */
	public Repuesto(String codigo) {
		super();
		this.codigo = codigo;
	}

	/**
	 * Instantiates a new repuesto.
	 *
	 * @param codigo the codigo
	 * @param descripcion the descripcion
	 * @param precio the precio
	 */
	public Repuesto(String codigo, String descripcion, double precio) {
		this(codigo);
		this.descripcion = descripcion;
		this.precio = precio;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Repuesto [codigo=" + codigo + ", descripcion=" + descripcion + ", precio=" + precio + "]";
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
	 * Gets the precio.
	 *
	 * @return the precio
	 */
	public double getPrecio() {
		return precio;
	}

	/**
	 * Sets the precio.
	 *
	 * @param precio the new precio
	 */
	public void setPrecio(double precio) {
		this.precio = precio;
	}

	/**
	 * Gets the codigo.
	 *
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
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
		Repuesto other = (Repuesto) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
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
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
}
