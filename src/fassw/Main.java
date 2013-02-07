package fassw;

import java.io.IOException;
import org.wsmo.common.exception.InvalidModelException;

/**
 * Contém o método de entrada do protótipo.
 * 
 * @author Murilo Honorio
 * @version 0.0
 */
public class Main {

    /**
     * Método de entrada da ferramenta.
     * @param args nome da operacao, arquivo de entrada, arquivo de saida
     */
    public static void main(String[] args) throws UnsupportedOperationException, InvalidModelException, IOException {
        boolean sucesso;
        
        if (args[0].equals("mapear")) {
            Mapeador mapeador = new Mapeador(args[1], args[2]);
            sucesso = mapeador.executar();
            if (sucesso) {
                System.out.println("Módulo mapeador efetuou a tarefa com sucesso.");
            }
            else {
                System.err.println("Módulo mapeador não conseguiu concluir a tarefa.");
            }
        }
    }
}
