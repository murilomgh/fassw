package fassw;

import fassw.util.ElementoNaoEsperadoException;
import org.w3c.dom.Node;

/**
 * GroundingDados.java
 * O usuario dessa interface realiza o mapeamento de quaquer componente XML Schema para uma
 * representacao (fragmento) WSMO, conforme transformacoes definidas com base no trabalho de Kopecky
 * et al (2007).
 * Os fragmentos WSMO sao escritos em WSML.
 * 
 * @author Murilo Honorio
 * @since 12-01-2012
 */
public interface GroundingDados {
    
    /**
     * Realiza o mapeamento para WSMO do elemento XML Schema <b>annotation</b>
     * 
     * @param elemento o nodo annotation
     * @return uma cadeia em WSML representanto o elemento mapeado
     * @throws ElementoNaoEsperadoException se o elemento nao eh compativel com o mapeamento
     * @see http://www.w3schools.com/schema/el_annotation.asp
     */
    public String mapearAnnotation(Node elemento) throws ElementoNaoEsperadoException;
    
    /**
     * Realiza o mapeamento para WSMO do elemento XML Schema <b>appinfo</b>.
     * Appinfo eh filho de Annotation.
     * 
     * @param elemento o nodo appinfo
     * @return uma cadeia em WSML representanto o elemento mapeado
     * @throws ElementoNaoEsperadoException se o elemento nao eh compativel com o mapeamento
     * @see http://www.w3schools.com/schema/el_appinfo.asp
     */
    public String mapearAppinfo(Node elemento) throws ElementoNaoEsperadoException;
    
    /**
     * Realiza o mapeamento para WSMO do elemento XML Schema <b>documentation</b>.
     * Documentation eh filho de Annotation.
     * 
     * @param elemento o nodo documentation
     * @return uma cadeia em WSML representanto o elemento mapeado
     * @throws ElementoNaoEsperadoException se o elemento nao eh compativel com o mapeamento
     * @see http://www.w3schools.com/schema/el_documentation.asp
     */
    public String mapearDocumentation(Node elemento) throws ElementoNaoEsperadoException;
    
    /**
     * Realiza o mapeamento para WSMO do elemento XML Schema <b>attributeGroup</b>.
     * 
     * @param elemento o nodo attributeGroup
     * @return uma cadeia em WSML representanto o elemento mapeado
     * @throws ElementoNaoEsperadoException se o elemento nao eh compativel com o mapeamento
     * @see http://www.w3schools.com/schema/el_attributegroup.asp
     */
    public String mapearAttributeGroup(Node elemento) throws ElementoNaoEsperadoException;
    
    /**
     * Realiza o mapeamento para WSMO do elemento XML Schema <b>attribute</b>.
     * 
     * @param elemento o nodo attribute
     * @return uma cadeia em WSML representanto o elemento mapeado
     * @throws ElementoNaoEsperadoException se o elemento nao eh compativel com o mapeamento
     * @see http://www.w3schools.com/schema/el_attributegroup.asp
     */
    public String mapearAttribute(Node elemento) throws ElementoNaoEsperadoException;
    
    /**
     * Realiza o mapeamento para WSMO do elemento XML Schema <b>simpleType</b>.
     * 
     * @param elemento o nodo simpleType
     * @return uma cadeia em WSML representanto o elemento mapeado
     * @throws ElementoNaoEsperadoException se o elemento nao eh compativel com o mapeamento
     * @see http://www.w3schools.com/schema/el_simpletype.asp
     */
    public String mapearSimpleType(Node elemento) throws ElementoNaoEsperadoException;
    
    /**
     * Realiza o mapeamento para WSMO do elemento XML Schema <b>restriction</b>.
     * Restriction eh filho de simpleType, simpleContent ou complexContent.
     * 
     * @param elemento o nodo restriction
     * @return uma cadeia em WSML representanto o elemento mapeado
     * @throws ElementoNaoEsperadoException se o elemento nao eh compativel com o mapeamento
     * @see http://www.w3schools.com/schema/el_restriction.asp
     */
    public String mapearRestriction(Node elemento) throws ElementoNaoEsperadoException;
    
    /**
     * Realiza o mapeamento para WSMO do elemento XML Schema <b>list</b>.
     * List eh filho de simpleType.
     * 
     * @param elemento o nodo list
     * @return uma cadeia em WSML representanto o elemento mapeado
     * @throws ElementoNaoEsperadoException se o elemento nao eh compativel com o mapeamento
     * @see http://www.w3schools.com/schema/el_list.asp
     */
    public String mapearList(Node elemento) throws ElementoNaoEsperadoException;
    
    /**
     * Realiza o mapeamento para WSMO do elemento XML Schema <b>union</b>.
     * Union eh filho de simpleType.
     * 
     * @param elemento o nodo union
     * @return uma cadeia em WSML representanto o elemento mapeado
     * @throws ElementoNaoEsperadoException se o elemento nao eh compativel com o mapeamento
     * @see http://www.w3schools.com/schema/el_union.asp
     */
    public String mapearUnion(Node elemento) throws ElementoNaoEsperadoException;
    
    /**
     * Realiza o mapeamento para WSMO do elemento XML Schema <b>element</b>.
     * 
     * @param elemento o nodo element
     * @return uma cadeia em WSML representanto o elemento mapeado
     * @throws ElementoNaoEsperadoException se o elemento nao eh compativel com o mapeamento
     * @see http://www.w3schools.com/schema/el_element.asp
     */
    public String mapearElement(Node elemento) throws ElementoNaoEsperadoException;
    
    /**
     * Realiza o mapeamento para WSMO do elemento XML Schema <b>complexType</b>.
     * 
     * @param elemento o nodo complexType
     * @return uma cadeia em WSML representanto o elemento mapeado
     * @throws ElementoNaoEsperadoException se o elemento nao eh compativel com o mapeamento
     * @see http://www.w3schools.com/schema/el_complextype.asp
     */
    public String mapearComplexType(Node elemento) throws ElementoNaoEsperadoException;
    
    /**
     * Realiza o mapeamento para WSMO do elemento XML Schema <b>simpleContent</b>.
     * SimpleContent eh filho de complexType.
     * 
     * @param elemento o nodo simpleContent
     * @return uma cadeia em WSML representanto o elemento mapeado
     * @throws ElementoNaoEsperadoException se o elemento nao eh compativel com o mapeamento
     * @see http://www.w3schools.com/schema/el_simpleContent.asp
     */
    public String mapearSimpleContent(Node elemento) throws ElementoNaoEsperadoException;
    
    /**
     * Realiza o mapeamento para WSMO do elemento XML Schema <b>complexContent</b>.
     * ComplexContent eh filho de complexType.
     * 
     * @param elemento o nodo complexContent
     * @return uma cadeia em WSML representanto o elemento mapeado
     * @throws ElementoNaoEsperadoException se o elemento nao eh compativel com o mapeamento
     * @see http://www.w3schools.com/schema/el_complexContent.asp
     */
    public String mapearComplexContent(Node elemento) throws ElementoNaoEsperadoException;
    
    /**
     * Realiza o mapeamento para WSMO do elemento XML Schema <b>extension</b>.
     * Extension eh filho de complexType.
     * 
     * @param elemento o nodo extension
     * @return uma cadeia em WSML representanto o elemento mapeado
     * @throws ElementoNaoEsperadoException se o elemento nao eh compativel com o mapeamento
     * @see http://www.w3schools.com/schema/el_extension.asp
     */
    public String mapearExtension(Node elemento) throws ElementoNaoEsperadoException;
    
    /**
     * Realiza o mapeamento para WSMO do elemento XML Schema <b>group</b>.
     * 
     * @param elemento o nodo group
     * @return uma cadeia em WSML representanto o elemento mapeado
     * @throws ElementoNaoEsperadoException se o elemento nao eh compativel com o mapeamento
     * @see http://www.w3schools.com/schema/el_group.asp
     */
    public String mapearGroup(Node elemento) throws ElementoNaoEsperadoException;
    
    /**
     * Realiza o mapeamento para WSMO do elemento XML Schema <b>all</b>.
     * 
     * @param elemento o nodo all
     * @return uma cadeia em WSML representanto o elemento mapeado
     * @throws ElementoNaoEsperadoException se o elemento nao eh compativel com o mapeamento
     * @see http://www.w3schools.com/schema/el_all.asp
     */
    public String mapearAll(Node elemento) throws ElementoNaoEsperadoException;
    
    /**
     * Realiza o mapeamento para WSMO do elemento XML Schema <b>choice</b>.
     * 
     * @param elemento o nodo choice
     * @return uma cadeia em WSML representanto o elemento mapeado
     * @throws ElementoNaoEsperadoException se o elemento nao eh compativel com o mapeamento
     * @see http://www.w3schools.com/schema/el_choice.asp
     */
    public String mapearChoice(Node elemento) throws ElementoNaoEsperadoException;
    
    /**
     * Realiza o mapeamento para WSMO do elemento XML Schema <b>sequence</b>.
     * 
     * @param elemento o nodo sequence
     * @return uma cadeia em WSML representanto o elemento mapeado
     * @throws ElementoNaoEsperadoException se o elemento nao eh compativel com o mapeamento
     * @see http://www.w3schools.com/schema/el_sequence.asp
     */
    public String mapearSequence(Node elemento) throws ElementoNaoEsperadoException;
}