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

    @Override
    public String toString() {
        String enderecoToString;
        try {
            enderecoToString = endereco.toString();
        }
        catch (NullPointerException e) {
            System.out.println("[Contato.toString] NullPointerException ao executar contato(" + this.getNome() + ").toString. Motivo: Endereco == null");
            enderecoToString = "null";
        }
        return "Contato{" + "id=" + id + ", nome=" + nome + ", email=" + email + ", fone=" + fone + ", usuario=" + usuario + ", endereco=" + enderecoToString + '}';
    }


}
