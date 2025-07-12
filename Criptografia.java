import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Criptografia {
    private static final SecureRandom random = new SecureRandom();
    private static final int ITERACOES_PADRAO = 100_000;
    private static final int TAMANHO_SALT = 16; // 16 bytes = 128 bits

    // Hash com salt
    public static String gerarHash(String senha){
        try {
            byte[] salt = gerarSalt();
            byte[] hash = calcularHashComSalt(senha, salt, ITERACOES_PADRAO);

            return "sha256:" + ITERACOES_PADRAO + ":" +
                Base64.getEncoder().encodeToString(salt) + ":" +
                Base64.getEncoder().encodeToString(hash);
                
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritmo não disponível", e);
        }
    }

    // verificar hash a partir da senha informada

    public static boolean verificarHash(String senha, String hashArmazenado){
        try {
            String[] partes = hashArmazenado.split(":");
            if (partes.length != 4 || !partes[0].equals("sha256")) {
                return false;
            }

            int iteracoes = Integer.parseInt(partes[1]);
            byte[] salt = Base64.getDecoder().decode(partes[2]);
            byte[] hashOriginal = Base64.getDecoder().decode(partes[3]);

            byte[] hashTeste = calcularHashComSalt(senha, salt, iteracoes);
            return MessageDigest.isEqual(hashOriginal, hashTeste);
        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] calcularHashComSalt(String senha, byte[] salt, int iteracoes) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(salt);
        
        byte[] hash = digest.digest(senha.getBytes());
        for (int i = 0; i < iteracoes - 1; i++) {
            digest.reset();
            hash = digest.digest(hash);
        }

        return hash;
    }

        private static byte[] gerarSalt(){
        byte[] salt = new byte[TAMANHO_SALT];
        random.nextBytes(salt);
        return salt;
    }

    public static String gerarSenhaAleatoria(int tamanho){
        if (tamanho < 12) throw new IllegalArgumentException("Tamanho mínimo de 12 caracteres");

        String maiusculas = "ABCDEFGHJKLMNPQRSTUVWXYZ";
        String minusculas = "abcdefghijkmnpqrstuvwxyz";
        String numeros = "23456789"; // boas práticas de segurança / recomendações do NIST remover 0 e 1
        String especiais = "!@#$%&*?"; // apenas caracteres mais comuns

        SecureRandom rand = new SecureRandom();
        StringBuilder sb = new StringBuilder(tamanho);

        // utilizando append para garantir pelo menos um char de cada uma das strings definidas acima
        sb.append(maiusculas.charAt(rand.nextInt(maiusculas.length())));
        sb.append(minusculas.charAt(rand.nextInt(maiusculas.length())));
        sb.append(numeros.charAt(rand.nextInt(maiusculas.length())));
        sb.append(especiais.charAt(rand.nextInt(maiusculas.length())));

        // soma todas as strings, pega aleatoriamente e completa a senha 
        String todos = maiusculas + minusculas + numeros + especiais;

        for (int i = 4; i < tamanho; i++){
            sb.append(todos.charAt(rand.nextInt(todos.length())));
        }

        // Embaralhamento de chars
        char[] array = sb.toString().toCharArray();
        
        for (int i = 0; i < array.length; i++) {
            int randomIndex = rand.nextInt(array.length);
            // adaptação do bubble sort
            char temp = array[i];
            array[i] = array[randomIndex];
            array[randomIndex] = temp;
        }

        return new String(array);
    }
}
