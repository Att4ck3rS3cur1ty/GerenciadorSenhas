package model;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import util.Auditoria;

public class Categoria {
    private String nome;
    private Set<Credencial> credenciais = new HashSet<>();
    private static final List<Categoria> todasCategorias = new ArrayList<>();

    public Categoria(String nome){
        this.nome = nome;
        todasCategorias.add(this); // auto-registro
    }

    // adicionar uma credencial a uma categoria
    public void adicionarCredencial(Credencial  credencial){
        credenciais.add(credencial);
        Auditoria.registrar(credencial.getUsuario().getLogin(), "Credencial adicionada à categoria " + nome);
    }

    // remover credencial da categoria
    public void removerCredencial(Credencial credencial){
        credenciais.remove(credencial);
    }

    public static List<Categoria> listarTodas(){
        return new ArrayList<>(todasCategorias);
    }

    public static Categoria buscarPorNome(String nome){
        return todasCategorias.stream()
            .filter(c -> c.getNome().equalsIgnoreCase(nome))
            .findFirst()
            .orElse(null);
    }

    // getters
    public String getNome(){
        return this.nome;
    }

    public Set<Credencial> getCredenciais(){
        return new HashSet<>(credenciais);
    }
}
