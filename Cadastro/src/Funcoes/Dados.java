package Funcoes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Dados {


    public ArrayList<String> getRegistros() {
        ArrayList<String> registros = new ArrayList<>();
        
        ConexaoSql conexao = null;
        try {
            conexao = new ConexaoSql();
            registros = conexao.getDados();
        } catch (SQLException ex) {
            Logger.getLogger(Dados.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return registros;
    }

    public void preencherTabela(JTable tabela) {

        DefaultTableModel model = new DefaultTableModel(
            new Object [][] {},
            new String [] {
                "ID", "NOME", "ENDERECO", "CPF", "TELEFONE", "CURSO", "SANGUE", "FATOR RH"
            }
        );

        ArrayList<String> registros = getRegistros();

        for (String registro : registros) {
            
            String[] partes = registro.split(", ");
            
            model.addRow(partes);
        }

        tabela.setModel(model);
    }   
}
