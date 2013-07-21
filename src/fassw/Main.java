package fassw;

import fassw.gui.InterfaceGrafica;
import java.io.IOException;

/**
 * Contém o metodo de entrada do prototipo.
 * 
 * @author Murilo Honorio
 * @version 0.0
 */
public class Main {
    
    /**
     * Método de entrada da ferramenta.
     * Caso passe como parametros "entrada.wsdl saida.wsml [opcao]", funciona em linha de comando, 
     * onde [opcao] pode ser -s (deseja tentar converter WSDL 1.1) ou -n (nao deseja tentar a conversao).
     * Senao, funciona atraves da interface grafica.
     * 
     * @param args nome da operacao, arquivo de entrada, arquivo de saida
     */
    public static void main(String[] args) throws UnsupportedOperationException, IOException {
        boolean sucesso = false;
        Mapeador mapeador;
        
        if (args.length == 3) {
            switch (args[2]) {
                case "-s":
                    mapeador = new Mapeador(args[0], args[1], true);
                    sucesso = mapeador.executar();
                    break;
                case "-n":
                    mapeador = new Mapeador(args[0], args[1], false);
                    sucesso = mapeador.executar();
                    break;
                default:
                    System.err.println("Parametro " + args[2] + " invalido. O programa espera '-s' ou '-n'.");
                    break;
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
        }
        if (sucesso) {
            System.out.println("Módulo mapeador efetuou a tarefa com sucesso.");
        } 
        else {
            System.err.println("Módulo mapeador não conseguiu concluir a tarefa.");
        }
    }
}
