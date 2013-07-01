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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.wsmo.common.exception.InvalidModelException;

/**
 *
 * @author Murilo Honorio
 */
public class GroundingDadosOLDTest {
    
    Scanner sc;
    
    public GroundingDadosOLDTest() {
    }
    
    /**
     * Cria o objeto GroundingDadosOLD e invoca o metodo gerar, que gera um documento WSML a
     * partir do documento WSDL fornecido como entrada.
     *
     * @throws FileNotFoundException
     */
    @BeforeClass
    public static void setUpClass() throws FileNotFoundException, UnsupportedEncodingException, IOException, InvalidModelException {
        //GroundingDados gd = new GroundingDadosOLD();
        
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
    
    @Test
    public void deveGerarListaDeElementos() {
        GroundingDadosOLD gd = new GroundingDadosOLD();
        ArrayList listaElementos = gd.gerarListaDeOperacoes();
        System.out.println(listaElementos.toString());
        assertTrue(true);
    }
}
