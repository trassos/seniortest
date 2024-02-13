# PROVA NÍVEL I-II - Sistema de Cadastro

Este projeto consiste em uma aplicação para gerenciar cadastros de produtos/serviços, pedidos e itens de pedido, desenvolvida como parte de uma prova de nível I. Utiliza tecnologias mínimas, incluindo PostgreSQL como banco de dados, Java 8+, JPA para mapeamento objeto-relacional e REST para comunicação utilizando JSON.

## Tecnologias Utilizadas

- PostgreSQL (REQUERIDO)
- Java 8+ (REQUERIDO)
- Maven (REQUERIDO)
- Spring (REQUERIDO)
- JPA (REQUERIDO)
- REST com JSON (REQUERIDO)
- Flyway

## Requisitos da Prova

O sistema deve atender aos seguintes requisitos:

1. Deverá ser desenvolvido um cadastro (Create/Read/Update/Delete/List com paginação)
   para as seguintes entidades: produto/serviço, pedido e itens de pedido.
2. Todos as entidades deverão ter um ID único do tipo UUID gerado automaticamente.
3. No cadastro de produto/serviço deverá ter uma indicação para diferenciar um produto de
   um serviço.
4. Deverá ser possível aplicar um percentual de desconto no pedido, porém apenas para os
   itens que sejam produto (não serviço); o desconto será sobre o valor total dos produtos.
5. Somente será possível aplicar desconto no pedido se ele estiver na situação Aberto
   (Fechado bloqueia).
6. Não deve ser possível excluir um produto/serviço se ele estiver associado a algum pedido.
7. Não deve ser possível adicionar um produto desativado em um pedido.

## Critérios de Aceitação

1. A prova deverá ser entregue completa (todos os itens resolvidos).
2. Deverão ser criados testes automatizados.
3. O código não poderá ter erros de compilação.
4. Deverá haver uma documentação mínima de como executar o projeto e suas
   funcionalidades

## Execução do Projeto

Para executar o projeto, siga estes passos:

1. Clone o repositório para sua máquina.
2. Abra o projeto em sua IDE de preferência.
3. Configure o arquivo `application.yml` com as credenciais do seu banco de dados PostgreSQL.
4. Importe a coleção de requisições do Postman para testar a API que está na pasta 'postman'.
5. Execute os testes.
6. Execute a aplicação e rode as requisições no Postman.
