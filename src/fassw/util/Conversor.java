package fassw.util;

import javax.swing.JOptionPane;
import org.apache.woden.tool.converter.Convert;

/**
 * Prototipo do conversor de serviços de WSDL 1.1 para 2.0.
 * Todo credito vai para Sagara Gunathunga {@link http://www.blogger.com/profile/00728327638746992048}
 * 
 * O conversão faz uso da biblioteca Apache Woden. O projeto Woden prove um conversor baseado em XSL para essa tarefa.
 *
 * @author murilo.honorio@usp.br
 * @version 0.1
 * Fonte: {@link http://ssagara.blogspot.com.br/2009/01/converting-wsdl11-to-wsdl20-using-woden.html}
 */
public class Conversor 
{
    /**
     * O metodo recebe um arquivo wsdl como entrada. A classe Convert realiza todo o trabalho.
     * Ela grava um arquivo com o mesmo nome e a extensão ".wsdl2". 
     * Caso o arquivo seja da versão 2.0 mostra mensagem na tela.
     * Caso o arquivo .wsdl2 já exista no diretorio de destino, emite mensagem na tela.
     * 
     * @see org.apache.woden.tool.converter.Convert#convertFile
     */
    public static boolean converter(String entrada, boolean linhaDeComando) {
        String novoNamespaceAlvo = null;
        String diretorioDestino = "temp"; //cria pasta temp como subpasta da entrada
        boolean mensagensDetalhadas = true;
        boolean sobrescreverArquivo = true;
        
        Convert conversor = new Convert();
        try {
            String convertFile = conversor.convertFile(novoNamespaceAlvo, entrada, diretorioDestino, mensagensDetalhadas, sobrescreverArquivo);
            if (linhaDeComando) {
                System.out.println(convertFile);
            }
            else {
                JOptionPane.showMessageDialog(null, "Modulo Conversor efetuou tarefa com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        catch (javax.wsdl.WSDLException e) {
            if (e.getLocation().contains("description")) {
                System.out.println("O arquivo fornecido como entrada já está na versão WSDL 2.0");
                e.printStackTrace();
            }
        }
        catch (java.io.IOException e) {
            if (e.getMessage().contains("already exists")) {
                System.out.println("O serviço já foi convertido anteriormente.");
                e.printStackTrace();
            }
        }
        return true;
    }
}
