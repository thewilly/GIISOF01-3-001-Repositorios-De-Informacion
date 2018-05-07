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
package uo.ri.business.impl.cash;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alb.util.date.DateUtil;
import alb.util.jdbc.Jdbc;
import alb.util.math.Round;
import uo.ri.common.BusinessException;
import uo.ri.conf.PersistenceFactory;
import uo.ri.persistence.AveriasGateway;
import uo.ri.persistence.FacturasGateway;

public class CreateInvoiceFor {

	private Connection connection;

	private List<Long> idsAveria;

	public CreateInvoiceFor( List<Long> lista ) {
		this.idsAveria = lista;
	}

	public Map<String, Object> execute() throws BusinessException {
		Map<String, Object> factura = new HashMap<String, Object>();
		try {
			connection = Jdbc.getConnection();
			connection.setAutoCommit( false );

			verificarAveriasTerminadas( idsAveria );

			long numeroFactura = generarNuevoNumeroFactura();
			Date fechaFactura = DateUtil.today();
			double totalFactura = calcularImportesAverias( idsAveria );
			double iva = porcentajeIva( totalFactura, fechaFactura );
			double importe = totalFactura * ( 1 + iva / 100 );
			importe = Round.twoCents( importe );

			long idFactura = crearFactura( numeroFactura, fechaFactura, iva, importe );
			vincularAveriasConFactura( idFactura, idsAveria );
			cambiarEstadoAverias( idsAveria, "FACTURADA" );

			factura.put( "numeroFactura", numeroFactura );
			factura.put( "fechaFactura", fechaFactura );
			factura.put( "totalFactura", totalFactura );
			factura.put( "iva", iva );
			factura.put( "importe", importe );

			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException ex) {
			}
			;
			throw new RuntimeException( e );
		} catch (BusinessException e) {
			try {
				connection.rollback();
			} catch (SQLException ex) {
			}
			;
			throw e;
		} finally {
			Jdbc.close( connection );
		}
		return factura;

	}

	private long crearFactura( long numeroFactura, Date fechaFactura, double iva,
			double totalConIva )
			throws BusinessException {

		FacturasGateway fGate = PersistenceFactory.getFacturasGateway();
		fGate.setConnection( connection );

		fGate.save( numeroFactura, fechaFactura, iva, totalConIva );

		return getGeneratedKey( numeroFactura ); // Id de la nueva factura
													// generada

	}

	private Long generarNuevoNumeroFactura() throws BusinessException {

		FacturasGateway fGate = PersistenceFactory.getFacturasGateway();
		fGate.setConnection( connection );

		return fGate.ultimoNumeroFactura();
	}

	private void verificarAveriasTerminadas( List<Long> idsAveria ) throws BusinessException {

		AveriasGateway aGate = PersistenceFactory.getAveriasGateway();
		aGate.setConnection( connection );

		aGate.verificarEstadoAveria( idsAveria );

	}

	private void cambiarEstadoAverias( List<Long> idsAveria, String status )
			throws BusinessException {

		AveriasGateway aGate = PersistenceFactory.getAveriasGateway();
		aGate.setConnection( connection );

		aGate.actualizarEstadoAveria( idsAveria, status );

	}

	private void vincularAveriasConFactura( long idFactura, List<Long> idsAveria )
			throws SQLException, BusinessException {

		FacturasGateway fGate = PersistenceFactory.getFacturasGateway();
		fGate.setConnection( connection );

		fGate.vincularAveriaFactura( idFactura, idsAveria );
	}

	private long getGeneratedKey( long numeroFactura ) throws BusinessException {

		FacturasGateway fGate = PersistenceFactory.getFacturasGateway();
		fGate.setConnection( connection );

		return fGate.recuperarClaveGenerada( numeroFactura );
	}

	private void actualizarImporteAveria( Long idAveria, double totalAveria )
			throws BusinessException {

		AveriasGateway aGate = PersistenceFactory.getAveriasGateway();
		aGate.setConnection( connection );

		aGate.actualizarImporteAveria( idAveria, totalAveria );

	}

	private double consultaImporteRepuestos( Long idAveria ) throws BusinessException {

		AveriasGateway aGate = PersistenceFactory.getAveriasGateway();
		aGate.setConnection( connection );

		return aGate.importeRepuestos( idAveria );
	}

	private double consultaImporteManoObra( Long idAveria ) throws BusinessException {

		AveriasGateway aGate = PersistenceFactory.getAveriasGateway();
		aGate.setConnection( connection );

		return aGate.importeManoObra( idAveria );

	}

	private double porcentajeIva( double totalFactura, Date fechaFactura ) {
		return DateUtil.fromString( "1/7/2012" ).before( fechaFactura ) ? 21.0 : 18.0;
	}

	protected double calcularImportesAverias( List<Long> idsAveria ) throws BusinessException {

		double totalFactura = 0.0;
		for (Long idAveria : idsAveria) {
			double importeManoObra = consultaImporteManoObra( idAveria );
			double importeRepuestos = consultaImporteRepuestos( idAveria );
			double totalAveria = importeManoObra + importeRepuestos;

			actualizarImporteAveria( idAveria, totalAveria );

			totalFactura += totalAveria;
		}
		return totalFactura;
	}

}
