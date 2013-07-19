package fassw;

import fassw.util.VarianteWSML;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Murilo Honorio
 */
public class GroundingCoreografiaTest {
   
    GroundingCoreografia gc;
    
    public GroundingCoreografiaTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        gc = new GroundingCoreografia();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Teste de geracao de arquivo completo
     */
    //TODO escrever teste para o metodo processar()
    @Test
    public void testProcessar() {
        /*
        GroundingCoreografia instance = null;
        boolean expResult = false;
        boolean result = instance.processar();
        assertEquals(expResult, result);
        */
        fail("The test case is a prototype.");
    }
    
    @Test
    public void testarDeclararWSMLVariantFlight() {
        String declaracao = gc.declararWSMLVariant(VarianteWSML.Flight.IRI());
        String esperado = "wsmlVariant _\"http://www.wsmo.org/wsml/wsml-syntax/wsml-flight\"\n";
        assertEquals("Variante Flight", esperado, declaracao);
    }
    
    @Test
    public void testarDeclararWSMLVariantRule() {
        String declaracao = gc.declararWSMLVariant(VarianteWSML.Rule.IRI());
        String esperado = "wsmlVariant _\"http://www.wsmo.org/wsml/wsml-syntax/wsml-rule\"\n";
        assertEquals("Variante Rule", esperado, declaracao);
    }
    
    @Test
    public void testarDeclararWSMLVariantCore() {
        String declaracao = gc.declararWSMLVariant(VarianteWSML.Core.IRI());
        String esperado = "wsmlVariant _\"http://www.wsmo.org/wsml/wsml-syntax/wsml-core\"\n";
        assertEquals("Variante Core", esperado, declaracao);
    }
    
    @Test
    public void testarDeclararNamespacesSemParametros() {
        String resultado = gc.declararNamespaces(null, null, null);
        String esperado = "";
        assertEquals(esperado, resultado);
    }
    
    @Test
    public void testarDeclararNamespacesComUmSchema() {
        String namespaceAlvo = "http://exemplo.com";
        String nomeDoServico = "servico";
        List<String> namespaceDasOntologias = new ArrayList<>();
        namespaceDasOntologias.add("http://exemplo.com/esquema");
        
        String esperado = "namespace { _\"http://exemplo.com/servico#\"\n,\n\tdc _\"http://purl.org/dc/elements/1.1#\",\n\toasm _\"http://www.wsmo.org/ontologies/choreography/oasm#\",\n\twsml _\"http://www.wsmo.org/wsml/wsml-syntax#\",\n\tonto1 _\"http://exemplo.com/esquema#\" }\n\n\n";
        String resultado = gc.declararNamespaces(namespaceAlvo, nomeDoServico, namespaceDasOntologias);
        assertEquals(esperado, resultado);
    }
    
    @Test
    public void testarDeclararNamespacesComDoisSchemas() {
        String namespaceAlvo = "http://exemplo.com";
        String nomeDoServico = "servico";
        List<String> namespaceDasOntologias = new ArrayList<>();
        namespaceDasOntologias.add("http://exemplo.com/esquema1");
        namespaceDasOntologias.add("http://exemplo.com/esquema2");
        
        String esperado = "namespace { _\"http://exemplo.com/servico#\"\n,\n\tdc _\"http://purl.org/dc/elements/1.1#\",\n\toasm _\"http://www.wsmo.org/ontologies/choreography/oasm#\",\n\twsml _\"http://www.wsmo.org/wsml/wsml-syntax#\",\n\tonto1 _\"http://exemplo.com/esquema1#\",\n\tonto2 _\"http://exemplo.com/esquema2#\" }\n\n\n";
        String resultado = gc.declararNamespaces(namespaceAlvo, nomeDoServico, namespaceDasOntologias);
        assertEquals(esperado, resultado);
    }
    
    @Test
    public void testarDeclararWebServiceComUmService() {
        String namespaceAlvo = "http://exemplo.com";
        String nomeDoServico = "servicoExemplo";
        String fragmentoIdentificador = namespaceAlvo + "#wsdl.service(servicoExemplo)";
        
        String esperado = "webService _\"http://exemplo.com\"\n\tannotations\n\t\tdc#title hasValue \"servicoExemplo\"\n\t\tdc#creator hasValue \"FASSW\"\n\t\twsml#endpointDescription hasValue \"http://exemplo.com#wsdl.service(servicoExemplo)\"\n\tendAnnotations\n\n";
        String resultado = gc.declararWebService(namespaceAlvo, nomeDoServico, fragmentoIdentificador);
        assertEquals(esperado, resultado);
    }
    
}