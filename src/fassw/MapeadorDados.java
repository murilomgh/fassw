package fassw;

import fassw.util.ElementoNaoEsperadoException;
import fassw.util.Leitor;
import fassw.util.VarianteWSML;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.FactoryConfigurationError;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Classe com a responsabilidade organizar a execucao do grounding de dados para os schema
 * embutidos no elemento types da descricao do servico web.
 * 
 * @author Murilo Honorio
 */
public class MapeadorDados {
    
    private static Integer contador = 1; //um contador que estabelece um nome unico para cade elemento que nao possua um nome
    private String entrada, caminho;
    private GroundingDados mapeador;

    public MapeadorDados(String entrada, String saida) {
        this.entrada = entrada;
        this.caminho = saida.substring(0, saida.lastIndexOf(File.separatorChar)) + File.separatorChar;
        this.mapeador = new GroundingDadosImpl(entrada);
    }

    /**
     * Inicia o grounding de dados, que obtem um objeto do tipo Document (DOM), testa se o elemento
     * types existe no documento WSDL importado e chama o metodo que percorre os esquemas embutidos
     * em types.
     */
    public boolean processar() {
        try {
            Document documento = Leitor.obterDocument(entrada);
            NodeList schema = documento.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "schema");
            
            if (schema == null) { //arquivo nao possui schema
                System.err.println("Documento fornecido nao contem um xml schema. Nao ha processamento a realizar");
                return true;
            } 
            percorrerSchema(schema);
            return true;
        } catch (ElementoNaoEsperadoException e) {
            System.err.println("Erro de logica: " + e.getMessage());
        } catch (FactoryConfigurationError e) {
            System.err.println("Nao foi possivel obter um factory para o Document Builder.");
            System.err.println("Mais detalhes: " + e.getMessage());
        }
        return false;
    }

    /**
     * Percorre cada um dos 'schemas', filhos do elemento 'types' da descricao WSDL.
     * Para cada esquema, chama a funcao adequada para tratar cada um dos elementos globais (ou seja,
     * filhos diretos de 'schema'.
     * 
     * @param types elemento types da descricao WSDL
     */
    private void percorrerSchema(NodeList schemas) throws ElementoNaoEsperadoException {
        StringBuilder saida = new StringBuilder();
        PrintWriter pw;
        
        int n = schemas.getLength();
        for (int i = 0; i < n; i++) {
            Node esquema = schemas.item(i);
            
            pw = null;
            
                
                List<Node> elementos = new ArrayList<>();
                //percorrer os elementos filhos de schema
                for (int j = 0; j < esquema.getChildNodes().getLength(); j++) {
                    Node item = esquema.getChildNodes().item(j);
                    if (item.getNodeType() == Node.ELEMENT_NODE) {
                        elementos.add(item);
                    }
                }
                String nomeOntologia = getNomeOntologia(esquema);
                //gerar cabecalho
                saida.append(declararCabecalho(esquema, nomeOntologia));
                
                
                
                for (Node elemento : elementos) {
                    String nomeElemento = elemento.getLocalName();

                    switch (nomeElemento) {
                        case "element": {
                            saida.append(mapeador.mapearElement(elemento));
                            break;
                        }
                        case "simpleType": {
                            saida.append(mapeador.mapearSimpleType(elemento));
                            break;
                        }
                        case "complexType": {
                            saida.append(mapeador.mapearComplexType(elemento));
                            break;
                        }
                        case "attribute": {
                            saida.append(mapeador.mapearAttribute(elemento));
                            break;
                        }
                        case "attributeGroup": {
                            saida.append(mapeador.mapearAttributeGroup(elemento));
                            break;
                        }
                        case "group": {
                            saida.append(mapeador.mapearGroup(elemento));
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                }
                
                try {
                    
                    String arquivoSaida = this.caminho.concat(nomeOntologia + ".wsml");
                    pw = new PrintWriter(arquivoSaida, "UTF-8");
                    pw.write(saida.toString());
                    saida.delete(0, saida.length());
                } 
                catch (FileNotFoundException | UnsupportedEncodingException ex) {
                    Logger.getLogger(MapeadorDados.class.getName()).log(Level.SEVERE, null, ex);
                } 
                finally {
                    pw.close();
                }
                contador++;
        }
    }

    /**
     * Realiza uma busca pelo nome local do namespace alvo. Caso encontre, atribui esse nome a 
     * ontologia ad-hoc criada.
     * 
     * @param esquema o nodo que representa o elemento schema
     * @return um nome para a ontologia
     */
    private String getNomeOntologia(Node esquema) {
        Node targetNamespace = esquema.getAttributes().getNamedItem("targetNamespace");
        NamedNodeMap atributos = esquema.getAttributes();
        
        for (int i = 0; i < atributos.getLength(); i++) {
            Node a = atributos.item(i);
            
            if (a.isSameNode(targetNamespace)) {
                continue;
            } //ignorar o proprio targetNamespace
            if (a.getNodeValue().equals(targetNamespace.getNodeValue())) {
                return a.getLocalName();
            }
        }
        return "onto" + contador;
    }

    /**
     * Metodo auxiliar para gerar o cabecalho do elemento Ontology a ser construido.
     * 
     * @param esquema o nodo do qual a ontologia sera gerada
     * @return um cabecalho para o elemento Ontology escrito WSML "surface"
     */
    private String declararCabecalho(Node esquema, String nomeOntologia) {
        StringBuilder saida = new StringBuilder();
        String targetNamespace = esquema.getAttributes().getNamedItem("targetNamespace").getNodeValue();
        
        //declarar variante
        saida.append("wsmlVariant _\"").append(VarianteWSML.Flight.IRI()).append("\"\n");
        
        //declarar namespaces
        saida.append("namespace {").append("_\"").append(targetNamespace).append("#\",\n");
        //Obter demais namespaces
        saida.append("\t").append("dc\t_\"http://purl.org/dc/elements/1.1#\",\n");
        saida.append("\t").append("wsml\t_\"http://www.wsmo.org/wsml/wsml-syntax#\"\n");
        saida.append("}\n\n");
        //declarar cabecalho do artefato
        saida.append("ontology _\"").append(targetNamespace).append("\"\n");
        saida.append("\t").append("annotations").append("\n");
        saida.append("\t\t").append("dc#title").append(" hasValue \"").append("ontologia ad-hoc ").append(nomeOntologia).append("\"\n");
        saida.append("\t\t").append("dc#creator").append(" hasValue \"").append("FASSW").append("\"\n");
        //TODO usar o metodo getCanonicalPath para obter o nome completo
        File arquivo = new File(entrada);
        try {
            saida.append("\t\t").append("dc#source").append(" hasValue \"").append(arquivo.getCanonicalPath()).append("\"\n");
        } catch (IOException ex) {
            Logger.getLogger(MapeadorDados.class.getName()).log(Level.SEVERE, null, ex);
        }
        saida.append("\t").append("endAnnotations").append("\n\n");
        
        return saida.toString();
    }
}
