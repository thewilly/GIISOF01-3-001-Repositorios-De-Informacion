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
package uo.ri.conf;

import java.io.IOException;
import java.util.Properties;

/**
 * 
 * Conf.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201805082120
 * @since 201805082120
 * @formatter Oviedo Computing Community
 */
public class Conf {
	
	// SATATIC MEMBERS

	private static final String CONF_FILE = "configuration.properties";

	private static Conf instance;
	
	public static String get( String key ) {
		return getInstance().getProperty( key );
	}

	private static Conf getInstance() {
		if (instance == null) {
			instance = new Conf();
		}
		return instance;
	}

	// NON-STATIC MEMEBERS 
	
	private Properties properties;
	
	private Conf() {
		properties = new Properties();
		try {
			properties.load( Conf.class.getClassLoader().getResourceAsStream( CONF_FILE ) );
		} catch (IOException e) {
			throw new RuntimeException( "Properties file can not be loaded", e );
		}
	}

	private String getProperty( String key ) {
		String value = properties.getProperty( key );
		if (value == null) {
			throw new RuntimeException( "Properties not found in config file" );
		}
		return value;
	}

}
