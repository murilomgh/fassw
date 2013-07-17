package fassw;

/**
 * 
 * @author Murilo Honorio
 * @version 0.0
 */
class ElementoNaoEsperadoException extends Exception {

    public ElementoNaoEsperadoException(String esperado, String fornecido) {
        System.err.println("O elemento esperado era " + esperado + ", porem o elemento fornecido foi " + fornecido);
    }

}
