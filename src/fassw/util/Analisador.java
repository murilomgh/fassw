package fassw.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.woden.WSDLException;
import org.apache.woden.WSDLFactory;
import org.apache.woden.WSDLReader;
import org.apache.woden.wsdl20.Description;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Classe com a responsabilidade de identificar se o arquivo de entrada eh valido e, caso afirmativo,
 * identificar a versao do WSDL.
 * 
 * @author Murilo Honorio
 * @version 0.0
 */
public class Analisador {
    
    public Analisador() {
        
    }
    
    public static boolean identificarVersao(String entrada, boolean linhaDeComando) {
        boolean isVersao20;

        //obter elemento raiz
        Document documento = WSDL.obterDOM(entrada);
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
            reader.setFeature(WSDLReader.FEATURE_VALIDATION, true); //<-- habilitar validacao do WSDL 2.0            
            Description descricao = reader.readWSDL(caminho);
            
            return true;
        }
        catch (WSDLException ex) {
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
                    Logger.getLogger(Analisador.class.getName()).log(Level.SEVERE, null, ex);
                    if (isLinhaDeComando) {
                        System.err.println("Erro desconhecido.");
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Atencao", "Teste", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
            }
            return false;
        }
    }
    
    private boolean analisarWSDL() {
        
        
        return false;
    }
}
