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

    private String erroDbRepository = null;

    public void inserir(Contato contato) throws Exception {

        Connection conn = FabricaDeConexoes.conectar();
        try {

            String sql = "INSERT INTO contatos (nome, email, fone, usuario) VALUES (?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, contato.getNome() );
            ps.setString(2, contato.getEmail() );
            ps.setString(3, contato.getFone() );
            ps.setString(4, contato.getUsuario() );
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("[ContatoRepository] SQLException ao inserir: registro duplicado?");
            this.setErroDbRepository(e.getMessage() );
//            System.out.println(e.getMessage() );
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[ContatoRepository] Exception generico ao inserir");
            this.setErroDbRepository(FabricaDeConexoes.getErroFc());
            throw e;
        } finally {
//            System.out.println("[ContatoRepository] fechando conexao com banco...");
            FabricaDeConexoes.desconectar(conn);
        }
    }

    public void editar(Contato contato) throws Exception {

        Connection conn = FabricaDeConexoes.conectar();
        try {

            String sql = "UPDATE contatos SET nome = ?, email = ?, fone = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, contato.getNome());
            ps.setString(2, contato.getEmail());
            ps.setString(3, contato.getFone());
            ps.setInt(4, contato.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("[ContatoRepository] Exception ao editar contato");
//            System.out.println(e.getMessage() );
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[ContatoRepository] Exception generico ao editar contato");
        } finally {
//            System.out.println("[ContatoRepository] fechando conexao com banco...");
            FabricaDeConexoes.desconectar(conn);
        }
    }

    public Contato buscarPorId(int id) throws Exception {

        Connection conn = FabricaDeConexoes.conectar();
        Contato contato = new Contato();
        try {

//            String sql = "INSERT INTO contatos (nome, email, fone) VALUES (?,?,?)";
            String sql = "SELECT * FROM contatos WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
//            ps.setString(2, contato.getEmail() );
//            ps.setString(3, contato.getFone() );
//            ps.executeUpdate();

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                contato.setId(rs.getInt("id"));
                contato.setNome(rs.getString("nome"));
                contato.setEmail(rs.getString("email"));
                contato.setFone(rs.getString("fone"));
            }
        } catch (SQLException e) {
            System.out.println("[ContatoRepository] Exception ao buscarPorId contato");
//            System.out.println(e.getMessage() );
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[ContatoRepository] Exception generico ao buscarPorId contato");
        } finally {
//            System.out.println("[ContatoRepository] fechando conexao com banco...");
            FabricaDeConexoes.desconectar(conn);
        }
        return contato;
    }

    public void excluirPorId(int id) throws Exception {

        Connection conn = FabricaDeConexoes.conectar();
        try {
//            String sql = "INSERT INTO contatos (nome, email, fone) VALUES (?,?,?)";
            String sql = "DELETE FROM contatos WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("[ContatoRepository] Exception ao excluir contato");
//            System.out.println(e.getMessage() );
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[ContatoRepository] Exception generico ao excluir contato");
        } finally {
//            System.out.println("[ContatoRepository] fechando conexao com banco...");
            FabricaDeConexoes.desconectar(conn);
        }
    }

    public List<Contato> listar(String usuarioNome) throws Exception {

        if (usuarioNome == null || usuarioNome.isBlank() ) {
            return null;
        }
        usuarioNome = usuarioNome.strip();
        
        List<Contato> retorno = new ArrayList<>();
        Connection conn = FabricaDeConexoes.conectar();

        try {
//            String sql = "SELECT * FROM contatos ORDER BY nome";
            String sql = "SELECT * FROM contatos WHERE usuario = ? ORDER BY nome;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, usuarioNome);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Contato contato = new Contato();
                contato.setId(rs.getInt("id"));
                contato.setNome(rs.getString("nome"));
                contato.setEmail(rs.getString("email"));
                contato.setFone(rs.getString("fone"));
                contato.setUsuario(rs.getString("usuario"));
                retorno.add(contato);
            }
        } catch (SQLException e) {
//            this.setErroDbRepository("[ContatoRepository] SQLException ao listar todos os contatos");
            this.setErroDbRepository(FabricaDeConexoes.getErroFc());
            System.out.println(getErroDbRepository());
//            System.out.println(e.getMessage() );
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
//            this.setErroDbRepository("[ContatoRepository] Exception generico ao listar todos os contatos");
            this.setErroDbRepository(FabricaDeConexoes.getErroFc() );
            System.out.println(getErroDbRepository());
        } finally {
//            System.out.println("[ContatoRepository] fechando conexao com banco...");
            FabricaDeConexoes.desconectar(conn);
        }
        return retorno;
    }

    public String getErroDbRepository() {
        return erroDbRepository;
    }

    public void setErroDbRepository(String erro) {
        this.erroDbRepository = erro;
    }

}
