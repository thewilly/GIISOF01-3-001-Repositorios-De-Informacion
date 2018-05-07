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

import uo.ri.persistence.AveriasGateway;
import uo.ri.persistence.BonosGateway;
import uo.ri.persistence.ClientesGateway;
import uo.ri.persistence.FacturasGateway;
import uo.ri.persistence.MecanicosGateway;
import uo.ri.persistence.MediospagoGateway;
import uo.ri.persistence.impl.AveriasGatewayImpl;
import uo.ri.persistence.impl.BonosGatewayImpl;
import uo.ri.persistence.impl.ClientesGatewayImpl;
import uo.ri.persistence.impl.FacturasGatewayImpl;
import uo.ri.persistence.impl.MecanicosGatewayImpl;
import uo.ri.persistence.impl.MediospagoGatewayImp;

public class PersistenceFactory {

	public PersistenceFactory() {}

	public static MecanicosGateway getMecanicosGateway() {
		return new MecanicosGatewayImpl();
	}

	public static FacturasGateway getFacturasGateway() {
		return new FacturasGatewayImpl();
	}

	public static AveriasGateway getAveriasGateway() {
		return new AveriasGatewayImpl();
	}

	public static ClientesGateway getClientesGateway() {
		return new ClientesGatewayImpl();
	}

	public static BonosGateway getBonosGateway() {
		return new BonosGatewayImpl();
	}

	public static MediospagoGateway getMediospagoGateway() {
		return new MediospagoGatewayImp();
	}

}
