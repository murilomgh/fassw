package fassw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.wsdl.Operation;
//--- Elementos de Apache Woden ---//
import org.apache.woden.WSDLException;
import org.apache.woden.WSDLFactory;
import org.apache.woden.WSDLReader;
import org.apache.woden.XMLElement;
import org.apache.woden.schema.InlinedSchema;
import org.apache.woden.wsdl20.Description;
import org.apache.woden.wsdl20.Interface;
import org.apache.woden.wsdl20.InterfaceMessageReference;
import org.apache.woden.wsdl20.InterfaceOperation;
import org.apache.woden.wsdl20.Service;
import org.apache.woden.wsdl20.enumeration.Direction;
import org.apache.woden.wsdl20.xml.DescriptionElement;
import org.apache.woden.wsdl20.xml.InterfaceElement;
import org.apache.woden.wsdl20.xml.InterfaceMessageReferenceElement;
import org.apache.woden.wsdl20.xml.InterfaceOperationElement;
import org.omwg.logicalexpression.Molecule;

/**
 * Classe responsavel por gerar o artefato WSMO Web Service, na sintaxe Surface, conforme os passos
 * desenvolvidos e descritos no apendice B.
 *
 * @author Murilo Honorio
 * @version 0.0
 */
public class GroundingCoreografia {

    private static File arquivoEntrada;
    private static File arquivoSaida;
    public static final String VARIANTE_RULE = "http://www.wsmo.org/wsml/wsml-syntax/wsml-rule";
    public static final String VARIANTE_FLIGHT = "http://www.wsmo.org/wsml/wsml-syntax/wsml-flight";

    GroundingCoreografia(String entrada, String saida) {
        this.arquivoEntrada = new File(entrada);
        this.arquivoSaida = new File(saida);
    }

    /**
     * Le a descricao do servico web contida no documento WSDL utilizando funcionalidades providas
     * pela biblioteca Apache Woden.
     *
     * @return
     */
    private Description carregarArquivoWSDL() {
        try {
            WSDLFactory factory = WSDLFactory.newInstance();
            WSDLReader reader = factory.newWSDLReader();
            reader.setFeature(WSDLReader.FEATURE_VALIDATION, true); //<-- enable WSDL 2.0 validation(optional) 

            Description descricao = reader.readWSDL(arquivoEntrada.getAbsolutePath()); //<-- the Description component, always returned
            return descricao;
        } catch (WSDLException ex) {
            switch (ex.getFaultCode()) {
                case WSDLException.INVALID_WSDL:
                    //wsdl invalido
                    System.out.println(ex.getFaultCode());
                    System.out.println(ex.getLocalizedMessage());
                    break;
                case WSDLException.OTHER_ERROR:
                    //arquivo invalido
                    System.out.println(ex.getFaultCode());
                    break;
                default:
                    Logger.getLogger(GroundingCoreografia.class.getName()).log(Level.SEVERE, null, ex);
                    break;
            }
            return null;
        }
    }

    /**
     * Gera um documento WSML contendo um WebService WSMO.
     *
     * @throws FileNotFoundException
     */
    boolean processar() {
        try (PrintWriter pw = new PrintWriter(arquivoSaida, "UTF-8")) {
            StringBuilder sb = new StringBuilder();

            //Componentes do Woden
            Description descricao = carregarArquivoWSDL(); //o componentes abstratos da descricao do servico
            DescriptionElement elementosXML = descricao.toElement(); //os elementos e atributos xml da descricao do servico

            String targetNamespaceWSDL = elementosXML.getTargetNamespace().toASCIIString();
            String nomeDoServico = elementosXML.getServiceElements()[0].getName().getLocalPart();

            sb.append(setWSMLVariant(VARIANTE_FLIGHT));

            InlinedSchema[] schemas = elementosXML.getTypesElement().getInlinedSchemas();
            //inserir namespace das ontologias
            sb.append(setNamespaces(targetNamespaceWSDL + "/" + nomeDoServico, schemas));

            Service[] servicos = descricao.getServices();
            //criar webservice
            sb.append(setWebService(targetNamespaceWSDL, nomeDoServico, servicos));

            //definir capacidade
            sb.append(setCapability(targetNamespaceWSDL, nomeDoServico, schemas));
            sb.append(setSharedVariables());

            //definir interface
            sb.append(setInterface(nomeDoServico, schemas));
            
            Interface[] listaDeInterfaces = descricao.getInterfaces();
            InterfaceOperation[] listaDeOperacoes = listaDeInterfaces[0].getInterfaceOperations();
            //definir pre e pos condicoes
            sb.append(setPreConditions(listaDeOperacoes));
            sb.append(setPostConditions(listaDeOperacoes));
            //gerar coreografia
            sb.append(setChoreography(targetNamespaceWSDL, nomeDoServico, schemas, listaDeOperacoes));
            //gerar regras de transicao
            sb.append(setTransitionRules(listaDeOperacoes));
            
            pw.append(sb.toString());


            return true;

            //TODO tratar todas as condicoes de erro
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GroundingCoreografia.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(GroundingCoreografia.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private String setWSMLVariant(String variante) {
        StringBuilder saida = new StringBuilder();
        saida.append("wsmlVariant").append(" _\"").append(variante).append("\"\n");
        return saida.toString();
    }

    private String setNamespaces(String targetNamespace, InlinedSchema[] schemas) {
        StringBuilder saida = new StringBuilder();
        saida.append("namespace {").append(" _\"").append(targetNamespace).append("\n,\n");
        //adicionar namespaces
        saida.append("\t").append("dc _\"").append("http://purl.org/dc/elements/1.1").append("#\",\n");
        saida.append("\t").append("oasm _\"").append("http://www.wsmo.org/ontologies/choreography/oasm").append("#\",\n");
        saida.append("\t").append("wsml _\"").append("http://www.wsmo.org/wsml/wsml-syntax").append("#\",\n");

        int i = 1;
        for (InlinedSchema schema : schemas) {
            saida.append("\t").append("onto" + i + "_\"" + schema.getNamespace() + "#").append("\"\n");
            i++;
        }
        saida.delete(saida.length() - 2, saida.length());
        saida.append(" }\n\n\n");
        return saida.toString();
    }

    private String setWebService(String targetNamespaceWSDL, String nomeDoServico, Service[] servicos) {
        StringBuilder saida = new StringBuilder();
        saida.append("webService _\"").append(targetNamespaceWSDL).append("\"\n");
        saida.append("\t").append("nonFunctionalProperties").append("\n");
        saida.append("\t\t").append("dc#title hasValue \"").append(nomeDoServico).append("\"\n");
        saida.append("\t\t").append("dc#creator hasValue \"").append("FASSW").append("\"\n");
        //endpoints
        for (Service s : servicos) {
            saida.append("\t\t").append("wsml#endpointDescription hasValue \"").append(s.getName().getNamespaceURI() + "#" + s.getFragmentIdentifier()).append("\"\n");
        }
        saida.append("\t").append("endNonFunctionalProperties").append("\n\n");
        return saida.toString();
    }

    private String setCapability(String targetNamespaceWSDL, String nomeDoServico, InlinedSchema[] schemas) {
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
