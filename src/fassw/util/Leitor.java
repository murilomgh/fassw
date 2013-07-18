package fassw.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.woden.ErrorHandler;
import org.apache.woden.ErrorReporter;
import org.apache.woden.WSDLException;
import org.apache.woden.WSDLFactory;
import org.apache.woden.WSDLReader;
import org.apache.woden.internal.ErrorReporterImpl;
import org.apache.woden.wsdl20.Description;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Classe com a responsabilidade de deserializar documentos WSDL.
 *
 * @author murilo.honorio@usp.br
 * @version 0.1
 */
public class Leitor {

    /**
     * Dado o caminho para um arquivo em XML, converte para uma representacao em memoria representada por um 
     * objeto Document.
     * 
     * @param entrada nome completo do arquivo incluindo o caminho
     * @return a representacao DOM do servico web
     */
    public static Document obterDocument(String entrada) {
        Document documento = null;

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            File arquivo = new File(entrada);
            
            docFactory.setNamespaceAware(true); //ativar suporte para namespaces em XML
            docFactory.setValidating(true);
            documento = docBuilder.parse(arquivo.getAbsoluteFile());
            documento.getDocumentElement().normalize(); //usado para garantir navegacao consistente pelo arquivo
        } catch (ParserConfigurationException e) {
            System.err.println("Nao foi possivel configurar o parser DOM.");
            System.err.println("Mais detalhes: " + e.getMessage());
        } catch (SAXException e) {
            System.err.println("Erro durante analise e interpretacao do arquivo.");
            System.err.println("Mais detalhes: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro de entrada e saida.");
            System.err.println("Mais detalhes: " + e.getMessage());
        }
        return documento;
    }
    
    /**
     * Dado o caminho para um arquivo escrito em WSDL 2.0, obtem a descricao do servico Web representada por
     * um objeto Description da biblioteca Apache Woden.
     *
     * @return um objeto Description que representa a descricao de um servico Web em WSDL
     */
    public static Description obterDescription(String entrada) {
        Description descricao = null;
        
        try {
            File arquivo = new File(entrada);
            WSDLFactory factory = WSDLFactory.newInstance();
            WSDLReader reader = factory.newWSDLReader();
            //reader.setFeature(WSDLReader.FEATURE_VALIDATION, true); //<-- enable WSDL 2.0 validation(optional)
            ErrorReporter errorReporter = reader.getErrorReporter();
            ErrorHandler errorHandler = errorReporter.getErrorHandler();
            
            
            ErrorReporter er = new ErrorReporterImpl();
            ErrorHandler errorHandler2 = er.getErrorHandler();
            descricao = reader.readWSDL(arquivo.getAbsolutePath()); //<-- the Description component, always returned
        } 
        catch (org.apache.woden.WSDLException e) {
            switch (e.getFaultCode()) {
                case WSDLException.INVALID_WSDL:
                    //wsdl invalido
                    System.out.println(e.getFaultCode());
                    System.out.println(e.getLocalizedMessage());
                    break;
                case WSDLException.OTHER_ERROR:
                    //arquivo invalido
                    System.out.println(e.getFaultCode());
                    e.printStackTrace();
                    break;
                default:
                    Logger.getLogger(Leitor.class.getName()).log(Level.SEVERE, null, e);
                    break;
            }
        }
        return descricao;
    }
}