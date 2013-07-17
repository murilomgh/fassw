package fassw;

import fassw.gui.InterfaceGrafica;
import fassw.util.Conversor;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.wsmo.common.exception.InvalidModelException;

/**
 * Contém o metodo de entrada do prototipo.
 * 
 * @author Murilo Honorio
 * @version 0.0
 */
public class Main {
    
    private String entrada, saida; //caminhos para os arquivos de entrada e saida

    /**
     * Método de entrada da ferramenta.
     * Caso passe como parametros "entrada.wsdl saida.wsml converter", funciona em linha de comando.
     * Senao, funciona atraves da interface grafica.
     * 
     * @param args nome da operacao, arquivo de entrada, arquivo de saida
     */
    public static void main(String[] args) throws UnsupportedOperationException, InvalidModelException, IOException {
        boolean sucesso = false;
        Mapeador mapeador;
        
        if (args.length == 3) {
            if (args[2].equals("s")) {
                mapeador = new Mapeador(args[0], args[1], true);
                sucesso = mapeador.executar();
            }
            else if (args[2].equals("n")) {
                mapeador = new Mapeador(args[0], args[1], false);
                sucesso = mapeador.executar();
            }
            else {
                System.err.println("Parametro " + args[2] + " invalido. O programa espera 's' ou 'n'.");
            }
            
            if (sucesso) {
                System.out.println("Módulo mapeador efetuou a tarefa com sucesso.");
            }
            else {
                System.err.println("Módulo mapeador não conseguiu concluir a tarefa.");
            }
        }
        else {
            InterfaceGrafica ig = new InterfaceGrafica();
            String entrada = ig.abrirArquivo();
            String saida = ig.salvarArquivo();
            //perguntar se deseja converter
            boolean converter = ig.converterArquivo();
            
            if (converter) {
                mapeador = new Mapeador(entrada, saida, true);
            } 
            else {
                mapeador = new Mapeador(entrada, saida, false);
            }
            sucesso = mapeador.executar();
            if (sucesso) {
                JOptionPane.showMessageDialog(null, "Módulo mapeador efetuou a tarefa com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(null, "Módulo mapeador não conseguiu concluir a tarefa.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
