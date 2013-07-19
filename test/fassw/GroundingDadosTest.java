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
        resultado = new File(".\\testes\\schema\\resultado.temp");
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
        documento = db.parse(new File(".\\testes\\schema\\annotation.xsd"));
        esperado = new File(".\\testes\\schema\\annotation.wsml");        
        pw = new PrintWriter(resultado);
        
        GroundingDadosImpl gd = new GroundingDadosImpl(xmlns);
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
        documento = db.parse(new File(".\\testes\\schema\\attributeGroup.xsd"));
        esperado = new File(".\\testes\\schema\\attributeGroup.wsml");        
        pw = new PrintWriter(resultado);
        
        GroundingDadosImpl gd = new GroundingDadosImpl(xmlns);
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
        documento = db.parse(new File(".\\testes\\schema\\attribute.xsd"));
        esperado = new File(".\\testes\\schema\\attribute.wsml");        
        //pw = new PrintWriter(resultado);
        
        GroundingDadosImpl gd = new GroundingDadosImpl(xmlns);
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
        documento = db.parse(new File(".\\testes\\schema\\simpleType.xsd"));
        esperado = new File(".\\testes\\schema\\simpleType.wsml");        
        
        GroundingDadosImpl gd = new GroundingDadosImpl(xmlns);
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
        documento = db.parse(new File(".\\testes\\schema\\complexType.xsd"));
        esperado = new File(".\\testes\\schema\\complexType.wsml");        
        
        GroundingDadosImpl gd = new GroundingDadosImpl(xmlns);
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
        documento = db.parse(new File(".\\testes\\schema\\element.xsd"));
        esperado = new File(".\\testes\\schema\\element.wsml");        
        
        GroundingDadosImpl gd = new GroundingDadosImpl(xmlns);
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
        for (Node elemento : elementos) {
            saida = saida + gd.mapearElement(elemento);
        }
        System.out.println(saida);
        FileUtils.write(resultado, saida);
        
        assertEquals(FileUtils.readFileToString(esperado, "utf-8"), FileUtils.readFileToString(resultado, "utf-8"));
    }
    
    @Test
    public void testMapearTudo() throws Exception {
        System.out.println("MapearEsquema");
        System.out.println("================");
        //carregar arquivo de origem
        documento = db.parse(new File(".\\testes\\schema\\esquema.xsd"));
        esperado = new File(".\\testes\\schema\\esquema.wsml");
        
        GroundingDadosImpl gd = new GroundingDadosImpl(xmlns);
        gd.resetContador();
        
        Node schema = documento.getElementsByTagNameNS(xmlns, "schema").item(0);
        List<Node> elementos = new ArrayList<>();
        //percorrer os elementos filhos de schema
        for (int i = 0; i < schema.getChildNodes().getLength(); i++) {
            Node item = schema.getChildNodes().item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE) {
                elementos.add(item);
            }
        }
        System.out.println("Numero de element testados: " + elementos.size());
        System.out.println("--------------------");
        String saida = "";
        for (Node elemento : elementos) {
            String nomeElemento = elemento.getLocalName();
              
            switch (nomeElemento) {
                case "element":
                    saida = saida + gd.mapearElement(elemento);
                    break;
                case "simpleType":
                    saida = saida + gd.mapearSimpleType(elemento);
                    break;
                case "complexType":
                    saida = saida + gd.mapearComplexType(elemento);
                    break;
                case "attribute":
                    saida = saida + gd.mapearAttribute(elemento);
                    break;
                case "attributeGroup":
                    saida = saida + gd.mapearAttributeGroup(elemento);
                    break;
                case "group":
                    saida = saida + gd.mapearGroup(elemento);
                    break;
                default:
                    break;
            }
        }
        System.out.println(saida);
        FileUtils.write(resultado, saida);
        
        assertEquals(FileUtils.readFileToString(esperado, "utf-8"), FileUtils.readFileToString(resultado, "utf-8"));
    }
}
