package br.com.ordenaspace.model;

/**
 * Descricao da funcao:
 * Representa os perfis de acesso suportados pela aplicacao.
 * Parametros e retorno:
 * Enum sem parametros; cada constante representa um nivel de permissao.
 * Armazenamento e persistencia:
 * E persistido como texto na coluna perfil da tabela de usuarios.
 * TODO para evolucao online/producao:
 * Expandir para modelo RBAC com permissoes por recurso e auditoria.
 */
public enum PerfilUsuario {
    ADMIN
}
