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
package uo.ri.business.impl.cash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import alb.util.random.Random;
import uo.ri.business.dto.VoucherDto;
import uo.ri.business.impl.Command;
import uo.ri.business.impl.util.DtoAssembler;
import uo.ri.business.repository.ClienteRepository;
import uo.ri.business.repository.MedioPagoRepository;
import uo.ri.conf.Factory;
import uo.ri.model.Association;
import uo.ri.model.Bono;
import uo.ri.model.Cliente;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.Check;

/**
 * CreateMPBond.java
 *
 * @author Guillermo Facundo Colunga
 * @version 201806032143
 * @since 201806032143
 * @formatter Oviedo Computing Community
 */
public class CreateMPBond implements Command<Void> {

    /** The voucher. */
    private VoucherDto voucher;

    /** The mppayment methods repository. */
    private MedioPagoRepository paymentMethodsRepository = Factory.repository
	    .forMedioPago();

    /** The clients repository. */
    private ClienteRepository clientsRepository = Factory.repository
	    .forCliente();

    /**
     * Instantiates a new creates the MP bond.
     *
     * @param voucher
     *            the voucher
     */
    public CreateMPBond(VoucherDto voucher) {
	this.voucher = voucher;
    }

    /**
     * This method checks that the client id corresponds to a real client in the
     * system.
     *
     * @param clientId
     *            the id of the client
     * @throws BusinessException
     *             the business exception
     */
    private void assertExistenceClient(Long clientId) throws BusinessException {
	Cliente c = clientsRepository.findById(clientId);
	Check.isNotNull(c, "Ese cliente no existe");
    }

    /*
     * (non-Javadoc)
     * 
     * @see uo.ri.business.impl.Command#execute()
     */
    public Void execute() throws BusinessException {
	Cliente cliente = clientsRepository.findById(voucher.clientId);
	voucher.code = generateBondCode();
	Bono b = DtoAssembler.toEntity(voucher);
	assertExistenceClient(voucher.clientId);
	Association.Pagar.link(cliente, b);
	paymentMethodsRepository.add(b);
	return null;
    }

    /**
     * This method generates a new voucher code that does not exists in the
     * system.
     *
     * @return the new code to be added
     */
    private String generateBondCode() {
	String code = "";
	Bono bond = null;
	do {
	    try {
		byte[] hashArray = MessageDigest.getInstance("MD5")
			.digest(Random.string(30).getBytes());
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < hashArray.length; i++) {
		    buffer.append(
			    Integer.toString((hashArray[i] & 0xff) + 0x100, 16)
				    .substring(1));
		}
		code = "B-" + buffer.toString();

	    } catch (NoSuchAlgorithmException e) {
		// Fall back method in case of error with the algorithm
		code = "B-" + Random.string(5) + "-" + Random.integer(0, 9999);
	    }
	    bond = paymentMethodsRepository.findVoucherByCode(code);
	} while (bond != null);
	return code;
    }

}
