package fassw.util;

/**
 * Especifica as 5 variantes do WSML. <br>
 * Maiores detalhes em: {@link http://www.wsmo.org/TR/d16/d16.1/v1.0/#sec:wsml-wsmlvariant}.
 * 
 * @author murilo.honorio@usp.br
 */
public enum VarianteWSML {
    Core ("http://www.wsmo.org/wsml/wsml-syntax/wsml-core"),
    Flight	("http://www.wsmo.org/wsml/wsml-syntax/wsml-flight"),
    Rule ("http://www.wsmo.org/wsml/wsml-syntax/wsml-rule"),
    DL ("http://www.wsmo.org/wsml/wsml-syntax/wsml-dl"),
    Full ("http://www.wsmo.org/wsml/wsml-syntax/wsml-full");

    private final String IRI;

    private VarianteWSML(String IRI) {
        this.IRI = IRI;
    }
    
    /**
     * O Internationalized Resource Identifier (IRI) da variante.
     * @return o IRI que pertence a variante
     */
    public String IRI() { return IRI; }
}
