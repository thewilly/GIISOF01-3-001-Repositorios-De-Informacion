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
package uo.ri.common;

/**
 * 
 * BusinessException.java (From source code provided by subject teachers)
 *
 * @author Guillermo Facundo Colunga
 * @version 201805082114
 * @since 201805082114
 * @formatter Oviedo Computing Community
 */
public class BusinessException extends Exception {

	private static final long serialVersionUID = -308694287126038961L;

	/**
	 * Allocates a business exception object and initializes it. represents.
	 */
	public BusinessException() {}

	/**
	 * Allocates a business exception object and initializes it so that it
	 * contains the given message.
	 * 
	 * @param message is the message of the exception.
	 */
	public BusinessException( String message ) {
		super( message );
	}

	/**
	 * Allocates a business exception object and initializes it so that it
	 * contains the given message and the given cause.
	 * 
	 * @param message is the message of the exception.
	 * @param cause is the cause of the exception.
	 */
	public BusinessException( String message, Throwable cause ) {
		super( message, cause );
	}

	/**
	 * Allocates a business exception object and initializes it so that it
	 * contains the given cause.
	 * 
	 * @param cause is the cause of the exception.
	 */
	public BusinessException( Throwable cause ) {
		super( cause );
	}

}
