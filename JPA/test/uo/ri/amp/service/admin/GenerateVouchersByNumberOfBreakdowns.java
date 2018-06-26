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
package uo.ri.amp.service.admin;

import static org.junit.Assert.assertTrue;
import static uo.ri.amp.service.util.FixtureRepository.registerNewBreakdownFor;
import static uo.ri.amp.service.util.FixtureRepository.registerNewClient;
import static uo.ri.amp.service.util.FixtureRepository.registerNewInvoicedBreakdownFor;
import static uo.ri.amp.service.util.FixtureRepository.registerNewVehicleFor;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uo.ri.amp.service.util.BaseServiceTests;
import uo.ri.business.AdminService;
import uo.ri.conf.Factory;
import uo.ri.model.Averia;
import uo.ri.model.Bono;
import uo.ri.model.Cliente;
import uo.ri.model.Vehiculo;
import uo.ri.util.exception.BusinessException;

/**
 * The Class GenerateVouchersByNumberOfBreakdowns.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class GenerateVouchersByNumberOfBreakdowns extends BaseServiceTests {

    /**
     * Assert are marked.
     *
     * @param avs
     *            the avs
     * @param value
     *            the value
     */
    private void assertAreMarked(List<Averia> avs, int value) {
	long counter = avs.stream().filter(a -> a.isUsadaBono3()).count();
	assertTrue(counter == value);
    }

    /**
     * Assert right voucher.
     *
     * @param c
     *            the c
     * @param expected
     *            the expected
     */
    private void assertRightVoucher(Cliente c, Bono expected) {
	assertTrue(expected.getAcumulado() == 0.0);
	assertTrue(expected.getCargos().size() == 0.0);
	assertTrue(expected.getCliente().equals(c));
	assertTrue(expected.getDescripcion().equals("Por tres averías"));
	assertTrue(expected.getDisponible() == 20.0 /* € */ );
    }

    /**
     * Generate breakdowns.
     *
     * @param invoiced
     *            the invoiced
     * @param notInvoiced
     *            the not invoiced
     * @return the list
     * @throws BusinessException
     *             the business exception
     */
    private List<Averia> generateBreakdowns(int invoiced, int notInvoiced)
	    throws BusinessException {

	Cliente c = registerNewClient();
	Vehiculo v = registerNewVehicleFor(c);
	List<Averia> res = generateInvoicedBreakdownsFor(v, invoiced);
	res.addAll(generateNotInvoicedBreakDownsFor(v, notInvoiced));
	return res;
    }

    /**
     * Generate invoiced breakdowns for.
     *
     * @param v
     *            the v
     * @param counter
     *            the counter
     * @return the list
     * @throws BusinessException
     *             the business exception
     */
    private List<Averia> generateInvoicedBreakdownsFor(Vehiculo v, int counter)
	    throws BusinessException {
	List<Averia> avs = new ArrayList<>();
	for (int i = 0; i < counter; i++) {
	    avs.add(registerNewInvoicedBreakdownFor(v));
	}
	return avs;
    }

    /**
     * Generate not invoiced break downs for.
     *
     * @param v
     *            the v
     * @param counter
     *            the counter
     * @return the list
     */
    private List<Averia> generateNotInvoicedBreakDownsFor(Vehiculo v,
	    int counter) {
	List<Averia> avs = new ArrayList<>();
	for (int i = 0; i < counter; i++) {
	    avs.add(registerNewBreakdownFor(v));
	}
	return avs;
    }

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Se generan n bonos con n*3 + 1 averías, y se marcan como usadas n*3
     * averias.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testGeneralizedVoucherGenerated() throws BusinessException {
	int N = 10;

	List<Averia> avs = generateBreakdowns(N * 3 + 1, 0);

	AdminService svc = Factory.service.forAdmin();
	int qty = svc.generateVouchers();

	assertTrue(qty == N);
	assertAreMarked(avs, N * 3);
    }

    /**
     * No se genera bono con dos averías y éstas no quedan marcadas.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testNoVoucherGenerated() throws BusinessException {
	Cliente c = registerNewClient();
	Vehiculo v = registerNewVehicleFor(c);
	Averia a1 = registerNewInvoicedBreakdownFor(v);
	Averia a2 = registerNewInvoicedBreakdownFor(v);

	AdminService svc = Factory.service.forAdmin();
	int qty = svc.generateVouchers();

	assertTrue(qty == 0);
	assertTrue(!a1.isUsadaBono3());
	assertTrue(!a2.isUsadaBono3());
    }

    /**
     * No se genera bono si el cliente tiene tres averías y una ya está usada.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testNoVoucherGeneratedWithOneMarked() throws BusinessException {
	Cliente c = registerNewClient();
	Vehiculo v = registerNewVehicleFor(c);
	Averia a1 = registerNewInvoicedBreakdownFor(v);
	Averia a2 = registerNewInvoicedBreakdownFor(v);
	Averia a3 = registerNewInvoicedBreakdownFor(v);

	a1.markAsBono3Used();

	AdminService svc = Factory.service.forAdmin();
	int qty = svc.generateVouchers();

	assertTrue(qty == 0);
	assertTrue(a1.isUsadaBono3());
	assertTrue(!a2.isUsadaBono3());
	assertTrue(!a3.isUsadaBono3());
    }

    /**
     * No se genera bono con tres averias si una de ellas no está pagada.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testNoVoucherGeneratedWithOneNoSettled()
	    throws BusinessException {
	Cliente c = registerNewClient();
	Vehiculo v = registerNewVehicleFor(c);
	Averia a1 = registerNewInvoicedBreakdownFor(v);
	Averia a2 = registerNewInvoicedBreakdownFor(v);
	Averia a3 = registerNewBreakdownFor(v); // <-- no pagada

	AdminService svc = Factory.service.forAdmin();
	int qty = svc.generateVouchers();

	assertTrue(qty == 0);
	assertTrue(!a1.isUsadaBono3());
	assertTrue(!a2.isUsadaBono3());
	assertTrue(!a3.isUsadaBono3());
    }

    /**
     * Se genera un bono por tres averias facturadas, con factura pagada y no
     * usadas previamente. Las averias quedan marcadas como usadas.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testOneVoucherGenerated() throws BusinessException {
	Cliente c = registerNewClient();
	Vehiculo v = registerNewVehicleFor(c);
	Averia a1 = registerNewInvoicedBreakdownFor(v);
	Averia a2 = registerNewInvoicedBreakdownFor(v);
	Averia a3 = registerNewInvoicedBreakdownFor(v);

	AdminService svc = Factory.service.forAdmin();
	int qty = svc.generateVouchers();

	assertTrue(qty == 1);
	assertTrue(a1.isUsadaBono3());
	assertTrue(a2.isUsadaBono3());
	assertTrue(a3.isUsadaBono3());

	Bono b = getFirstVoucher(c);
	assertRightVoucher(c, b);
    }

    /**
     * Si el cliente tiene 5 averias, se genera un bono y se marcan 3 como
     * usadas.
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testOneVoucherGeneratedWithFiveUsedThree()
	    throws BusinessException {

	List<Averia> avs = generateBreakdowns(5, 0);

	AdminService svc = Factory.service.forAdmin();
	int qty = svc.generateVouchers();

	assertTrue(qty == 1);
	assertAreMarked(avs, 3);

	Cliente c = avs.get(0).getVehiculo().getCliente();
	Bono b = getFirstVoucher(c);
	assertRightVoucher(c, b);
    }

    /**
     * Con tres clientes, se genera bono para dos. Cada cliente tienes tres
     * averias, pero hay una sin pagar
     *
     * @throws BusinessException
     *             the business exception
     */
    @Test
    public void testThreeClientsTwoVouchersGenerated()
	    throws BusinessException {
	List<Averia> avs1 = generateBreakdowns(3, 0);
	List<Averia> avs2 = generateBreakdowns(3, 0);
	List<Averia> avs3 = generateBreakdowns(2, 1); // <-- queda una no pagada

	AdminService svc = Factory.service.forAdmin();
	int qty = svc.generateVouchers();

	assertTrue(qty == 2);
	assertAreMarked(avs1, 3);
	assertAreMarked(avs2, 3);
	assertAreMarked(avs3, 0); // <-- none of then is marked
    }

}
