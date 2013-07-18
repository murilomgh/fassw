package fassw.util;

import org.apache.woden.wsdl20.Description;
import org.w3c.dom.Document;

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
     * @return 
     */
    public static boolean identificarVersao(String entrada) {
        boolean isVersao20 = false;

        //obter elemento raiz
        Document documento = Leitor.obterDocument(entrada);
        String elemento = documento.getDocumentElement().getTagName();

        //verificar versao
        if (elemento.contains("description")) {
            isVersao20 = true;
        } 
        else if (elemento.contains("definitions")) {
            isVersao20 = false;
        } 
        else {
            System.err.println("Nao foi possivel identificar a versao do arquivo WSDL.");
            throw new RuntimeException("Não foi possível identificar a versão do arquivo WSDL.");
        }
        return isVersao20;
    }

    /**
     * Metodo que efetua validacao da descricao WSDL
     *
     * @param caminho
     * @return
     */
    //TODO incluir validacao mais sofisticada, corrigir comportamento
    public static boolean analisarArquivo(String caminho) {
        Description descricao = Leitor.obterDescription(caminho);
        
        if (descricao == null) {
            return false;
        } 
        else {
            return true;
        }
    }
}