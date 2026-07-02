# 🛒 StorePDV - Sistema de Gerenciamento de Loja

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)

## 📝 Sobre o Projeto

O **StorePDV** é um sistema de gerenciamento de loja com foco no Back-end. Desenvolvido com **Java** e **Spring Boot**, o projeto fornece uma API robusta para realizar operações de cadastro, leitura, atualização e exclusão (CRUD) das principais entidades de um ambiente de varejo. O foco deste projeto é aplicar boas práticas de desenvolvimento, padrões de projeto e estruturação de código limpo.

## ✨ Funcionalidades

* **Gerenciamento de Produtos:** Criação, listagem, atualização e remoção do catálogo de produtos.
* **Gerenciamento de Clientes:** Controle da base de clientes da loja.
* **Gerenciamento de Funcionários:** Administração de dados dos colaboradores.
* **Tratamento de Exceções:** Retorno de mensagens de erro claras e amigáveis para o cliente da API.

## 🛠️ Tecnologias e Ferramentas

* **Linguagem:** Java 17
* **Framework:** Spring Boot
* **Banco de Dados:** MySQL
* **Gerenciamento de Dependências:** Maven
* **Controle de Versão:** Git e GitHub

## 🏗️ Arquitetura

O projeto foi estruturado seguindo o padrão de **Arquitetura em Camadas**, visando o isolamento de responsabilidades, fácil manutenção e escalabilidade:

* **Controller:** Responsável por receber as requisições HTTP e retornar as respostas (Endpoints REST).
* **Service:** Onde residem todas as regras de negócio da aplicação.
* **Repository:** Responsável pela comunicação direta com o banco de dados e persistência dos dados.

## 🚀 Como executar o projeto localmente

### Pré-requisitos

Antes de começar, você vai precisar ter instalado em sua máquina as seguintes ferramentas:
* [Java JDK](https://www.oracle.com/java/technologies/downloads/) (Versão utilizada no projeto)
* [Git](https://git-scm.com)
* [MySQL](https://dev.mysql.com/downloads/installer/)

### 🎲 Rodando o Back-end

1. **Clone este repositório**
   ```bash
   git clone [https://github.com/JoaoHenrique2109/StorePDV.git](https://github.com/JoaoHenrique2109/StorePDV.git)
