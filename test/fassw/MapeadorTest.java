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
        Mapeador mapeador = new Mapeador("Z:\\git-repo\\fassw\\testes\\exemplo\\servicoExemplo20.wsdl", ".\\testes\\exemplo\\servicoExemplo20.wsml", true);
        boolean resultadoEsperado = true;
        boolean result = mapeador.executar();
        assertEquals(resultadoEsperado, result);
    }
}
