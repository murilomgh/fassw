package fassw;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author Murilo Honorio
 */
public class MapeadorTest {
    
    /**
     * Um simples teste de conversao de arquivo no caso mais comum.
     * @throws Exception
     */
    @Test
    public void testExecutarSemConverterComSucesso() throws Exception {
        System.out.println("Teste executar()");
        System.out.println("================");
        //Mapeador mapeador = new Mapeador(".\\testes\\exemplo\\servicoExemplo20.wsdl", ".\\testes\\exemplo\\servicoExemplo20.wsml", false);
        Mapeador mapeador = new Mapeador(".\\samples\\warehouse.wsdl", ".\\samples\\warehouse.wsml", false);
        boolean resultadoEsperado = true;
        boolean result = mapeador.executar();
        assertEquals(resultadoEsperado, result);
    }
}
