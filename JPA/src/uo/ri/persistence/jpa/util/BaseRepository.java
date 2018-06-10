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
package uo.ri.persistence.jpa.util;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * The Class BaseRepository.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 * @param <T> the generic type
 */
public class BaseRepository<T> {
	
	/**
	 * Adds the.
	 *
	 * @param t the t
	 */
	public void add(T t) {
		Jpa.getManager().persist( t );
	}

	/**
	 * Removes the.
	 *
	 * @param t the t
	 */
	public void remove(T t) {
		Jpa.getManager().remove( t );
	}

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the t
	 */
	public T findById(Long id) {
		return Jpa.getManager().find(type, id);
	}

	/**
	 * Find all.
	 *
	 * @return the list
	 */
	public List<T> findAll() {
		String entity = type.getName();
		String query = "select o from " + entity + " o";
		
		return Jpa.getManager()
				.createQuery(query, type)
				.getResultList();
	}

	/**
	 * As find() and the query "select x from X x" needs the type of the entity
	 * here there is a reflective way of getting it.
	 */
	private Class<T> type;

	/**
	 * Instantiates a new base repository.
	 */
	public BaseRepository() {
		this.type = hasckTheTypeOfGenericParamenter();
	 }

	/**
	 * This is a hack to recover the runtime reflective type of <T>.
	 *
	 * @return the class
	 */
	@SuppressWarnings("unchecked")
	private Class<T> hasckTheTypeOfGenericParamenter() {
		ParameterizedType superType = 
			(ParameterizedType)	getClass().getGenericSuperclass();
	    return (Class<T>) superType.getActualTypeArguments()[0];
	}
	
}
