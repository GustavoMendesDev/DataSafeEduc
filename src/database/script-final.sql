CREATE DATABASE datasafe;
USE datasafe;

-- PLANOS DO SISTEMA
CREATE TABLE plano (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    limite_usuarios INT,
    limite_consultas INT
);

-- CLIENTES (CURSINHOS)
CREATE TABLE cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    plano_id INT,
    nome VARCHAR(200) NOT NULL,
    email VARCHAR(200),
    ativo TINYINT(1) DEFAULT 1,
    criado_em DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (plano_id) REFERENCES plano(id)
);

-- ROLES (ADMIN, ANALISTA...)
CREATE TABLE role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(30) NOT NULL
);

-- USUÁRIOS DO SISTEMA
CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT,
    role_id INT,
    nome VARCHAR(200),
    email VARCHAR(200),
    senha_hash TEXT,
    ativo TINYINT(1) DEFAULT 1,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);

-- EDIÇÕES DO ENEM
CREATE TABLE edicao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ano SMALLINT NOT NULL,
    ativa TINYINT(1) DEFAULT 1
);

-- ÁREAS DO CONHECIMENTO
CREATE TABLE area (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo CHAR(2),
    nome VARCHAR(80)
);

-- HABILIDADES DA MATRIZ ENEM
CREATE TABLE habilidade (
    id INT AUTO_INCREMENT PRIMARY KEY,
    area_id INT,
    codigo_inep VARCHAR(10),
    descricao TEXT,

    FOREIGN KEY (area_id) REFERENCES area(id)
);

-- NÍVEIS DE DIFICULDADE
CREATE TABLE nivel_dificuldade (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(20),
    valor_min DECIMAL(8,4),
    valor_max DECIMAL(8,4)
);

-- ITENS (QUESTÕES DO ENEM)
CREATE TABLE item (
    id INT AUTO_INCREMENT PRIMARY KEY,
    edicao_id INT,
    area_id INT,
    habilidade_id INT,
    co_item VARCHAR(20),
    co_prova VARCHAR(20),
    param_a DECIMAL(8,4), -- discriminação
    param_b DECIMAL(8,4), -- dificuldade
    nivel_id INT,
    anulada TINYINT(1) DEFAULT 0,
    FOREIGN KEY (edicao_id) REFERENCES edicao(id),
    FOREIGN KEY (area_id) REFERENCES area(id),
    FOREIGN KEY (habilidade_id) REFERENCES habilidade(id),
    FOREIGN KEY (nivel_id) REFERENCES nivel_dificuldade(id)
);