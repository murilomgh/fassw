package fassw;

import fassw.gui.InterfaceGrafica;
import fassw.util.Analisador;
import fassw.util.Conversor;
import java.io.File;
import javax.swing.JOptionPane;
/**
 * Classe responsável por ler as descrições dos serviços Web em WSDL e transformar em descrições 
 * WSML segundo o modelo WSMO.
 * @author Murilo Honorio
 * @version 0.0
 */
class Mapeador {
    private String entrada, saida; //caminhos para os arquivos de entrada e saida
    boolean linhaDeComando;

    //versao do mapeador por linha de comando
    public Mapeador(String entrada, String saida) {
        this.entrada = entrada;
        this.saida = saida;
        this.linhaDeComando = true;
    }
    
    //versao do mapeador com interface grafica
    public Mapeador() {
        InterfaceGrafica ig = new InterfaceGrafica();
        this.entrada = ig.abrirArquivo();
        this.saida = ig.salvarArquivo();
        this.linhaDeComando = false;
    }

    /**
     * 
     * @return 
     */
    public boolean executar() {
        boolean sucesso;
        
        try {
            sucesso = Analisador.identificarVersao(entrada, linhaDeComando);
            if (sucesso == false) {
                int resposta = JOptionPane.showConfirmDialog(null, "Arquivo WSDL versao 1.1. Gostaria de converter?", "Versao diferente", JOptionPane.YES_NO_OPTION);

                if (resposta == JOptionPane.YES_OPTION) {
                    sucesso = Conversor.converter(entrada, linhaDeComando);
                    int index = entrada.lastIndexOf(File.separatorChar);
                    //converter o caminho de entrada para o arquivo temporario convertido
                    entrada = entrada.substring(0, index) + File.separatorChar + "temp" + File.separatorChar + entrada.substring(index, entrada.length()) + "2";
                } else {
                    sucesso = false;
                }
            }

            if (sucesso == true) {
                sucesso = Analisador.analisarArquivo(entrada, linhaDeComando);
            }

            return sucesso;
        } catch (RuntimeException re) {
            System.exit(0);
        }
        return false;
    }
}
