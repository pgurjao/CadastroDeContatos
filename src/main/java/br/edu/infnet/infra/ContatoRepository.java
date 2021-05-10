package br.edu.infnet.infra;

import br.edu.infnet.domain.Contato;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class ContatoRepository {
    
    public void inserir (Contato contato) throws Exception {
        
        Connection conn = FabricaDeConexoes.conectar();
        try {
            
            String sql = "INSERT INTO contatos (nome, email, fone) VALUES (?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, contato.getNome() );
            ps.setString(2, contato.getEmail() );
            ps.setString(3, contato.getFone() );
            ps.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("[ContatoRepository] Exception ao inserir: registro duplicado?");
//            System.out.println(e.getMessage() );
            throw e;
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("[ContatoRepository] Exception generico ao inserir");
        } finally {
//            System.out.println("[ContatoRepository] fechando conexao com banco...");
            FabricaDeConexoes.desconectar(conn);
        }
    }
}
