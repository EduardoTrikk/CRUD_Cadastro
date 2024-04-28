package Funcoes;

import Telas.PainelCadastrados;
import Telas.PainelCadastro;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Botoes {

    public void btnDeletar(JTable jTable1) {
        int index = jTable1.getSelectedRow();

        if (index != -1) {
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            Object idObject = model.getValueAt(index, 0);

            if (idObject instanceof Integer) {
                int id = (int) idObject;
                try {
                    ConexaoSql con = new ConexaoSql();
                    con.removerPessoa(id);
                    model.removeRow(index);
                    JOptionPane.showMessageDialog(null, "Removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    Logger.getLogger(PainelCadastrados.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (idObject instanceof String) {
                String idString = ((String) idObject).trim();
                try {
                    int id = Integer.parseInt(idString);
                    ConexaoSql con = new ConexaoSql();
                    con.removerPessoa(id);
                    model.removeRow(index);
                    JOptionPane.showMessageDialog(null, "Removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Formato de ID inválido", "Erro", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    Logger.getLogger(PainelCadastrados.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Formato de ID inválido", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Nenhuma linha selecionada", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    public void btnInserir(String nome, String endereco, String cpf, String telefone, String tipoSanguineo, String rh, String curso) {
        ConexaoSql con = null;
        try {
            con = new ConexaoSql();
        } catch (SQLException ex) {
            Logger.getLogger(PainelCadastro.class.getName()).log(Level.SEVERE, null, ex);
        }   

        if (con != null) {
            if (con.inserirPessoa(nome, endereco, cpf, telefone, tipoSanguineo, rh, curso)) {
                JOptionPane.showMessageDialog(null, "Pessoa inserida com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao inserir pessoa.");
            }  
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao criar conexão com o banco de dados.");
        }
    }                                  
    public void btnAlterarDados(JTable jTable1) {
    try {
        int linhaSelecionada = jTable1.getSelectedRow();
        if (linhaSelecionada != -1) {
            String[] campos = {"Nome", "Endereco", "CPF", "Telefone", "Curso", "T_sangue", "RH"};

            StringBuilder mensagem = new StringBuilder("Escolha o número correspondente ao campo a ser alterado:\n");
            for (int i = 0; i < campos.length; i++) {
                mensagem.append(i + 1).append(". ").append(campos[i]).append("\n");
            }

            int escolhaCampo = Integer.parseInt(JOptionPane.showInputDialog(null, mensagem.toString()));

            if (escolhaCampo >= 1 && escolhaCampo <= campos.length) {
                String campo = campos[escolhaCampo - 1];
                String valorAntigo = jTable1.getValueAt(linhaSelecionada, 0).toString().trim();

                String novoValor = JOptionPane.showInputDialog(null, "Digite o novo valor para " + campo + ":");
                if (novoValor != null) {
                    novoValor = novoValor.trim();
                    int id = Integer.parseInt(valorAntigo);
                    btnAlterar(campo, novoValor, id);

                    JOptionPane.showMessageDialog(null, "Registro alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                    model.setRowCount(0);
                    Dados da = new Dados();
                    da.preencherTabela(jTable1);
                }
                } else {
                    JOptionPane.showMessageDialog(null, "Escolha inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Nenhuma linha selecionada.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Escolha inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void btnAlterar(String campo, String valor, int id) {
        try {
            ConexaoSql conexao = new ConexaoSql();
            Connection connection = conexao.getConnection();

            if (!tabelaExiste("T_CADASTRO", connection)) {
                System.out.println("A tabela T_CADASTRO não existe no banco de dados.");
                return;
            }
            String sql = "UPDATE T_CADASTRO SET " + campo + " = ? WHERE id = ?";
            PreparedStatement psInsert = connection.prepareStatement(sql);
            psInsert.setString(1, valor);
            psInsert.setInt(2, id);

            if (psInsert.executeUpdate() > 0) {
                System.out.println("Operação bem-sucedida! Informação alterada.");
            } else {
                System.out.println("Falha ao executar a operação.");
            }
            conexao.desconectar();
        } catch (SQLServerException e) {
            if (e.getMessage().contains("Os dados de sequência ou binários estão truncados")) {
                JOptionPane.showMessageDialog(null, "O valor inserido é muito grande. Por favor, insira um valor menor.");
            } else {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private boolean tabelaExiste(String nomeTabela, Connection connection) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet rs = metaData.getTables(null, null, nomeTabela, null);
        return rs.next();
    }
}

