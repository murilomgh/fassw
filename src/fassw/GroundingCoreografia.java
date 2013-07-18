package fassw;

import fassw.util.Gravador;
import fassw.util.Leitor;
import fassw.util.VarianteWSML;
import java.util.ArrayList;
import java.util.List;
//--- Elementos de Apache Woden ---//
import org.apache.woden.schema.InlinedSchema;
import org.apache.woden.wsdl20.Description;
import org.apache.woden.wsdl20.Interface;
import org.apache.woden.wsdl20.InterfaceMessageReference;
import org.apache.woden.wsdl20.InterfaceOperation;
import org.apache.woden.wsdl20.Service;
import org.apache.woden.wsdl20.enumeration.Direction;

/**
 * Classe responsavel por gerar o artefato WSMO Web Service, na sintaxe Surface, conforme os passos
 * desenvolvidos e descritos no apendice B.
 *
 * @author Murilo Honorio
 */
public class GroundingCoreografia {

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
        String targetNamespaceWSDL = servicoWSDL.getName().getNamespaceURI();
        String nomeDoServico = servicoWSDL.getName().getLocalPart();
        String fragmentoIdentificadorServico = targetNamespaceWSDL + "#" + servicoWSDL.getFragmentIdentifier().toString();
        List<String> namespaceDasOntologias = new ArrayList();

        //definir variante WSML-Flight
        docWSML.append(declararWSMLVariant(VarianteWSML.Flight.IRI()));
        
        //obter os esquemas que estao dentro do arquivo
        InlinedSchema[] names = descricao.toElement().getTypesElement().getInlinedSchemas();
        for (InlinedSchema schema : names) {
            namespaceDasOntologias.add(schema.getNamespace().toASCIIString());
        }

        //inserir namespace das ontologias
        docWSML.append(declararNamespaces(targetNamespaceWSDL, nomeDoServico, namespaceDasOntologias));

        //criar webservice
        docWSML.append(declararWebService(targetNamespaceWSDL, nomeDoServico, fragmentoIdentificadorServico));

        //definir capacidade
        docWSML.append(declararCapability(targetNamespaceWSDL, nomeDoServico, names));
        docWSML.append(setSharedVariables());

        //definir interface
        docWSML.append(setInterface(nomeDoServico, names));

        //processar as interfaces do servico Web caso tenham sido declaradas
        Interface[] listaDeInterfaces = descricao.getInterfaces();
        if (listaDeInterfaces.length > 0) {
            InterfaceOperation[] listaDeOperacoes = listaDeInterfaces[0].getInterfaceOperations();
            //definir pre e pos condicoes
            docWSML.append(setPreConditions(listaDeOperacoes));
            docWSML.append(setPostConditions(listaDeOperacoes));
            //gerar coreografia
            docWSML.append(setChoreography(targetNamespaceWSDL, nomeDoServico, names, listaDeOperacoes));
            //gerar regras de transicao
            docWSML.append(setTransitionRules(listaDeOperacoes));
        }
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
     * Declara um WebService WSMO. Inclui propriedades nao funcionais
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

    protected String declararCapability(String targetNamespaceWSDL, String nomeDoServico, InlinedSchema[] schemas) {
        StringBuilder saida = new StringBuilder();
        saida.append("capability ").append(nomeDoServico + "Capability").append("\n\n");
        saida.append("\t").append("importsOntology").append("\n");

        for (InlinedSchema schema : schemas) {
            saida.append("\t\t_\"").append(schema.getNamespace()).append("\"\n");
        }
        saida.append("\n");
        return saida.toString();
    }

    private String setSharedVariables() {
        return "sharedVariables {?request, ?response}\n\n";
    }

    private String setInterface(String nomeDoServico, InlinedSchema[] schemas) {
        StringBuilder saida = new StringBuilder();
        saida.append("interface ").append(nomeDoServico + "Interface").append("\n\n");
        saida.append("\t").append("importsOntology").append("\n");

        for (InlinedSchema schema : schemas) {
            saida.append("\t\t_\"").append(schema.getNamespace()).append("\"\n");
        }
        saida.append("\n");
        return saida.toString();
    }

    private Object setPreConditions(InterfaceOperation[] listaDeOperacoes) {
        StringBuilder saida = new StringBuilder();
        saida.append("\tprecondition").append("\n");
        saida.append("\t\t").append("definedBy").append("\n");
        for (InterfaceOperation op : listaDeOperacoes) {
            InterfaceMessageReference[] interfaceMessageReferences = op.getInterfaceMessageReferences();
            for (InterfaceMessageReference imr : interfaceMessageReferences) {
                if (imr.getDirection().equals(Direction.IN)) {
                    saida.append("\t\t\t?request memberOf ").append(op.getName().getPrefix() + "#" + op.getName().getLocalPart()).append("or\n");
                }
            }
        }
        saida.delete(saida.length() - 3, saida.length());
        saida.append(".\n\n");
        return saida.toString();
    }

    private String setPostConditions(InterfaceOperation[] listaDeOperacoes) {
        StringBuilder saida = new StringBuilder();
        saida.append("\tpostcondition").append("\n");
        saida.append("\t\t").append("definedBy").append("\n( ");
        /*
         out.println("First operation name: " +listaDeOperacoes[0].getName());
         out.println("First operation pattern: " +listaDeOperacoes[0].getMessageExchangePattern().toString());
         out.println("First operation style: " +listaDeOperacoes[0].getStyle()[0].toString());
         */
        for (InterfaceOperation op : listaDeOperacoes) {
            String mep = op.getMessageExchangePattern().toASCIIString();

            switch (mep) {
                case "http://www.w3.org/ns/wsdl/in-out":
                    saida.append("IN-OUT");
                    break;
                case "http://www.w3.org/ns/wsdl/out-in":
                    saida.append("OUT-IN");
                    break;
                case "http://www.w3.org/ns/wsdl/in-only":
                    saida.append("IN-ONLY");
                    break;
                default:
                    saida.append("OUTROS");
                    break;
            }
        }
        //saida.delete(saida.length() - 3, saida.length());
        saida.append(" ).\n\n");
        return saida.toString();
    }

    private String setChoreography(String targetNamespaceWSDL, String nomeDoServico, InlinedSchema[] schemas, InterfaceOperation[] listaDeOperacoes) {
        StringBuilder saida = new StringBuilder();
        saida.append("\tchoreography ").append(nomeDoServico + "Choreography").append("\n");
        saida.append("\tstateSignature").append("\n");
        saida.append("\t\timportsOntology").append("\n");
        for (InlinedSchema schema : schemas) {
            saida.append("\t\t\t_\"").append(schema.getNamespace()).append("\"\n");
        }
        saida.append("\n");
        //AUTOMATIZAR
        saida.append("\t\t").append("in").append("\n");
        saida.append("\t\t\t").append("concept ").append("--FOR CONCEITOS--").append(" withGrounding").append("\n");
        saida.append("\t\t\t\t").append("--GERAR FRAGMENTO--").append(",\n\n");
        
        //saida.delete(saida.length()-3, saida.length()); //tirar a ultima virgula
        saida.append("\t\t").append("out").append("\n");
        saida.append("\t\t\t").append("concept ").append("--FOR CONCEITOS--").append(" withGrounding").append("\n");
        saida.append("\t\t\t\t").append("--GERAR FRAGMENTO--").append(",\n\n");
        //saida.delete(saida.length()-3, saida.length()); //tirar a ultima virgula
        return saida.toString();
    }

    private String setTransitionRules(InterfaceOperation[] listaDeOperacoes) {
        StringBuilder saida = new StringBuilder();
        saida.append("\ttransitionRules ").append("\n");
        //REPETIR PARA TODOS OS CONCEITOS
        saida.append("\t\t").append("forall {?request} with (?request memberOf ").append("--IN-OP--").append(") do").append("\n");
        saida.append("\t\t\t").append("add (?response memberOf ").append("--OUT-OP--").append(")\n");
        saida.append("\t\t").append("endForall").append("\n");
        return saida.toString();
    }
}
