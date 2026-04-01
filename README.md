# API de Gerenciamento de Chamados - IntegrAllTech

API REST desenvolvida em Java com Spring Boot para o desafio técnico de Desenvolvedor Júnior da IntegrAllTech. O sistema permite a criação e o gerenciamento de chamados de suporte, além de contar com uma integração com Inteligência Artificial (Groq) para triagem automática dos tickets.

## Tecnologias Utilizadas
* **Java 17+**
* **Spring Boot 3** (Web, Data JPA, Validation, Lombok)
* **Banco de Dados H2** (Em memória) 
* **Groq API** (Llama 3 para análise de chamados) 

## Pré-requisitos
Para rodar este projeto, você precisará ter instalado:
* Java Development Kit (JDK) 17 ou superior.
* Maven.
* Uma chave de API gratuita da Groq (obtenha em [console.groq.com](https://console.groq.com/)).

## Como executar o projeto

1. **Clone o repositório:**
   git clone https://github.com/edugoesx/teste_integralltech.git

2. **Configure a Chave da API (IA):**
  Abra o arquivo src/main/resources/application.properties e insira sua chave da Groq:
  groq.api.key=SUA_CHAVE_AQUI

3. **Inicie a aplicação:**
  Você pode rodar diretamente pela sua IDE (executando a classe principal) ou pelo terminal usando o Maven.
  A API estará disponível em http://localhost:8080

 ## Endpoints da API
 
 Abaixo estão as rotas disponíveis no sistema:
  POST /api/chamados - Cria um novo chamado.
  GET /api/chamados - Lista todos os chamados.
  GET /api/chamados/{id} - Busca um chamado específico pelo ID.
  GET /api/chamados/setor/{setor} - Lista chamados filtrados por setor (TI, MANUTENCAO, RH, FINANCEIRO).
  PUT /api/chamados/{id} - Atualiza dados de um chamado.
  DELETE /api/chamados/{id} - Cancela um chamado (Soft Delete).
  POST /api/chamados/{id}/analisar - Envia o chamado para triagem e sugestão da Inteligência Artificial.

4. **Como testar a Integração com IA**

Crie um chamado via POST /api/chamados com um título e descrição (ex: "Computador não liga").
Copie o id gerado na resposta.
Faça uma requisição POST /api/chamados/{id}/analisar (deixe o corpo/body vazio).
A IA retornará um JSON sugerindo a prioridade, o setor responsável e um resumo do problema .
