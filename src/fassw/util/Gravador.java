package fassw.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

/**
 * Classe com a responsabilidade de serializar documentos WSDL.
 * @author Murilo Honorio
 * @version 0.0
 */
public class Gravador {
    
    /**
     * Dado um objeto DOM Document um caminho, grava o arquivo em disco.
     * 
     * @param documento a representacao DOM do documento WSDL
     * @param caminho nome completo do arquivo incluindo o caminho
     */
    public static void gravarWSDL(Document documento, String caminho) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = transformerFactory.newTransformer();
            DOMSource documentoOrigem = new DOMSource(documento);
            StreamResult resultado = new StreamResult(new File(caminho));
            transformer.transform(documentoOrigem, resultado);
        } catch (TransformerException e) {
            System.err.println("Erro na gravação do arquivo WSDL.");
            System.err.println(e.toString());
            e.printStackTrace();
        }
    }
    
    /**
     * Dado um documento serializado, grava o arquivo no caminho fornecido
     * @param documento 
     */
    public static void gravarWSML(String documento, String caminho) {
        PrintWriter pw = null;
        
        try {
            File arquivo = new File(caminho);
            pw = new PrintWriter(arquivo.getAbsoluteFile(), "UTF-8");
            pw.append(documento);
            
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(Gravador.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            pw.close();
        }
    }
}
