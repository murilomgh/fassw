package fassw.util;

import javax.swing.JOptionPane;
import org.apache.woden.WSDLException;
import org.apache.woden.WSDLFactory;
import org.apache.woden.WSDLReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Classe com a responsabilidade de identificar se o arquivo de entrada eh valido e qual a versao
 * do Leitor fornecido para o mapeador.
 * 
 * @author Murilo Honorio
 * @version 0.0
 */
public class Analisador {
    
    /**
     * Metodo que identifica a versao do arquivo WSDL verificando elemento raiz.
     * Caso "description" -> WSDL 2.0
     * Caso "definitions" -> WSDL 1.1
     * @param entrada
     * @param linhaDeComando
     * @return 
     */
    public static boolean identificarVersao(String entrada, boolean linhaDeComando) {
        boolean isVersao20;

        //obter elemento raiz
        Document documento = Leitor.obterDocument(entrada);
        Element raiz = documento.getDocumentElement();
        String elemento = raiz.getTagName();

        //verificar versao
        if (elemento.contains("description")) {
            if (linhaDeComando) {
                System.out.println("WSDL 2.0");
            } 
            isVersao20 = true;
        } else if (elemento.contains("definitions")) {
            if (linhaDeComando) {
                System.out.println("WSDL 1.1");
            }
            isVersao20 = false;
        } 
        else {
            if (linhaDeComando) {
                System.err.println("Nao foi possivel identificar a versao do arquivo WSDL.");
            }
            else {
                JOptionPane.showMessageDialog(null, "Nao foi possivel identificar a versao do arquivo WSDL.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
            throw new RuntimeException("Não foi possível identificar a versão do arquivo WSDL.");
        }
        return isVersao20;
    }

    public static boolean analisarArquivo(String caminho, boolean isLinhaDeComando) {
        
        try {
            WSDLFactory factory = WSDLFactory.newInstance();
            WSDLReader reader = factory.newWSDLReader();
            reader.setFeature(org.apache.woden.WSDLReader.FEATURE_VALIDATION, true);
            reader.readWSDL(caminho);
            System.out.println("Analise OK");
            return true;
        }
        //tratamento para erros fatais
        catch (WSDLException ex) {
            System.out.println(ex.getFaultCode());
            switch (ex.getFaultCode()) {
                case WSDLException.INVALID_WSDL:
                    //wsdl invalido
                    if (isLinhaDeComando) {
                        System.out.println(ex.getFaultCode() + "\n" + ex.getLocalizedMessage());
                    }
                    else {
                        JOptionPane.showMessageDialog(null, ex.getFaultCode());
                        JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                    }
                    break;
                case WSDLException.OTHER_ERROR:
                    //arquivo invalido
                    if (isLinhaDeComando) {
                        System.out.println("Arquivo invalido." + ex.getFaultCode());
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Arquivo invalido");
                    }
                    break;
                default:
                    //Logger.getLogger(Analisador.class.getName()).log(Level.SEVERE, null, ex);
                    if (isLinhaDeComando) {
                        System.err.println("Erro desconhecido. Possivelmente arquivo invalido.");
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Erro desconhecido. Possivelmente arquivo invalido.", "Falha", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
            }
            return false;
        }
    }
}