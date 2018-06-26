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
 * The Class Mecanico.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
@Entity
@Table(name = "TMecanicos")
public class Mecanico {

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The dni. */
    @Column(unique = true)
    private String dni;

    /** The apellidos. */
    private String apellidos;

    /** The nombre. */
    private String nombre;

    /** The asignadas. */
    @OneToMany(mappedBy = "mecanico")
    private Set<Averia> asignadas = new HashSet<>();

    /** The intervenciones. */
    @OneToMany(mappedBy = "mecanico")
    private Set<Intervencion> intervenciones = new HashSet<>();

    /**
     * Instantiates a new mecanico.
     */
    Mecanico() {
    }

    /**
     * Instantiates a new mecanico.
     *
     * @param dni
     *            the dni
     */
    public Mecanico(String dni) {
	super();
	this.dni = dni;
    }

    /**
     * Instantiates a new mecanico.
     *
     * @param dni
     *            the dni
     * @param nombre
     *            the nombre
     * @param apellidos
     *            the apellidos
     */
    public Mecanico(String dni, String nombre, String apellidos) {
	this(dni);
	this.nombre = nombre;
	this.apellidos = apellidos;
    }

    /**
     * Gets the asignadas.
     *
     * @return the sets the
     */
    Set<Averia> _getAsignadas() {
	return asignadas;
    }

    /**
     * Gets the intervenciones.
     *
     * @return the sets the
     */
    Set<Intervencion> _getIntervenciones() {
	return intervenciones;
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
	Mecanico other = (Mecanico) obj;
	if (dni == null) {
	    if (other.dni != null)
		return false;
	} else if (!dni.equals(other.dni))
	    return false;
	return true;
    }

    /**
     * Gets the apellidos.
     *
     * @return the apellidos
     */
    public String getApellidos() {
	return apellidos;
    }

    /**
     * Gets the asignadas.
     *
     * @return the asignadas
     */
    public Set<Averia> getAsignadas() {
	return new HashSet<>(asignadas);
    }

    /**
     * Gets the dni.
     *
     * @return the dni
     */
    public String getDni() {
	return dni;
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
     * Gets the intervenciones.
     *
     * @return the intervenciones
     */
    public Set<Intervencion> getIntervenciones() {
	return new HashSet<>(intervenciones);
    }

    /**
     * Gets the nombre.
     *
     * @return the nombre
     */
    public String getNombre() {
	return nombre;
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
	result = prime * result + ((dni == null) ? 0 : dni.hashCode());
	return result;
    }

    /**
     * Sets the apellidos.
     *
     * @param apellidos
     *            the new apellidos
     */
    public void setApellidos(String apellidos) {
	this.apellidos = apellidos;
    }

    /**
     * Sets the nombre.
     *
     * @param nombre
     *            the new nombre
     */
    public void setNombre(String nombre) {
	this.nombre = nombre;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "Mecanico [dni=" + dni + ", apellidos=" + apellidos + ", nombre="
		+ nombre + "]";
    }

}
