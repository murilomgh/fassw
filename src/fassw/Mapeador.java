package fassw;

import fassw.util.Analisador;
import fassw.util.Conversor;
import java.io.File;
/**
 * Classe responsável por ler as descrições dos serviços Web em WSDL e transformar em descrições 
 * WSML segundo o modelo WSMO.
 * @author Murilo Honorio
 * @version 0.0
 */
class Mapeador {
    private String entrada, saida; //caminhos para os arquivos de entrada e saida
    boolean linhaDeComando;
    boolean converter; //deseja converter caso WSDL 1.1

    public Mapeador(String entrada, String saida, boolean converter) {
        this.entrada = entrada;
        this.saida = saida;
        this.converter = converter;
        this.linhaDeComando = true;
    }

    /**
     * Realiza o processo de conversao do servico web.
     * 
     * @return 
     */
    public boolean executar() {
        boolean sucesso;
        
        try {
            sucesso = Analisador.identificarVersao(entrada, linhaDeComando);
            if (!sucesso) {
                if (converter == true) {
                    sucesso = Conversor.converter(entrada, linhaDeComando);
                    int index = entrada.lastIndexOf(File.separatorChar);
                    //converter o caminho de entrada para o arquivo temporario convertido
                    entrada = entrada.substring(0, index) + File.separatorChar + "temp" + File.separatorChar + entrada.substring(index, entrada.length()) + "2";
                }
                else {
                    sucesso = false;
                }
            }

            if (sucesso) {
                sucesso = Analisador.analisarArquivo(entrada, linhaDeComando);
            }
            if (sucesso) {
                MapeadorDados gd = new MapeadorDados(entrada, saida);
                sucesso = gd.processar();
                System.out.println("Geracao de Ontology OK");
            }
            if (sucesso) {
                GroundingCoreografia gc = new GroundingCoreografia(entrada, saida);
                sucesso = gc.processar();
            }
            return sucesso;
        } catch (RuntimeException re) {
            System.err.println(re.fillInStackTrace());
            System.exit(0);
        }
        return false;
    }
}
