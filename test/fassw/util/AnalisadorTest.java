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
        boolean esperado = true;
        boolean result = Analisador.identificarVersao(entrada);
        assertEquals(esperado, result);
    }
    
    /**
     * Test of identificarVersao method, of class Analisador.
     */
    @Test
    public void testIdentificarVersao11() {
        System.out.println("Identificar Versao 1.1");
        String entrada = ".\\testes\\analisador\\servico11.wsdl";
        boolean esperado = false; //equivale a WSDL 1.1
        boolean result = Analisador.identificarVersao(entrada);
        assertEquals(esperado, result);
    }

    /**
     * Test of analisarArquivo method, of class Analisador.
     */
    @Test
    public void testAnalisarArquivo() {
        System.out.println("Analisar Arquivo Sintaxe Errada");
        String caminho = ".\\testes\\analisador\\sintaxeErrada.wsdl";
        boolean resultado = false;
        boolean result = Analisador.analisarArquivo(caminho);
        assertEquals(resultado, result);
    }
}
