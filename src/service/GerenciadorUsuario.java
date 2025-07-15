package service;
import java.util.ArrayList;
import java.util.List;
import model.Usuario;
import util.Auditoria;
import util.ConfiguracaoSeguranca;

public class GerenciadorUsuario {
    private static List<Usuario> usuarios = new ArrayList<>();
    
    // Cadastrar novo usuário
    public static void cadastrarUsuario(String nome, String login, String senha){
        try {
            if (existeLogin(login)) {
                throw new IllegalArgumentException("Login já está em uso! Escolha outro.");
            }

            ConfiguracaoSeguranca.getInstancia().validarSenha(senha);
            
            Usuario novoUsuario = new Usuario(nome, login, senha);
            usuarios.add(novoUsuario);
            Auditoria.registrar(login, "Novo usuário cadastrado");
            System.out.println("Usuário cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            Auditoria.registrar(login, "Tentativa de cadastro com senha inválida: " + e.getMessage());
            throw e; // tratar na main
        }

    }

    // fix para usuários duplicados: método de verificação se existe login
    private static boolean existeLogin(String login){
        return usuarios.stream().anyMatch(u -> u.getLogin().equalsIgnoreCase(login));
    }

    // buscar por login
    public static Usuario buscarUsuario(String login){
        for (Usuario usuario : usuarios) {
            if (usuario.getLogin().equals(login)) {
                return usuario;
            }
        }
        return null;
    }

    // listar todos os usuários (função admin apenas)
    public static List<Usuario> listarUsuarios(){
        return new ArrayList<>(usuarios); // Retorna cópia
    }

    // atualizar usuário (nome especificamente)
    public static boolean atualizarUsuario(String login, String novoNome){
        Usuario usuario = buscarUsuario(login);
        if (usuario != null) {
            usuario.setNome(novoNome);
            Auditoria.registrar(login, "Dados atualizados");
            return true;
        }
        return false;
    }

    public static boolean removerUsuario(String login){
        Usuario usuario = buscarUsuario(login);
        if (usuario != null) {
            usuarios.remove(usuario);
            Auditoria.registrar(login, "Usuário removido");
            return true;
        }
        return false;
    }
}
