package fassw;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * 
 * @author Murilo Honorio
 * @version 0.0
 */
public class GroundingDados {
    
    private String WSDLTypes;
    static StringBuilder artefato = new StringBuilder();
    static Integer variavelNomeId = 1; //um contador que estabelece um nome unico para cade elemento que nao possua um nome
    Document documento;

    public GroundingDados(String nomeDoArquivo) {
        this.WSDLTypes = nomeDoArquivo;
    }

    /**
     * Inicia o grounding de dados, segue os seguintes passos:
     * - Cria uma instancia de um factory que nos fornece um document builder
     * - Obter uma instancia do document builder
     * - Realiza o parse do arquivo recebido como parametro pelo construtor da classe
     * - Chama o metodo que percorre os esquemas contidos no arquivo
     */
    public void inicializar() {
        
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            documento = db.parse(new File(WSDLTypes));

            Element types = documento.getDocumentElement();
            percorrerSchemas(types);

        } catch (FactoryConfigurationError e) {
            System.err.println("Nao foi possivel obter um factory para o Document Builder.");
            System.err.println("Mais detalhes: " + e.getMessage());
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
    }

    /**
     * Percorre cada um dos 'schemas', filhos do elemento 'types' da descricao WSDL.
     * Para cada esquema, chama a funcao adequada para tratar cada um dos elementos globais (ou seja,
     * filhos diretos de 'schema'.
     * 
     * @param types elemento types da descricao WSDL
     */
    private void percorrerSchemas(Element types) {
        Node esquema = types.getFirstChild();
        
        while (esquema != null) {
            String namespace = esquema.getNamespaceURI();
            
            if (namespace != null && namespace.equals("http://www.w3.org/2001/XMLSchema")) {
                Node elemento = esquema.getFirstChild();
                
                while (elemento != null) {
                    //percorrer apenas os elementos globais
                    if (elemento.getNodeType() == Node.ELEMENT_NODE) {
                        String nomeElemento = elemento.getLocalName();
                        
                        switch (nomeElemento) {
                            case "element" :
                                mapearElement(elemento);
                                break;
                            case "simpleType" :
                                transformarSimpleType(elemento);
                                break;
                            case "complexType" :
                                transformarComplexType(elemento);
                                break;
                            case "attribute" :
                                transformarAttribute(elemento);
                                break;
                            case "attributeGroup" :
                                transformarAttributeGroup(elemento);
                                break;
                            case "group" :
                                transformarGroup(elemento);
                                break;
                            default :
                                break;
                        }
                    }
                    elemento = elemento.getNextSibling();
                }
            }
            esquema = esquema.getNextSibling();
        }
    }
    
    private String transformarComplexType(Node elemento) {
        StringBuilder sb = new StringBuilder();
        String nomeDoElemento;
        Node name = elemento.getAttributes().getNamedItem("name");
        Node id = elemento.getAttributes().getNamedItem("id");
        
        //verificar se o nome eh nulo, tentar pelo id
        if (name == null) {
            if (id == null) { 
                nomeDoElemento = "complexType_" + gerarID();
                name = documento.createAttribute("name");
                name.setNodeValue(nomeDoElemento);
                elemento.getAttributes().setNamedItem(name);
            }
            else {
                nomeDoElemento = id.getNodeValue();
                name = documento.createAttribute("name");
                name.setNodeValue(nomeDoElemento);
                elemento.getAttributes().setNamedItem(name);
            }
        }
        else {
            nomeDoElemento = name.getNodeValue();
        }
        sb.append("concept ").append(nomeDoElemento).append("\n");
        
        //percorrer os atributos e gerar annotations
        sb.append("\t").append("annotations").append("\n");
        sb.append("\t\t").append("xmlType hasValue ").append("\"complexType\"").append("\n");
        Node atributo;
        
        atributo = elemento.getAttributes().getNamedItem("abstract");
        if (atributo != null) 
            sb.append("\t\t").append("abstract hasValue \"").append(atributo.getNodeValue()).append("\"\n");
        
        atributo = elemento.getAttributes().getNamedItem("mixed");
        if (atributo != null)
            sb.append("\t\t").append("mixed hasValue \"").append(atributo.getNodeValue()).append("\"\n");
            
        //TODO implementar transformacoes das restricoes/extensoes dentro de 'block' e 'final'
        atributo = elemento.getAttributes().getNamedItem("block");
        if (atributo != null)
            sb.append("\t\t").append("block hasValue \"").append(atributo.getNodeValue()).append("\"\n");
            
        atributo = elemento.getAttributes().getNamedItem("final");
        if (atributo != null) 
            sb.append("\t\t").append("final hasValue \"").append(atributo.getNodeValue()).append("\"\n");
        
        Node filho = elemento.getFirstChild();
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE && "annotation".equals(filho.getLocalName())) {
                sb.append(transformarAnnotation(filho, "\t"));
                break;
            }
            filho = filho.getNextSibling();
        }
        
        sb.append("\t").append("endAnnotations").append("\n");
        
        filho = elemento.getFirstChild();
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE) {
                String componente = filho.getLocalName();
                
                switch (componente) {
                    case "simpleContent":
                        sb.append(" subConceptOf ");
                        Node base = filho.getAttributes().getNamedItem("base");
                        sb.append(transformarElementoBase(base));
                        break;
                    case "complexContent":
                        sb.append(transformarComplexContent(filho));
                        break;
                    
                }
            }
            filho = filho.getNextSibling();
        }
        
        System.out.println(sb.toString());
        return sb.toString();
    }

    //garantido que nao eh nulo
    private String transformarSimpleType(Node elemento) {
        StringBuilder sb = new StringBuilder();
        String nomeDoElemento;
        Node name = elemento.getAttributes().getNamedItem("name");

        if (name == null) {
            nomeDoElemento = "simpleType_" + gerarID(); //convencao para atributos sem nome
            name = documento.createAttribute("name");
            name.setNodeValue(nomeDoElemento);
            elemento.getAttributes().setNamedItem(name);
        } 
        else {
            nomeDoElemento = name.getNodeValue();
        }
        sb.append("concept ").append(nomeDoElemento);

        //determinar qual o subcomponente
        Node filho = elemento.getFirstChild();
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE) {
                String componente = filho.getLocalName();
                
                switch (componente) {
                    case "restriction":
                        sb.append(" subConceptOf ");
                        Node base = filho.getAttributes().getNamedItem("base");
                        sb.append(transformarElementoBase(base));
                        break;
                    case "union":
                        String nomeUnion = "union_" + gerarID();
                        sb.append(" subConceptOf ");
                        sb.append(nomeUnion);
                        
                        Node id = documento.createAttribute("id");
                        id.setNodeValue(nomeUnion);
                        filho.getAttributes().setNamedItem(id);
                        
                        break;
                    
                }
            }
            filho = filho.getNextSibling();
        }
        sb.append("\n");

        filho = elemento.getFirstChild();
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE) {
                String nomeComponente = filho.getLocalName();

                switch (nomeComponente) {
                    case "annotation":
                        sb.append("annotations\n");
                        sb.append(transformarAnnotation(filho, "\t"));
                        sb.append("endAnnotations\n");
                        break;
                    case "restriction":
                        sb.append(transformarRestriction(filho, nomeDoElemento, "\t"));
                        break;
                    case "list":
                        sb.append(transformarList(filho, "\t")).append("\n");
                        break;
                    case "union":
                        sb.append(transformarUnion(filho, "\t"));
                        break;
                    default:
                        break;
                }
            }
            filho = filho.getNextSibling();
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    private String transformarRestriction(Node elemento, String nomeElementoPai, String indentacao) {
        StringBuilder sb = new StringBuilder();
        Node id = elemento.getAttributes().getNamedItem("id");

        sb.append(indentacao).append("axiom ");
        if (id == null) { 
            //restriction SEM atributo id
            String idRestriction = gerarID() + "_constraint";
            id = documento.createAttribute("id");
            id.setNodeValue(idRestriction);
            elemento.getAttributes().setNamedItem(id);
            sb.append(idRestriction).append("\n");
        } else {
            //restriction COM atributo id
            sb.append(id.getNodeValue()).append("\n");
        }

        Node filho = elemento.getFirstChild();
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE
                    && filho.getLocalName() != null
                    && filho.getLocalName().equals("annotation")) {
                sb.append(indentacao).append("annotations\n");
                sb.append(transformarAnnotation(filho, indentacao + "\t"));
                sb.append(indentacao).append("endAnnotations\n");
            }
            filho = filho.getNextSibling();
        }

        sb.append(indentacao).append("\tdefinedBy\n");
        sb.append(indentacao).append("\t\t").append("!- naf (\n");
        sb.append(indentacao).append("\t\t").append("?X memberOf ");

        Node base = elemento.getAttributes().getNamedItem("base");
        sb.append(transformarElementoBase(base)).append("\n");
        
        sb.append(indentacao).append("\t\t\t").append("and ?X memberOf ").append(nomeElementoPai).append(" ?\n\n");

        filho = elemento.getFirstChild();
        boolean primeiroEnum = true;
        
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE) {
                String nomeComponente = filho.getLocalName();
                Node value = filho.getAttributes().getNamedItem("value");
                

                if (value != null) {
                    String valor = value.getNodeValue();

                    switch (nomeComponente) {
                        case "minExclusive":
                            sb.append(indentacao).append("\t\t").append("and X > ").append(valor).append(" ?\n");
                            break;
                        case "minInclusive":
                            sb.append(indentacao).append("\t\t").append("and X >= ").append(valor).append(" ?\n");
                            break;
                        case "maxExclusive":
                            sb.append(indentacao).append("\t\t").append("and X < ").append(valor).append(" ?\n");
                            break;
                        case "maxInclusive":
                            sb.append(indentacao).append("\t\t").append("and X <= ").append(valor).append(" ?\n");
                            break;
                        case "totalDigits": //criar daqui para baixo
                            break;
                        case "fractionDigits":
                            break;
                        case "length":
                            break;
                        case "minLength":
                            break;
                        case "maxLength":
                            break;
                        case "enumeration":
                            sb.append(indentacao).append("\t\t");
                            
                            if (primeiroEnum == true) {
                                primeiroEnum = false;
                                sb.append("and ( X hasValue '").append(valor).append("'-\n");
                            }
                            else {
                                sb.deleteCharAt(sb.length()-5);
                                sb.append("or X hasValue '").append(valor).append("' )\n");
                            }
                            break;
                        case "whiteSpace":
                            break;
                        case "pattern":
                            break;
                        default:
                            break;
                    }
                }
            }
            filho = filho.getNextSibling();
        }
        sb.append(indentacao).append("\t)\n");
        return sb.toString();
    }

    /**
     * Processa o elemento 'annotation' e seus filhos 'documentation' e 'appinfo'
     * 
     * @param elemento
     * @param indentacao
     * @return 
     */
    private String transformarAnnotation(Node elemento, String indentacao) {
        StringBuilder sb = new StringBuilder();
        String identificador = obterID(elemento);
        
        sb.append(indentacao).append("\tannotationId hasValue \"").append(identificador).append("\"\n");
        
        Node filho = elemento.getFirstChild();
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE) {
                String nomeComponente = filho.getLocalName();
                
                switch (nomeComponente) {
                    case "appinfo":
                        sb.append(indentacao).append("\t").append(transformarAppInfo(filho, indentacao + "\t"));
                        break;
                    case "documentation":
                        sb.append(indentacao).append("\t").append(transformarDocumentation(filho, indentacao + "\t"));
                        break;
                    default:
                        break;
                }
            }
            filho = filho.getNextSibling();
        }
        return sb.toString();
    }

    private String transformarAppInfo(Node elemento, String indentacao) {
        StringBuilder sb = new StringBuilder();
        Node source = elemento.getAttributes().getNamedItem("source");
        
        if (source != null) {
            sb.append("appinfoSource hasValue _\"").append(source.getNodeValue()).append("\"\n");
            sb.append(indentacao);
        }
        
        sb.append("appinfo hasValue \"").append(elemento.getTextContent().trim()).append("\"\n");
        
        return sb.toString();
    }

    private String transformarDocumentation(Node elemento, String indentacao) {
        StringBuilder sb = new StringBuilder();
        Node source = elemento.getAttributes().getNamedItem("source");
        Node language = elemento.getAttributes().getNamedItem("xml:lang");
        
        if (source != null) {
            sb.append("documentationSource hasValue _\"").append(source.getNodeValue()).append("\"\n");
            sb.append(indentacao);
        }
        
        if (language != null) {
            sb.append("dc#language hasValue \"").append(language.getNodeValue()).append("\"\n");
            sb.append(indentacao);
        }
        
        sb.append("documentation hasValue \"").append(elemento.getTextContent().trim()).append("\"\n");
        
        return sb.toString();
    }

    /**
     * Realiza a converao entre os tipos de dados embutidos xml (built-in datatypes xml) e os tipos
     * de dados embutidos em WSML. Trata-se apenas de conversao de formatacao, ja que o WSML suporta
     * todos os tipos de dados XML.
     * Conforme a lista em http://www.wsmo.org/TR/d16/d16.1/v1.0/, apêndice B.1, 
     * usamos a sintaxe resumida para os tipos de dados.
     * 
     * @param elemento
     * @return resultado a avaliacao
     */
    private String transformarElementoBase(Node elemento) {
        String resultado;
        String[] base = elemento.getNodeValue().split(":");
        String namespace = elemento.lookupNamespaceURI(base[0]);

        if (namespace.equals("http://www.w3.org/2001/XMLSchema")) {
            resultado = "_" + base[1].toLowerCase();
        } else {
            resultado = namespace + "#" + base[1];
        }
        return resultado;
    }

    private String transformarUnion(Node elemento, String indentacao) {
        StringBuilder sb = new StringBuilder();
        String id = obterID(elemento);
        sb.append("\nconcept ").append(id).append(" subConceptOf ").append("{ ");
        
        Node memberTypes = elemento.getAttributes().getNamedItem("memberTypes");
        
        if (memberTypes != null) {
            String[] membros = memberTypes.getNodeValue().split(" ");
            
            for (String m : membros) {
                sb.append(m).append(", ");
            }
        }
        
        Node filho = elemento.getFirstChild();
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE && filho.getLocalName().equals("simpleType")) {
                String nomeSimpleType = "item_in_union_" + gerarID();
                Node name = documento.createAttribute("name");
                
                name.setNodeValue(nomeSimpleType);
                filho.getAttributes().setNamedItem(name);
                sb.append(nomeSimpleType).append(", ");
            }
            filho = filho.getNextSibling();
        }
        
        filho = elemento.getFirstChild();
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE && filho.getLocalName().equals("annotation")) {
                sb.append(transformarAnnotation(filho, indentacao + "\t"));
                break;
            }
            filho = filho.getNextSibling();
        }
        
        sb.deleteCharAt(sb.length()-2);
        sb.append("}").append("\n");
        
        
        filho = elemento.getFirstChild();
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE && filho.getLocalName().equals("simpleType")) {
                transformarSimpleType(filho);
            }
            filho = filho.getNextSibling();
        }
        
        return sb.toString();
    }



    private Object transformarList(Node elemento, String indentacao) {
        StringBuilder sb = new StringBuilder();
        sb.append(indentacao).append("hasValues (1*) ofType ");
        
        Node itemType = elemento.getAttributes().getNamedItem("itemType");

        if (itemType != null) {
            sb.append(itemType.getNodeValue()).append("\n");
        } else {
            Node filho = elemento.getFirstChild();
            
            while (filho != null) {
                if (filho.getNodeType() == Node.ELEMENT_NODE) {
                    Node name = filho.getAttributes().getNamedItem("name");
                    String nomeSimpleType = "list_simple_type_" + gerarID();
                    
                    if (name == null) {
                        name = documento.createAttribute("name");
                        name.setNodeValue(nomeSimpleType);
                        filho.getAttributes().setNamedItem(name);
                    }
                    
                    sb.append(nomeSimpleType).append("\n\n");
                    sb.append(transformarSimpleType(filho));
                }
                filho = filho.getNextSibling();
            } 
        }
        return sb.toString();
    }

    //TODO Completar esse aqui e os demais que seguem a mesma estrutura
    private String transformarComplexContent(Node elemento) {
        StringBuilder saida = new StringBuilder();
        
        String identificador = obterID(elemento);
        
        
        return saida.toString();
    }
    
    /**
     * O elemento element define um elemento. 
     * Verifica se eh filho do schema
     * 
     * @param elemento
     * @return 
     */
    private String mapearElement(Node elemento) {
        StringBuilder sb = new StringBuilder();
        
        if (elemento.getParentNode().getLocalName().equals("schema")) {
            //caso seja filho de schema, o atributo name é requerido
            //GERAR CONCEPT
        }
        else {
            //significa que eh filho de um choice, all, sequence ou group
            //adicionar um nome ou id
        }
        
        Node type = elemento.getAttributes().getNamedItem("type");
        if (type != null) {
            //significa que o elemento eh baseado em um tipo embutido, ou em um simpleType ou complexType definido externamente
            //se for um simpleType ou complexType, deve ter um name. Se tem um name, ele eh o nome do conceito
            String tipo = type.getNodeValue();
        }
        else { //significa que 
            
        }
        
        
        return sb.toString();
    }

    private void transformarAttribute(Node elemento) {
        throw new UnsupportedOperationException("Ainda nao implementado."); //To change body of generated methods, choose Tools | Templates.
    }

    private void transformarAttributeGroup(Node elemento) {
        throw new UnsupportedOperationException("Ainda nao implementado."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Extension e Restriction sao elementos que sao filhos de simpleType, simpleContent e complexContent.
     * Ambos possuem os mesmo filhos: group, all, choice e sequence.
     * O comportamento para o mapeamento de ambos eh o mesmo: criar um conceito e atributos referentes aos filhos
     * 
     * @param elemento 
     */
    private String transformarExtensionRestriction(Node elemento) {
        StringBuilder saida = new StringBuilder();
        saida.append("concept ").append(obterNome(elemento)).append("\n");
        saida.append(processarAnnotations(elemento));


        Node filho = elemento.getFirstChild();

        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE) {
                String idFilho = obterID(filho);

                switch (filho.getLocalName()) {
                    case "group":
                        saida.append("\t").append("groupAttribute ofType ").append(idFilho).append("\n\n");
                        saida.append(transformarGroup(filho));
                        break;
                    case "all":
                        saida.append("\t").append("allAttribute ofType ").append(idFilho).append("\n\n");
                        saida.append(transformarAllChoiceSequence(filho));
                        break;
                    case "choice":
                        saida.append("\t").append("choiceAttribute ofType ").append(idFilho).append("\n\n");
                        saida.append(transformarAllChoiceSequence(filho));
                        break;
                    case "sequence":
                        saida.append("\t").append("sequenceAttribute ofType ").append(idFilho).append("\n\n");
                        saida.append(transformarAllChoiceSequence(filho));
                        break;
                    default:
                        break;
                }
            }
            filho = filho.getNextSibling();
        }
        System.out.println(saida.toString());
        return saida.toString();
    }
    
    /**
     * O elemento Group eh utilizado para definir um grupo de elementos para serem usados em definicoes complexType.
     * Nao pode ter um atributo name e um atributo ref ao mesmo tempo. 
     * - Caso tenha um 'name', seus fihos serao 'all', 'choice' ou 'sequence'.
     * - Caso tenha um 'ref', faz referencia a outro grupo.
     * 
     * @param elemento 
     */
    private String transformarGroup(Node elemento) {
        StringBuilder saida = new StringBuilder();        
        saida.append("concept ").append(obterNome(elemento)).append("\n");       
        saida.append(processarAnnotations(elemento));
        
        if (elemento.getAttributes().getNamedItem("ref") == null) {
            Node filho = elemento.getFirstChild();

            while (filho != null) {
                if (filho.getNodeType() == Node.ELEMENT_NODE) {
                    String idFilho = obterID(filho);
                    
                    switch (filho.getLocalName()) {
                        case "all" :
                            saida.append("\t").append("allAttribute ofType ").append(idFilho).append("\n\n");
                            saida.append(transformarAllChoiceSequence(filho));
                            break;
                        case "choice" :
                            saida.append("\t").append("choiceAttribute ofType ").append(idFilho).append("\n\n");
                            saida.append(transformarAllChoiceSequence(filho));
                            break;
                        case "sequence" :
                            saida.append("\t").append("sequenceAttribute ofType ").append(idFilho).append("\n\n");
                            saida.append(transformarAllChoiceSequence(filho));
                            break;
                        default :
                            break;
                    }
                }
                filho = filho.getNextSibling();
            }
        }
        else {
            Node ref = elemento.getAttributes().getNamedItem("ref");
            saida.append("\t").append("attributeRef ofType ").append(ref.getNodeValue()).append("\n");
        }

        System.out.println(saida.toString());
        return saida.toString();
    }

    /**
     * Metodo auxiliar que retorna a String que representa o atributo id. Caso o elemento nao possua
     * o atributo id, um id unico global dentro do esquema eh gerado.
     * 
     * @param elemento
     * @return 
     */
    private String obterID(Node elemento) {
        Node id = elemento.getAttributes().getNamedItem("id");
        
        if (id == null) {
            id = documento.createAttribute("id");
            id.setNodeValue("id." + gerarID());
            elemento.getAttributes().setNamedItem(id);
        }
        return elemento.getAttributes().getNamedItem("id").getNodeValue();
    }

    private Object processarAnnotations(Node elemento) {
        StringBuilder saida = new StringBuilder();
        saida.append("\t").append("annotations").append("\n");
        saida.append("\t\t").append("xmlType hasValue \"").append(elemento.getLocalName()).append("\"").append("\n");
        
        if (elemento.getAttributes().getNamedItem("base") != null) {
            saida.append("\t\t").append("baseType hasvalue \"");
            saida.append(elemento.getAttributes().getNamedItem("base").getNodeValue()).append("\"\n");
        }
        if (elemento.getAttributes().getNamedItem("maxOccurs") != null) {
            saida.append("\t\t").append("maxOccurs hasvalue \"");
            saida.append(elemento.getAttributes().getNamedItem("maxOccurs").getNodeValue()).append("\"\n");
        }
        if (elemento.getAttributes().getNamedItem("minOccurs") != null) {
            saida.append("\t\t").append("minOccurs hasvalue \"");
            saida.append(elemento.getAttributes().getNamedItem("minOccurs").getNodeValue()).append("\"\n");
        }
        
        Node filho = elemento.getFirstChild();
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE && filho.getLocalName().equals("annotation")) {
                saida.append(transformarAnnotation(filho, "\t"));
            }
            filho = filho.getNextSibling();
        }
        
        saida.append("\t").append("endAnnotations").append("\n");
        
        return saida.toString();
        
    }

    private String transformarAllChoiceSequence(Node elemento) {
        StringBuilder saida = new StringBuilder();
        String nome = obterNome(elemento);
        
        saida.append("concept ").append(nome).append("\n");
        saida.append(processarAnnotations(elemento));
        
        //percorrer os filhos, todos element
        Node filho = elemento.getFirstChild();
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE) {
                saida.append("\t").append(obterNome(filho)).append(" ofType ").append(obterID(filho)).append("\n");
            }
            filho = filho.getNextSibling();
        }
        
        return saida.toString();
    }
    
    
    private String transformarGenerico(Node elemento) {
        StringBuilder saida = new StringBuilder();
        
        return saida.toString();
    }

    /**
     * Metodo auxiliar que retorna o valor do atributo 'name' de um elemento, caso exista.
     * Caso contrario, gera o atributo 'name', com valor que eh a combinacao do elemento e seus id.
     * 
     * @param elemento
     * @return 
     */
    private String obterNome(Node elemento) {
        String id = obterID(elemento);
        Node name = elemento.getAttributes().getNamedItem("name");
        
        if (name == null) {
            name = documento.createAttribute("name");
            name.setNodeValue(elemento.getLocalName() + "_" + id);
            elemento.getAttributes().setNamedItem(name);
        }
        return elemento.getAttributes().getNamedItem("name").getNodeValue();
    }
    
     /**
     * Metodo auxiliar que retorna numeros em sequencia para gerar ids unicos.
     */
    private String gerarID() {
        variavelNomeId++;
        return variavelNomeId.toString();
    }
}
