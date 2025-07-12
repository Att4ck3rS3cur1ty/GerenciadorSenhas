public class Credencial {

    // Atributos encapsulados

    private String servico;
    private Usuario usuario;
    private String senha;
    private String url;

    // Construtor

    public Credencial(String servico, Usuario usuario, String senha){
        this.servico = servico;
        this.usuario = usuario;
        this.senha = senha;
    }

    // Getters e Setters

    public String getServico(){
        return this.servico;
    }

    public void setServico(String servico){
        this.servico = servico;
    }

    public Usuario getUsuario(){
        return this.usuario;
    }

    public void setUsuario(Usuario usuario){
        this.usuario = usuario;
    }

    public String getSenha(){
        return this.senha;
    }

    public void setSenha(String senha){
        this.senha = senha;
    }

    public String getUrl(){
        return this.url;
    }
    
    public void setUrl(String url){
        this.url = url;
    }

    public double getEntropia(){
        if (this.senha == null || this.senha.isEmpty()) {
            System.out.println("Senha em branco!");
            return 0;
        }

        int tamanhoAlfabeto = 0;
        boolean temMinusculas = !this.senha.equals(this.senha.toLowerCase());
        boolean temMaiusculas = !this.senha.equals(this.senha.toUpperCase());
        boolean temNumeros = this.senha.matches(".*\\d.*");
        boolean temEspeciais = this.senha.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");

        if (temMinusculas) {
            tamanhoAlfabeto += 26;
        }

        if (temMaiusculas) {
            tamanhoAlfabeto += 26;
        }

        if (temNumeros) {
            tamanhoAlfabeto += 10;
        }

        if (temEspeciais) {
            tamanhoAlfabeto += 32;
        }

        // Cálculo de entropia: log2(tamanhoAlfabeto^comprimento)
        return this.senha.length() * Math.log(tamanhoAlfabeto) / Math.log(2);
    }

    // Sobrescreve o método toString da superclasse Object
    @Override
    public String toString(){
        return String.format(
            "Credencial [servico='%s', usuario='%s', senha=******, url='%s', entropia=%.1f bits]",
            servico,
            usuario.getLogin(),
            url,
            getEntropia());
    }
}
