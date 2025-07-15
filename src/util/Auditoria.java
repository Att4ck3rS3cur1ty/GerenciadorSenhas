package util;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Auditoria{
    // Lista para armazenar os registros
    private static List<String> registros = new ArrayList<>();

    // registrar ações
    public static void registrar(String usuario, String acao){
        String registro = LocalDateTime.now() + " - " + usuario + " - " + acao;
        registros.add(registro);
        System.out.println(registro);
    }

    // obter registros
    public static List<String> getRegistros(){
        return new ArrayList<>(registros);
    }

    // limpar registros
    public static void limparRegistros(){
        registros.clear();
    }
}