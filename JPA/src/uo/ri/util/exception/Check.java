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
package uo.ri.util.exception;

/**
 * The Class Check.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class Check {

	/**
	 * Checks if is false.
	 *
	 * @param condition the condition
	 * @throws BusinessException the business exception
	 */
	public static void isFalse(boolean condition) throws BusinessException {
		isTrue( ! condition, "Invalid assertion");
	}

	/**
	 * Checks if is false.
	 *
	 * @param condition the condition
	 * @param errorMsg the error msg
	 * @throws BusinessException the business exception
	 */
	public static void isFalse(boolean condition, String errorMsg) throws BusinessException {
		isTrue( ! condition, errorMsg);
	}

	/**
	 * Checks if is not null.
	 *
	 * @param o the o
	 * @throws BusinessException the business exception
	 */
	public static void isNotNull(Object o) throws BusinessException {
		isTrue( o != null, o.getClass().getName() + " cannot be null here"); 
	}

	/**
	 * Checks if is not null.
	 *
	 * @param o the o
	 * @param errorMsg the error msg
	 * @throws BusinessException the business exception
	 */
	public static void isNotNull(Object o, String errorMsg) throws BusinessException {
		isTrue( o != null, errorMsg); 
	}

	/**
	 * Checks if is null.
	 *
	 * @param o the o
	 * @throws BusinessException the business exception
	 */
	public static void isNull(Object o) throws BusinessException {
		isTrue( o == null, o.getClass().getName() + " cannot be null here"); 
	}

	/**
	 * Checks if is null.
	 *
	 * @param o the o
	 * @param errorMsg the error msg
	 * @throws BusinessException the business exception
	 */
	public static void isNull(Object o, String errorMsg) throws BusinessException {
		isTrue( o == null, errorMsg); 
	}

	/**
	 * Checks if is true.
	 *
	 * @param condition the condition
	 * @throws BusinessException the business exception
	 */
	public static void isTrue(boolean condition) throws BusinessException {
		isTrue(condition, "Invalid assertion");
	}

	/**
	 * Checks if is true.
	 *
	 * @param condition the condition
	 * @param errorMsg the error msg
	 * @throws BusinessException the business exception
	 */
	public static void isTrue(boolean condition, String errorMsg) throws BusinessException {
		if ( condition == true ) return;
		throw new BusinessException( errorMsg );
	}

}
