Crie uma API REST Java 17 para simular telemetria GPS de viaturas e fornecer a ultima posicao por `tabletSatelital` para um sistema externo chamado ORDENA SPACE.

Requisitos obrigatorios:

- Stack: Java 17, Spring Boot 3.x, Maven.
- Banco: pode usar Oracle, PostgreSQL ou H2. Para agora, deixe uma tabela simples com apenas a ultima posicao de cada tablet.
- A API deve identificar o registro pelo campo `tablet_satelital`.
- O objetivo e permitir que outro sistema consulte a ultima posicao GPS de um tablet e verifique se a viatura esta dentro ou fora do poligono cadastrado.
- Nao precisa implementar poligono nessa API. Ela so fornece latitude e longitude.
- A API deve retornar uma posicao simulada fixa por tablet. Depois eu mesmo farei inserts no banco.
- Metade dos tablets cadastrados no banco devem estar com coordenadas que normalmente fiquem dentro da area, e a outra metade fora da area. Nao precisa validar isso na API; basta deixar claro no seed SQL quais coordenadas sao “dentro” e quais sao “fora”.
- O sistema consumidor vai consultar pelo numero do tablet satelital configurado na viatura.

Modelo de dados:

Criar uma tabela chamada `gps_posicoes` com os campos:
- `id`
- `tablet_satelital` varchar unique not null
- `latitude` number/decimal not null
- `longitude` number/decimal not null
- `capturado_em` timestamp not null
- `observacao` varchar nullable

Criar entidade, repository, service e controller.

Endpoint obrigatorio:

1. `GET /api/telemetria/tablets/{tabletSatelital}/ultima-posicao`

Resposta 200 exemplo:
```json
{
  "tabletSatelital": "80001",
  "latitude": -23.561684,
  "longitude": -46.625378,
  "capturadoEm": "2026-06-07T18:40:00Z",
  "observacao": "simulada dentro da area"
}
```

Resposta 404 exemplo:
```json
{
  "error": "Tablet satelital nao encontrado."
}
```

Regras funcionais:
- Buscar por `tabletSatelital`.
- Se encontrar, retornar a ultima posicao.
- Se nao encontrar, retornar 404 com JSON.
- Validar que `tabletSatelital` nao venha vazio.
- Expor `application/json`.
- Criar tratamento global de excecoes.

Seed inicial:
- Gerar um arquivo SQL com pelo menos 6 inserts de exemplo.
- Tablets sugeridos: `80001`, `80002`, `80003`, `80004`, `80005`, `80006`.
- Nos inserts, deixar comentarios indicando quais coordenadas simulam “dentro” e quais simulam “fora”.

Extras importantes:
- Adicionar Swagger/OpenAPI.
- Adicionar `README.md` com instrucoes para rodar localmente.
- Adicionar `application-example.yml` com configuracao de banco.
- Deixar arquitetura organizada em camadas: controller, service, repository, entity, dto, exception.
- Gerar codigo completo, compilavel e pronto para subir.

Formato esperado da entrega:
- Estrutura do projeto
- `pom.xml`
- Classes Java completas
- SQL de criacao da tabela
- SQL de seed
- Exemplo de resposta HTTP
- Instrucoes para rodar
