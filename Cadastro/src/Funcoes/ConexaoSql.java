package Funcoes;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.String;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import static sun.jvm.hotspot.HelloWorld.e;

public class ConexaoSql {
    private Connection conexao;
    private final String URLDB = "jdbc:sqlserver://localhost:1433;databaseName=CADASTROUEPA;"
            + "encrypt=true;"
            + "trustServerCertificate=true;";

    private final String usuario = "Eduardo";
    private final String senha = "Eduardo";
    
    public ConexaoSql() throws SQLException{
        
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conexao = DriverManager.getConnection(URLDB, usuario, senha);
        } catch (ClassNotFoundException ex){
            System.out.println("Driver não localizado");
        } catch (SQLException ex){
            System.out.println("Erro ao acessar o banco" + ex.getMessage());
        } 
    }
    
    public Connection getConnection() {
        return conexao;
    }

    public void desconectar() {
        if (conexao != null) {
            try {
                conexao.close();
                System.out.println("Conexão encerrada.");
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }

    
    public boolean inserirPessoa(String nome, String endereco, String cpf, String telefone, String tipoSanguineo, String rh, String curso) {
        try (PreparedStatement psInsert = conexao.prepareStatement("INSERT INTO T_CADASTRO (nome, endereco, cpf, telefone, curso, t_sangue, rh) VALUES (?,?,?,?,?,?,?)")) {
            psInsert.setString(1, nome);
            psInsert.setString(2, endereco);
            psInsert.setString(3, cpf);
            psInsert.setString(4, telefone);
            psInsert.setString(5, curso);
            psInsert.setString(6, tipoSanguineo);
            psInsert.setString(7, rh);
        
            return psInsert.executeUpdate() > 0;
        } catch(SQLException e) {
            
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean alterarPessoa(String campo, String valor, int id) {
        try {
            PreparedStatement psInsert = conexao.prepareStatement("UPDATE CADASTROS SET " + campo + " = ? WHERE id = ?");
            psInsert.setString(1, valor);
            psInsert.setInt(2, id); 

            int rowsAffected = psInsert.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Operação com Sucesso! Informação Alterada!");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Nenhum registro atualizado!");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean removerPessoa(int id){
        try {
            PreparedStatement psDelete = conexao.prepareStatement("DELETE FROM T_CADASTRO WHERE id=?");
            psDelete.setInt(1, id);
            psDelete.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public ArrayList<String> getDados() {
        ArrayList<String> dados = new ArrayList<>();
        
        try (Connection conexao = getConnection();
             PreparedStatement ps = conexao.prepareStatement("SELECT id, nome, endereco, cpf, telefone, curso, t_sangue, rh FROM T_CADASTRO");
             ResultSet da = ps.executeQuery()) {
            
            
            while (da.next()) {
                String id = da.getString("id");
                String nome = da.getString("nome");
                String endereco = da.getString("endereco");
                String cpf = da.getString("cpf");
                String telefone = da.getString("telefone");
                String t_sangue = da.getString("t_sangue");
                String rh = da.getString("rh");
                String curso = da.getString("curso");
                
                
                String dado = " " + id + ", " + nome + ", " + endereco + ", " + cpf + ", " + telefone + ", " + curso + ", " + t_sangue + ", " + rh;
                dados.add(dado);
            }
            
        } catch (SQLException e) {
            
            e.printStackTrace();
        }
        
        return dados;
    }
}
