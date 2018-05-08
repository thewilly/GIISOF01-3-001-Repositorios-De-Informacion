/*******************************************************************************
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package uo.ri.persistence;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import uo.ri.common.BusinessException;

public interface ClientsGateway {

	/**
	 * Registra a un cliente que ha sido recomendado
	 * 
	 * @param dni
	 * @param nombre
	 * @param apellidos
	 * @param zipcode
	 * @param telefono
	 * @param correo
	 * @param idRecomendador
	 * @throws BusinessException
	 */
	void createWithRecommender( String dni, String nombre, String apellidos, int zipcode,
			int telefono, String correo,
			Long idRecomendador ) throws BusinessException;

	List<Map<String, Object>> findAllClients() throws BusinessException;

	/**
	 * Devuelve todos los ids de los clientes en una lista
	 * 
	 * @return
	 * @throws BusinessException
	 */
	List<Long> findAllClientsIds() throws BusinessException;

	/**
	 * Devuelve todos los detalles de un cliente en un String
	 * 
	 * @param idClient
	 * @return
	 * @throws BusinessException
	 */
	String findByClientId( long idClient ) throws BusinessException;

	/**
	 * Devuelve una lista con todos los clientes que han sido recomendados por
	 * otro cliente que se le pasa como parámetro
	 * 
	 * @param idRecomendator
	 * @return
	 * @throws BusinessException
	 */
	List<Map<String, Object>> findClientsByRecommender( long idRecomendator )
			throws BusinessException;

	void remove( long idClient ) throws BusinessException;

	void save( String dni, String nombre, String apellidos, int zipcode, int telefono,
			String correo )
			throws BusinessException;

	void setConnection( Connection con );

	void update( long id, String dni, String nombre, String apellidos, int zipcode, int telefono,
			String correo )
			throws BusinessException;

}
