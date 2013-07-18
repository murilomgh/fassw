package fassw.util;

/**
 * Classe que contem excecao lancada em caso de problemas de logica no algoritmo de grounding de dados.
 * @author Murilo Honorio
 */
public class ElementoNaoEsperadoException extends Exception {

    /**
     * Excecao lancada caso alguma funcao de mapeamento seja alimentada com parametro invalido.
     * @param esperado elemento XML para o qual o mapeamento foi projetado
     * @param fornecido elemento XML fornecido como entrada
     */
    public ElementoNaoEsperadoException(String esperado, String fornecido) {
        System.err.println("O elemento esperado era " + esperado + ", porem o elemento fornecido foi " + fornecido);
    }
}
