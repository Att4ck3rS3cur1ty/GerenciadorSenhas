import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    protected  boolean alterarSenha(String atual, String nova){
        if(!verificarSenha(atual)) return false;

        // verificar a nova senha contra as políticas
        if(!ConfiguracaoSeguranca.getInstancia().validarSenha(nova)) {
            throw new IllegalArgumentException(("Nova senha não atende às políticas de segurança"));
        }

        this.senhaHash = Criptografia.gerarHash(nova);
        return true;
    }

    protected boolean verificarSenha(String senha){
        return Criptografia.verificarHash(senha, this.senhaHash);
    }

}