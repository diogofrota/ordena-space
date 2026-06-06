package br.com.ordenaspace.model;

/**
 * Descricao da funcao:
 * Modela o usuario autenticavel que acessa o painel administrativo.
 * Parametros e retorno:
 * Entidade mutavel com id, email, hash de senha e perfil.
 * Armazenamento e persistencia:
 * Mapeia a tabela usuarios, usada por login e controle de sessao.
 * TODO para evolucao online/producao:
 * Adicionar ultimo acesso, bloqueio por tentativas e MFA para perfis sensiveis.
 */
public class Usuario {
    private Long id;
    private String email;
    private String senhaHash;
    private PerfilUsuario perfil;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilUsuario perfil) {
        this.perfil = perfil;
    }
}
