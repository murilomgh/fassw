package fassw.gui;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * A interface grafica consiste em gerar uma instancia de JFileChooser.
 * Os metodos abrirArquivo e salvarArquivo usam o JFileChooser para retornar cadeias contendo caminhos
 * para abrir e salvar arquivos.
 * 
 * Referências: 
 * <br> {@link http://cinforum.forumeiros.com/t2-tutorial-usando-o-jfilechooser}
 * <br> {@link http://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html#input}
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
            return caminho;
        } else {
            JOptionPane.showMessageDialog(null, "Usuario nao definiu onde salvar o resultado. Programa encerrado.", "Atencao!", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
        return null;
    }
    
    public boolean converterArquivo() {
        int converter = JOptionPane.showConfirmDialog(null, 
                        "Em caso de arquivo WSDL versao 1.1, gostaria de tentar converter?", 
                        "Versao diferente", 
                        JOptionPane.YES_NO_OPTION);
        if (converter == JOptionPane.YES_OPTION) {
            return true;
        } 
        else {
            return false;
        }
    }
}