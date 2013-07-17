package fassw.util;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Essa classe serve para serializar e deserializar documentos WSDL.
 *
 * @author murilo.honorio@usp.br
 * @version 0.1
 */
public class SerializadorDOM {

    /**
     * Dado o arquivo SerializadorDOM converte para uma representação em memória
     * 
     * @param entrada nome completo do arquivo incluindo o caminho
     * @return a representacao DOM do servico SerializadorDOM
     */
    public static Document obterDOM(String entrada) {
        Document documento = null;

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            
            docFactory.setNamespaceAware(true); //ativar suporte para namespaces em XML
            docFactory.setValidating(true);
            documento = docBuilder.parse(entrada);
            documento.getDocumentElement().normalize(); //usado para garantir navegacao consistente pelo arquivo
        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.err.println("Erro na leitura do arquivo WSDL.");
            System.err.println(e.toString());
        }
        return documento;
    }

    /**
     * Dado o documento DOM e um caminho, grava o arquivo em disco.
     * 
     * @param documento a representacao DOM do SerializadorDOM
     * @param nomeArquivo nome completo do arquivo incluindo o caminho
     */
    public static void DOMparaWSDL(Document documento, String nomeArquivo) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = transformerFactory.newTransformer();
            DOMSource wsdlOrigem = new DOMSource(documento);
            StreamResult resultado = new StreamResult(new File(nomeArquivo));
            transformer.transform(wsdlOrigem, resultado);
        } catch (TransformerException e) {
            System.err.println("Erro na gravação do arquivo WSDL.");
            System.err.println(e.toString());
        }
    }
}