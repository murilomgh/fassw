package fassw;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.woden.WSDLException;
import org.apache.woden.WSDLFactory;
import org.apache.woden.WSDLReader;
import org.apache.woden.wsdl20.Description;
import org.apache.woden.wsdl20.ElementDeclaration;
import org.apache.woden.wsdl20.Interface;
import org.apache.woden.wsdl20.InterfaceMessageReference;
import org.apache.woden.wsdl20.InterfaceOperation;
import org.apache.woden.wsdl20.TypeDefinition;
import org.apache.woden.wsdl20.enumeration.Direction;
import org.apache.ws.commons.schema.XmlSchemaAnnotation;
import org.apache.ws.commons.schema.XmlSchemaElement;
import org.apache.ws.commons.schema.XmlSchemaSimpleType;
import org.w3c.dom.Element;

/**
 * 
 * @author Murilo Honorio
 * @version 0.0
 */
public class GroundingDadosOLD {
    
    static File entrada = new File("warehouse.wsdl");
    
    public ArrayList gerarListaDeOperacoes() {
        ArrayList<String> elementos = new ArrayList<>();
        Description descricao = carregarArquivoWSDL(); //o componentes abstratos da descricao do servico
        
        
        ElementDeclaration[] elementDeclarations = descricao.getElementDeclarations();
        
        //ElementDeclaration e = descricao.getElementDeclaration(new QName("money"));
        //System.out.println(e.toString());
        
        
        for (ElementDeclaration ed : elementDeclarations)
        {
            elementos.add(ed.getName().toString());
            
            
            //identificar o tipo de elemento que o objeto representa
            if (ed.getContentModel().equals("org.w3c.dom")) {
                Element ele = (Element) ed.getContent();
                System.out.println("Element: " + ele.getLocalName());
            }
            else if (ed.getContentModel().equals("org.apache.ws.commons.schema")) {
                XmlSchemaElement ele = (XmlSchemaElement) ed.getContent();
                
                System.out.println("XMLSchemaElement: " + ele.getName());
                transformarElement(ele);
            }
            
        }
        
        System.out.println("--- --- ---");
        
        TypeDefinition[] typeDefinitions = descricao.getTypeDefinitions();
        for (TypeDefinition td : typeDefinitions) {
            //identificar o tipo de elemento que o objeto representa
            if (td.getContentModel().equals("org.w3c.dom")) {
                Element ele = (Element) td.getContent();
                System.out.println("Element: " + ele.getLocalName());
            }
            else if (td.getContentModel().equals("org.apache.ws.commons.schema")) {
                XmlSchemaSimpleType ele = (XmlSchemaSimpleType) td.getContent();
                System.out.println("XMLSchemaElement: " + ele.getContent().toString());
                
                
            }
        }
        
        
        Interface[] interfaces = descricao.getInterfaces();
        
        ArrayList<String> elementosDasOperacoes = new ArrayList<>();
        
        for (Interface i : interfaces) {
            InterfaceOperation[] allInterfaceOperations = i.getAllInterfaceOperations();
            for (InterfaceOperation io : allInterfaceOperations) {
                System.out.println("MEP: " + io.getMessageExchangePattern().toASCIIString());
                
                InterfaceMessageReference[] interfaceMessageReferences = io.getInterfaceMessageReferences();
                for (InterfaceMessageReference imr : interfaceMessageReferences) {
                    
                    //verificar se o conteudo referencia um elemento no XML Schema global
                    if (imr.getMessageContentModel().contains("#element")) {
                        String elemento = imr.getElementDeclaration().getName().toString();
                        if (imr.getDirection().equals(Direction.IN)) {
                            System.out.println("IN: " + elemento);
                        }
                        if (imr.getDirection().equals(Direction.OUT)) { //Direction.OUT
                            System.out.println("OUT: " + elemento);
                        }
                        
                        elementosDasOperacoes.add(elemento);
                    }
                }
            }
        }
        
        for (String x : elementosDasOperacoes) {
            
        }

        return elementos;
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
                    Logger.getLogger(GroundingCoreografia.class.getName()).log(Level.SEVERE, null, ex);
                    break;
            }
            return null;
        }
    }

    private void transformarElement(XmlSchemaElement ele) {
        System.out.println("'concept' " + ele.getName());
        System.out.println("\t'xmlType' 'hasValue 'element'");
        System.out.println("\t'substitutionGroup' 'hasValue " + ele.getQName().toString());
        System.out.println("\t'default' 'hasValue' " + ele.getDefaultValue());
        System.out.println("\t'fixed' 'hasValue' " + ele.getFixedValue());
        System.out.println("\t'form' 'hasValue' " + ele.getForm().toString());
        System.out.println("\t'maxOccurs' 'hasValue' " + ele.getMaxOccurs());
        System.out.println("\t'minOccurs' 'hasValue' " + ele.getMinOccurs());
        System.out.println("\t'nillable' 'hasValue' " + ele.isNillable());
        System.out.println("\t'abstract' 'hasValue' " + ele.isAbstract());
        //Demais atributos que possam haver nao sao considerados.
        
        
        XmlSchemaAnnotation annotation = ele.getAnnotation();
    }
}
