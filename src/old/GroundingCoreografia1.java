package old;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
//--- Elementos de Apache Woden ---//
import org.apache.woden.WSDLException;
import org.apache.woden.WSDLFactory;
import org.apache.woden.WSDLReader;
import org.apache.woden.wsdl20.Description;
import org.apache.woden.wsdl20.enumeration.Direction;
import org.apache.woden.wsdl20.xml.DescriptionElement;
import org.apache.woden.wsdl20.xml.InterfaceElement;
import org.apache.woden.wsdl20.xml.InterfaceMessageReferenceElement;
import org.apache.woden.wsdl20.xml.InterfaceOperationElement;
//--- Elementos de WSMO4J ---//
import org.omwg.ontology.*;
import org.wsmo.common.*;
import org.wsmo.common.exception.*;
import org.wsmo.factory.*;
import org.wsmo.service.*;
import org.wsmo.service.signature.StateSignature;
import org.wsmo.wsml.*;

/**
 * Classe responsavel por gerar o artefato WSMO Web Service, na sintaxe Surface, conforme os 
 * passos desenvolvidos e descritos no apendice B.
 * 
 * @author Murilo Honorio
 * @version 0.0
 */
public class GroundingCoreografia1 {
    
    private static File entrada;
    private static File saida;
    
    public static final String VARIANTE_RULE = "\"http://www.wsmo.org/wsml/wsml-syntax/wsml-rule\"";
    public static final String VARIANTE_FLIGHT = "\"http://www.wsmo.org/wsml/wsml-syntax/wsml-flight\"";
    
    static StringBuilder documentoWSML = new StringBuilder();

    GroundingCoreografia1(String entrada, String saida) {
        this.entrada = new File(entrada);
        this.saida = new File(saida);
    }
    
    /**
     * Gera um documento WSML contendo um WebService WSMO.
     * 
     * @throws FileNotFoundException 
     */
    void gerar2() throws FileNotFoundException, UnsupportedEncodingException {
        try (PrintWriter pw = new PrintWriter(saida, "UTF-8")) {
            anexarVariante();
            anexarNamespaceDefault();
            
            
            pw.write(documentoWSML.toString());
        }
    }
    
    void gerar() throws IOException, InvalidModelException {
        WsmoFactory factory = Factory.createWsmoFactory(null);
        DataFactory dataFactory = Factory.createDataFactory(null);
        
        //definicao do namespace
        Description descricao = carregarArquivoWSDL(); //o componentes abstratos da descricao do servico
        DescriptionElement elementosXML = descricao.toElement(); //os elementos e atributos xml da descricao do servico
        
        String targetNamespaceWSDL = elementosXML.getTargetNamespace().toASCIIString();
        String nomeDoServico = elementosXML.getServiceElements()[0].getName().getLocalPart();
        
        //criarWebService()
        String SERVICE_ID = targetNamespaceWSDL + "/" + nomeDoServico;
        IRI serviceIRI = factory.createIRI(SERVICE_ID);
        
        WebService servico = factory.createWebService(serviceIRI);
        
        //anexarVariante()
        servico.setWsmlVariant(org.wsmo.common.WSML.WSML_FLIGHT);
        //END anexarVariante()    
                
        //anexarNamespaceDefault()
        servico.setDefaultNamespace(factory.createNamespace("", 
                factory.createIRI(targetNamespaceWSDL + "/" + nomeDoServico + "#")));
        
        servico.addNamespace(factory.createNamespace("dc", 
                  factory.createIRI("http://purl.org/dc/elements/1.1#")));
        servico.addNamespace(factory.createNamespace("oasm", 
                factory.createIRI("http://www.wsmo.org/ontologies/choreography/oasm#")));
        
        //adicionarOntologias()
        for (int i = 1; i < 2; i++) {
            Namespace ontologias = factory.createNamespace("onto" + i, 
                factory.createIRI(targetNamespaceWSDL + "#"));
            
            servico.addNamespace(ontologias);
        }
        
        //definirNFPs()
        servico.addNFPValue(factory.createIRI(NFP.DC_TITLE), dataFactory
                .createWsmlString(nomeDoServico));
        servico.addNFPValue(factory.createIRI(NFP.DC_CREATOR), dataFactory
                .createWsmlString("FASSW")); //TODO obter essas informacoes
        
        //adicionarResponsesAsNFP
        IRI responseIRI = factory.createIRI("http://response");
        
        for (InterfaceElement ie : elementosXML.getInterfaceElements()) {
            for (InterfaceOperationElement ope : ie.getInterfaceOperationElements()) {
                for (InterfaceMessageReferenceElement imre : ope.getInterfaceMessageReferenceElements()) {
                    if (imre.getDirection().equals(Direction.OUT)) {
                        Value v = factory.getInstance(factory.createIRI(
                                imre.getElement().getQName().getNamespaceURI() + "#" +
                                imre.getElement().getQName().getLocalPart())
                                );
                        servico.addNFPValue(responseIRI, v);
                    }
                }
            }
        }
        
        Capability capacidade = factory.createCapability(
                factory.createIRI(servico.getDefaultNamespace(), nomeDoServico + "Capability"));
        
        servico.setCapability(capacidade);
        
        Ontology onto1 = factory.createOntology(factory.createIRI(targetNamespaceWSDL));
        capacidade.addOntology(onto1);
        
        
        
        //declarar e construir expressões
        LogicalExpressionFactory leFactory = Factory.createLogicalExpressionFactory(null);
        
        //TODO um laço que mapeie todas as mensagens de entrada
        Variable v1 = leFactory.createVariable("request1");
        Variable v2 = leFactory.createVariable("request2");
        Variable v3 = leFactory.createVariable("response1");
        Variable v4 = leFactory.createVariable("response2");
        capacidade.addSharedVariable(v1);
        capacidade.addSharedVariable(v2);
        
        Concept c1 = factory.createConcept(factory.createIRI(servico.findNamespace("onto1"), "findEvents"));
        Concept c2 = factory.createConcept(factory.createIRI(servico.findNamespace("onto1"), "findEventsByVenue"));
        Concept c3 = factory.createConcept(factory.createIRI(servico.findNamespace("onto1"), "findEventsResponse"));
        Concept c4 = factory.createConcept(factory.createIRI(servico.findNamespace("onto1"), "findEventsByVenueResponse"));
        
        //definicao da pre-condicao
        Axiom preCondicao = factory.createAxiom(factory.createAnonymousID());
        
        preCondicao.addDefinition(leFactory.createDisjunction(
                leFactory.createMemberShipMolecule(v1, c1.getIdentifier()), 
                leFactory.createMemberShipMolecule(v2, c2.getIdentifier())));
        
        //definicao da pos-condicao
        Axiom posCondicao = factory.createAxiom(factory.createAnonymousID());
        
        posCondicao.addDefinition(
                leFactory.createDisjunction(
                    leFactory.createConjunction(
                        leFactory.createMemberShipMolecule(v1, c1.getIdentifier()), 
                        leFactory.createMemberShipMolecule(v3, c3.getIdentifier())),
                    leFactory.createConjunction(
                        leFactory.createMemberShipMolecule(v2, c2.getIdentifier()), 
                        leFactory.createMemberShipMolecule(v4, c4.getIdentifier()))));
        
        capacidade.addPreCondition(preCondicao);
        capacidade.addPostCondition(posCondicao);
        
        //construcao da interface do web service
        Interface interface1 = factory.createInterface(
                factory.createIRI(servico.getDefaultNamespace(), nomeDoServico + "Interface"));
        
        /*
        Choreography chor = interface1.createChoreography(
                factory.createIRI(servico.getDefaultNamespace(), nomeDoServico + "Chor"));
        */
        
        
        
        interface1.addOntology(onto1);
        
        servico.addInterface(interface1);
        
        Serializer serializer = Factory.createSerializer(new HashMap<String, Object>(0));
          serializer.serialize(new TopEntity[] {servico}, new PrintWriter(saida, "UTF-8"));
    }
    
    /**
     * Acrescenta a variante ao documento WSML. 
     * Nesse momento, apenas a variante "WSML Rule" é aceita.
     */
    private void anexarVariante() {
        documentoWSML.append("wsmlVariant _" + VARIANTE_FLIGHT);
    }
    
    /**
     * Gera e acrescenta o namespace default ao documento WSML, utilizando o target namespace 
     * declarado no documento WSDL. 
     */
    private void anexarNamespaceDefault() {
        Description descricao = carregarArquivoWSDL(); //o componentes abstratos da descricao do servico
        DescriptionElement elementosXML = descricao.toElement(); //os elementos e atributos xml da descricao do servico
        
        String targetNamespaceWSDL = elementosXML.getTargetNamespace().toASCIIString();
        String namespaceDefaultWSML = "namespace { _\"" + targetNamespaceWSDL + "#\"\n,";
        
        documentoWSML.append("\n").append(namespaceDefaultWSML);
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
            
            Description descricao = reader.readWSDL(entrada.getAbsolutePath()); //<-- the Description component, always returned
            
            return descricao;
        }
        catch (WSDLException ex) {
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
                    Logger.getLogger(GroundingCoreografia1.class.getName()).log(Level.SEVERE, null, ex);
                    break;
            }
            return null;
        }
    }

    boolean processar() {
        try (PrintWriter pw = new PrintWriter(saida, "UTF-8")) {
            
            
            return true;
        }
        //TODO tratar todas as condicoes de erro
        catch (FileNotFoundException ex) {
            Logger.getLogger(GroundingCoreografia1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(GroundingCoreografia1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
