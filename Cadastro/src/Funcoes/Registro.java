package Funcoes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static javax.management.remote.JMXConnectorFactory.connect;


public class Registro {
    
    public boolean Registrar(String email, String senha){
        try {
            ConexaoSql con = new ConexaoSql();
            Connection conexao = con.getConnection();
            
            try (PreparedStatement psInsert = conexao.prepareStatement("INSERT INTO T_LOGIN (email, senha) VALUES (?,?)")) {
                psInsert.setString(1, email);
                psInsert.setString(2, senha);
                
                int rowsAffected = psInsert.executeUpdate();
                
                psInsert.close();
                conexao.close();
                
                return rowsAffected > 0;
            }
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean Login(String email, String senha){
        try {
            ConexaoSql con = new ConexaoSql();
            
            try (Connection conexao = con.getConnection();
                 PreparedStatement psInsert = conexao.prepareStatement("SELECT email, senha FROM T_LOGIN WHERE email = ? AND senha = ?")) {

                psInsert.setString(1, email);
                psInsert.setString(2, senha);
                
                try (ResultSet rs = psInsert.executeQuery()) {
                    boolean autenticacao = rs.next();
                    
                    rs.close();
                    psInsert.close();
                    conexao.close();
                    
                    return autenticacao;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

