package br.edu.infnet.infra;

import br.edu.infnet.domain.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioRepository {

    private String erroDbRepository = null;

    public Usuario buscarPorUsuario(String nomeUsuario) throws Exception {

        Connection conn = FabricaDeConexoes.conectar();
        Usuario usuario = new Usuario();
        try {

//            String sql = "INSERT INTO contatos (nome, email, fone) VALUES (?,?,?)";
            String sql = "SELECT * FROM usuarios WHERE usuario = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nomeUsuario);
//            ps.executeUpdate();

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                usuario.setUsuario(rs.getString("usuario"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setCreate_time(rs.getString("create_time"));
            }
        } catch (SQLException e) {
            System.out.println("[UsuarioRepository] Exception ao buscarPorUsuario usuario");
//            e.printStackTrace();
            this.setErroDbRepository(e.getMessage() );
            System.out.println("[UsuarioRepository] ErroDbRepository = " + this.getErroDbRepository() );
//            System.out.println(e.getMessage() );
            throw e;
        } catch (Exception e) {
//            e.printStackTrace();
            this.setErroDbRepository(FabricaDeConexoes.getErroFc());
//            System.out.println("[UsuarioRepository] erroDbRepo = " + this.getErroDbRepository() );
            System.out.println("[UsuarioRepository] Exception generico ao buscarPorUsuario usuario");
            throw e;
        } finally {
//            System.out.println("[UsuarioRepository] fechando conexao com banco...");
            FabricaDeConexoes.desconectar(conn);
        }
        return usuario;
    }
    
    public void gravarSessao(String sessionId, String usuario) throws Exception {

        Connection conn = FabricaDeConexoes.conectar();
//        Usuario usuario = new Usuario();
        try {

            String sql = "UPDATE usuarios SET session_id = ? WHERE usuario = ?";
//            String sql = "SELECT * FROM usuarios WHERE usuario = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, sessionId);
            ps.setString(2, usuario);
            ps.executeUpdate();

//            ResultSet rs = ps.executeQuery();
//
//            while (rs.next()) {
//                usuario.setUsuario(rs.getString("usuario"));
//                usuario.setSenha(rs.getString("senha"));
//                usuario.setCreate_time(rs.getString("create_time"));
//            }
        } catch (SQLException e) {
            System.out.println("[UsuarioRepository] Exception ao gravarSessao do usuario");
//            e.printStackTrace();
            this.setErroDbRepository(FabricaDeConexoes.getErroFc());
            System.out.println("[UsuarioRepository] ErroDbRepository = " + this.getErroDbRepository() );
//            System.out.println(e.getMessage() );
            throw e;
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("[UsuarioRepository] Exception generico ao gravarSessao do usuario");
        } finally {
//            System.out.println("[UsuarioRepository] fechando conexao com banco...");
            FabricaDeConexoes.desconectar(conn);
        }
    }

    public String getErroDbRepository() {
        return erroDbRepository;
    }

    public void setErroDbRepository(String erroDbRepository) {
        this.erroDbRepository = erroDbRepository;
    }
}
