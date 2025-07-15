package util;
public class ConfiguracaoSeguranca {
    // Definição das políticas de senha
    private int tamanhoMinimoSenha = 12;
    private boolean exigirMaiusculas = true;
    private boolean exigirMinusculas = true;
    private boolean exigirNumeros = true;
    private boolean exigirEspeciais = true;
    private int diasExpiracaoSenha = 90;
    private int maxTentativasLogin = 5;

    // Padrão Singleton para garantir que o sistema tem apenas uma instância da classe
    private static ConfiguracaoSeguranca instancia;

    private ConfiguracaoSeguranca() {}

    // synchronized para proteger recursos que são acessados concorrentemente
    public static synchronized ConfiguracaoSeguranca getInstancia(){
        if (instancia == null) {
            instancia = new ConfiguracaoSeguranca();
        }

        return instancia;
    }

    // validar se a senha está atendendo aos requisitos
    public boolean validarSenha(String senha){
        if(senha == null || senha.length() < tamanhoMinimoSenha){
            throw new IllegalArgumentException(
                String.format("Senha deve ter no mínimo %d caracteres. Caracteres informados: %d", tamanhoMinimoSenha, senha != null ? senha.length() : 0));
        }

        if(exigirMaiusculas && !senha.matches(".*[A-Z].*")){
            throw new IllegalArgumentException("Senha deve ter pelo menos uma letra maiúscula");
        }

        if(exigirMaiusculas && !senha.matches(".*[a-z].*")){
            throw new IllegalArgumentException("Senha deve ter pelo menos uma letra minúscula");
        }

        if(exigirNumeros && !senha.matches(".*\\d.*")){
            throw new IllegalArgumentException("Senha deve ter pelo menos um número");
        }

        if(exigirEspeciais && !senha.matches(".*[!@#$%&*?].*")){
            throw new IllegalArgumentException("Senha deve ter pelo menos um caractere especial");
        }

        return true;
    }

    // Bloco de getters e setters

    public int getTamanhoMinimoSenha(){
        return this.tamanhoMinimoSenha;
    }

    public void setTamanhoMinimoSenha(int tamanho){
        if(tamanho >= 8){ // proibido menor do que 8
            this.tamanhoMinimoSenha = tamanho;
        }
    }

    public boolean getExigirMaiusculas(){
        return this.exigirMaiusculas;
    }

    public void setExigirMaiusculas(boolean exigir){
        this.exigirMaiusculas = exigir;
    }

    public boolean getExigirMinusculas(){
        return this.exigirMinusculas;
    }

    public void setExigirMinusculas(boolean exigir){
        this.exigirMinusculas = exigir;
    }

    public boolean getExigirNumeros(){
        return this.exigirNumeros;
    }

    public void setExigirNumeros(boolean exigir){
        this.exigirNumeros = exigir;
    }

    public boolean getExigirEspeciais(){
        return this.exigirEspeciais;
    }

    public void setExigirEspeciais(boolean exigir){
        this.exigirEspeciais = exigir;
    }

    public int getDiasExpiracaoSenha(){
        return this.diasExpiracaoSenha;
    }

    public void setDiasExpiracaoSenha(int expiracao){
        this.diasExpiracaoSenha = expiracao;
    }

    public int getMaxTentativasLogin(){
        return this.maxTentativasLogin;
    }

    public void setMaxTentativasLogin(int max_tentativas){
        this.maxTentativasLogin = max_tentativas;
    }
}
