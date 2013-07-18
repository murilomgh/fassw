package fassw;

import fassw.util.ElementoNaoEsperadoException;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Implementacao dos metodos para gerar fragmentos WSML que representam conceitos WSMO, incluindo
 * diversos metodos auxiliares.
 * 
 * @author Murilo Honorio
 * @version 0.0
 */
public class GroundingDadosImpl implements GroundingDados {
    
    private static int CONTADOR = 1; //utilizado para obter ids unicos, sendo increentado por getIdUnico()
    private final String XMLSchemaNamespace;

    public GroundingDadosImpl(String xmlns) {
        this.XMLSchemaNamespace = "http://www.w3.org/2001/XMLSchema";
    }
    
    //--- INICIO DAS FUNCOES AUXILIARES ---//
    /**
     * Retorna o valor do atributo <b>id</b> do elemento.
     * Caso o atributo nao exista, ele eh criado.
     * 
     * @param elemento
     * @param documento
     * @return 
     */
    private String getIdUnico(Node elemento) {
        Node id = elemento.getAttributes().getNamedItem("id");
        
        if (id == null) {
            id = elemento.getOwnerDocument().createAttribute("id");
            id.setNodeValue("id." + CONTADOR);
            CONTADOR++;
            elemento.getAttributes().setNamedItem(id);
        }
        return elemento.getAttributes().getNamedItem("id").getNodeValue();
    }
    
    /**
     * Retorna o valor do atributo 'name' de um elemento, caso exista.
     * Caso contrario, gera o atributo 'name', com valor que eh a combinacao do elemento e seus id.
     * O atributo 'nam' eh adicionado ao elemento caso ele nao possua um elemento 'ref'
     * 
     * @param elemento
     * @return 
     */
    private String getNome(Node elemento) {
        String id = getIdUnico(elemento);
        Node name = elemento.getAttributes().getNamedItem("name");
        Node ref = elemento.getAttributes().getNamedItem("ref");
        
        if (name == null) {
            String nome = elemento.getLocalName() + "." + id;
            
            if (ref == null) { //se nao tem ref, adiciona o atributo name ao elemento
                name = elemento.getOwnerDocument().createAttribute("name");
                name.setNodeValue(nome);
                elemento.getAttributes().setNamedItem(name);
                return elemento.getAttributes().getNamedItem("name").getNodeValue();
            }
            else { //se tem ref, apenas retorna o nome gerado
                return nome;
            }
        }
        return elemento.getAttributes().getNamedItem("name").getNodeValue();
    }
    
     /**
     * Metodo auxiliar que gera cadeias em WSML para QNames ou built-in types.
     * 
     * @param baseType um atributo base ou type, cujo valor sera lido
     * @param pai o nodo pai que contem o atributo
     * @return uma representacao WSML do valor do nodo
     */
    private String transformarBaseOuType(Node baseType, Node pai) {
        boolean tipoEmbutido = isTipoEmbutido(baseType.getNodeValue(), pai);
        if (tipoEmbutido) { //garantidamente tem namespace
            String[] valorBase = baseType.getNodeValue().split(":");
            return "_" + valorBase[1].toLowerCase();
        } else {
            return baseType.getNodeValue().replace(":", "#");
        }
    }
    
    /**
     * Reinicia contador usado para gerar id unicos. Metodo util para as baterias de testes.
     */
    public void resetContador() {
        CONTADOR = 1;
    }
    
    /**
     * Metodo auxiliar que recupera qualquer elemento que estiver dentro do schema, desde que seja do
     * mesmo tipo.
     * 
     * @param ref o QName do elemento procurado
     * @param pai o nodo pai, o qual contem o atributo
     * @return o elemento encontrado ou null
     */
    private Node recuperarElemento(String ref, String tipoElemento, Node pai) {
        //obter lista com todos os elementos do mesmo tipo do nodo pai
        NodeList elementos = pai.getOwnerDocument().getElementsByTagNameNS(XMLSchemaNamespace, tipoElemento);
        //percorrer os elementos em busca 
        for (int i = 0; i < elementos.getLength(); i++) {
            Node name = elementos.item(i).getAttributes().getNamedItem("name");
            if (name != null) {
                if (name.getNodeValue().equals(ref)) {
                    return elementos.item(i);
                }
            }
        }
        return null;
    }
    
    /**
     * Processa qualquer elemento XML Schema e retorna as propriedades nao funcionais do elemento
     * em WSML.
     * 
     * @param elemento qualquer elemento XML Schema mapeado
     * @return uma cadeia contendo as anotacoes
     */
    private String getAnnotations(Node elemento) throws ElementoNaoEsperadoException {
        StringBuilder saida = new StringBuilder();
        saida.append("\t").append("annotations").append("\n");
        saida.append("\t\t").append("xmlType hasValue \"").append(elemento.getLocalName()).append("\"").append("\n");

        //todos
        if (elemento.getAttributes().getNamedItem("id") != null) { 
            saida.append("\t\t").append("id hasValue \"");
            saida.append(elemento.getAttributes().getNamedItem("id").getNodeValue()).append("\"\n");
        }
        if (elemento.getAttributes().getNamedItem("base") != null) { 
            saida.append("\t\t").append("baseType hasValue \"");
            saida.append(elemento.getAttributes().getNamedItem("base").getNodeValue()).append("\"\n");
        }
        //attribute, element
        if (elemento.getAttributes().getNamedItem("default") != null) {
            saida.append("\t\t").append("default hasValue \"");
            saida.append(elemento.getAttributes().getNamedItem("default").getNodeValue()).append("\"\n");
        }
        //attribute, element
        if (elemento.getAttributes().getNamedItem("fixed") != null) {
            saida.append("\t\t").append("fixed hasValue \"");
            saida.append(elemento.getAttributes().getNamedItem("fixed").getNodeValue()).append("\"\n");
        }
        //attribute, element
        if (elemento.getAttributes().getNamedItem("form") != null) {
            saida.append("\t\t").append("form hasValue \"");
            saida.append(elemento.getAttributes().getNamedItem("form").getNodeValue()).append("\"\n");
        }
        //element
        if (elemento.getAttributes().getNamedItem("maxOccurs") != null) {
            saida.append("\t\t").append("maxOccurs hasValue \"");
            saida.append(elemento.getAttributes().getNamedItem("maxOccurs").getNodeValue()).append("\"\n");
        }
        //element
        if (elemento.getAttributes().getNamedItem("minOccurs") != null) {
            saida.append("\t\t").append("minOccurs hasValue \"");
            saida.append(elemento.getAttributes().getNamedItem("minOccurs").getNodeValue()).append("\"\n");
        }
        //element
        if (elemento.getAttributes().getNamedItem("nillable") != null) {
            saida.append("\t\t").append("nillable hasValue \"");
            saida.append(elemento.getAttributes().getNamedItem("nillable").getNodeValue()).append("\"\n");
        }
        //element
        if (elemento.getAttributes().getNamedItem("abstract") != null) {
            saida.append("\t\t").append("abstract hasValue \"");
            saida.append(elemento.getAttributes().getNamedItem("abstract").getNodeValue()).append("\"\n");
        }
        //attribute
        if (elemento.getAttributes().getNamedItem("use") != null) {
            saida.append("\t\t").append("use hasValue \"");
            saida.append(elemento.getAttributes().getNamedItem("use").getNodeValue()).append("\"\n");
        }
        //any processContents
        if (elemento.getAttributes().getNamedItem("processContents") != null) {
            saida.append("\t\t").append("processContents hasValue \"");
            saida.append(elemento.getAttributes().getNamedItem("processContents").getNodeValue()).append("\"\n");
        } 
        //element
        if (elemento.getAttributes().getNamedItem("block") != null) {
            saida.append("\t\t").append("block hasValue ");
            
            Node block = elemento.getAttributes().getNamedItem("block");
            String listaMembros = block.getNodeValue().replace(":", "#");
            String[] membros = listaMembros.split(" ");
            
            if (membros.length == 1) { //#all
                saida.append("\"").append(membros[0]).append("\"\n");
            }
            else { //extension, restriction, substitution
                saida.append("{ ");
                for (String membro : membros) {
                    saida.append(membro).append(", ");
                }
                saida.deleteCharAt(saida.length()-2);
                saida.append("}").append("\n");
            }
        }
        //element
        if (elemento.getAttributes().getNamedItem("final") != null) {
            saida.append("\t\t").append("final hasValue ");

            Node finalAttr = elemento.getAttributes().getNamedItem("final");
            String listaMembros = finalAttr.getNodeValue().replace(":", "#");
            String[] membros = listaMembros.split(" ");

            if (membros.length == 1) { //#all
                saida.append("\"").append(membros[0]).append("\"\n");
            } else { //extension, restriction
                saida.append("{ ");
                for (String membro : membros) {
                    saida.append(membro).append(", ");
                }
                saida.deleteCharAt(saida.length() - 2);
                saida.append("}").append("\n");
            }
        }

        //mapear demais anotacoes
        Node filho = elemento.getFirstChild();
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE && filho.getLocalName().equals("annotation")) {
                saida.append(mapearAnnotation(filho));
            }
            filho = filho.getNextSibling();
        }

        saida.append("\t").append("endAnnotations").append("\n");

        return saida.toString();
    }
    
    /**
     * Metodo auxiliar que detecta se um dado QName eh um tipo de dados embutido (built-in datatype) XML.
     * 
     * @param QName valor do atributo <b>type</b> ou <b>base</b>
     * @param pai nodo do elemento que contem o atributo
     * @return true se for um built-in datatype que pode ser mapeado para WSML
     */
    private boolean isTipoEmbutido(String QName, Node pai) {
        String[] valor = QName.split(":");
        //Identificar se o QName eh prefixado ou nao
        if (valor.length == 2) {
            //QName eh prefixado, verificar se o namespace eh o padrao
            String namespace = pai.lookupNamespaceURI(valor[0]);
            if (namespace != null && namespace.equals(XMLSchemaNamespace)) {
                //identificar os tipos embutidos que tem conversao para WSML
                switch (valor[1]) {
                    case "string":
                        return true;
                    case "decimal":
                        return true;
                    case "integer":
                        return true;
                    case "float":
                        return true;
                    case "double":
                        return true;
                    case "boolean":
                        return true;
                    case "dateTime":
                        return true;
                    case "time":
                        return true;
                    case "date":
                        return true;
                    case "gYearMonth":
                        return true;
                    case "gYear":
                        return true;
                    case "gMonthDay":
                        return true;
                    case "gDay":
                        return true;
                    case "gMonth":
                        return true;
                    case "hexBinary":
                        return true;
                    default:
                        return false;
                }
            }
            else {
                //refere-se a algum outro tipo em outro namespace
                return false;
            }
        }
        else {
            //QName nao prefixado, portanto nao eh um tipo embutido
            return false;
        }
    }
    
    //--- FIM DAS FUNCOES AUXILIARES ---//
    
    /**
     * O elemento annotation eh um elemento que especifica comentarios dentro do schema.
     * 
     * <p>annotation mapeia para propriedades nao funcionais do conceito WSMO ao qual pertence.</p>
     * 
     * <p> Mais detalhes em: {@link http://www.w3schools.com/schema/el_annotation.asp Documentação XML Schema} </p>
     * @param raiz o nodo cujo nome local eh annotation
     * @return um fragmento WSMO escrito em WSML
     * @throws ElementoNaoEsperadoException caso seja passado outro elemento como parametro
     */
    @Override
    public String mapearAnnotation(Node raiz) throws ElementoNaoEsperadoException {
        //verificar se o elemento esta correto
        if (raiz.getLocalName().equals("annotation") == false) {
            throw new ElementoNaoEsperadoException("annotation", raiz.getLocalName());
        }
        StringBuilder saida = new StringBuilder();
        String id = getIdUnico(raiz);
        
        saida.append("\t").append("\tannotationId hasValue \"").append(id).append("\"\n");
        
        Node filho = raiz.getFirstChild();
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE) {
                String nomeComponente = filho.getLocalName();
                
                switch (nomeComponente) {
                    case "appinfo":
                        saida.append(mapearAppinfo(filho));
                        break;
                    case "documentation":
                        saida.append(mapearDocumentation(filho));
                        break;
                    default:
                        break;
                }
            }
            filho = filho.getNextSibling();
        }
        return saida.toString();
    }

    /**
     * O elemento appinfo especifica informacoes usadas por aplicacoes para processar instrucoes.
     * 
     * <p>appinfo mapeia para propriedades nao funcionais do conceito WSMO ao qual pertence.</p>
     * 
     * <p> Mais detalhes em: {@link http://www.w3schools.com/schema/el_appinfo.asp Documentação XML Schema} </p>
     * @param raiz o nodo cujo nome local eh appinfo
     * @return um fragmento WSMO escrito em WSML
     * @throws ElementoNaoEsperadoException caso seja passado outro elemento como parametro
     * @see http://msdn.microsoft.com/en-us/library/ms256134.aspx
     */
    @Override
    public String mapearAppinfo(Node raiz) throws ElementoNaoEsperadoException {
        //verificar se o elemento esta correto
        if (raiz.getLocalName().equals("appinfo") == false) {
            throw new ElementoNaoEsperadoException("appinfo", raiz.getLocalName());
        }
        StringBuilder saida = new StringBuilder();
        Node source = raiz.getAttributes().getNamedItem("source");
        
        if (source != null) {
            saida.append("\t").append("\t").append("appinfoSource hasValue _\"").append(source.getNodeValue()).append("\"\n");
        }
        saida.append("\t").append("\t").append("appinfo hasValue \"").append(raiz.getTextContent().trim()).append("\"\n");
        return saida.toString();
    }

    /**
     * O elemento documentation define comentarios de texto em um schema.
     * 
     * <p>documentation mapeia para propriedades nao funcionais do conceito WSMO ao qual pertence.</p>
     * 
     * <p> Mais detalhes em: {@link http://www.w3schools.com/schema/el_documentation.asp Documentação XML Schema} </p>
     * @param raiz o nodo cujo nome local eh documentation
     * @return um fragmento WSMO escrito em WSML
     * @throws ElementoNaoEsperadoException caso seja passado outro elemento como parametro
     * @see 
     */
    @Override
    public String mapearDocumentation(Node raiz) throws ElementoNaoEsperadoException {
        //verificar se o elemento esta correto
        if (raiz.getLocalName().equals("documentation") == false) {
            throw new ElementoNaoEsperadoException("documentation", raiz.getLocalName());
        }
        StringBuilder saida = new StringBuilder();
        Node source = raiz.getAttributes().getNamedItem("source");
        Node language = raiz.getAttributes().getNamedItem("xml:lang");
        
        if (source != null) {
            saida.append("\t").append("\t").append("documentationSource hasValue _\"").append(source.getNodeValue()).append("\"\n");
        }
        
        if (language != null) {
            saida.append("\t").append("\t").append("dc#language hasValue \"").append(language.getNodeValue()).append("\"\n");
        }
        saida.append("\t").append("\t").append("documentation hasValue \"").append(raiz.getTextContent().trim()).append("\"\n");
        return saida.toString();
    }
    
    /**
     * O elemento attributeGroup eh utilizado para agrupar um conjunto de declaracoes de atributo, 
     * de modo que eles podem ser incorporados como um grupo em definicoes de elementos complexType.
     * 
     * <p>AttributeGroup eh mapeado para um conceito WSMO, que pode ser usado como alicerce para 
     * outros conceitos mais complexos. Os atributos <b>name</b> e <b> ref </b> sao mutuamente exclusivos. </p>
     * 
     * <p> Mais detalhes em: {@link http://www.w3schools.com/schema/el_attributegroup.asp Documentação XML Schema} </p>
     * @param raiz o nodo cujo nome local eh attributeGroup
     * @return um fragmento WSMO escrito em WSML
     * @throws ElementoNaoEsperadoException caso seja passado outro elemento como parametro
     */
    @Override
    public String mapearAttributeGroup(Node raiz) throws ElementoNaoEsperadoException {
        if (raiz.getLocalName().equals("attributeGroup") == false) {
            throw new ElementoNaoEsperadoException("attributeGroup", raiz.getLocalName());
        }
        StringBuilder saida = new StringBuilder();
        String id = getIdUnico(raiz);
        Node ref = raiz.getAttributes().getNamedItem("ref");
        
        if (ref == null) { //nao possui o atributo ref, portanto eh filho de schema
            Node name = raiz.getAttributes().getNamedItem("name");
            Node filho = raiz.getFirstChild();
            
            saida.append("concept ").append(name.getNodeValue()).append("\n");
            saida.append(getAnnotations(raiz));
            
            List<Node> filhos = new ArrayList<>(); //guardar filhos para processar posteriormente
            while (filho != null) {
                if (filho.getNodeType() == Node.ELEMENT_NODE) {
                    String idFilho = getIdUnico(filho);

                    switch (filho.getLocalName()) {
                        case "attributeGroup": //garantidamente possui atributo ref
                            saida.append("\t").append(mapearAttributeGroup(filho)).append("\n");
                            break;
                        case "attribute":
                            if (filho.getAttributes().getNamedItem("ref") == null) {
                                filhos.add(filho);
                            }
                            saida.append("\t").append(getNome(filho)).append(" ofType ").append(idFilho).append("\n");
                            break;
                        default:
                            break;
                    }
                }
                filho = filho.getNextSibling();
            }
            saida.append("\n");
            for (Node elem : filhos) {
                saida.append(mapearAttribute(elem));
            }
        }
        else { //possui o atributo ref, portanto eh filho de complexType
            saida.append(id).append(" ofType ").append(ref.getNodeValue());
        }
        //System.out.println(saida.toString());
        return saida.toString();
    }

    /**
     * O elemento attribute define um atributo.
     * 
     * <p>Atributos em XML Schema sempre sao definidos como simpleTypes. Essa definicao pode ocorrer
     * por meio de referencias, usando o atributo <b>type</b> ou embutindo o simpleType como filho
     * do proprio attribute. Se usar <b>ref</b> o atributo refere-se a um atributo com name.</p>
     * 
     * <p> Mais detalhes em: {@link http://www.w3schools.com/schema/el_attribute.asp Documentação XML Schema} </p>
     * @param raiz o nodo cujo nome local eh attribute
     * @return um fragmento WSMO escrito em WSML
     * @throws ElementoNaoEsperadoException caso seja passado outro elemento como parametro
     */
    @Override
    public String mapearAttribute(Node raiz) throws ElementoNaoEsperadoException {
        if (raiz.getLocalName().equals("attribute") == false) {
            throw new ElementoNaoEsperadoException("attribute", raiz.getLocalName());
        }
        StringBuilder saida = new StringBuilder();
        String id = getIdUnico(raiz);
        String nome = getNome(raiz);
        Node ref = raiz.getAttributes().getNamedItem("ref");
        Node type = raiz.getAttributes().getNamedItem("type");
        
        saida.append("concept ").append(nome).append("\n");
        saida.append(getAnnotations(raiz));
        
        if (ref == null) { 
            if (type == null) { //significa que possui um filho simpleType, ou seja, definido internamente
                Node filho = raiz.getFirstChild();
                
                while (filho != null) {
                    //percorrer apenas os elementos
                    if (filho.getNodeType() == Node.ELEMENT_NODE) {
                        if (filho.getLocalName().equals("simpleType")) {
                            String idFilho = getIdUnico(filho);
                            saida.append("\t").append("attributeSimpleType").append(" ofType ").append(idFilho).append("\n\n");
                            saida.append(mapearSimpleType(filho));
                            break;
                        }
                    }
                    filho = filho.getNextSibling();
                }
                
            } 
            else { //significa que possui um type, que pode referenciar um tipo embutido ou um simpleType externamente
                boolean tipoEmbutido = isTipoEmbutido(type.getNodeValue(), raiz);
                
                //identificar se a referencia eh a um tipo embutido
                if (tipoEmbutido) {
                    String[] valorTipo = type.getNodeValue().split(":");
                    saida.append("\t").append("value").append(" ofType _").append(valorTipo[1].toLowerCase()).append("\n");
                }
                else {
                    String valorTipo = type.getNodeValue().replace(":", "#");
                    saida.append("\t").append("value").append(" ofType ").append(valorTipo).append("\n");
                }
            }
        }
        else { //referencia algum outro atributo que possui name
            String nomeRef = transformarBaseOuType(ref, raiz);
            saida.append("\t").append("refAttribute ofType ").append(nomeRef).append("\n");
        }
        saida.append("\n");
        //System.out.println(saida.toString());
        return saida.toString();
    }
    
    /**
     * O elemento simpleType define um tipo simples e especifica as restrições e as informações 
     * sobre os valores de atributos ou elementos somente-texto.
     * 
     * <p>Um simpleType tem sempre um dos seguintes componentes filhos: um <b>restriction</b>, um 
     * <b>list</b> ou um <b>union</b>. Um atributo <b>name</b> eh obrigatorio se o simpleType for
     * filho de <b>schema</b>.</p>
     * 
     * <p>O mapeamento de um simpleType pode resultar na geracao de um ou mais componentes WSMO. O
     * atributo <b>id</b> eh usado para estabelecer a ligacao entre os elementos do simpleType.
     * Caso ele nao exista, eh criado automaticamente por meio de funcao auxiliar.</p>
     * 
     * <p> Mais detalhes em: {@link http://www.w3schools.com/schema/el_simpletype.asp Documentação XML Schema} </p>
     * @param raiz o nodo cujo nome local eh simpleType
     * @return um fragmento WSMO escrito em WSML
     * @throws ElementoNaoEsperadoException caso seja passado outro elemento como parametro
     */
    @Override
    public String mapearSimpleType(Node raiz) throws ElementoNaoEsperadoException {
        if (raiz.getLocalName().equals("simpleType") == false) {
            throw new ElementoNaoEsperadoException("simpleType", raiz.getLocalName());
        }
        StringBuilder saida = new StringBuilder();
        String nome = getNome(raiz);

        saida.append("concept ").append(nome);

        //determinar qual o subcomponente
        Node filho = raiz.getFirstChild();
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE) {
                String componente = filho.getLocalName();
                
                switch (componente) {
                    case "restriction":
                        saida.append(" subConceptOf ");
                        Node base = filho.getAttributes().getNamedItem("base"); //Atributo base eh obrigatorio
                        saida.append(transformarBaseOuType(base, raiz)).append("\n");
                        break;
                    case "list":
                        saida.append("\n");
                        break;
                    case "union":
                        String nomeUnion = getNome(filho);
                        saida.append(" subConceptOf ").append(nomeUnion).append("\n");
                        break;
                    default:
                        break;
                }
            }
            filho = filho.getNextSibling();
        }
        saida.append(getAnnotations(raiz));

        //gerar conceito do subcomponente
        filho = raiz.getFirstChild();
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE) {
                String nomeComponente = filho.getLocalName();

                switch (nomeComponente) {
                    case "restriction":
                        saida.append("\n").append(mapearRestriction(filho)).append("\n");
                        break;
                    case "list":
                        saida.append(mapearList(filho)).append("\n");
                        break;
                    case "union":
                        saida.append("\n").append(mapearUnion(filho)).append("\n");
                        break;
                    default:
                        break;
                }
            }
            filho = filho.getNextSibling();
        }
        
        //System.out.println(saida.toString());
        return saida.toString();
    }

    /**
     * O elemento restriction determina restrições sobre definicoes de simpleType, simpleContent ou
     * complexContent.
     * 
     * <p>O atributo <b>base</b> eh requerido e indica o nome de tipo embutido, <b>simpleType</b> ou
     * <b>complexType</b> que eh restringido. Um <b>restriction</b> possui um ou mais elementos 
     * filhos <b>facets</b> XML.</p>
     * 
     * <p>O mapeamento de um restriction resulta na geracao de um axioma WSMO, cuja expressao varia de
     * acordo com as facetas.
     * <br>Facets mapeados: enumeration; minExclusive; minInclusive; maxExclusive; maxInclusive.
     * <br>Facets nao-mapeados: totalDigits; fractionDigits; length; minLength; maxLength; whiteSpace; pattern.</p>
     * 
     * <p> Mais detalhes em: {@link http://www.w3schools.com/schema/el_restriction.asp Documentação XML Schema} </p>
     * @param raiz o nodo cujo nome local eh restriction
     * @return um fragmento WSMO escrito em WSML
     * @throws ElementoNaoEsperadoException caso seja passado outro elemento como parametro
     */
    @Override
    public String mapearRestriction(Node raiz) throws ElementoNaoEsperadoException {
        //verificar se o elemento esta correto
        if (raiz.getLocalName().equals("restriction") == false) {
            throw new ElementoNaoEsperadoException("restriction", raiz.getLocalName());
        }
        StringBuilder saida = new StringBuilder();
        String id = getIdUnico(raiz);
        String elementoPai = raiz.getParentNode().getLocalName();
        
        if (elementoPai.equals("complexContent")) {
            String nome = getNome(raiz);
            saida.append("concept ").append(nome).append("\n");
            saida.append(getAnnotations(raiz));

            //mapear atributos
            Node filho = raiz.getFirstChild();
            while (filho != null) {
                if (filho.getNodeType() == Node.ELEMENT_NODE) {
                    String componente = filho.getLocalName();

                    switch (componente) {
                        case "group":
                            saida.append("\t").append("groupAttribute ofType ").append(getIdUnico(filho)).append("\n");
                            break;
                        case "all":
                            saida.append("\t").append("allAttribute ofType ").append(getIdUnico(filho)).append("\n");
                            break;
                        case "choice":
                            saida.append("\t").append("choiceAttribute ofType ").append(getIdUnico(filho)).append("\n");
                            break;
                        case "sequence":
                            saida.append("\t").append("sequenceAttribute ofType ").append(getIdUnico(filho)).append("\n");
                            break;
                        case "attribute":
                            saida.append("\t").append(getNome(filho)).append(" ofType ").append(getIdUnico(filho)).append("\n");
                            break;
                        case "attributeGroup":
                            saida.append("\t").append(getNome(filho)).append(" ofType ").append(getIdUnico(filho)).append("\n");
                            break;
                        default:
                            break;
                    }
                }
                filho = filho.getNextSibling();
            }
            //gerar conceitos filhos
            filho = raiz.getFirstChild();
            while (filho != null) {
                if (filho.getNodeType() == Node.ELEMENT_NODE) {
                    String componente = filho.getLocalName();

                    switch (componente) {
                        case "group":
                            saida.append("\n").append(mapearGroup(filho));
                            break;
                        case "all":
                            saida.append("\n").append(mapearAll(filho));
                            break;
                        case "choice":
                            saida.append("\n").append(mapearChoice(filho));
                            break;
                        case "sequence":
                            saida.append("\n").append(mapearSequence(filho));
                            break;
                        case "attribute":
                            saida.append("\n").append(mapearAttribute(filho));
                            break;
                        case "attributeGroup":
                            saida.append("\n").append(mapearAttributeGroup(filho));
                            break;
                        default:
                            break;
                    }
                }
                filho = filho.getNextSibling();
            }
            //System.out.println(saida.toString());
            return saida.toString();
        }
        if (elementoPai.equals("simpleContent")) {
            String nome = getNome(raiz);
            saida.append("concept ").append(nome).append("\n");
            saida.append(getAnnotations(raiz));

            //mapear atributos
            Node filho = raiz.getFirstChild();
            while (filho != null) {
                if (filho.getNodeType() == Node.ELEMENT_NODE) {
                    String componente = filho.getLocalName();

                    switch (componente) {
                        case "attribute":
                            saida.append("\t").append(getNome(filho)).append(" ofType ").append(getIdUnico(filho)).append("\n");
                            break;
                        case "attributeGroup":
                            saida.append("\t").append(getNome(filho)).append(" ofType ").append(getIdUnico(filho)).append("\n");
                            break;
                        default:
                            break;
                    }
                }
                filho = filho.getNextSibling();
            }
            //gerar conceitos filhos
            filho = raiz.getFirstChild();
            while (filho != null) {
                if (filho.getNodeType() == Node.ELEMENT_NODE) {
                    String componente = filho.getLocalName();

                    switch (componente) {
                        case "attribute":
                            saida.append("\n").append(mapearAttribute(filho));
                            break;
                        case "attributeGroup":
                            saida.append("\n").append(mapearAttributeGroup(filho));
                            break;
                        default:
                            break;
                    }
                }
                filho = filho.getNextSibling();
            }
        }
        //gerar axioma para o simpleType ou simpleContent
        Node base = raiz.getAttributes().getNamedItem("base");
        
        saida.append("axiom ").append(id).append("\n");
        saida.append(getAnnotations(raiz));
        saida.append("\t").append("definedBy").append("\n");
        saida.append("\t\t").append("!- naf (").append("\n");
        saida.append("\t\t").append("?X memberOf ");
        saida.append(transformarBaseOuType(base, raiz)).append("\n");
        saida.append("\t\t").append("and ?X memberOf ").append(getNome(raiz.getParentNode())).append(" ?").append("\n");

        Node filho = raiz.getFirstChild();
        boolean primeiroEnum = true;
        
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE) {
                String nomeComponente = filho.getLocalName();
                Node value = filho.getAttributes().getNamedItem("value");

                if (value != null) {
                    String valor = value.getNodeValue();

                    switch (nomeComponente) {
                        case "minExclusive":
                            saida.append("\t\t").append("and X > ").append(valor).append(" ?\n");
                            break;
                        case "minInclusive":
                            saida.append("\t\t").append("and X >= ").append(valor).append(" ?\n");
                            break;
                        case "maxExclusive":
                            saida.append("\t\t").append("and X < ").append(valor).append(" ?\n");
                            break;
                        case "maxInclusive":
                            saida.append("\t\t").append("and X <= ").append(valor).append(" ?\n");
                            break;
                        case "totalDigits":
                            //NAO DEFINIDO
                            break;
                        case "fractionDigits":
                            //NAO DEFINIDO
                            break;
                        case "length":
                            //NAO DEFINIDO
                            break;
                        case "minLength":
                            //NAO DEFINIDO
                            break;
                        case "maxLength":
                            //NAO DEFINIDO
                            break;
                        case "enumeration":
                            saida.append("\t\t");
                            
                            if (primeiroEnum == true) {
                                primeiroEnum = false;
                                saida.append("and ( X hasValue '").append(valor).append("' \n");
                            }
                            else {
                                saida.deleteCharAt(saida.length()-4);
                                saida.append("or X hasValue '").append(valor).append("' )\n");
                            }
                            break;
                        case "whiteSpace":
                            //NAO DEFINIDO
                            break;
                        case "pattern":
                            //NAO DEFINIDO
                            break;
                        default:
                            break;
                    }
                }
            }
            filho = filho.getNextSibling();
        }
        saida.append("\t\t").append(").").append("\n");
        return saida.toString();
    }

    /**
     * O elemento list define um elemento simpleType como uma lista de valores de um tipo de dados especificado.
     * 
     * <p>Um <b>list</b> pode conter elementos <b>simpleType</b> definidos no proprio escopo da lista ou 
     * definidos em outro lugar no <b>schema</b>. No primeiro caso, nao ha nome associado e ele eh
     * gerado conforme convencao. No segundo caso, cada item possui um nome associado.</p>
     * <p>O resultado da transformacao da lista esta definido dentro do conceito gerado pelo simpleType
     * que a contem.</p>
     * 
     * <p> Mais detalhes em: {@link http://www.w3schools.com/schema/el_list.asp Documentação XML Schema} </p>
     * @param raiz o nodo cujo nome local eh restriction
     * @return um fragmento WSMO escrito em WSML
     * @throws ElementoNaoEsperadoException caso seja passado outro elemento como parametro
     */
    @Override
    public String mapearList(Node raiz) throws ElementoNaoEsperadoException {
        //verificar se o elemento esta correto
        if (raiz.getLocalName().equals("list") == false) {
            throw new ElementoNaoEsperadoException("list", raiz.getLocalName());
        }
        StringBuilder saida = new StringBuilder();
        saida.append("\t").append("hasValues (1 *) ofType ");
        
        Node itemType = raiz.getAttributes().getNamedItem("itemType");
        if (itemType != null) { //se itemType eh requerido, significa que nao tem simpleType embutido
            String item = transformarBaseOuType(itemType, raiz);
            saida.append(item).append("\n");
        } 
        else {
            Node filho = raiz.getFirstChild();
            
            while (filho != null) {
                if (filho.getNodeType() == Node.ELEMENT_NODE) {
                    Node name = filho.getAttributes().getNamedItem("name");
                    String nomeSimpleType = "list_simple_type_" + getIdUnico(filho);
                    
                    if (name == null) {
                        name = raiz.getOwnerDocument().createAttribute("name");
                        name.setNodeValue(nomeSimpleType);
                        filho.getAttributes().setNamedItem(name);
                    }
                    saida.append(nomeSimpleType).append("\n\n");
                    saida.append(mapearSimpleType(filho));
                }
                filho = filho.getNextSibling();
            } 
        }
        return saida.toString();
    }

    /**
     * O elemento union define um elemento simpleType como uma colecao de valores dos tipos de dados especificados.
     * 
     * <p>A definição de um <b>union</b> considera tres tipos possiveis que podem ser usados​​. O 
     * primeiro sao built-in data types, que sao diretamente suportados em WSMO sem alteracao. O 
     * segundo sao <b>simpleTypes</b> definidos em outras partes do schema e portanto tem um atributo 
     * nome que pode ser usado para identifica-los. O terceiro sao <b>simpleTypes</b> definidos no 
     * ambito do proprio <b>union</b>, sao <b>simpleTypes</b> que nao tem um atributo nome definido.</p>
     * 
     * <p> Mais detalhes em: {@link http://www.w3schools.com/schema/el_union.asp Documentação XML Schema} </p>
     * @param raiz o nodo cujo nome local eh union
     * @return um fragmento WSMO escrito em WSML
     * @throws ElementoNaoEsperadoException caso seja passado outro elemento como parametro
     */
    @Override
    public String mapearUnion(Node raiz) throws ElementoNaoEsperadoException {
        //verificar se o elemento esta correto
        if (raiz.getLocalName().equals("union") == false) {
            throw new ElementoNaoEsperadoException("union", raiz.getLocalName());
        }
        StringBuilder saida = new StringBuilder();
        String id = getIdUnico(raiz);
        saida.append("concept ").append(id).append(" subConceptOf ").append("{ ");
        
        Node memberTypes = raiz.getAttributes().getNamedItem("memberTypes");
        if (memberTypes != null) {
            String listaMembros = memberTypes.getNodeValue().replace(":", "#");
            String[] membros = listaMembros.split(" ");
            
            for (String membro : membros) {
                saida.append(membro).append(", ");
            }
        }
        
        Node filho = raiz.getFirstChild();
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE && filho.getLocalName().equals("simpleType")) {
                String nomeSimpleType = "item_in_union_" + getIdUnico(filho);
                Node name = raiz.getOwnerDocument().createAttribute("name");
                
                name.setNodeValue(nomeSimpleType);
                filho.getAttributes().setNamedItem(name);
                saida.append(nomeSimpleType).append(", ");
            }
            filho = filho.getNextSibling();
        }
        saida.deleteCharAt(saida.length()-2);
        saida.append("}").append("\n");
        saida.append(getAnnotations(raiz));
        
        filho = raiz.getFirstChild();
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE && filho.getLocalName().equals("simpleType")) {
                mapearSimpleType(filho);
            }
            filho = filho.getNextSibling();
        }
        return saida.toString();
    }

    /**
     * O elemento element define um elemento. Hahaha...
     * 
     * <p>Element pode possuir um nome ou nao. O nome eh obrigatorio se for filho de <b>schema</b>.
     * Pode ser definido por um <b>simpleType</b> ou um <b>complexType</b>, quer seja internamente 
     * (filho) ou externamente (atraves do atributo <b>type</b>). No caso trivial, o <b>simpleType</b>
     * eh um tipo embutido.</p>
     * 
     * <p>Se houver um atributo <b>ref</b> estiver presente, os elementos complexType, simpleType, key,
     * keyref e unique, e os atributos nillable, default, fixed, form, block e type nao podem estar.
     * Ele faz referencia a outro <b>element</b>. <b>ref</b> nao eh permitido em filhos de <b>schema</b>.
     * </p>
     * 
     * <p>Element mapeia para um conceitos WSMO e seus filhos tambem, de acordo com as outras 
     * transformações.</p>
     * 
     * <p> Mais detalhes em: {@link http://www.w3schools.com/schema/el_element.asp Documentação XML Schema} </p>
     * @param raiz o nodo cujo nome local eh element
     * @return um fragmento WSMO escrito em WSML
     * @throws ElementoNaoEsperadoException caso seja passado outro elemento como parametro
     * @see http://msdn.microsoft.com/en-us/library/ms256118.aspx
     */
    @Override
    public String mapearElement(Node raiz) throws ElementoNaoEsperadoException {
        //verificar se o elemento esta correto
        if (raiz.getLocalName().equals("element") == false) {
            throw new ElementoNaoEsperadoException("element", raiz.getLocalName());
        }
        StringBuilder saida = new StringBuilder();
        
        String nome = getNome(raiz);
        saida.append("concept ").append(nome).append("\n");
        saida.append(getAnnotations(raiz));
        
        Node ref = raiz.getAttributes().getNamedItem("ref");
        
        if (ref == null) { //element eh filho de schema
            Node type = raiz.getAttributes().getNamedItem("type");
            Node substitutionGroup = raiz.getAttributes().getNamedItem("substitutionGroup");
            
            if (substitutionGroup != null) { //elemento definido por substitutionGroup nao eh convertido
                return "";
            }
            if (type == null) { //elemento definido internamente
                //determinar qual o subcomponente
                Node filho = raiz.getFirstChild();
                while (filho != null) {
                    if (filho.getNodeType() == Node.ELEMENT_NODE) {
                        String componente = filho.getLocalName();
                        String id = getIdUnico(filho);

                        switch (componente) {
                            case "simpleType":
                                saida.append("\t").append("simpleTypeAttribute ofType ").append(id).append("\n");
                                saida.append("\n").append(mapearSimpleType(filho));
                                break;
                            case "complexType":
                                saida.append("\t").append("complexTypeAttribute ofType ").append(id).append("\n");
                                saida.append("\n").append(mapearComplexType(filho));
                                break;
                            default:
                                break;
                        }
                    }
                    filho = filho.getNextSibling();
                }
            }
            else { //elemento definido por type
                String QName = transformarBaseOuType(type, raiz);
                boolean tipoEmbutido = isTipoEmbutido(type.getNodeValue(), raiz);
                if (tipoEmbutido) {
                    saida.append("\t").append("refAttribute").append(" ofType ").append(QName).append("\n\n");
                }
                else {
                    Node elementoRef = recuperarElemento(QName, "simpleType", raiz);
                    if (elementoRef != null) { //eh simpleType
                        String id = getIdUnico(elementoRef);
                        saida.append("\t").append(QName).append(" ofType ").append(id).append("\n");
                        saida.append("\n").append(mapearElement(elementoRef));
                    }
                    else { //eh complexType
                        elementoRef = recuperarElemento(QName, "complexType", raiz);
                        String id = getIdUnico(elementoRef);
                        saida.append("\t").append(QName).append(" ofType ").append(id).append("\n");
                    }
                }
            }
        }
        else { //elemento eh definido por outro element
            Node elementoRef = recuperarElemento(ref.getNodeValue(), "element", raiz);
            String QName = ref.getNodeValue().replace(":", "#");
            
            if (elementoRef != null) { //o ref esta dentro do documento
                String id = getIdUnico(elementoRef);
                saida.append("\t").append(QName).append(" ofType ").append(id).append("\n");
                //mapear element? NAO POIS ESTA NO SCHEMA, PODE ACABAR DUPLICANDO
            }
            else { //nao encontrou o elemento indicado por ref
                saida.append("\t").append("refElementAttribute").append(" ofType ").append(QName).append("\n");
            }
        }
        //System.out.println(saida.toString());
        return saida.toString();
    }

    /**
     * O elemento complexType define um tipo complexo, que eh um elemento XML que contem outros
     * elementos e/ou atributos.
     * 
     * <p>Um elemento <b>complexType</b> pode ser definido por um agrupamento particular de elementos
     * filhos usando palavras-chave, como <b>sequence</b> ou <b>choice</b>, para definir a estrutura
     * exigida desses elementos filhos.</p>
     * <p> Um <b>complexType</b> também pode ser definido por restricoes ou extensoes sobre um 
     * <b>simpleType</b> ou <b>complexType</b> usando os elementos <b>simpleContent</b> ou 
     * <b>complexContent</b>.</p>
     * 
     * <p>complexType mapeia para um conceito WSMO.</p>
     * 
     * <p> Mais detalhes em: {@link http://www.w3schools.com/schema/el_complextype.asp Documentação XML Schema} </p>
     * @param raiz o nodo cujo nome local eh complexType
     * @return um fragmento WSMO escrito em WSML
     * @throws ElementoNaoEsperadoException caso seja passado outro elemento como parametro
     */
    @Override
    public String mapearComplexType(Node raiz) throws ElementoNaoEsperadoException {
        //verificar se o elemento esta correto
        if (raiz.getLocalName().equals("complexType") == false) {
            throw new ElementoNaoEsperadoException("complexType", raiz.getLocalName());
        }
        StringBuilder saida = new StringBuilder();
        
        String nome = getNome(raiz);
        saida.append("concept ").append(nome).append("\n");
        saida.append(getAnnotations(raiz));
        
        //mapear atributos
        Node filho = raiz.getFirstChild();
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE) {
                String componente = filho.getLocalName();
                
                switch (componente) {
                    case "simpleContent":
                        break;
                    case "complexContent":
                        break;
                    case "group":
                        saida.append("\t").append("groupAttribute ofType ").append(getIdUnico(filho)).append("\n");
                        break;
                    case "all":
                        saida.append("\t").append("allAttribute ofType ").append(getIdUnico(filho)).append("\n");
                        break;
                    case "choice":
                        saida.append("\t").append("choiceAttribute ofType ").append(getIdUnico(filho)).append("\n");
                        break;
                    case "sequence":
                        saida.append("\t").append("sequenceAttribute ofType ").append(getIdUnico(filho)).append("\n");
                        break;
                    case "attribute":
                        saida.append("\t").append(getNome(filho)).append(" ofType ").append(getIdUnico(filho)).append("\n");
                        break;
                    case "attributeGroup":
                        saida.append("\t").append(getNome(filho)).append(" ofType ").append(getIdUnico(filho)).append("\n");
                        break;
                    default:
                        break;
                }
            }
            filho = filho.getNextSibling();
        }
        //gerar conceitos filhos
        filho = raiz.getFirstChild();
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE) {
                String componente = filho.getLocalName();
                
                switch (componente) {
                    case "simpleContent": //processar extension ou restriction
                        saida.append(mapearSimpleContent(filho));
                        break;
                    case "complexContent":
                        saida.append(mapearComplexContent(filho));
                        break;
                    case "group":
                        saida.append("\n").append(mapearGroup(filho));
                        break;
                    case "all":
                        saida.append("\n").append(mapearAll(filho));
                        break;
                    case "choice":
                        saida.append("\n").append(mapearChoice(filho));
                        break;
                    case "sequence":
                        saida.append("\n").append(mapearSequence(filho));
                        break;
                    case "attribute":
                        saida.append("\n").append(mapearAttribute(filho));
                        break;
                    case "attributeGroup":
                        saida.append("\n").append(mapearAttributeGroup(filho));
                        break;
                    default:
                        break;
                }
            }
            filho = filho.getNextSibling();
        }
        //System.out.println(saida.toString());
        return saida.toString() + "\n";
    }

    /**
     * O elemento simpleContent contem extensoes ou restricoes sobre um complexType ou um simpleType.
     * 
     * <p>simpleContent mapeia para um atributo do conceito WSMO gerado a partir do seu elemento pai.</p>
     * 
     * <p> Mais detalhes em: {@link http://www.w3schools.com/schema/el_simplecontent.asp Documentação XML Schema} </p>
     * @param raiz o nodo cujo nome local eh simpleContent
     * @return um fragmento WSMO escrito em WSML
     * @throws ElementoNaoEsperadoException caso seja passado outro elemento como parametro
     */
    @Override
    public String mapearSimpleContent(Node raiz) throws ElementoNaoEsperadoException {
        //verificar se o elemento esta correto
        if (raiz.getLocalName().equals("simpleContent") == false) {
            throw new ElementoNaoEsperadoException("simpleContent", raiz.getLocalName());
        }
        StringBuilder saida = new StringBuilder();
        Node filho = raiz.getFirstChild();
            
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE) {
                String elementoFilho = filho.getLocalName();
                saida.append("\t").append("simpleContentAttribute ofType ").append(getIdUnico(filho)).append("\n");

                if (elementoFilho.equals("extension")) {
                    saida.append("\n").append(mapearExtension(filho));
                }
                else { //restriction em um simpleContent resulta no mesmo mapeamento de simpleType
                    saida.append("\n").append(mapearRestriction(filho));
                }
            }
            filho = filho.getNextSibling();
        }
        return saida.toString();
    }

     /**
     * O elemento complexContent define extensoes ou restricoes sobre um complexType que contenha
     * conteudo misto ou somente elementos.
     * 
     * <p>complexContent mapeia para um atributo do conceito WSMO gerado a partir do seu elemento pai.</p>
     * 
     * <p> Mais detalhes em: {@link http://www.w3schools.com/schema/el_complexcontent.asp Documentação XML Schema} </p>
     * @param raiz o nodo cujo nome local eh complexContent
     * @return um fragmento WSMO escrito em WSML
     * @throws ElementoNaoEsperadoException caso seja passado outro elemento como parametro
     */
    @Override
    public String mapearComplexContent(Node raiz) throws ElementoNaoEsperadoException {
        //verificar se o elemento esta correto
        if (raiz.getLocalName().equals("complexContent") == false) {
            throw new ElementoNaoEsperadoException("complexContent", raiz.getLocalName());
        }
        StringBuilder saida = new StringBuilder();
        Node filho = raiz.getFirstChild();

        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE) {
                String elementoFilho = filho.getLocalName();
                saida.append("\t").append("complexContentAttribute ofType ").append(getIdUnico(filho)).append("\n");

                if (elementoFilho.equals("extension")) {
                    saida.append("\n").append(mapearExtension(filho));
                }
                else { //restriction em um simpleContent resulta no mesmo mapeamento de simpleType
                    saida.append("\n").append(mapearRestriction(filho));
                }
            }
            filho = filho.getNextSibling();
        }
        return saida.toString();
    }
    
    /**
     * O elemento extension estende um elemento simpleType ou complexType existente.
     * 
     * <p>extension mapeia para um conceito WSMO.</p>
     * 
     * <p> Mais detalhes em: {@link http://www.w3schools.com/schema/el_extension.asp Documentação XML Schema} </p>
     * @param raiz o nodo cujo nome local eh extension
     * @return um fragmento WSMO escrito em WSML
     * @throws ElementoNaoEsperadoException caso seja passado outro elemento como parametro
     */
    @Override
    public String mapearExtension(Node raiz) throws ElementoNaoEsperadoException {
        //EH PRATICAMENTO O MESMO DE COMPLEX TYPE
        //verificar se o elemento esta correto
        if (raiz.getLocalName().equals("extension") == false) {
            throw new ElementoNaoEsperadoException("extension", raiz.getLocalName());
        }
        StringBuilder saida = new StringBuilder();
        Node base = raiz.getAttributes().getNamedItem("base"); //base eh requerido, mas nao eh usado na transformacao
        
        String nome = getNome(raiz);
        saida.append("concept ").append(nome).append("\n");
        saida.append(getAnnotations(raiz));
        
        //mapear atributos
        Node filho = raiz.getFirstChild();
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE) {
                String componente = filho.getLocalName();
                
                switch (componente) {
                    case "group":
                        saida.append("\t").append("groupAttribute ofType ").append(getIdUnico(filho)).append("\n");
                        break;
                    case "all":
                        saida.append("\t").append("allAttribute ofType ").append(getIdUnico(filho)).append("\n");
                        break;
                    case "choice":
                        saida.append("\t").append("choiceAttribute ofType ").append(getIdUnico(filho)).append("\n");
                        break;
                    case "sequence":
                        saida.append("\t").append("sequenceAttribute ofType ").append(getIdUnico(filho)).append("\n");
                        break;
                    case "attribute":
                        saida.append("\t").append(getNome(filho)).append(" ofType ").append(getIdUnico(filho)).append("\n");
                        break;
                    case "attributeGroup":
                        saida.append("\t").append(getNome(filho)).append(" ofType ").append(getIdUnico(filho)).append("\n");
                        break;
                    default:
                        break;
                }
            }
            filho = filho.getNextSibling();
        }
        //gerar conceitos filhos
        filho = raiz.getFirstChild();
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE) {
                String componente = filho.getLocalName();
                
                switch (componente) {
                    case "group":
                        saida.append("\n").append(mapearGroup(filho));
                        break;
                    case "all":
                        saida.append("\n").append(mapearAll(filho));
                        break;
                    case "choice":
                        saida.append("\n").append(mapearChoice(filho));
                        break;
                    case "sequence":
                        saida.append("\n").append(mapearSequence(filho));
                        break;
                    case "attribute":
                        saida.append("\n").append(mapearAttribute(filho));
                        break;
                    case "attributeGroup":
                        saida.append("\n").append(mapearAttributeGroup(filho));
                        break;
                    default:
                        break;
                }
            }
            filho = filho.getNextSibling();
        }
        //System.out.println(saida.toString());
        return saida.toString();
    }

    /**
     * O elemento group define um grupo de elementos para serem usados em definicoes de complexType.
     * 
     * <p>group mapeia para um conceito WSMO.</p>
     * 
     * <p> Mais detalhes em: {@link http://www.w3schools.com/schema/el_group.asp Documentação XML Schema} </p>
     * @param raiz o nodo cujo nome local eh group
     * @return um fragmento WSMO escrito em WSML
     * @throws ElementoNaoEsperadoException caso seja passado outro elemento como parametro
     * @see http://msdn.microsoft.com/en-us/library/ms256093.aspx
     */
    @Override
    public String mapearGroup(Node raiz) throws ElementoNaoEsperadoException {
        //verificar se o elemento esta correto
        if (raiz.getLocalName().equals("group") == false) {
            throw new ElementoNaoEsperadoException("group", raiz.getLocalName());
        }
        StringBuilder saida = new StringBuilder();
        String nome = getNome(raiz);
        Node ref = raiz.getAttributes().getNamedItem("ref");

        saida.append("concept ").append(nome).append("\n");
        saida.append(getAnnotations(raiz));
        
        if (ref == null) { //possui name
            Node filho = raiz.getFirstChild();
            
            //gerar atributos do conceito
            while (filho != null) {
                if (filho.getNodeType() == Node.ELEMENT_NODE) {
                    String componente = filho.getLocalName();

                    switch (componente) {
                        case "all":
                            saida.append("\t").append("allAttribute ofType ").append(getIdUnico(filho)).append("\n");
                            break;
                        case "choice":
                            saida.append("\t").append("choiceAttribute ofType ").append(getIdUnico(filho)).append("\n");
                            break;
                        case "sequence":
                            saida.append("\t").append("sequenceAttribute ofType ").append(getIdUnico(filho)).append("\n");
                            break;
                        default:
                            break;
                    }
                }
                filho = filho.getNextSibling();
            }
            //gerar demais conceitos
            filho = raiz.getFirstChild();
            while (filho != null) {
                if (filho.getNodeType() == Node.ELEMENT_NODE) {
                    String componente = filho.getLocalName();

                    switch (componente) {
                        case "all":
                            saida.append("\n").append(mapearAll(filho));
                            break;
                        case "choice":
                            saida.append("\n").append(mapearChoice(filho));
                            break;
                        case "sequence":
                            saida.append("\n").append(mapearSequence(filho));
                            break;
                        default:
                            break;
                    }
                }
                filho = filho.getNextSibling();
            }
        }
        else { //elemento eh definido por outro group
            Node groupRef = recuperarElemento(ref.getNodeValue(), "group", raiz);
            String QName = ref.getNodeValue().replace(":", "#");
            
            if (groupRef != null) { //o ref esta dentro do documento
                String id = getIdUnico(groupRef);
                saida.append("\t").append(QName).append(" ofType ").append(id).append("\n");
                //NAO MAPEAR, POIS ESTA NA RAIZ DO SCHEMA E SERA DUPLICADO
                //saida.append("\n").append(mapearGroup(groupRef));
            }
            else { //nao encontrou o elemento indicado por ref
                saida.append("\t").append("refGroupAttribute").append(" ofType ").append(QName).append("\n");
            }
        }
        
        //System.out.println(saida.toString());
        return saida.toString();
    }

    /**
     * O elemento all especifica que cada elemento filho pode aparecer em qualquer ordem. Cada
     * elemento filho pode ocorrer zero ou uma vez.
     * 
     * <p>all mapeia para um conceito WSMO.</p>
     * 
     * <p> Mais detalhes em: {@link http://www.w3schools.com/schema/el_all.asp Documentação XML Schema} </p>
     * @param raiz o nodo cujo nome local eh all
     * @return um fragmento WSMO escrito em WSML
     * @throws ElementoNaoEsperadoException caso seja passado outro elemento como parametro
     */
    @Override
    public String mapearAll(Node raiz) throws ElementoNaoEsperadoException {
        //verificar se o elemento esta correto
        if (raiz.getLocalName().equals("all") == false) {
            throw new ElementoNaoEsperadoException("all", raiz.getLocalName());
        }
        StringBuilder saida = new StringBuilder();
        String nome = getNome(raiz);
        
        saida.append("concept ").append(nome).append("\n");
        saida.append(getAnnotations(raiz));
        
        //gerar atributos do conceito
        Node filho = raiz.getFirstChild();
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE && filho.getNodeName().equals("element")) {
                saida.append("\t").append(getNome(filho)).append(" ofType ").append(getIdUnico(filho)).append("\n");
            }
            filho = filho.getNextSibling();
        }
        //gerar demais conceitos
        filho = raiz.getFirstChild();
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE && filho.getNodeName().equals("element")) {
                saida.append("\n").append(mapearElement(filho));
            }
            filho = filho.getNextSibling();
        }
        return saida.toString();
    }

    /**
     * O elemento choice permite a somente um dos elementos contidos no seu escopo estar presente no
     * elemento que o contem.
     * 
     * <p>choice mapeia para um conceito WSMO.</p>
     * 
     * <p> Mais detalhes em: {@link http://www.w3schools.com/schema/el_choice.asp Documentação XML Schema} </p>
     * @param raiz o nodo cujo nome local eh choice
     * @return um fragmento WSMO escrito em WSML
     * @throws ElementoNaoEsperadoException caso seja passado outro elemento como parametro
     */
    @Override
    public String mapearChoice(Node raiz) throws ElementoNaoEsperadoException {
        //verificar se o elemento esta correto
        if (raiz.getLocalName().equals("choice") == false) {
            throw new ElementoNaoEsperadoException("choice", raiz.getLocalName());
        }
        StringBuilder saida = new StringBuilder();
        String nome = getNome(raiz);
        
        saida.append("concept ").append(nome).append("\n");
        saida.append(getAnnotations(raiz));
        
        //gerar atributos do conceito
        Node filho = raiz.getFirstChild();
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE) {
                String componente = filho.getLocalName();
                
                switch (componente) {
                    case "element":
                        saida.append("\t").append(getNome(filho)).append(" ofType ").append(getIdUnico(filho)).append("\n");
                        break;
                    case "group":
                        saida.append("\t").append(getNome(filho)).append(" ofType ").append(getIdUnico(filho)).append("\n");
                        break;
                    case "choice":
                        saida.append("\t").append("choiceAttribute ofType ").append(getIdUnico(filho)).append("\n");
                        break;
                    case "sequence":
                        saida.append("\t").append("sequenceAttribute ofType ").append(getIdUnico(filho)).append("\n");
                        break;
                    case "any":
                        //TODO mapear elemento Any
                        break;
                    default:
                        break;
                }
            }
            filho = filho.getNextSibling();
        }
        //gerar demais conceitos
        filho = raiz.getFirstChild();
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE) {
                String componente = filho.getLocalName();
                
                switch (componente) {
                    case "element":
                        saida.append("\n").append(mapearElement(filho));
                        break;
                    case "group":
                        saida.append("\n").append(mapearGroup(filho));
                        break;
                    case "choice":
                        saida.append("\n").append(mapearChoice(filho));
                        break;
                    case "sequence":
                        saida.append("\n").append(mapearSequence(filho));
                        break;
                    case "any":
                        //TODO mapear elemento Any
                        break;
                    default:
                        break;
                }
            }
            filho = filho.getNextSibling();
        }
        return saida.toString();
    }

     /**
     * O elemento sequence estabelece a ordem em que os elementos filhos devem aparecer. Cada elemento
     * filho pode ocorrer zero ou mais vezes.
     * 
     * <p>sequence mapeia para um conceito WSMO.</p>
     * 
     * <p> Mais detalhes em: {@link http://www.w3schools.com/schema/el_sequence.asp Documentação XML Schema} </p>
     * @param raiz o nodo cujo nome local eh sequence
     * @return um fragmento WSMO escrito em WSML
     * @throws ElementoNaoEsperadoException caso seja passado outro elemento como parametro
     */   
    @Override
    public String mapearSequence(Node raiz) throws ElementoNaoEsperadoException {
        //OBS.: identico ao group
        //verificar se o elemento esta correto
        if (raiz.getLocalName().equals("sequence") == false) {
            throw new ElementoNaoEsperadoException("sequence", raiz.getLocalName());
        }
        StringBuilder saida = new StringBuilder();
        String nome = getNome(raiz);
        
        saida.append("concept ").append(nome).append("\n");
        saida.append(getAnnotations(raiz));
        
        //gerar atributos do conceito
        Node filho = raiz.getFirstChild();
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE) {
                String componente = filho.getLocalName();
                
                switch (componente) {
                    case "element":
                        saida.append("\t").append(getNome(filho)).append(" ofType ").append(getIdUnico(filho)).append("\n");
                        break;
                    case "group":
                        saida.append("\t").append(getNome(filho)).append(" ofType ").append(getIdUnico(filho)).append("\n");
                        break;
                    case "choice":
                        saida.append("\t").append("choiceAttribute ofType ").append(getIdUnico(filho)).append("\n");
                        break;
                    case "sequence":
                        saida.append("\t").append("sequenceAttribute ofType ").append(getIdUnico(filho)).append("\n");
                        break;
                    case "any":
                        //TODO mapear elemento Any
                        break;
                    default:
                        break;
                }
            }
            filho = filho.getNextSibling();
        }
        //gerar demais conceitos
        filho = raiz.getFirstChild();
        while (filho != null) {
            if (filho.getNodeType() == Node.ELEMENT_NODE) {
                String componente = filho.getLocalName();
                
                switch (componente) {
                    case "element":
                        saida.append("\n").append(mapearElement(filho));
                        break;
                    case "group":
                        saida.append("\n").append(mapearGroup(filho));
                        break;
                    case "choice":
                        saida.append("\n").append(mapearChoice(filho));
                        break;
                    case "sequence":
                        saida.append("\n").append(mapearSequence(filho));
                        break;
                    case "any":
                        //TODO mapear elemento Any
                        break;
                    default:
                        break;
                }
            }
            filho = filho.getNextSibling();
        }
        return saida.toString();
    }
}