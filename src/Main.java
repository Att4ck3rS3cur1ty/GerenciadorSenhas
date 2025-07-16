import java.util.InputMismatchException;
import java.util.Scanner;
import model.Categoria;
import model.Credencial;
import model.Usuario;
import service.GerenciadorUsuario;
import util.Auditoria;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        
        do {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Cadastrar usuário");
            System.out.println("2. Adicionar credencial");
            System.out.println("3. Listar usuários e credenciais");
            System.out.println("4. Alterar senha");
            System.out.println("5. Ver logs de auditoria");
            System.out.println("6. Gerenciar categorias");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        cadastrarUsuario(scanner);
                        break;
                    case 2:
                        adicionarCredencial(scanner);
                        break;
                    case 3:
                        listarUsuarios();
                        break;
                    case 4:
                        alterarSenha(scanner);
                        break;
                    case 5:
                        verLogsAuditoria();
                        break;
                    case 6:
                        gerenciarCategorias(scanner);
                        break;
                    case 0:
                        System.out.println("Saindo...");
                        break;

                    default:
                        System.out.println("Opção inválida!");
                }

            } catch (InputMismatchException e) {
                scanner.nextLine();
                continue;
            }

            
        } while (opcao != 0);
        scanner.close();
    }

    private static void cadastrarUsuario(Scanner scanner){
        System.out.println("\nNome: ");
        String nome = scanner.nextLine();

        System.out.println("Login: ");
        String login = scanner.nextLine();
        
        System.out.println("Senha: ");
        String senha = scanner.nextLine();

        try {
            GerenciadorUsuario.cadastrarUsuario(nome, login, senha);
        } catch (IllegalArgumentException e) {
            System.out.println("Cadastro falhou: " + e.getMessage());
        }
    }

    private static void adicionarCredencial(Scanner scanner){
        System.out.println("\nLogin do usuário: ");
        String login = scanner.nextLine();

        Usuario usuario = GerenciadorUsuario.buscarUsuario(login);
        if (usuario == null) {
            System.out.println("Usuário não encontrado");
            return;
        }


        System.out.println("Serviço (ex: Gmail: )");
        String servico = scanner.nextLine();

        System.out.println("Senha do serviço: ");
        String senha = scanner.nextLine();

        Credencial credencial = new Credencial(servico, usuario, senha);
        usuario.adicionarCredencial(credencial);
        System.out.println("Credencial adicionada com sucesso!");
        System.out.printf("Força da senha: %.1f bits\n", credencial.getEntropia());

        // mostra a classificação de acordo com a entropia
        if (credencial.getEntropia() < 28) {
            System.out.println("AVISO: esta senha é considerada FRACA!");
        }

        else if(credencial.getEntropia() < 60){
            System.out.println("AVISO: esta senha é considerada MODERADA!");
        }

        else{
            System.out.println("Senha FORTE detectada!");
        }
    }

    private static void listarUsuarios(){
        System.out.println("\n=== USUÁRIOS CADASTRADOS ===");
        
        for (Usuario usuario : GerenciadorUsuario.listarUsuarios()) {
            System.out.println("Nome: " + usuario.getNome() + " | Login: " + usuario.getLogin());
            
            System.out.println("Credenciais: ");
            if (usuario.getCredenciais().isEmpty()) {
                System.out.println("Nenhuma credencial cadastrada para o usuário " + usuario.getNome());
            }

            else{
                usuario.getCredenciais().forEach((credencial) -> {
                    System.out.println("  - " + credencial.getServico() + " (Força: " + String.format("%.1f", credencial.getEntropia()) + ")" + " bits");
                });
                System.out.println("------");

                usuario.getCredenciais().forEach(cred -> {
                    System.out.println("  - " + cred.getServico() + 
                        " | Categorias: " + 
                        (cred.getCategorias().isEmpty() ? "Nenhuma" : cred.getNomesCategorias()));
                });
            }
            Auditoria.registrar("SISTEMA", "Listagem de usuários solicitada");
        }
    }

    private static void alterarSenha(Scanner scanner){
        System.out.println("\nLogin do usuário: ");
        String login = scanner.nextLine();

        Usuario usuario = GerenciadorUsuario.buscarUsuario(login);
        if (usuario == null) {
            System.out.println("Usuário não encontrado!");
            return;
        }

        try {
            System.out.println("Senha atual: ");
            String atual = scanner.nextLine();

            System.out.println("Nova senha: ");
            String nova = scanner.nextLine();

            if (usuario.alterarSenha(atual, nova)) {
                System.out.println("Senha alterada com sucesso!");
            } 

            else {
                System.out.println("Senha atual incorreta!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Erro na senha: " + e.getMessage());
            System.out.println("Por favor, tente novamente com uma senha mais forte.");
        }
    }

    private static void verLogsAuditoria(){
        System.out.println("\n=== LOGS DE AUDITORIA ===");
        for (String registro : Auditoria.getRegistros()) {
            System.out.println(registro);           
        }
    }

    private static void gerenciarCategorias(Scanner scanner) {
    int opcao;
    
    do {
        System.out.println("\n=== GERENCIAR CATEGORIAS ===");
        System.out.println("1. Criar categoria");
        System.out.println("2. Vincular credencial");
        System.out.println("3. Desvincular credencial"); 
        System.out.println("4. Listar categorias");
        System.out.println("5. Remover categoria");
        System.out.println("6. Voltar");
        System.out.print("Escolha: ");

        try {
            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer
        } catch (InputMismatchException e) {
            System.out.println("Erro: Digite apenas números de 1 a 6.");
            scanner.nextLine(); // Limpar input inválido
            opcao = 0; // Forçar nova iteração
            continue;
        }

        switch (opcao) {
            case 1:
                System.out.print("Nome da categoria: ");
                String nome = scanner.nextLine();
                new Categoria(nome);
                System.out.println("Categoria criada!");
                break;

            case 2:
                System.out.print("Login do usuário: ");
                Usuario usuario = GerenciadorUsuario.buscarUsuario(scanner.nextLine());

                if (usuario == null) {
                    System.out.println("Usuário não encontrado!");
                    break;
                }

                if (usuario.getCredenciais().isEmpty()) {
                    System.out.println("Nenhuma credencial disponível para este usuário");
                    break;
                }

                System.out.println("Credenciais disponíveis:");
                usuario.getCredenciais().forEach(c -> System.out.println("- " + c.getServico()));

                System.out.print("Serviço da credencial: ");
                String servico = scanner.nextLine();

                Credencial cred = usuario.getCredenciais().stream()
                    .filter(c -> c.getServico().equalsIgnoreCase(servico))
                    .findFirst()
                    .orElse(null);

                if (cred == null) {
                    System.out.println("Credencial não encontrada!");
                    break;
                }

                System.out.print("Categoria para vincular: ");
                String nomeCat = scanner.nextLine();
                Categoria cat = Categoria.buscarPorNome(nomeCat);

                if (cat != null) {
                    cred.adicionarCategoria(cat);
                    System.out.println("Credencial vinculada à categoria '" + nomeCat + "'!");
                } else {
                    System.out.println("Categoria não encontrada!");
                }
                break;

            case 3: 
            System.out.print("Login do usuário: ");
            Usuario user = GerenciadorUsuario.buscarUsuario(scanner.nextLine());

            if (user != null && !user.getCredenciais().isEmpty()) {
                System.out.println("Credenciais vinculadas:");
                user.getCredenciais().forEach(c -> 
                    System.out.println("- " + c.getServico() + 
                        " (" + c.getNomesCategorias() + ")"));
            
                System.out.print("Serviço da credencial: ");
                String svc = scanner.nextLine();

                Credencial crd = user.getCredenciais().stream() 
                    .filter(c -> c.getServico().equalsIgnoreCase(svc))
                    .findFirst()
                    .orElse(null);
            
                if (crd != null && !crd.getCategorias().isEmpty()) {
                    System.out.print("Categoria para desvincular: ");
                    String nomeCategoria = scanner.nextLine(); // Nova variável
                    Categoria ctg = Categoria.buscarPorNome(nomeCategoria);
                
                    if (ctg != null && crd.removerCategoria(ctg)) { // Corrigido aqui
                        ctg.removerCredencial(crd);
                        System.out.println("Desvinculação realizada!");
                    } else {
                        System.out.println("Falha na desvinculação!");
                    }
                }
            }
            break;

            case 4:
                System.out.println("\n=== CATEGORIAS ===");
                if (Categoria.listarTodas().isEmpty()) {
                    System.out.println("Nenhuma categoria cadastrada ainda.");
                } else {
                    Categoria.listarTodas().forEach(c -> 
                        System.out.println("- " + c.getNome() + 
                            " (" + c.getCredenciais().size() + " credenciais)"));
                }
                break;

            case 5:
                System.out.print("Nome da categoria a remover: ");
                String nomeRemover = scanner.nextLine();
                
                if (Categoria.removerCategoria(nomeRemover)) {
                    System.out.println("Categoria removida com sucesso!");
                } 
                
                else {
                    System.out.println("Categoria não encontrada!");
                }
                break;

            case 6:
                System.out.println("Voltando ao menu principal...");
                break;

            default:
                System.out.println("Opção inválida! Digite de 1 a 5.");
        }
        
        if (opcao != 6) {
            System.out.println("\nPressione Enter para continuar...");
            scanner.nextLine();
        }
        
    } while (opcao != 6);
}
}
