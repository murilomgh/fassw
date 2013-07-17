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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * As classes de teste realizam a comparacao entre os arquivos gerados e os esperados como retorno.
 * 
 * @author Murilo Honorio
 */
public class GroundingDadosTest {
    
    private PrintWriter pw;
    private Document documento;
    private DocumentBuilder db;
    private String xmlns = "http://www.w3.org/2001/XMLSchema";
    private File esperado;
    private File resultado;
    
    
    public GroundingDadosTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws ParserConfigurationException, FileNotFoundException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        db = dbf.newDocumentBuilder();
        resultado = new File(".\\testes\\resultado.temp");
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Os testes consistem em ler um documento contendo exemplos, gerar a saida e comparar com a
     * saida esperada.
     * 
     * @throws Exception 
     */
    @Test
    public void testMapearAnnotation() throws Exception {
        System.out.println("mapearAnnotation");
        System.out.println("================");
        //carregar arquivo de origem
        documento = db.parse(new File(".\\testes\\annotation.xsd"));
        esperado = new File(".\\testes\\annotation.wsml");        
        pw = new PrintWriter(resultado);
        
        GroundingDados gd = new GroundingDados(xmlns);
        gd.resetContador();
        Node schema = documento.getElementsByTagNameNS(xmlns, "schema").item(0);
        List<Node> elementos = new ArrayList<>();
        //percorrer os elementos filhos de schema
        for (int i = 0; i < schema.getChildNodes().getLength(); i++) {
            Node item = schema.getChildNodes().item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE && 
                    item.getLocalName().equals("annotation")) {
                elementos.add(item);
            }
        }
        System.out.println("Numero de annotation: " + elementos.size());
        System.out.println("---------------------------");
        String saida = "";
        for (Node elemento : elementos) {
            saida = saida + gd.mapearAnnotation(elemento) + "\n";
        }
        System.out.println(saida);
        FileUtils.write(resultado, saida);
        pw.close();
        
        assertEquals(FileUtils.readFileToString(esperado, "utf-8"), FileUtils.readFileToString(resultado, "utf-8"));
    }

    /**
     * Os testes consistem em ler um documento contendo exemplos, gerar a saida e comparar com a
     * saida esperada.
     * Todo arquivo de teste possui apenas um schema e os filhos do schema sao sempre do mesmo tipo.
     * 
     * @throws Exception 
     */
    @Test
    public void testMapearAttributeGroup() throws Exception {
        System.out.println("mapearAttributeGroup");
        System.out.println("====================");
        //carregar arquivo de origem
        documento = db.parse(new File(".\\testes\\attributeGroup.xsd"));
        esperado = new File(".\\testes\\attributeGroup.wsml");        
        pw = new PrintWriter(resultado);
        
        GroundingDados gd = new GroundingDados(xmlns);
        gd.resetContador();
        Node schema = documento.getElementsByTagNameNS(xmlns, "schema").item(0);
        List<Node> elementos = new ArrayList<>();
        //percorrer os elementos filhos de schema
        for (int i = 0; i < schema.getChildNodes().getLength(); i++) {
            Node item = schema.getChildNodes().item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE && 
                    item.getLocalName().equals("attributeGroup")) {
                elementos.add(item);
            }
        }
        System.out.println("Numero de attributeGroup: " + elementos.size());
        System.out.println("---------------------------");
        String saida = "";
        for (Node elemento : elementos) {
            saida = saida + gd.mapearAttributeGroup(elemento);
        }
        System.out.println(saida);
        FileUtils.write(resultado, saida);
        pw.close();
        
        assertEquals(FileUtils.readFileToString(esperado, "utf-8"), FileUtils.readFileToString(resultado, "utf-8"));
    }
    
    /**
     * Os testes consistem em ler um documento contendo exemplos, gerar a saida e comparar com a
     * saida esperada.
     * 
     * @throws Exception 
     */
    @Test
    public void testMapearAttribute() throws Exception {
        System.out.println("mapearAttribute");
        System.out.println("===============");
        //carregar arquivo de origem
        documento = db.parse(new File(".\\testes\\attribute.xsd"));
        esperado = new File(".\\testes\\attribute.wsml");        
        //pw = new PrintWriter(resultado);
        
        GroundingDados gd = new GroundingDados(xmlns);
        gd.resetContador();
        Node schema = documento.getElementsByTagNameNS(xmlns, "schema").item(0);
        List<Node> elementos = new ArrayList<>();
        //percorrer os elementos filhos de schema
        for (int i = 0; i < schema.getChildNodes().getLength(); i++) {
            Node item = schema.getChildNodes().item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE && 
                    item.getLocalName().equals("attribute")) {
                elementos.add(item);
            }
        }
        System.out.println("Numero de attribute: " + elementos.size());
        System.out.println("---------------------------");
        String saida = "";
        for (Node elemento : elementos) {
            saida = saida + gd.mapearAttribute(elemento);
        }
        System.out.println(saida);
        FileUtils.write(resultado, saida);
        //pw.close();
        
        assertEquals(FileUtils.readFileToString(esperado, "utf-8"), FileUtils.readFileToString(resultado, "utf-8"));
    }
    
    /**
     * Os testes consistem em ler um documento contendo exemplos, gerar a saida e comparar com a
     * saida esperada.
     * 
     * @throws Exception 
     */
    @Test
    public void testMapearSimpleType() throws Exception {
        System.out.println("MapearSimpleType");
        System.out.println("================");
        //carregar arquivo de origem
        documento = db.parse(new File(".\\testes\\simpleType.xsd"));
        esperado = new File(".\\testes\\simpleType.wsml");        
        
        GroundingDados gd = new GroundingDados(xmlns);
        gd.resetContador();
        Node schema = documento.getElementsByTagNameNS(xmlns, "schema").item(0);
        List<Node> elementos = new ArrayList<>();
        //percorrer os elementos filhos de schema
        for (int i = 0; i < schema.getChildNodes().getLength(); i++) {
            Node item = schema.getChildNodes().item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE && 
                    item.getLocalName().equals("simpleType")) {
                elementos.add(item);
            }
        }
        System.out.println("Numero de simpleType: " + elementos.size());
        System.out.println("-----------------------");
        String saida = "";
        for (Node elemento : elementos) {
            saida = saida + gd.mapearSimpleType(elemento);
        }
        System.out.println(saida);
        FileUtils.write(resultado, saida);
        
        assertEquals(FileUtils.readFileToString(esperado, "utf-8"), FileUtils.readFileToString(resultado, "utf-8"));
    }

    /**
     * Os testes consistem em ler um documento contendo exemplos, gerar a saida e comparar com a
     * saida esperada.
     * 
     * @throws Exception 
     */
    @Test
    public void testMapearComplexType() throws Exception {
        System.out.println("MapearComplexType");
        System.out.println("================");
        //carregar arquivo de origem
        documento = db.parse(new File(".\\testes\\complexType.xsd"));
        esperado = new File(".\\testes\\complexType.wsml");        
        
        GroundingDados gd = new GroundingDados(xmlns);
        gd.resetContador();
        Node schema = documento.getElementsByTagNameNS(xmlns, "schema").item(0);
        List<Node> elementos = new ArrayList<>();
        //percorrer os elementos filhos de schema
        for (int i = 0; i < schema.getChildNodes().getLength(); i++) {
            Node item = schema.getChildNodes().item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE && 
                    item.getLocalName().equals("complexType")) {
                elementos.add(item);
            }
        }
        System.out.println("Numero de complexType: " + elementos.size());
        System.out.println("--------------------");
        String saida = "";
        for (Node elemento : elementos) {
            saida = saida + gd.mapearComplexType(elemento);
        }
        System.out.println(saida);
        FileUtils.write(resultado, saida);
        
        assertEquals(FileUtils.readFileToString(esperado, "utf-8"), FileUtils.readFileToString(resultado, "utf-8"));
    }    
    
    /**
     * Os testes consistem em ler um documento contendo exemplos, gerar a saida e comparar com a
     * saida esperada.
     * 
     * @throws Exception 
     */
    @Test
    public void testMapearElement() throws Exception {
        System.out.println("MapearElement");
        System.out.println("================");
        //carregar arquivo de origem
        documento = db.parse(new File(".\\testes\\element.xsd"));
        esperado = new File(".\\testes\\element.wsml");        
        
        GroundingDados gd = new GroundingDados(xmlns);
        gd.resetContador();
        Node schema = documento.getElementsByTagNameNS(xmlns, "schema").item(0);
        List<Node> elementos = new ArrayList<>();
        //percorrer os elementos filhos de schema
        for (int i = 0; i < schema.getChildNodes().getLength(); i++) {
            Node item = schema.getChildNodes().item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE && 
                    item.getLocalName().equals("element")) {
                elementos.add(item);
            }
        }
        System.out.println("Numero de element testados: " + elementos.size());
        System.out.println("--------------------");
        String saida = "";
        int i = 1;
        for (Node elemento : elementos) {
            saida = saida + gd.mapearElement(elemento);
            i++;
        }
        System.out.println(saida);
        FileUtils.write(resultado, saida);
        
        assertEquals(FileUtils.readFileToString(esperado, "utf-8"), FileUtils.readFileToString(resultado, "utf-8"));
    }
}
