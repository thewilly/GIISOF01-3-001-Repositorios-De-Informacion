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
package uo.ri.business.impl.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static alb.util.jdbc.Jdbc.*;
import uo.ri.common.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.MechanicsGateway;

/**
 * 
 * UpdateMechanic.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201805081327
 * @since 201805081327
 * @formatter Oviedo Computing Community
 */
public class UpdateMechanic {

	private long mechanicId;
	private String name, surname;

	/**
	 * Action to update the mechanic name and surname. The mechanic where the
	 * update will take place is the one who's id is provided as first
	 * parameter.
	 * 
	 * @param mechanicId is the id of the mechanic to update.
	 * @param name is the new name for the mechanic.
	 * @param surname is the new surname for the mechanic.
	 */
	public UpdateMechanic( long mechanicId, String name, String surname ) {
		this.name = name;
		this.surname = surname;
		this.mechanicId = mechanicId;
	}

	/**
	 * For the given mechanic id will update the name and the surname with the
	 * ones provided.
	 * 
	 * @throws BusinessException if any error occurs during the mechanic update
	 *             or during persistence.
	 */
	public void execute() throws BusinessException {
		Connection connection = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			// Getting the connection.
			connection = getConnection();

			// Creating the data gateway.
			MechanicsGateway mechanicsGW = PersistenceFactory.getMechanicsGateway();
			mechanicsGW.setConnection( connection );

			// Updating the mechanic.
			mechanicsGW.update( name, surname, mechanicId );

		} catch (SQLException e) {
			throw new RuntimeException( e );
		} finally {
			// Closing the connection, the prepared statement and the result set.
			close( rs, pst, connection );
		}
	}

}
