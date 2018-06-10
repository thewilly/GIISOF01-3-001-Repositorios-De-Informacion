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
package uo.ri.business.impl.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import uo.ri.business.dto.FailureDto;
import uo.ri.business.dto.CardDto;
import uo.ri.business.dto.CashDto;
import uo.ri.business.dto.ClientDto;
import uo.ri.business.dto.InvoiceDto;
import uo.ri.business.dto.MechanicDto;
import uo.ri.business.dto.PaymentMeanDto;
import uo.ri.business.dto.VoucherDto;
import uo.ri.model.Averia;
import uo.ri.model.Bono;
import uo.ri.model.Cliente;
import uo.ri.model.Factura;
import uo.ri.model.Mecanico;
import uo.ri.model.MedioPago;
import uo.ri.model.Metalico;
import uo.ri.model.TarjetaCredito;
import uo.ri.model.types.Address;

/**
 * DtoAssembler.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class DtoAssembler {

	/**
	 * This method transforms a list of averia into a list of breakdown dto.
	 *
	 * @param list the list of averia to be transformed
	 * @return the list of breakdown dto
	 */
	public static List<FailureDto> toBreakdownDtoList(List<Averia> list) {
		return list.stream().map(a -> toDto(a)).collect(Collectors.toList());
	}

	/**
	 * This method transforms a list of clients to a list of client dto.
	 *
	 * @param clientes the list of client
	 * @return the list of client dto
	 */
	public static List<ClientDto> toClientDtoList(List<Cliente> clientes) {
		List<ClientDto> res = new ArrayList<>();
		for (Cliente c : clientes) {
			res.add(toDto(c));
		}
		return res;
	}

	/**
	 * This method transforms an averia into a breakdown dto.
	 *
	 * @param a the averia to be transformed
	 * @return the breakdown dto
	 */
	public static FailureDto toDto(Averia a) {
		FailureDto dto = new FailureDto();
		dto.id = a.getId();
		dto.invoiceId = a.getFactura().getId();
		dto.vehicleId = a.getVehiculo().getId();
		dto.description = a.getDescripcion();
		dto.date = a.getFecha();
		dto.total = a.getImporte();
		dto.status = a.getStatus().toString();
		return dto;
	}

	/**
	 * This method transforms a voucher into a voucher dto.
	 *
	 * @param b the voucher to be transformed
	 * @return the voucher dto
	 */
	public static VoucherDto toDto(Bono b) {
		VoucherDto dto = new VoucherDto();
		dto.id = b.getId();
		dto.clientId = b.getCliente().getId();
		dto.accumulated = b.getAcumulado();
		dto.code = b.getCodigo();
		dto.description = b.getDescripcion();
		dto.available = b.getDisponible();
		return dto;
	}

	/**
	 * This method transforms a Client, into a Client dto.
	 *
	 * @param c the client
	 * @return the client dto
	 */
	public static ClientDto toDto(Cliente c) {
		ClientDto dto = new ClientDto();

		dto.id = c.getId();
		dto.dni = c.getDni();
		dto.name = c.getNombre();
		dto.surname = c.getApellidos();
		dto.phone = c.getPhone();
		dto.email = c.getEmail();
		dto.addressStreet = c.getAddress().getStreet();
		dto.addressCity = c.getAddress().getCity();
		dto.addressZipcode = c.getAddress().getZipcode();

		return dto;
	}

	/**
	 * This method transforms the factura object from the model into a invoice
	 * dto.
	 *
	 * @param factura the factura to be transformed
	 * @return the invice dto
	 */
	public static InvoiceDto toDto(Factura factura) {
		InvoiceDto dto = new InvoiceDto();
		dto.id = factura.getId();
		dto.number = factura.getNumero();
		dto.date = factura.getFecha();
		dto.total = factura.getImporte();
		dto.taxes = factura.getIva();
		dto.status = factura.getStatus().toString();
		return dto;
	}

	/**
	 * This method transforms a mechanic into a mechanic dto.
	 *
	 * @param m the mechanic
	 * @return the mechanic dto
	 */
	public static MechanicDto toDto(Mecanico m) {
		MechanicDto dto = new MechanicDto();
		dto.id = m.getId();
		dto.dni = m.getDni();
		dto.name = m.getNombre();
		dto.surname = m.getApellidos();
		return dto;
	}

	/**
	 * This method transforms a medioPago into a payment mean dto.
	 *
	 * @param mp the medioPago to be transformed
	 * @return the payment mean dto
	 */
	private static PaymentMeanDto toDto(MedioPago mp) {
		if (mp instanceof Bono) {
			return toDto((Bono) mp);
		} else if (mp instanceof TarjetaCredito) {
			return toDto((TarjetaCredito) mp);
		} else if (mp instanceof Metalico) {
			return toDto((Metalico) mp);
		} else {
			throw new RuntimeException("Unexpected type of payment mean");
		}
	}

	/**
	 * This method transforms a metalico object into a cash dto.
	 *
	 * @param m the metalico to be transformed
	 * @return the cash dto
	 */
	public static CashDto toDto(Metalico m) {
		CashDto dto = new CashDto();
		dto.id = m.getId();
		dto.clientId = m.getCliente().getId();
		dto.accumulated = m.getAcumulado();
		return dto;
	}

	/**
	 * This method transforms a credit card object from the model into a card
	 * dto.
	 *
	 * @param tc the credit card to be transformed
	 * @return the card dto
	 */
	public static CardDto toDto(TarjetaCredito tc) {
		CardDto dto = new CardDto();
		dto.id = tc.getId();
		dto.clientId = tc.getCliente().getId();
		dto.accumulated = tc.getAcumulado();
		dto.cardNumber = tc.getNumero();
		dto.cardExpirationDate = tc.getValidez();
		dto.cardType = tc.getTipo();
		return dto;
	}

	/**
	 * This method transforms the dto that receives as a parameter to an object
	 * TarjetaCredito.
	 *
	 * @param card the dto to be transformed
	 * @return the TarjetaCredito
	 */
	public static TarjetaCredito toEntity(CardDto card) {
		TarjetaCredito t = new TarjetaCredito(card.cardNumber);
		t.setTipo(card.cardType);
		t.setValidez(card.cardExpirationDate);
		return t;
	}

	/**
	 * This method transforms a client dto into a client.
	 *
	 * @param dto the dto to be transformed
	 * @return the client
	 */
	public static Cliente toEntity(ClientDto dto) {
		Cliente c = new Cliente(dto.dni, dto.name, dto.surname);
		Address addr = new Address(dto.addressStreet, dto.addressCity, dto.addressZipcode);
		c.setAddress(addr);
		c.setEmail(dto.email);
		c.setPhone(dto.phone);
		return c;
	}

	/**
	 * This method transforms a mechanic dto into a mechanic.
	 *
	 * @param dto the dto to be transformed
	 * @return the mechanic
	 */
	public static Mecanico toEntity(MechanicDto dto) {
		return new Mecanico(dto.dni, dto.name, dto.surname);
	}

	/**
	 * This method transforms a voucher dto into a Bono object.
	 *
	 * @param voucher the voucherdto to be transform
	 * @return the Bono object
	 */
	public static Bono toEntity(VoucherDto voucher) {
		Bono b = new Bono(voucher.code, voucher.available);
		b.setDescripcion(voucher.description);
		return b;
	}

	/**
	 * This method transforms a list of mechanic into a list of mechanic dto.
	 *
	 * @param list the list of mechanic
	 * @return the list of mechanic dto
	 */
	public static List<MechanicDto> toMechanicDtoList(List<Mecanico> list) {
		List<MechanicDto> res = new ArrayList<>();
		for (Mecanico m : list) {
			res.add(toDto(m));
		}
		return res;
	}

	/**
	 * This method transforms a list of medio pago, into a list of payment mean
	 * dto.
	 *
	 * @param list the list of medioPago to be transformed
	 * @return the list of payment mean dto
	 */
	public static List<PaymentMeanDto> toPaymentMeanDtoList(List<MedioPago> list) {
		return list.stream().map(mp -> toDto(mp)).collect(Collectors.toList());
	}

	/**
	 * This method transforms a list of voucher into a list of voucher dto.
	 *
	 * @param list the list of voucher
	 * @return the list of voucher dto
	 */
	public static List<VoucherDto> toVoucherDtoList(List<Bono> list) {
		List<VoucherDto> res = new ArrayList<>();
		for (Bono b : list) {
			res.add(toDto(b));
		}
		return res;
	}
}
