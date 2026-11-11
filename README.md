# Chef IA API

API REST que sugere receitas com inteligência artificial a partir dos ingredientes que você tem em casa. Você cadastra os ingredientes da sua cozinha (nome, categoria, quantidade e validade) e a API monta um prompt com essa lista e pede ao **Google Gemini** uma receita com quantidades e modo de preparo passo a passo.

## Como funciona

```
Cliente ──► /api/v1/ingredient ──► IngredientService ──► H2 (tabela ingredients)
   │
   └──────► /api/v1/gemini ──► lê todos os ingredientes ──► GeminiAIService ──► API do Gemini
```

1. Os ingredientes são cadastrados pelo CRUD em `/api/v1/ingredient` e gravados em um banco H2.
2. `GET /api/v1/gemini` busca todos os ingredientes cadastrados e monta o prompt:
   *"Você é um chefe de cozinha, com base nesses ingredientes me sugira uma receita, detalhando a quantidade de cada ingrediente, e o modo de preparo passo a passo"*, seguido da lista.
3. A chamada ao Gemini é feita com `WebClient` (não bloqueante) e a resposta é devolvida ao cliente.

## Tecnologias

- **Java 21** e **Spring Boot 3.4.4** (Web, Data JPA, WebFlux)
- **WebClient** para consumir a API do Gemini (`gemini-2.0-flash`)
- **H2 Database** para persistência
- **Flyway** para versionar o schema
- **Lombok**
- **Maven Wrapper** (`mvnw`)

## Pré-requisitos

- Java 21 ou superior
- Uma chave da API do Gemini, gerada no [Google AI Studio](https://aistudio.google.com/app/apikey)

Não é preciso instalar banco de dados nem Maven: o H2 roda embutido e o projeto traz o Maven Wrapper.

## Configuração

A aplicação lê quatro variáveis de ambiente (referenciadas em `src/main/resources/application.properties`):

| Variável | Descrição | Exemplo |
| --- | --- | --- |
| `DATABASE_URL` | URL JDBC do H2 | `jdbc:h2:file:./data/chefia` |
| `DATABASE_USERNAME` | Usuário do banco | `sa` |
| `DATABASE_PASSWORD` | Senha do banco | `sua-senha` |
| `GEMINI_API_KEY` | Chave da API do Gemini | — |

Com a URL do exemplo, o H2 grava o arquivo do banco na pasta `data/` do projeto, que já existe no repositório; os arquivos `.db` dentro dela são ignorados pelo git. Para um banco só em memória, que se perde ao parar a aplicação, use `jdbc:h2:mem:chefia`.

O Spring Boot não carrega arquivos `.env` sozinho. Exporte as variáveis no terminal ou configure-as na *run configuration* da sua IDE. Nunca faça commit da sua chave do Gemini.

## Como executar

```bash
# 1. Clone o repositório
git clone https://github.com/CarlosLonghi/API-Chef-IA.git
cd API-Chef-IA

# 2. Defina as variáveis de ambiente (Linux/macOS)
export DATABASE_URL=jdbc:h2:file:./data/chefia
export DATABASE_USERNAME=sa
export DATABASE_PASSWORD=sua-senha
export GEMINI_API_KEY=sua-chave

# 3. Suba a aplicação
./mvnw spring-boot:run
```

No Windows (PowerShell), use `$env:DATABASE_URL="jdbc:h2:file:./data/chefia"` para cada variável e rode `.\mvnw.cmd spring-boot:run`.

A API fica disponível em `http://localhost:8080`. Na primeira inicialização, o Flyway cria a tabela `ingredients` (`src/main/resources/db/migrations`).

O console do H2 fica em `http://localhost:8080/h2-console`; entre com a mesma URL JDBC, usuário e senha das variáveis de ambiente.

## Endpoints

### Ingredientes — `/api/v1/ingredient`

| Método | Rota | Descrição | Resposta |
| --- | --- | --- | --- |
| `POST` | `/api/v1/ingredient` | Cadastra um ingrediente | `200` + ingrediente criado |
| `GET` | `/api/v1/ingredient` | Lista todos os ingredientes | `200` + lista |
| `GET` | `/api/v1/ingredient/{id}` | Busca um ingrediente por id | `200` + ingrediente (`null` se não existir) |
| `PUT` | `/api/v1/ingredient/{id}` | Atualiza um ingrediente | `200` + ingrediente, ou `404` |
| `DELETE` | `/api/v1/ingredient/{id}` | Remove um ingrediente | `200` + `"Ingrediente deletado"`, ou `404` |

Corpo de `POST` e `PUT`:

```json
{
  "name": "Tomate",
  "category": "VEGETABLE",
  "quantity": 1.5,
  "validate": "2026-12-20"
}
```

| Campo | Tipo | Obrigatório | Observação |
| --- | --- | --- | --- |
| `name` | texto | sim | Nome do ingrediente |
| `category` | enum | sim | Um dos valores da tabela abaixo |
| `quantity` | número | não | Quantidade, na unidade da categoria |
| `validate` | data (`AAAA-MM-DD`) | não | Data de validade |

Categorias aceitas em `category`:

| Valor | Significado | Unidade |
| --- | --- | --- |
| `VEGETABLE` | Vegetal | Kg |
| `FRUIT` | Fruta | Kg |
| `MEAT` | Carne | Kg |
| `FISH` | Peixe | Kg |
| `GRAIN` | Grão / Cereal | Kg |
| `SPICE` | Tempero | Kg |
| `OIL` | Óleo | Litro |
| `SAUCE` | Molho | Litro |
| `BEVERAGE` | Bebida | Litro |
| `OTHER` | Outro | Unidade |

### Receita — `/api/v1/gemini`

| Método | Rota | Descrição |
| --- | --- | --- |
| `GET` | `/api/v1/gemini` | Gera uma receita com todos os ingredientes cadastrados |

A resposta é o JSON devolvido pelo Gemini, sem tratamento. O texto da receita fica em `candidates[0].content.parts[0].text`.

## Exemplo de uso

```bash
# Cadastra dois ingredientes
curl -X POST http://localhost:8080/api/v1/ingredient \
  -H "Content-Type: application/json" \
  -d '{"name": "Tomate", "category": "VEGETABLE", "quantity": 1.5, "validate": "2026-12-20"}'

curl -X POST http://localhost:8080/api/v1/ingredient \
  -H "Content-Type: application/json" \
  -d '{"name": "Peito de frango", "category": "MEAT", "quantity": 0.8, "validate": "2026-12-15"}'

# Pede a receita e extrai só o texto (requer jq)
curl -s http://localhost:8080/api/v1/gemini | jq -r '.candidates[0].content.parts[0].text'
```

## Estrutura do projeto

```
src/main/java/com/carloslonghi/ChefIA/
├── config/         # WebClientConfig: WebClient apontando para a API do Gemini
├── controller/     # IngredientController (CRUD) e RecipeController (receita)
├── model/          # Entidade Ingredient
│   └── enums/      # IngredientCategory e Unit
├── repository/     # IngredientRepository (Spring Data JPA)
├── service/        # IngredientService e GeminiAIService
└── ChefIaApplication.java

src/main/resources/
├── application.properties
└── db/migrations/  # V1__create_ingredients_table.sql

data/               # arquivos do H2 (ignorados pelo git)
```

## Limitações conhecidas

- Não há autenticação: qualquer cliente com acesso à API pode ler e alterar os ingredientes.
- Não há validação dos dados de entrada (`@Valid`); um `category` fora da lista ou um `name` ausente resulta em erro `400`/`500` genérico.
- A receita sempre usa **todos** os ingredientes cadastrados; não dá para escolher um subconjunto nem informar preferências.
- A resposta do Gemini é repassada como veio, sem extrair o texto da receita nem tratar erros da API (chave inválida, limite de requisições).
- O modelo (`gemini-2.0-flash`) está fixo na URL em `application.properties`; para trocar, edite a propriedade `gemini.api.url`.
- Os testes cobrem apenas a subida do contexto do Spring.

## Próximos passos

- Validação de entrada e tratamento global de erros
- DTOs de requisição e resposta, devolvendo só o texto da receita
- Documentação da API com Swagger/OpenAPI
- Testes unitários e de integração
- Docker Compose para subir a aplicação

## Autor

Desenvolvido por [Carlos Longhi](https://github.com/CarlosLonghi).
