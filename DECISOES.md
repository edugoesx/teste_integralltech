# Decisões Técnicas e Diário de Desenvolvimento

Este documento explica brevemente as principais decisões técnicas tomadas durante o desafio e documenta os aprendizados e dificuldades encontradas no processo.

## 1. Arquitetura e Padrões de Projeto
Optei por utilizar a arquitetura em camadas padrão do Spring Boot (Controller, Service, Repository). O objetivo foi respeitar o princípio de Responsabilidade Única (SOLID). O `Controller` lida apenas com requisições HTTP, o `Service` concentra as regras de negócio (como a transição de status e validações) e o `Repository` abstrai a comunicação com o banco de dados H2.

## 2. Uso de DTOs (Data Transfer Objects)
Decidi implementar DTOs (`ChamadoRequestDTO`, `AnaliseIaResponseDTO`) para não expor a entidade de domínio diretamente na API. Isso traz mais segurança e permite validar as entradas (usando `@Valid` e anotações como `@NotBlank`) antes mesmo da requisição chegar à camada de serviço.

## 3. Integração com Inteligência Artificial
Para a análise dos chamados, realizei a integração real consumindo a API da Groq (modelo Llama 3) via `RestTemplate`.
A principal decisão aqui foi o **Prompt Engineering**: configurei a IA para assumir o papel de analista de suporte e exigi que a saída fosse *estritamente* um objeto JSON com chaves e valores pré-definidos (ex: prioridades e setores baseados nos Enums do sistema). Isso garantiu que o código Java conseguisse desserializar a resposta de forma previsível, sem quebrar a aplicação.

## 4. Front-end Básico (Bônus)
Para demonstrar domínio full-stack e melhorar a usabilidade do teste, desenvolvi uma interface simples utilizando React e Vite. Aproveitando minha vivência desenvolvendo projetos com back-end em Java Spring e front-end em React, adaptei os conceitos de consumo de API e componentização para o React de forma bem natural. 
Optei por usar a API nativa `fetch` do JavaScript, mantendo o projeto leve. A principal barreira aqui foi o clássico erro de CORS ao tentar comunicar o front (na porta 5173) com o back (na porta 8080), o qual resolvi rapidamente adicionando a anotação `@CrossOrigin` no `ChamadoController`.

## 5. Dificuldades Encontradas e Aprendizados
Durante o desenvolvimento com o auxílio de IA, enfrentei alguns obstáculos que foram ótimas oportunidades de aprendizado:

* **Mapeamento de Enums:** Em determinado momento, criei o arquivo de `Prioridade` como uma `class` normal em vez de `enum`. Isso gerou um erro de contexto estático (`Non-static field cannot be referenced`) ao tentar acessá-lo no Service. Percebi o erro, converti para `enum` e entendi como o Java trata essas constantes por baixo dos panos.
* **Erro 404 (Not Found) e Estrutura de Pacotes:** Ao testar o primeiro endpoint no Insomnia, recebi um erro 404. Após investigar, percebi que havia criado o pacote `controllers` fora da raiz da classe principal (onde fica o `@SpringBootApplication`). Aprendi sobre o *Component Scan* do Spring, refatorei as pastas para dentro do pacote principal e o roteamento voltou a funcionar.
* **Organização de Imports:** Ao mover as pastas para corrigir o erro 404, enfrentei vários erros de `cannot resolve symbol`. Foi necessário limpar os imports antigos e reimportar as classes com o novo caminho dos pacotes.
