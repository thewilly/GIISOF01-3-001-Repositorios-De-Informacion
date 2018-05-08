package uo.ri.persistence;

import java.sql.Connection;

/**
 * 
 * RequestsConnection.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201805082141
 * @since 201805082141
 * @formatter Oviedo Computing Community
 */
public interface RequestsConnection {

	/**
	 * Sets the connection of the java type that implements this interface.
	 * 
	 * @param connection to be used.
	 */
	void setConnection( Connection connection );
}
