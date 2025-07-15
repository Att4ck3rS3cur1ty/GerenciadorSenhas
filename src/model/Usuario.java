package model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import service.Criptografia;
import util.Auditoria;
import util.ConfiguracaoSeguranca;

public class Usuario{
    private String nome;
    private String login;
    private String senhaHash;
    // private boolean autenticacao;
    private List<Credencial> credenciais; // 1:N

    public Usuario(String nome, String login, String senha){
        this.nome = nome;
        this.login = login;
        this.senhaHash = Criptografia.gerarHash(senha);
        this.credenciais = new ArrayList<>();
    }

    public String getNome(){
        return this.nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getLogin(){
        return this.login;
    }

    public void setLogin(String login){
        this.login = login;
    }

    public String getSenhaHash(){
        return this.senhaHash;
    }

    protected void setSenhaHash(String hash){
        if (hash == null) throw new IllegalArgumentException();
        this.senhaHash = hash;
    }

    public void adicionarCredencial(Credencial c){
        this.credenciais.add(c);
    }

    public List<Credencial> getCredenciais(){
        return Collections.unmodifiableList(credenciais);
    }
    
    /*
    public boolean autenticar(String senha){

    }

     //? nao esta no diagrama
    */

    public boolean alterarSenha(String atual, String nova){
        if(!verificarSenha(atual)) {
            Auditoria.registrar(this.login, "Tentativa falha de alteração de senha");
            return false;
        }

        try {
            ConfiguracaoSeguranca.getInstancia().validarSenha(nova);
            this.senhaHash = Criptografia.gerarHash(nova);
            Auditoria.registrar(this.login, "Senha alterada com sucesso");
            return true;
        } catch (IllegalArgumentException e) {
            Auditoria.registrar(this.login, "Erro ao alterar senha: " + e.getMessage());
            throw e;
        }
    }

    protected boolean verificarSenha(String senha){
        return Criptografia.verificarHash(senha, this.senhaHash);
    }

}