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
import uo.ri.business.impl.foreman.AddClient;
import uo.ri.business.impl.foreman.AddClientWithRecomendator;
import uo.ri.business.impl.foreman.FindAllClients;
import uo.ri.business.impl.foreman.FindAllClientsByRecomendator;
import uo.ri.business.impl.foreman.DeleteClient;
import uo.ri.business.impl.foreman.ShowClientDetail;
import uo.ri.business.impl.foreman.UpdateClient;
import uo.ri.common.BusinessException;

public class ForemanServiceImpl implements ForemanService {

	@Override
	public void addClient( String dni, String nombre, String apellidos, int zipcode, int telefono,
			String correo )
			throws BusinessException {

		AddClient a = new AddClient( dni, nombre, apellidos, zipcode, telefono, correo );
		a.execute();

	}

	@Override
	public void addClientWithRecomendator( String dni, String nombre, String apellidos, int zipcode,
			int telefono,
			String correo, long idRecomendador ) throws BusinessException {

		AddClientWithRecomendator aR = new AddClientWithRecomendator( dni, nombre, apellidos,
				zipcode, telefono, correo,
				idRecomendador );
		aR.execute();

	}

	@Override
	public void deleteClient( Long idClient ) throws BusinessException {

		DeleteClient r = new DeleteClient( idClient );
		r.execute();

	}

	@Override
	public List<Map<String, Object>> findAllClients() throws BusinessException {

		FindAllClients f = new FindAllClients();
		return f.execute();
	}

	@Override
	public List<Map<String, Object>> findAllClientsByRecomendator( Long idRecomendador )
			throws BusinessException {

		FindAllClientsByRecomendator fR = new FindAllClientsByRecomendator( idRecomendador );
		return fR.execute();
	}

	@Override
	public void updateClient( long idClient, String dni, String nombre, String apellidos,
			int zipcode, int telefono,
			String correo ) throws BusinessException {

		UpdateClient u = new UpdateClient( idClient, dni, nombre, apellidos, zipcode, telefono,
				correo );
		u.execute();

	}

	@Override
	public String showDetailClient( Long idClient ) throws BusinessException {

		ShowClientDetail s = new ShowClientDetail( idClient );
		return s.execute();
	}

}
