package br.com.ordenaspace.config;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Descricao da funcao:
 * Gera hashes de senha para autenticacao local sem expor a senha em texto puro no codigo Java.
 * Parametros e retorno:
 * Recebe uma senha em texto puro e retorna o hash SHA-256 em hexadecimal.
 * Armazenamento e persistencia:
 * O valor retornado e persistido em coluna de hash no banco e comparado no login.
 * TODO para evolucao online/producao:
 * Substituir SHA-256 simples por BCrypt ou Argon2 com salt e politica de rotacao.
 */
public final class PasswordUtil {

    private PasswordUtil() {
    }

    /**
     * Descricao da funcao:
     * Aplica SHA-256 sobre uma senha informada.
     * Parametros e retorno:
     * Recebe String senha e retorna String hexadecimal.
     * Armazenamento e persistencia:
     * O hash gerado pode ser armazenado em tabela de usuarios para validacao posterior.
     * TODO para evolucao online/producao:
     * Versionar algoritmo de hash para suportar migracoes de credenciais sem reset global.
     */
    public static String sha256(String rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte current : hashed) {
                builder.append(String.format("%02x", current));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 nao disponivel no runtime atual.", exception);
        }
    }
}
