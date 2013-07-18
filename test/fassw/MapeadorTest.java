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

    @Test
    public void testExecutarSemConverterComSucesso() throws Exception {
        System.out.println("Teste executar()");
        System.out.println("================");
        Mapeador mapeador = new Mapeador(".\\testes\\exemplo\\servicoExemplo20.wsdl", ".\\testes\\exemplo\\servicoExemplo20.wsml", false);
        boolean resultadoEsperado = true;
        boolean result = mapeador.executar();
        assertEquals(resultadoEsperado, result);
    }
}
