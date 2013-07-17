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
package old;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import old.GroundingDados;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import static org.junit.Assert.*;

/**
 *
 * @author Murilo Honorio
 */
public class GroundingDadosTest {
    
    GroundingDados groundingDados;
    
    public GroundingDadosTest() {
        groundingDados = new GroundingDados(null, null);
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
     * Test of processar method, of class GroundingDados.
     */
    @Test
    public void testProcessar() {
        System.out.println("processar");
    }
    
    /**
     * Teste do metodo de mapeamento do elemento attributeGroup
     */
    @Test
    public void testAttributeGroup() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document documento = db.parse(new File(".\\testes\\attributeGroup.xsd"));
        Node raiz = documento.getElementsByTagName("attributeGroup").item(0);
        
        String esperado = "";
        String retornado = groundingDados.transformarAttributeGroup(raiz);
        retornado = "";
        
        assertEquals(esperado, retornado);
    }
}
