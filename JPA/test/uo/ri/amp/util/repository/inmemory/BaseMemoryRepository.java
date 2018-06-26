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
package uo.ri.amp.util.repository.inmemory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alb.util.reflection.ReflectionUtil;

/**
 * The Class BaseMemoryRepository.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 * @param <T>
 *            the generic type
 */
public abstract class BaseMemoryRepository<T> {

    /** The counter. */
    long counter = 0L;

    /** The entities. */
    protected Map<Long, T> entities = new HashMap<>();

    /**
     * Adds the.
     *
     * @param t
     *            the t
     */
    public void add(T t) {
	Long l = nextId();
	putAttr(t, "id", l);
	entities.put(l, t);
    }

    /**
     * Find all.
     *
     * @return the list
     */
    public List<T> findAll() {
	return new ArrayList<>(entities.values());
    }

    /**
     * Find by id.
     *
     * @param id
     *            the id
     * @return the t
     */
    public T findById(Long id) {
	return entities.get(id);
    }

    /**
     * Gets the attr.
     *
     * @param owner
     *            the owner
     * @param fieldName
     *            the field name
     * @return the attr
     */
    private Long getAttr(T owner, String fieldName) {
	Field field = ReflectionUtil.getField(owner.getClass(), fieldName);
	return (Long) ReflectionUtil.getFieldValue(owner, field);
    }

    /**
     * Next id.
     *
     * @return the long
     */
    private Long nextId() {
	return ++counter;
    }

    /**
     * Put attr.
     *
     * @param owner
     *            the owner
     * @param fieldName
     *            the field name
     * @param value
     *            the value
     */
    private void putAttr(T owner, String fieldName, Long value) {
	Field field = ReflectionUtil.getField(owner.getClass(), fieldName);
	ReflectionUtil.applyValueToField(owner, field, value);
    }

    /**
     * Removes the.
     *
     * @param t
     *            the t
     */
    public void remove(T t) {
	Long id = getAttr(t, "id");
	entities.remove(id);
    }

}
