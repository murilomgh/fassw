package fassw;

import fassw.util.Analisador;
import fassw.util.Conversor;
import java.io.File;

/**
 * Classe responsável por ler as descrições dos serviços Web em WSDL e transformar em descrições 
 * WSML segundo o modelo WSMO.
 * 
 * @author Murilo Honorio
 */
class Mapeador {
    private String entrada, saida; //caminhos para os arquivos de entrada e saida
    boolean conversao; //flag se tentar conversao caso receba documento WSDL 1.1

    public Mapeador(String entrada, String saida, boolean conversao) {
        this.entrada = entrada;
        this.saida = saida;
        this.conversao = conversao;
    }

    /**
     * Realiza o processo de conversao do servico Web WSDL em artefatos Ontology e WebService WSMO.
     * 
     * @return um booleano indicando se a conversao ocorreu com sucesso.
     */
    public boolean executar() {
        boolean sucesso;
        
        try {
            sucesso = Analisador.identificarVersao(entrada);
            if (!sucesso) {
                if (conversao == true) {
                    sucesso = Conversor.converter(entrada);
                    int index = entrada.lastIndexOf(File.separatorChar);
                    //desviar o caminho de entrada para o arquivo temporario convertido
                    entrada = entrada.substring(0, index) + File.separatorChar + "temp" + File.separatorChar + entrada.substring(index, entrada.length()) + "2";
                }
                else {
                    sucesso = false;
                }
            }

            if (sucesso) {
                sucesso = Analisador.analisarArquivo(entrada);
            }
            if (sucesso) {
                MapeadorDados gd = new MapeadorDados(entrada, saida);
                sucesso = gd.processar();
            }
            if (sucesso) {
                GroundingCoreografia gc = new GroundingCoreografia();
                sucesso = gc.processar(entrada, saida);
            }
            return sucesso;
        } catch (RuntimeException re) {
            System.err.println(re.fillInStackTrace());
            System.exit(0);
        }
        return false;
    }
}