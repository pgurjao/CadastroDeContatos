package br.edu.infnet.infra;

import java.sql.Connection;
import java.sql.DriverManager;

public class FabricaDeConexoes {
    
    public static Connection conectar() {
        
        Connection retorno = null;
        try {
            // 1 - PARAMETROS
            String driver = "com.mysql.cj.jdbc.Driver";
            String usuario = "root";
            String senha = "root";
            String url = "jdbc:mysql://localhost:3306/javaweb-armenio?zeroDateTimeBehavior=CONVERT_TO_NULL";
            
            // 2 - CARREGAR O DRIVER NA MEMORIA
            Class.forName(driver);
            
            // 3 - CONECTAR COM O BANCO
            retorno = DriverManager.getConnection(url, usuario, senha);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[FabricaDeConexoes] Erro ao conectar no banco de dados");
        }
        return retorno;
    }
    
    public static void desconectar (Connection conn) {
        
        try {
            if (conn != null && !conn.isClosed() ) {
                conn.close();
//                System.out.println("[FabricaDeConexoes] Conexao com banco fechada com sucesso");
            } else {
                System.out.println("[FabricaDeConexoes] Estado da conexao com banco desconhecido, impossivel fechar");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[FabricaDeConexoes] Erro ao desconectar no banco de dados");
        }
    }
}
