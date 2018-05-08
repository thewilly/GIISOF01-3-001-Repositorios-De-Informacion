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
package uo.ri.business.impl;

import java.util.List;
import java.util.Map;

import uo.ri.business.ForemanService;
import uo.ri.business.impl.foreman.CreateClient;
import uo.ri.business.impl.foreman.CreateRecommendedClient;
import uo.ri.business.impl.foreman.FindAllClients;
import uo.ri.business.impl.foreman.FindClientsByRecommender;
import uo.ri.business.impl.foreman.RemoveClient;
import uo.ri.business.impl.foreman.FindClient;
import uo.ri.business.impl.foreman.UpdateClient;
import uo.ri.common.BusinessException;

/**
 * 
 * ForemanServiceImpl.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201805082106
 * @since 201805082106
 * @formatter Oviedo Computing Community
 */
public class ForemanServiceImpl implements ForemanService {

	@Override
	public void createClient( String dni, String name, String surname, int zipCode,
			int phoneNumber, String email ) throws BusinessException {

		CreateClient create = new CreateClient( dni, name, surname, zipCode, phoneNumber, email );
		create.execute();

	}

	@Override
	public void createRecommendedClient( String dni, String name, String surname, int zipCode,
			int phoneNumber, String email, long recomenderId ) throws BusinessException {

		CreateRecommendedClient create = new CreateRecommendedClient( dni, name, surname,
				zipCode, phoneNumber, email, recomenderId );
		create.execute();

	}

	@Override
	public List<Map<String, Object>> findAllClients() throws BusinessException {
		FindAllClients find = new FindAllClients();
		return find.execute();
	}

	@Override
	public List<Map<String, Object>> findAllClientsByRecomendator( Long recommenderId )
			throws BusinessException {

		FindClientsByRecommender find = new FindClientsByRecommender( recommenderId );
		return find.execute();
	}

	@Override
	public String findByClientId( Long clientId ) throws BusinessException {

		FindClient find = new FindClient( clientId );
		return find.execute();
	}

	@Override
	public void removeClient( Long clientId ) throws BusinessException {
		RemoveClient remove = new RemoveClient( clientId );
		remove.execute();

	}

	@Override
	public void updateClient( long clientId, String dni, String name, String surname,
			int zipCode, int phoneNumber, String email ) throws BusinessException {

		UpdateClient update = new UpdateClient( clientId, dni, name, surname, zipCode, phoneNumber,
				email );
		update.execute();

	}

}
