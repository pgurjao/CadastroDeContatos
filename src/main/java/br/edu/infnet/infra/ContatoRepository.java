package br.edu.infnet.infra;

import br.edu.infnet.domain.Contato;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

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
    
    public void editar (Contato contato) throws Exception {
        
        Connection conn = FabricaDeConexoes.conectar();
        try {
            
            String sql = "UPDATE contatos SET nome = ?, email = ?, fone = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, contato.getNome() );
            ps.setString(2, contato.getEmail() );
            ps.setString(3, contato.getFone() );
            ps.setInt(4, contato.getId() );
            
            ps.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("[ContatoRepository] Exception ao editar contato");
//            System.out.println(e.getMessage() );
            throw e;
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("[ContatoRepository] Exception generico ao editar contato");
        } finally {
//            System.out.println("[ContatoRepository] fechando conexao com banco...");
            FabricaDeConexoes.desconectar(conn);
        }
    }
    
    public void buscar (Contato contato) throws Exception {
        
        Connection conn = FabricaDeConexoes.conectar();
        try {
            
//            String sql = "INSERT INTO contatos (nome, email, fone) VALUES (?,?,?)";
            String sql = "SELECT * FROM contatos WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, contato.getEmail() );
//            ps.setString(2, contato.getEmail() );
//            ps.setString(3, contato.getFone() );
            ps.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("[ContatoRepository] Exception ao buscar contato");
//            System.out.println(e.getMessage() );
            throw e;
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("[ContatoRepository] Exception generico ao buscar contato");
        } finally {
//            System.out.println("[ContatoRepository] fechando conexao com banco...");
            FabricaDeConexoes.desconectar(conn);
        }
    }
    
    public void excluir (int id) throws Exception {
        
        Connection conn = FabricaDeConexoes.conectar();
        try {
//            String sql = "INSERT INTO contatos (nome, email, fone) VALUES (?,?,?)";
            String sql = "DELETE FROM contatos WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("[ContatoRepository] Exception ao excluir contato");
//            System.out.println(e.getMessage() );
            throw e;
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("[ContatoRepository] Exception generico ao excluir contato");
        } finally {
//            System.out.println("[ContatoRepository] fechando conexao com banco...");
            FabricaDeConexoes.desconectar(conn);
        }
    }
    
    public List<Contato> listar() throws Exception {
        
        List<Contato> retorno = new ArrayList<>();
        Connection conn = FabricaDeConexoes.conectar();
        
        try {
            String sql = "SELECT * FROM contatos ORDER BY nome";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next() ) {
                Contato contato = new Contato();
                contato.setId(rs.getInt("id") );
                contato.setNome(rs.getString("nome") );
                contato.setEmail(rs.getString("email") );
                contato.setFone(rs.getString("fone") );
                retorno.add(contato);
            }
        }
        catch (SQLException e) {
            System.out.println("[ContatoRepository] Exception ao buscar contato");
//            System.out.println(e.getMessage() );
            throw e;
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("[ContatoRepository] Exception generico ao buscar contato");
        } finally {
//            System.out.println("[ContatoRepository] fechando conexao com banco...");
            FabricaDeConexoes.desconectar(conn);
        }
        return retorno;
    }
    
}
