package fassw;

import java.io.IOException;
import javax.swing.JOptionPane;
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
     * Caso passe como parametro "mapear entrada.wsdl saida.wsml", funciona em linha de comando.
     * Senao, funciona atraves da interface grafica.
     * 
     * @param args nome da operacao, arquivo de entrada, arquivo de saida
     */
    public static void main(String[] args) throws UnsupportedOperationException, InvalidModelException, IOException {
        boolean sucesso;
        
        if (args.length == 3 && args[0].equals("mapear")) {
            Mapeador mapeador = new Mapeador(args[1], args[2]);
            sucesso = mapeador.executar();
            if (sucesso) {
                System.out.println("Módulo mapeador efetuou a tarefa com sucesso.");
            }
            else {
                System.err.println("Módulo mapeador não conseguiu concluir a tarefa.");
            }
        }
        else {
            Mapeador mapeador = new Mapeador();
            sucesso = mapeador.executar();
            if (sucesso) {
                JOptionPane.showMessageDialog(null, "Módulo mapeador efetuou a tarefa com sucesso.");
            }
            else {
                JOptionPane.showMessageDialog(null, "Módulo mapeador não conseguiu concluir a tarefa.");
            }
        }
    }
}
