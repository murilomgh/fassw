/*
 * Copyright (C) 2013 Murilo Honorio
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fassw;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Murilo Honorio
 */
public class MapeadorTest {
    
    public MapeadorTest() {
    }

    /**
     * Test of executar method, of class Mapeador.
     */
    @Test
    public void testExecutar() throws Exception {
        System.out.println("executar");
        Mapeador instance = new Mapeador(null, null);
        boolean expResult = true;
        boolean result = instance.executar();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
