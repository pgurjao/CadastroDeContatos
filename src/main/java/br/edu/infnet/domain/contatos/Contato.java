package br.edu.infnet.domain.contatos;

import br.edu.infnet.domain.contatos.Endereco;

public class Contato {

    private int id;
    private String nome;
    private String email;
    private String fone;
    private String usuario;
    private Endereco endereco;

    public Contato() {
        this.setEndereco(null);
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

//    @Override
//    public String toString() {
//        System.out.println("[Contato.toString] entrou");
//        System.out.println("[Contato.toString] endereco = " + endereco);
//        String enderecoToString = null;
//        System.out.println("[Contato.toString] printando endereco um por um...");
//        System.out.println("[Contato.toString] cep");
//        try {
//            if (endereco.getCep() == null) {
//                System.out.println("[Contato.toString] CEP == null");
//            }
//            if (endereco.getLogradouro() == null) {
//                System.out.println("[Contato.toString] LOGRADOURO == null");
//            }
//            if (endereco.getComplemento()== null) {
//                System.out.println("[Contato.toString] COMPLEMENTO == null");
//            }
//            if (endereco.getBairro()== null) {
//                System.out.println("[Contato.toString] BAIRRO == null");
//            }
//            if (endereco.getLocalidade()== null) {
//                System.out.println("[Contato.toString] LOCALIDADE == null");
//            }
//            if (endereco.getUf()== null) {
//                System.out.println("[Contato.toString] UF == null");
//            }
//        }
//        catch (NullPointerException e) {
//            System.out.println("[Contato.toString] NullPointerException fazendo endereco.getAlgumaCoisa()");
//            return null;
//        }
//        enderecoToString = endereco.toString();
////        if ( enderecoToString == null) {
////            enderecoToString = "nulll";
////            System.out.println("[Contato.toString] enderecoToString == null");
////        } else {
////            System.out.println("[Contato.toString] enderecoToString != null");
////        }
//        return "Contato{" + "id=" + id + ", nome=" + nome + ", email=" + email + ", fone=" + fone + ", usuario=" + usuario + ", endereco=" + enderecoToString + "\"" + "}";
//    }

    @Override
    public String toString() {
        String enderecoToString;
        try {
            enderecoToString = endereco.toString();
        }
        catch (NullPointerException e) {
            System.out.println("[Contato.toString] NullPointerException ao executar contato(" + this.getNome() + ").toString. Motivo: Endereco == null");
            enderecoToString = "nulll";
        }
        return "Contato{" + "id=" + id + ", nome=" + nome + ", email=" + email + ", fone=" + fone + ", usuario=" + usuario + ", endereco=" + enderecoToString + '}';
    }
    
    
}
