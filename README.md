# 🧘 ABrEak - Monitor de Pausas Saudáveis no Trabalho

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green?style=for-the-badge&logo=springboot)
![Node.js](https://img.shields.io/badge/Node.js-20.x-green?style=for-the-badge&logo=node.js)
![Azure](https://img.shields.io/badge/Azure-Cloud-blue?style=for-the-badge&logo=microsoftazure)
![Oracle](https://img.shields.io/badge/Oracle-Database-red?style=for-the-badge&logo=oracle)

**Aplicação web para monitoramento e registro de pausas saudáveis durante a jornada de trabalho**

[🎥 Vídeo Demonstração](https://youtu.be/BZGQSG8kMO0) • [📄 Documentação Completa](#)

</div>

---

## 📋 Sobre o Projeto

O **ABrEak** é uma aplicação desenvolvida como projeto da disciplina de **DevOps Tools & Cloud Computing** da FIAP, implementando uma arquitetura de microsserviços completa no Microsoft Azure.

A solução permite que trabalhadores registrem e monitorem três tipos essenciais de pausas:

- 💧 **Hidratação** - Lembretes para consumo regular de água
- 🤸 **Alongamento** - Pausas para exercícios físicos e prevenção de LER/DORT
- 👁️ **Descanso Visual** - Intervalos para reduzir fadiga ocular

### 🎯 Objetivos

- Promover saúde e bem-estar no ambiente de trabalho
- Prevenir doenças ocupacionais relacionadas ao sedentarismo
- Aumentar produtividade através de pausas estruturadas
- Demonstrar implementação de infraestrutura cloud com Azure

---

## 🏗️ Arquitetura
```
┌─────────────┐
│   Cliente   │
│  (Browser)  │
└──────┬──────┘
       │ HTTP
       ▼
┌──────────────────────┐
│   VM Windows         │
│   Frontend (Node.js) │
│   20.42.46.151:8080  │
└──────┬───────────────┘
       │ API REST
       ▼
┌──────────────────────┐
│   VM Linux (Ubuntu)  │
│   Backend Spring Boot│
│   20.185.240.145:8080│
└──────┬───────────────┘
       │ JDBC
       ▼
┌──────────────────────┐
│   Oracle Database    │
│   FIAP               │
└──────────────────────┘
```

### Componentes Azure

- **Resource Group:** `rg-abreak`
- **Virtual Network:** `rg-abreak` (10.0.0.0/16)
  - `subnet-frontend`: 10.0.1.0/24
  - `subnet-backend`: 10.0.2.0/24
- **Network Security Groups:**
  - `nsg-frontend`: HTTP (80), HTTPS (443), RDP (3389)
  - `nsg-backend`: SSH (22), API (8080)
- **Virtual Machines:**
  - `vm-frontend-windows`: Windows Server 2025, Standard B1s
  - `vm-backend-linux`: Ubuntu 22.04, Standard B1s

---

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 17** - Linguagem de programação
- **Spring Boot 3.x** - Framework web
- **Maven 3.9.9** - Gerenciamento de dependências
- **Oracle JDBC Driver** - Conexão com banco de dados
- **Spring Data JPA** - ORM para persistência

### Frontend
- **HTML5 / CSS3** - Estrutura e estilização
- **JavaScript (Vanilla)** - Interatividade
- **Node.js 20.x** - Servidor web
- **Fetch API** - Requisições HTTP

### Infraestrutura
- **Microsoft Azure** - Provedor de cloud
- **Oracle Database** - Banco de dados FIAP
- **Git** - Controle de versão

---

## 📁 Estrutura do Projeto
```
abreak-devops-azure/
├── backend/                    # API REST Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/abreak/
│   │   │   │       ├── controller/   # Controllers REST
│   │   │   │       ├── model/        # Entidades JPA
│   │   │   │       ├── repository/   # Repositórios
│   │   │   │       ├── service/      # Lógica de negócio
│   │   │   │       └── config/       # Configurações (CORS)
│   │   │   └── resources/
│   │   │       └── application.properties  # Config DB
│   │   └── test/
│   ├── pom.xml                # Dependências Maven
│   └── README.md
│
├── frontend/                   # Interface Web
│   ├── index.html             # Página principal
│   ├── styles.css             # Estilos
│   ├── script.js              # Lógica JavaScript
│   ├── config.js              # Configuração API
│   └── server.js              # Servidor Node.js (opcional)
│
└── README.md                  # Este arquivo
```

---

## 🚀 Como Executar

### Pré-requisitos

- Java 17+
- Maven 3.6+
- Node.js 20+ (opcional para frontend)
- Oracle Database (credenciais FIAP)
- Git

### 1️⃣ Clonar o Repositório
```bash
git clone https://github.com/matheusmariotto1206/abreak-devops-azure.git
cd abreak-devops-azure
```

### 2️⃣ Configurar Backend
```bash
cd backend

# Editar application.properties com credenciais Oracle
nano src/main/resources/application.properties

# Compilar e executar
mvn clean install
mvn spring-boot:run
```

**Backend rodando em:** `http://localhost:8080`

### 3️⃣ Configurar Frontend

**Opção A: HTTP Server (Node.js)**
```bash
cd frontend
npm install -g http-server
http-server -p 8080
```

**Opção B: Python**
```bash
cd frontend
python -m http.server 8080
```

**Opção C: Abrir direto**
```bash
# Abrir index.html no navegador
open index.html  # Mac
start index.html # Windows
```

**Frontend rodando em:** `http://localhost:8080`

---

## 🌐 Deploy no Azure

### Backend (VM Linux)
```bash
# SSH na VM
ssh azureuser@20.185.240.145

# Clonar repositório
git clone https://github.com/matheusmariotto1206/abreak-devops-azure.git
cd abreak-devops-azure/backend

# Compilar
mvn clean package

# Executar
nohup java -jar target/abreak-api.jar &
```

### Frontend (VM Windows)
```powershell
# RDP na VM: 20.42.46.151

# Clonar repositório (PowerShell)
cd C:\
git clone https://github.com/matheusmariotto1206/abreak-devops-azure.git
cd abreak-devops-azure\frontend

# Instalar http-server
npm install -g http-server

# Executar
http-server -p 8080
```

**Aplicação disponível em:** `http://20.42.46.151:8080`

---

## 📡 Endpoints da API

### Usuários

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/usuarios` | Listar todos os usuários |
| GET | `/api/usuarios/{id}` | Buscar usuário por ID |
| POST | `/api/usuarios` | Criar novo usuário |
| PUT | `/api/usuarios/{id}` | Atualizar usuário |
| DELETE | `/api/usuarios/{id}` | Deletar usuário |

### Pausas

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/pausas` | Listar todas as pausas |
| GET | `/api/pausas/usuario/{id}` | Listar pausas de um usuário |
| POST | `/api/pausas` | Registrar nova pausa |
| PUT | `/api/pausas/{id}` | Editar pausa |
| DELETE | `/api/pausas/{id}` | Deletar pausa |

### Exemplos de Requisição

**Criar Usuário:**
```json
POST /api/usuarios
Content-Type: application/json

{
  "nome": "João Silva",
  "email": "joao@example.com",
  "senha": "senha123",
  "cargo": "Desenvolvedor",
  "metaPausasDiarias": 6
}
```

**Registrar Pausa:**
```json
POST /api/pausas
Content-Type: application/json

{
  "idUsuario": 1,
  "tipoPausa": "hidratacao",
  "duracaoSegundos": 60
}
```

---

## 💾 Banco de Dados

### Schema Oracle

**Tabela: TB_USUARIOS**
```sql
CREATE TABLE TB_USUARIOS (
  ID_USUARIO NUMBER PRIMARY KEY,
  NOME VARCHAR2(100) NOT NULL,
  EMAIL VARCHAR2(100) UNIQUE NOT NULL,
  SENHA VARCHAR2(255) NOT NULL,
  CARGO VARCHAR2(50),
  META_PAUSAS_DIARIAS NUMBER DEFAULT 6
);
```

**Tabela: TB_PAUSAS**
```sql
CREATE TABLE TB_PAUSAS (
  ID_PAUSA NUMBER PRIMARY KEY,
  ID_USUARIO NUMBER NOT NULL,
  TIPO_PAUSA VARCHAR2(20) NOT NULL,
  DATA_HORA TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  DURACAO_SEGUNDOS NUMBER NOT NULL,
  CONSTRAINT FK_USUARIO FOREIGN KEY (ID_USUARIO) 
    REFERENCES TB_USUARIOS(ID_USUARIO)
);
```

---

## 👥 Integrantes

| Nome | RM | GitHub |
|------|----|----|
| Matheus Barbosa Mariotto | 560276 | [@matheusmariotto1206](https://github.com/matheusmariotto1206) |
| Felipe Anselmo | 560661 | - |
| João Vinícius | 559369 | - |

**Turma:** 2TDSPB - Manhã  
**Instituição:** FIAP - Faculdade de Informática e Administração Paulista  
**Disciplina:** DevOps Tools & Cloud Computing

---

## 📝 Documentação Adicional

- [📄 Documento PDF Completo](./docs/ABrEak_DevOps_Azure_Projeto.pdf)
- [🎥 Vídeo Demonstração no YouTube](https://youtu.be/BZGQSG8kMO0)
- [☁️ Azure Architecture Center](https://learn.microsoft.com/pt-br/azure/architecture/)

---

## 📸 Screenshots

### Tela Principal
![Tela Principal](./docs/screenshots/tela-principal.png)

### Dashboard de Pausas
![Dashboard](./docs/screenshots/dashboard.png)

### Gerenciamento de Usuários
![Usuários](./docs/screenshots/usuarios.png)

---

## 🔧 Troubleshooting

### Erro de Conexão com Banco
```
Verifique as credenciais no application.properties
Confirme que o Oracle aceita conexões externas
Teste conectividade: telnet oracle-fiap.com 1521
```

### CORS Error no Frontend
```
Verifique CorsConfig.java no backend
Adicione o IP do frontend nos allowed origins
Reinicie o backend após mudanças
```

### VM não acessa API
```
Verifique Network Security Groups
Confirme regras de entrada na porta 8080
Teste: curl http://20.185.240.145:8080/api/usuarios
```

---

## 📄 Licença

Este projeto foi desenvolvido para fins educacionais como parte do currículo da FIAP.

---

## 🤝 Contribuindo

Este é um projeto acadêmico, mas sugestões são bem-vindas!

1. Fork o projeto
2. Crie uma branch (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

---

## 📞 Contato

Para dúvidas sobre o projeto:
- **Email:** rm560276@fiap.com.br
- **GitHub Issues:** [Abrir Issue](https://github.com/matheusmariotto1206/abreak-devops-azure/issues)

---

<div align="center">

**Desenvolvido com ❤️ pela Equipe ABrEak**

⭐ Se este projeto te ajudou, considere dar uma estrela!

</div>
