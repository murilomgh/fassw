package fassw;

import fassw.util.Gravador;
import fassw.util.Leitor;
import fassw.util.VarianteWSML;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
//--- Elementos de Apache Woden ---//
import org.apache.woden.schema.InlinedSchema;
import org.apache.woden.wsdl20.Description;
import org.apache.woden.wsdl20.ElementDeclaration;
import org.apache.woden.wsdl20.Interface;
import org.apache.woden.wsdl20.InterfaceMessageReference;
import org.apache.woden.wsdl20.InterfaceOperation;
import org.apache.woden.wsdl20.Service;
import org.apache.woden.wsdl20.enumeration.Direction;
import org.apache.woden.wsdl20.fragids.FragmentIdentifier;

/**
 * Classe responsavel por gerar o artefato WSMO Web Service, na sintaxe Surface, conforme os passos
 * desenvolvidos e descritos no apendice B.
 *
 * @author Murilo Honorio
 */
public class GroundingCoreografia {
    
    //conjunto das possiveis Message Exchange Patterns usadas pelas operacoes em WSDL
    private final Set<String> MEP = new HashSet<>(Arrays.asList(
            "http://www.w3.org/ns/wsdl/in-only", 
            "http://www.w3.org/ns/wsdl/robust-in-only",
            "http://www.w3.org/ns/wsdl/in-out",
            //adicionais, ver em http://www.w3.org/TR/wsdl20-additional-meps/#patterns
            "http://www.w3.org/ns/wsdl/in-opt-out", 
            "http://www.w3.org/ns/wsdl/out-only",
            "http://www.w3.org/ns/wsdl/robust-out-only",
            "http://www.w3.org/ns/wsdl/out-in",
            "http://www.w3.org/ns/wsdl/out-opt-in"));
    
    //controla o numero de variaveis compartilhadas pelas pre e pos condicoes
    private int sharedVar = 0;
    private String endPointAddress; //o endereco para acessar o servico
    //Listas de declaracoes de pre condicoes, pos condicoes e conceitos (marcados como in e out)
    List<String> preCondicoes = new ArrayList<>();
    List<String> posCondicoes = new ArrayList<>();
    List<String> conceitosInGnd = new ArrayList<>();
    List<String> conceitosInTR = new ArrayList<>();
    List<String> conceitosOut = new ArrayList<>();

    /**
     * Gera e salvar em arquivo um WebService WSMO, escrito em WSML, conforme as regras definidas no trabalho de
     * conclusao de curso. O artefato consiste em um conjunto de declaracoes, na sintaxe 'surface'.
     * 
     * @return um valor booleano indicando se o processo foi concluido com sucesso
     */
    boolean processar(String entrada, String saida) {
        StringBuilder docWSML = new StringBuilder();
        //Componentes do Woden
        Description descricao = Leitor.obterDescription(entrada); //o componentes abstratos da descricao do servico
        if (descricao == null) {
            System.err.println("Nao foi possível obter uma descricao. Documento WSDL apresenta falhas.");
            return false;
        }
        if (descricao.getServices().length == 0) {
            System.err.println("Documento WSDL fornecido nao contem elemento Service. O processamento foi interrompido.");
            return false;
        }
        //obter propriedades gerais do servico Web
        Service servicoWSDL = descricao.getServices()[0];
        endPointAddress = servicoWSDL.getEndpoints()[0].getAddress().toASCIIString();
        String targetNamespaceWSDL = servicoWSDL.getName().getNamespaceURI();
        String nomeDoServico = servicoWSDL.getName().getLocalPart();
        String fragmentoIdentificadorServico = targetNamespaceWSDL + "#" + servicoWSDL.getFragmentIdentifier().toString();
        //obter namespaces para as ontologias
        List<String> namespaceDasOntologias = new ArrayList();
        InlinedSchema[] esquemasWSDL = descricao.toElement().getTypesElement().getInlinedSchemas();
        
        for (InlinedSchema schema : esquemasWSDL) {
            namespaceDasOntologias.add(schema.getNamespace().toASCIIString());
        }

        //-- WEB SERVICE --//
        //definir variante WSML-Flight
        docWSML.append(declararWSMLVariant(VarianteWSML.Flight.IRI()));
        //declarar namespaces
        docWSML.append(declararNamespaces(targetNamespaceWSDL, nomeDoServico, namespaceDasOntologias));
        //criar webservice
        docWSML.append(declararWebService(targetNamespaceWSDL, nomeDoServico, fragmentoIdentificadorServico));
        //-- WEB SERVICE --//
        
        //-- CAPACITY --//
        //processar as operacoes do servico web para obter as precondicoes, pos condicoes e conceitos compartilhados
        Interface[] interfacesWSDL = descricao.getInterfaces();
        processarOperacoesDoServicoWSDL(interfacesWSDL);
        //declarar a capacidade
        docWSML.append(definirCapability(targetNamespaceWSDL, nomeDoServico, namespaceDasOntologias));
        //declarar as pre-condicoes e as pos-condicoes
        if (sharedVar > 0) {
            docWSML.append(declararPreCondition(preCondicoes));
            docWSML.append(declararPostCondition(posCondicoes));
        }
        //-- CAPACITY --//
        
        //-- INTERFACE --//
        docWSML.append(declararInterface(nomeDoServico, namespaceDasOntologias));
        //gerar a coreografia do servico
        docWSML.append(declararChoreography(nomeDoServico, namespaceDasOntologias, conceitosInGnd, conceitosOut));
        //declarar as regras de transicao
        docWSML.append(declararTransitionRules(nomeDoServico, conceitosInTR));
        //-- INTERFACE --//
        
        Gravador.gravarWSML(docWSML.toString(), saida);
        return true;
    }

    /**
     * Declara a variante WSML usada pelo documento. WSML tem cinco variantes (WSML-Core, WSML-DL,
     * WSML-Flight, WSML-Rule e WSML-Full.
     * Os documentos 
     * 
     * @param IRIdaVariante o IRI que identifica a variante de linguagem desejada
     * @return uma declaracao wsmlVariant em WSML
     */
    protected String declararWSMLVariant(String IRIdaVariante) {
        return "wsmlVariant _\"" + IRIdaVariante + "\"\n";
    }

    /**
     * Declara o namespace padrao e namespace adicionais relacionados com o artefato. 
     * 
     * @param targetNamespace o namespace alvo do servico web convertido
     * @param nomeDoServico o valor do atributo name do elemento service da descricao wsdl do servico
     * @param namespaceDasOntologias uma lista de namespaces dos elementos schema embutidos na descricao wsdl do servico
     * @return uma declaracao namespace em WSML
     */
    protected String declararNamespaces(String targetNamespace, String nomeDoServico, List<String> namespaceDasOntologias) {
        if (targetNamespace == null || nomeDoServico == null || namespaceDasOntologias == null) { return ""; }
        
        StringBuilder saida = new StringBuilder();
        saida.append("namespace {").append(" _\"").append(targetNamespace).append("/").append(nomeDoServico).append("#\"\n,\n");
        //adicionar namespaces
        saida.append("\t").append("dc _\"").append("http://purl.org/dc/elements/1.1").append("#\",\n");
        saida.append("\t").append("oasm _\"").append("http://www.wsmo.org/ontologies/choreography/oasm").append("#\",\n");
        saida.append("\t").append("wsml _\"").append("http://www.wsmo.org/wsml/wsml-syntax").append("#\",\n");

        for (int i = 0; i < namespaceDasOntologias.size(); i++) {
            saida.append("\t").append("onto").append(i+1).append(" _\"").append(namespaceDasOntologias.get(i)).append("#\",\n");
        }
        saida.delete(saida.length() - 2, saida.length());
        saida.append(" }\n\n\n");
        return saida.toString();
    }

    /**
     * Declara um WebService WSMO. Inclui as propriedades nao funcionais:
     * <br>* dc#title: nome do servico web
     * <br>* dc#creator: FASSW
     * <br>* <b>wsml#endpointDescription</b>: aponta de um WSMO webService para o servico WSDL 
     * apropriado. Prove detalhes de enderecamento e rede para comunicacao das mensagens.
     * 
     * @param namespaceAlvo o targetNamespace da descricao WSDL
     * @param nome o atributo name do elemento Service da descricao WSDL
     * @param fragmento o fragmento identificador do elemento Service da descricao WSDL
     * @return uma declaracao webService em WSML
     */
    protected String declararWebService(String namespaceAlvo, String nome, String fragmento) {
        StringBuilder saida = new StringBuilder();
        saida.append("webService _\"").append(namespaceAlvo).append("\"\n");
        saida.append("\t").append("annotations").append("\n");
        saida.append("\t\t").append("dc#title hasValue \"").append(nome).append("\"\n");
        saida.append("\t\t").append("dc#creator hasValue \"").append("FASSW").append("\"\n");
        saida.append("\t\t").append("wsml#endpointDescription hasValue \"").append(fragmento).append("\"\n");
        saida.append("\t").append("endAnnotations").append("\n\n");
        return saida.toString();
    }

    /**
     * Cria a capacidade do servico Web. Cada webService WSMO pode ter somente uma capacidade.
     * A capacidade inclui:
     * - importsOntology: ontologias utilizadas
     * - sharedVariables
     * - precondition
     * - postcondition
     * Esses elementos sao declarados no corpo do processamento.
     * 
     * @param targetNamespaceWSDL
     * @param nomeDoServico
     * @param schemas
     * @return uma capacidade sharedVariables em WSML
     */
    protected String definirCapability(String targetNamespaceWSDL, String nomeDoServico, List<String> namespaceDasOntologias) {
        StringBuilder saida = new StringBuilder();
        saida.append("capability ").append(nomeDoServico).append("Capability").append("\n\n");
        saida.append("\t").append("importsOntology").append("\n");

        for (String namespace : namespaceDasOntologias) {
            saida.append("\t\t_\"").append(namespace).append("\"\n");
        }
        saida.append("\n");
        
        //declarar as variaveis compartilhadas
        saida.append(declararSharedVariables());
        
        //declarar pre
        
        return saida.toString();
    }

    /**
     * Esse bloco declara as variaveis compartilhadas entre pre-condicoes, pos-condicoes, assuncoes 
     * e efeitos de uma capacidade. 
     * Sao criadas apenas duas no nosso prototipo: request para
     * as operacoes de entrada do servico Web e response para as operacoes de saida.
     * 
     * @return uma declaracao sharedVariables em WSML
     */
    protected String declararSharedVariables() {
        StringBuilder saida = new StringBuilder();
        if (sharedVar > 0) {
            saida.append("\tsharedVariables {");
            for (int i = 1; i <= sharedVar; i++) {
                saida.append("?request").append(i).append(", ");
            }
            saida.delete(saida.length()-2, saida.length());
            saida.append("}\n\n");
        }
        return saida.toString();
    }

    /**
     * Bloco que declara as pre condicoes do servico Web.
     * No prototipo, a precondicao sao os conceitos que representam as operacoes de entrada.
     * 
     * @param listaDeOperacoes
     * @return um bloco precondition em WSML
     */
    protected String declararPreCondition(List<String> preCondicoes) {
        StringBuilder saida = new StringBuilder();
        
        saida.append("\tprecondition").append("\n");
        saida.append("\t\t").append("definedBy").append("\n");
        
        for (String preCondicao : preCondicoes) {
            saida.append("\t\t\t").append(preCondicao).append(" or\n");
        }
        saida.delete(saida.length()-4, saida.length());
        saida.append(".\n\n");
        return saida.toString();
    }

    /**
     * Bloco que declara as pos condicoes do servico Web.
     * No prototipo, a pos condicao sao disjuncoes de conjuncoes entre um conceito de entrada e
     * um conceito de saida.
     * 
     * @param listaDeOperacoes
     * @return um bloco postcondition em WSML
     */
    protected String declararPostCondition(List<String> posCondicoes) {
        StringBuilder saida = new StringBuilder();
        saida.append("\tpostcondition").append("\n");
        saida.append("\t\t").append("definedBy").append("\n ");
        
        for (String posCondicao : posCondicoes) {
            saida.append("\t\t\t").append(posCondicao).append(" or\n");
        }
        saida.delete(saida.length() - 4, saida.length());
        saida.append(".\n\n");
        return saida.toString();
    }

    /**
     * Declara a interface que o webService oferece para consumo.
     * 
     * @param nomeDoServico nome do servico web
     * @param namespaceDasOntologias uma lista contendo referencias das ontologias importadas
     * @return uma declaracao interface em WSML
     */
    protected String declararInterface(String nomeDoServico, List<String> namespaceDasOntologias) {
        StringBuilder saida = new StringBuilder();
        saida.append("interface ").append(nomeDoServico + "Interface").append("\n\n");
        saida.append("\t").append("importsOntology").append("\n");

        for (String namespace : namespaceDasOntologias) {
            saida.append("\t\t_\"").append(namespace).append("\"\n");
        }
        saida.append("\n");
        return saida.toString();
    }

    /**
     * Declara os conceitos que a interface do servico web WSMO vai oferecer e define o grounding
     * para os conceitos marcados como In, ao fornecer o fragmento identificador da operacao.
     * 
     * @param nomeDoServico o nome do servico web
     * @param schemas o namespace das ontologias importadas
     * @param conceitosInGnd lista de conceitos marcados como in, incluindo o grounding
     * @param conceitosOut lista de conceitos marcados como out
     * @return um bloco choreography em WSML
     */
    protected String declararChoreography(String nomeDoServico, List<String> namespaceOntologias, List<String> conceitosIn, List<String> conceitosOut) {
        StringBuilder saida = new StringBuilder();
        saida.append("\tchoreography ").append(nomeDoServico + "Choreography").append("\n\n");
        saida.append("\t\tstateSignature").append("\n");
        saida.append("\t\t\timportsOntology").append("\n");
        for (String namespace : namespaceOntologias) {
            saida.append("\t\t\t\t_\"").append(namespace).append("\"\n");
        }
        saida.append("\n");
        //declarar os conceitos marcados como in e out
        if (sharedVar > 0) {
            saida.append("\t\t\t").append("in").append("\n");
            
            for (String in : conceitosIn) {
                saida.append("\t\t\t\t").append(in).append(",\n");
            }
            saida.delete(saida.length()-2, saida.length());
            saida.append("\n\n");
            saida.append("\t\t\t").append("out").append("\n");
            
            for (String out : conceitosOut) {
                saida.append("\t\t\t\t").append(out).append(",\n");
            }
            saida.delete(saida.length()-2, saida.length());
            saida.append("\n\n");
        }
        
        saida.append("\t\t\t").append("controlled").append("\n");
        saida.append("\t\t\t\t").append("concept oasm#ControlState").append("\n\n");
        return saida.toString();
    }

    /**
     * Declara as regras de transicao para interacao basica com o servico web WSMO gerado.
     * 
     * @param nomeDoServico o nome do servico web WSDL
     * @param conceitosIn lista de conceitos marcados com o papel 'in'
     * @return um bloco contendo regras de transicao em WSML
     */
    protected String declararTransitionRules(String nomeDoServico, List<String> conceitosIn) {
        StringBuilder saida = new StringBuilder();
        saida.append("\t\ttransitionRules ").append(nomeDoServico).append("StateSignatureRules").append("\n");
        //REPETIR PARA TODOS OS CONCEITOS
        for (String in : conceitosIn) {
            saida.append("\t\t\t").append(in);
            saida.append("\t\tand ?controlstate[oasm#value hasValue oasm#InitialState]) do\n");
            saida.append("\t\t\t\t").append("delete (?controlstate [oasm#value hasValue oasm#InitialState])\n");
            saida.append("\t\t\t\t").append("add (?controlstate [oasm#value hasValue oasm#EndState])\n");
            saida.append("\t\t\t").append("endForall").append("\n\n");
        }
        return saida.toString();
    }

    /**
     * Metodo auxiliar para gerar uma declaracao de pre condicao em WSML
     * 
     * @param elemento uma declaracao de elemento em WSDL
     * @return declaracao de pre condicao em WSML
     */
    private String gerarPreCondicao(ElementDeclaration elemento) {
        int numero = sharedVar+1;
        return "?request" + numero + " memberOf onto1#" + elemento.getName().getLocalPart();
    }

    /**
     * Metodo auxiliar para gerar uma declaracao de pos condicao em WSML
     * 
     * @param elementoIn uma declaracao de elemento input em WSDL
     * @param elementoOut uma declaracao de elemento output em WSDL
     * @return declaracao de pos condicao em WSML
     */
    private String gerarPosCondicao(ElementDeclaration elementoIn, ElementDeclaration elementoOut) {
        int numero = sharedVar+1;
        return "(?request" + numero + " memberOf onto1#" + elementoIn.getName().getLocalPart() 
                + " and ?response" + numero + " memberOf onto1#" + elementoOut.getName().getLocalPart() + ")";
    }

    /**
     * Metodo auxiliar para gerar uma declaracao de conceito marcado como <b>in</b> na stateSignature de uma
     * interface de servico Web WSMO.
     * Efetua o grounding relacionando o conceito com o fragmento identificador da operacao
     * 
     * @param elemento uma declaracao de elemento input em WSDL
     * @param fragmento o fragmento identificador da operacao WSDL a qual pertence o elemento
     * @return uma declaracao de conceito com grounding em WSML
     */
    private String gerarConceitoInComGrounding(ElementDeclaration elemento, FragmentIdentifier fragmento) {
        return "concept onto1#" + elemento.getName().getLocalPart() + "\n\t\t\t\t\t withGrounding _\"" 
                + endPointAddress + "#" + fragmento.toString() + "\"";
    }
    
    /**
     * Metodo auxiliar para gerar uma declaracao de conceito marcado como <b>out</b> na stateSignature de uma
     * interface de servico Web WSMO.
     * 
     * @param elementoOut uma declaracao de elemento output em WSDL
     * @return  uma declaracao de conceito
     */
    private String gerarConceitoOut(ElementDeclaration elementoOut) {
        return "concept onto1#" + elementoOut.getName().getLocalPart();
    }
    
     /**
     * Metodo auxiliar para gerar uma declaracao de conceito marcado como <b>in</b> na transitionRule 
     * contida na coreografia do servico Web WSMO.
     * 
     * @param elementoIn uma declaracao de elemento input em WSDL
     * @return uma expressao de regra de transicao em WSML
     */
    private String gerarConceitoInRegraTransicao(ElementDeclaration elementoIn) {
        int numero = sharedVar+1;
        return "forall {?request" + numero + ",?controlstate} with (?request" + numero + 
                " memberOf onto#1" + elementoIn.getName().getLocalPart() + "\n";
    }

    /**
     * Esse metodo auxiliar percorre todas as operacoes de todas as interfaces de um servico web em WSDL.
     * O processamento visa calcular a quantidade de variaveis compartilhadas e popular as seguintes listas:
     * <br> pre condicoes; pos condicoes; conceitos marcados como 'in' e o respectivo grounding; 
     * conceitos marcados como 'out'.
     * 
     * @param interfacesWSDL os elementos interface do documento WSDL
     */
    private void processarOperacoesDoServicoWSDL(Interface[] interfacesWSDL) {
        if (interfacesWSDL.length > 0) {
            for (int i = 0; i < interfacesWSDL.length; i++) { //para cada interface, obter as operacoes
                InterfaceOperation[] operacoesWSDL = interfacesWSDL[i].getInterfaceOperations();
                
                for (InterfaceOperation operacao : operacoesWSDL) {
                    String padraoMensagem = operacao.getMessageExchangePattern().toASCIIString();
                    //verificar se a operacao esta definida
                    if (MEP.contains(padraoMensagem)) {
                        InterfaceMessageReference[] mensagens = operacao.getInterfaceMessageReferences();
                        
                        switch (padraoMensagem) {
                            case "http://www.w3.org/ns/wsdl/in-only": {
                                ElementDeclaration elemento = mensagens[0].getElementDeclaration();
                                FragmentIdentifier fragmento = mensagens[0].getFragmentIdentifier();
                                
                                if (elemento != null) {
                                    preCondicoes.add(gerarPreCondicao(elemento));
                                    conceitosInGnd.add(gerarConceitoInComGrounding(elemento, fragmento));
                                    conceitosInTR.add(gerarConceitoInRegraTransicao(elemento));
                                    sharedVar++;
                                }
                                break;
                            }
                            case "http://www.w3.org/ns/wsdl/robust-in-only": {
                                ElementDeclaration elemento = mensagens[0].getElementDeclaration();
                                FragmentIdentifier fragmento = mensagens[0].getFragmentIdentifier();
                                
                                if (elemento != null) {
                                    preCondicoes.add(gerarPreCondicao(elemento));
                                    conceitosInGnd.add(gerarConceitoInComGrounding(elemento, fragmento));
                                    conceitosInTR.add(gerarConceitoInRegraTransicao(elemento));
                                    sharedVar++;
                                }
                                break;
                            }
                            case "http://www.w3.org/ns/wsdl/in-out": {
                                ElementDeclaration elementoIn = null;
                                ElementDeclaration elementoOut = null;
                                FragmentIdentifier fragmentoIn = null;
                                //TODO podemos ter problemas em caso de erro de sintaxe?
                                for (InterfaceMessageReference e : mensagens) {
                                    if (e.getDirection().equals(Direction.IN)) {
                                        elementoIn = e.getElementDeclaration();
                                        fragmentoIn = e.getFragmentIdentifier();
                                    }
                                    else {
                                        elementoOut = e.getElementDeclaration();
                                    }
                                }
                                
                                if (elementoIn != null && elementoOut != null) {
                                    preCondicoes.add(gerarPreCondicao(elementoIn));
                                    posCondicoes.add(gerarPosCondicao(elementoIn, elementoOut));
                                    conceitosInGnd.add(gerarConceitoInComGrounding(elementoIn, fragmentoIn));
                                    conceitosInTR.add(gerarConceitoInRegraTransicao(elementoIn));
                                    conceitosOut.add(gerarConceitoOut(elementoOut));
                                    sharedVar++;
                                }
                                break;
                            }
                            case "http://www.w3.org/ns/wsdl/in-opt-out": {
                                if (mensagens.length == 1) {
                                    ElementDeclaration elemento = mensagens[0].getElementDeclaration();
                                    FragmentIdentifier fragmento = mensagens[0].getFragmentIdentifier();

                                    if (elemento != null) {
                                        preCondicoes.add(gerarPreCondicao(elemento));
                                        conceitosInGnd.add(gerarConceitoInComGrounding(elemento, fragmento));
                                        conceitosInTR.add(gerarConceitoInRegraTransicao(elemento));
                                        sharedVar++;
                                    }
                                    break;
                                }
                                else {
                                    ElementDeclaration elementoIn = null;
                                    ElementDeclaration elementoOut = null;
                                    FragmentIdentifier fragmentoIn = null;
                                    //TODO podemos ter problemas em caso de erro de sintaxe?
                                    for (InterfaceMessageReference e : mensagens) {
                                        if (e.getDirection().equals(Direction.IN)) {
                                            elementoIn = e.getElementDeclaration();
                                            fragmentoIn = e.getFragmentIdentifier();
                                        } else {
                                            elementoOut = e.getElementDeclaration();
                                        }
                                    }

                                    if (elementoIn != null && elementoOut != null) {
                                        preCondicoes.add(gerarPreCondicao(elementoIn));
                                        posCondicoes.add(gerarPosCondicao(elementoIn, elementoOut));
                                        conceitosInGnd.add(gerarConceitoInComGrounding(elementoIn, fragmentoIn));
                                        conceitosInTR.add(gerarConceitoInRegraTransicao(elementoIn));
                                        conceitosOut.add(gerarConceitoOut(elementoOut));
                                        sharedVar++;
                                    }
                                }
                                break;
                            }
                            case "http://www.w3.org/ns/wsdl/out-only":
                                //nao tratado no momento
                                break;
                            case "http://www.w3.org/ns/wsdl/robust-out-only":
                                //nao tratado no momento
                                break;
                            case "http://www.w3.org/ns/wsdl/out-in": {
                                //inverter pre e pos condicao
                                ElementDeclaration elementoIn = null;
                                ElementDeclaration elementoOut = null;
                                FragmentIdentifier fragmentoIn = null;
                                //TODO podemos ter problemas em caso de erro de sintaxe?
                                for (InterfaceMessageReference e : mensagens) {
                                    if (e.getDirection().equals(Direction.IN)) {
                                        elementoIn = e.getElementDeclaration();
                                        fragmentoIn = e.getFragmentIdentifier();
                                    } else {
                                        elementoOut = e.getElementDeclaration();
                                    }
                                }

                                if (elementoIn != null && elementoOut != null) {
                                    preCondicoes.add(gerarPreCondicao(elementoIn));
                                    posCondicoes.add(gerarPosCondicao(elementoIn, elementoOut));
                                    conceitosInGnd.add(gerarConceitoInComGrounding(elementoIn, fragmentoIn));
                                    conceitosInTR.add(gerarConceitoInRegraTransicao(elementoIn));
                                    conceitosOut.add(gerarConceitoOut(elementoOut));
                                    sharedVar++;
                                }
                                break;
                            }
                            case "http://www.w3.org/ns/wsdl/out-opt-in": {
                                if (mensagens.length == 1) {
                                    //mesmo caso de out-only
                                } 
                                else {
                                    //TODO podemos ter problemas em caso de erro de sintaxe?
                                    ElementDeclaration elementoIn = null;
                                    ElementDeclaration elementoOut = null;
                                    FragmentIdentifier fragmentoIn = null;
                                    //TODO podemos ter problemas em caso de erro de sintaxe?
                                    for (InterfaceMessageReference e : mensagens) {
                                        if (e.getDirection().equals(Direction.IN)) {
                                            elementoIn = e.getElementDeclaration();
                                            fragmentoIn = e.getFragmentIdentifier();
                                        } else {
                                            elementoOut = e.getElementDeclaration();
                                        }
                                    }

                                    if (elementoIn != null && elementoOut != null) {
                                        preCondicoes.add(gerarPreCondicao(elementoIn));
                                        posCondicoes.add(gerarPosCondicao(elementoIn, elementoOut));
                                        conceitosInGnd.add(gerarConceitoInComGrounding(elementoIn, fragmentoIn));
                                        conceitosInTR.add(gerarConceitoInRegraTransicao(elementoIn));
                                        conceitosOut.add(gerarConceitoOut(elementoOut));
                                        sharedVar++;
                                    }
                                }
                                break;
                            }
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }
}
