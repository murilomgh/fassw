package fassw.gui;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * A interface gráfica consiste em gerar uma instancia de JFileChooser.
 * Os metodos abrirArquivo e salvarArquivo usam o JFileChooser para retornar cadeias contendo caminhos
 * para abrir e salvar arquivos.
 * 
 * @author Murilo Honorio
 * @version 0.0
 */
public class InterfaceGrafica {

    JFileChooser chooser;

    public InterfaceGrafica() {
        chooser = new JFileChooser();
    }

    public String abrirArquivo() {
        String caminho = "";
        JOptionPane.showMessageDialog(null, "Por favor escolha o arquivo de entrada.", "Escolher arquivo WSDL", JOptionPane.QUESTION_MESSAGE);
        int retorno = chooser.showOpenDialog(null);
        if (retorno == JFileChooser.APPROVE_OPTION) {
            caminho = chooser.getSelectedFile().getAbsolutePath();
        }
        if (retorno == JFileChooser.CANCEL_OPTION) {
            System.exit(0);
        }
        return caminho;
    }
    
    public String salvarArquivo() {
        String caminho = "";
        File arquivo = null;
        JOptionPane.showMessageDialog(null, "Escolha a pasta de saida. Digite nome e extensao do arquivo.", "Local de salvamento", JOptionPane.QUESTION_MESSAGE);
        int retorno = chooser.showSaveDialog(null);
        if (retorno == JFileChooser.APPROVE_OPTION) {
            caminho = chooser.getSelectedFile().getAbsolutePath();
        }
        if (!caminho.equals("")) {
            arquivo = new File(caminho);
        } else {
            JOptionPane.showMessageDialog(null, "Usuario nao definiu onde salvar o resultado. Programa encerrado.", "Atencao!", JOptionPane.WARNING_MESSAGE);
        }
        return caminho;
    }
}