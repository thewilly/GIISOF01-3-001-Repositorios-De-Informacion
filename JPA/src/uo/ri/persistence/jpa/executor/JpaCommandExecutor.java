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
package uo.ri.persistence.jpa.executor;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import uo.ri.business.impl.Command;
import uo.ri.business.impl.CommandExecutor;
import uo.ri.persistence.jpa.util.Jpa;
import uo.ri.util.exception.BusinessException;

/**
 * The Class JpaCommandExecutor.
 *
 * @author Guillermo Facundo Colunga
 * @version 201806081225
 */
public class JpaCommandExecutor implements CommandExecutor {

    /*
     * (non-Javadoc)
     * 
     * @see
     * uo.ri.business.impl.CommandExecutor#execute(uo.ri.business.impl.Command)
     */
    @Override
    public <T> T execute(Command<T> cmd) throws BusinessException {
	EntityManager mapper = Jpa.createEntityManager();
	EntityTransaction trx = mapper.getTransaction();
	trx.begin();

	T res;
	try {
	    res = cmd.execute();
	    trx.commit();

	} catch (BusinessException | PersistenceException ex) {
	    if (trx.isActive()) {
		trx.rollback();
	    }
	    throw ex;
	} finally {
	    if (mapper.isOpen()) {
		mapper.close();
	    }
	}

	return res;
    }
}
