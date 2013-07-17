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
package fassw.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Murilo Honorio
 */
public class AnalisadorTest {
    
    public AnalisadorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of identificarVersao method, of class Analisador.
     */
    @Test
    public void testIdentificarVersao20() {
        System.out.println("Identificar Versao 2.0");
        String entrada = ".\\testes\\analisador\\servico20.wsdl";
        boolean linhaDeComando = true;
        boolean esperado = true;
        boolean result = Analisador.identificarVersao(entrada, linhaDeComando);
        assertEquals(esperado, result);
    }
    
    /**
     * Test of identificarVersao method, of class Analisador.
     */
    @Test
    public void testIdentificarVersao11() {
        System.out.println("Identificar Versao 1.1");
        String entrada = ".\\testes\\analisador\\servico11.wsdl";
        boolean linhaDeComando = true;
        boolean esperado = false; //equivale a WSDL 1.1
        boolean result = Analisador.identificarVersao(entrada, linhaDeComando);
        assertEquals(esperado, result);
    }

    /**
     * Test of analisarArquivo method, of class Analisador.
     */
    @Test
    public void testAnalisarArquivo() {
        System.out.println("Analisar Arquivo Sintaxe Errada");
        String caminho = ".\\testes\\analisador\\sintaxeErrada.wsdl";
        boolean isLinhaDeComando = true;
        boolean resultado = false;
        Analisador a = new Analisador();
        boolean result = a.analisarArquivo(caminho, isLinhaDeComando);
        assertEquals(resultado, result);
    }
}
