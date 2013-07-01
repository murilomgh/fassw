package fassw;

import fassw.gui.InterfaceGrafica;
/**
 * Classe responsável por ler as descrições dos serviços Web em WSDL e transformar em descrições 
 * WSML segundo o modelo WSMO.
 * @author Murilo Honorio
 * @version 0.0
 */
class Mapeador {
    private String entrada, saida; //caminhos para os arquivos de entrada e saida
    boolean linhaDeComando;

    //versao do mapeador por linha de comando
    public Mapeador(String entrada, String saida) {
        this.entrada = entrada;
        this.saida = saida;
        this.linhaDeComando = true;
    }
    
    //versao do mapeador com interface grafica
    public Mapeador() {
        InterfaceGrafica ig = new InterfaceGrafica();
        this.entrada = ig.abrirArquivo();
        this.saida = ig.salvarArquivo();
        this.linhaDeComando = false;
    }

    
    public boolean executar() {
        
        boolean sucessoGroundingDados = false;
        
        
        return true;
    }
}
