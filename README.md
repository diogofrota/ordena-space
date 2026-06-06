# ORDENA SPACE

Sistema web Java JSP para monitoramento de equipes policiais em campo por viaturas com tablets satelitais. A aplicacao usa `Tomcat 10.1+`, `Jakarta Servlet/JSP`, `JDBC`, `Oracle Database` e `Maven`, sem Spring.

## Stack

- Java 17+
- Tomcat 10.1+
- Jakarta Servlet 6.0
- Jakarta Server Pages 3.1
- JSP + JSTL
- JDBC Oracle
- Maven WAR

## Estrutura

```text
src/main/java/br/com/ordenaspace/config
src/main/java/br/com/ordenaspace/model
src/main/java/br/com/ordenaspace/dao
src/main/java/br/com/ordenaspace/servlet
src/main/java/br/com/ordenaspace/filter
src/main/webapp/WEB-INF/views
src/main/webapp/assets/css
src/main/webapp/assets/js
src/main/webapp/WEB-INF/web.xml
sql/schema.sql
sql/seed_demo.sql
```

## Funcionalidades entregues

- Login com sessao HTTP e `AuthFilter`
- Dashboard com totais de viaturas, policiais, setores e servicos ativos
- CRUD de viaturas
- CRUD de policiais
- Cadastro de setores com mapa Leaflet/OpenStreetMap e ate 6 pontos GPS
- Listagem tabular dos setores com todos os vertices
- Mapa consolidado dos setores
- Ativacao de servico por viatura, setor e equipe
- Finalizacao manual de servicos
- Monitoramento com status GPS, coordenadas e modais simulados de voz/video
- Endpoint `POST /api/gps` com algoritmo `point-in-polygon` em Java

## Usuario inicial

- Email: `policial@policial.com`
- Senha: `caveira`
- Perfil: `ADMIN`

Observacao:
A senha nao fica em texto puro no codigo Java. O banco guarda o hash SHA-256. Ha um `TODO` no codigo para migracao futura para BCrypt ou Argon2.

## Como configurar Oracle

1. Tenha uma instancia Oracle acessivel localmente ou remotamente.
2. Crie um usuario/schema para a aplicacao.
3. Defina a URL JDBC no formato Oracle, por exemplo:

```bash
export DB_URL="jdbc:oracle:thin:@//localhost:1521/FREEPDB1"
export DB_USER="ordena_space"
export DB_PASSWORD="sua_senha"
```

4. Execute o schema:

```bash
sqlplus ordena_space/sua_senha@//localhost:1521/FREEPDB1 @sql/schema.sql
```

5. Execute a carga demo:

```bash
sqlplus ordena_space/sua_senha@//localhost:1521/FREEPDB1 @sql/seed_demo.sql
```

## Como rodar localmente

1. Configure Java 17+:

```bash
java -version
```

2. Configure as variaveis de ambiente exigidas:

```bash
export DB_URL="jdbc:oracle:thin:@//localhost:1521/FREEPDB1"
export DB_USER="ordena_space"
export DB_PASSWORD="sua_senha"
```

3. Gere o WAR:

```bash
./mvnw clean package
```

4. O artefato sera criado em:

```text
target/ordena-space.war
```

5. Copie ou publique esse WAR no `Tomcat 10.1+`.

## Como rodar no Tomcat 10.1+

### Opcao 1: deploy manual

1. Inicie o Tomcat 10.1 ou superior.
2. Copie `target/ordena-space.war` para a pasta `webapps/`.
3. Aguarde a expansao automatica do WAR.
4. Abra:

```text
http://localhost:8080/ordena-space/login
```

### Opcao 2: IDE

1. Importe o projeto Maven.
2. Configure um servidor `Tomcat 10.1+`.
3. Faca deploy do artifact WAR `ordena-space:war`.
4. Garanta que as variaveis `DB_URL`, `DB_USER` e `DB_PASSWORD` estejam disponiveis para o processo do Tomcat.

## Endpoint GPS futuro

Endpoint implementado:

```http
POST /api/gps
Content-Type: application/json
```

Payload:

```json
{
  "tabletSatelital": "80001",
  "latitude": -22.9645,
  "longitude": -43.1905
}
```

Comportamento:

- Localiza o servico ativo pela viatura ligada ao `tablet_satelital`
- Atualiza `latitude_atual` e `longitude_atual`
- Verifica se o ponto esta dentro do poligono do setor
- Define `NORMAL` quando estiver dentro
- Define `ALERTA` quando estiver fora
- Retorna JSON com o status atualizado

Validacoes implementadas:

- Aceita apenas `POST`
- Exige `Content-Type: application/json`
- Valida `tabletSatelital`, latitude e longitude
- Rejeita setores sem poligono GPS valido

## Variaveis de ambiente

Obrigatorias:

- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`

Essas variaveis sao lidas por `DatabaseConnectionFactory`.

## Deploy no Railway

O projeto ja esta preparado para deploy como WAR, mas Railway normalmente trabalha melhor com processo customizado. Para evoluir para producao no Railway:

1. Empacote o WAR com `./mvnw clean package`.
2. Use uma imagem com `Java 17+` e `Tomcat 10.1+`.
3. Configure no ambiente do Railway:
   `DB_URL`, `DB_USER`, `DB_PASSWORD`
4. Ajuste o start command para iniciar o Tomcat com o WAR publicado.
5. Aponte o banco Oracle acessivel pela rede ou substitua a camada JDBC por um banco suportado nativamente pelo ambiente escolhido.

Observacao importante:
Railway nao oferece Oracle nativo como banco gerenciado. Na pratica, voce precisara usar uma instancia Oracle externa acessivel publicamente, com regras de rede e credenciais seguras.

## Deploy com Docker no Railway

Este repositorio contem um `Dockerfile` para evitar falhas do builder automatico do Railway com `JAVA_HOME` e garantir:

- Java 17 no build
- Maven no build
- Tomcat 10.1 no runtime
- bind automatico na porta `PORT` fornecida pelo Railway

Passos:

1. No Railway, use deploy a partir deste repositório GitHub.
2. Garanta que o projeto detecte o `Dockerfile`.
3. Configure as variaveis:
   `DB_URL`, `DB_USER`, `DB_PASSWORD`
4. Faça novo deploy.

Com essa configuracao, o WAR e gerado na etapa Docker e publicado como `ROOT.war` no Tomcat.

## Observacoes de evolucao

- Migrar hash SHA-256 para BCrypt/Argon2
- Adicionar pool de conexoes
- Implementar edicao de cadastros
- Persistir trilha completa de GPS por servico
- Adicionar autorizacao por perfil
- Criar integracao real de chamada de voz/video

## Seguranca de sessao aplicada

- Sessao HTTP com timeout de 30 minutos
- Tracking mode restrito a cookie
- Cookie de sessao marcado como `HttpOnly`
- Regeneracao de sessao no login para reduzir risco de fixation
- Headers de no-cache nas paginas autenticadas
