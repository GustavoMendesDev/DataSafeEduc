DROP DATABASE IF EXISTS evasao_escolar;
CREATE DATABASE evasao_escolar
CHARACTER SET utf8mb4
COLLATE utf8mb4_general_ci;

USE evasao_escolar;

CREATE TABLE tipo_cliente (
    id_tipo_cliente INT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(50) NOT NULL
);

CREATE TABLE tipo_instituicao (
    id_tipo_instituicao INT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(50) NOT NULL
);

CREATE TABLE nivel_ensino (
    id_nivel_ensino INT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(50) NOT NULL
);

CREATE TABLE status_matricula (
    id_status_matricula INT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(30) NOT NULL
);

CREATE TABLE tipo_custo (
    id_tipo_custo INT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(30) NOT NULL
);

CREATE TABLE tipo_repasse (
    id_tipo_repasse INT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(50) NOT NULL
);

CREATE TABLE tipo_indicador (
    id_tipo_indicador INT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(50) NOT NULL
);

CREATE TABLE nivel_risco (
    id_nivel_risco INT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(20) NOT NULL
);

CREATE TABLE tipo_acao (
    id_tipo_acao INT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(50) NOT NULL
);

CREATE TABLE cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    telefone VARCHAR(20),
    id_tipo_cliente INT NOT NULL,
    status BOOLEAN DEFAULT TRUE,
    data_cadastro DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_tipo_cliente) REFERENCES tipo_cliente(id_tipo_cliente)
);

CREATE TABLE instituicao (
    id_instituicao INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_tipo_instituicao INT NOT NULL,
    id_nivel_ensino INT NOT NULL,
    nome VARCHAR(150) NOT NULL,
    cidade VARCHAR(100),
    estado CHAR(2),
    data_cadastro DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
    FOREIGN KEY (id_tipo_instituicao) REFERENCES tipo_instituicao(id_tipo_instituicao),
    FOREIGN KEY (id_nivel_ensino) REFERENCES nivel_ensino(id_nivel_ensino)
);

CREATE TABLE aluno (
    id_aluno INT AUTO_INCREMENT PRIMARY KEY,
    id_instituicao INT NOT NULL,
    faixa_etaria VARCHAR(20),
    turno VARCHAR(20),
    FOREIGN KEY (id_instituicao) REFERENCES instituicao(id_instituicao)
);

CREATE TABLE matricula (
    id_matricula INT AUTO_INCREMENT PRIMARY KEY,
    id_aluno INT NOT NULL,
    id_status_matricula INT NOT NULL,
    serie VARCHAR(20),
    ano_letivo YEAR NOT NULL,
    data_registro DATE,
    FOREIGN KEY (id_aluno) REFERENCES aluno(id_aluno),
    FOREIGN KEY (id_status_matricula) REFERENCES status_matricula(id_status_matricula)
);

CREATE TABLE historico_matricula (
    id_historico INT AUTO_INCREMENT PRIMARY KEY,
    id_matricula INT NOT NULL,
    situacao_final VARCHAR(30),
    data_encerramento DATE,
    FOREIGN KEY (id_matricula) REFERENCES matricula(id_matricula)
);

CREATE TABLE indicador (
    id_indicador INT AUTO_INCREMENT PRIMARY KEY,
    id_instituicao INT NOT NULL,
    id_tipo_indicador INT NOT NULL,
    valor DECIMAL(6,2) NOT NULL,
    ano_letivo YEAR NOT NULL,
    data_calculo DATE,
    FOREIGN KEY (id_instituicao) REFERENCES instituicao(id_instituicao),
    FOREIGN KEY (id_tipo_indicador) REFERENCES tipo_indicador(id_tipo_indicador)
);

CREATE TABLE risco_evasao (
    id_risco INT AUTO_INCREMENT PRIMARY KEY,
    id_instituicao INT NOT NULL,
    id_nivel_risco INT NOT NULL,
    criterio VARCHAR(255),
    ano_letivo YEAR,
    FOREIGN KEY (id_instituicao) REFERENCES instituicao(id_instituicao),
    FOREIGN KEY (id_nivel_risco) REFERENCES nivel_risco(id_nivel_risco)
);

CREATE TABLE custo (
    id_custo INT AUTO_INCREMENT PRIMARY KEY,
    id_instituicao INT NOT NULL,
    id_tipo_custo INT NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    ano_referencia YEAR NOT NULL,
    FOREIGN KEY (id_instituicao) REFERENCES instituicao(id_instituicao),
    FOREIGN KEY (id_tipo_custo) REFERENCES tipo_custo(id_tipo_custo)
);

CREATE TABLE repasse (
    id_repasse INT AUTO_INCREMENT PRIMARY KEY,
    id_instituicao INT NOT NULL,
    id_tipo_repasse INT NOT NULL,
    valor_por_aluno DECIMAL(10,2) NOT NULL,
    ano_referencia YEAR NOT NULL,
    FOREIGN KEY (id_instituicao) REFERENCES instituicao(id_instituicao),
    FOREIGN KEY (id_tipo_repasse) REFERENCES tipo_repasse(id_tipo_repasse)
);

CREATE TABLE simulacao (
    id_simulacao INT AUTO_INCREMENT PRIMARY KEY,
    id_instituicao INT NOT NULL,
    evasao_atual DECIMAL(5,2),
    reducao_prevista DECIMAL(5,2),
    impacto_financeiro DECIMAL(12,2),
    data_simulacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_instituicao) REFERENCES instituicao(id_instituicao)
);

CREATE TABLE cenario (
    id_cenario INT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(100),
    percentual_reducao DECIMAL(5,2)
);

CREATE TABLE simulacao_cenario (
    id_simulacao INT NOT NULL,
    id_cenario INT NOT NULL,
    impacto_estimado DECIMAL(12,2),
    PRIMARY KEY (id_simulacao, id_cenario),
    FOREIGN KEY (id_simulacao) REFERENCES simulacao(id_simulacao),
    FOREIGN KEY (id_cenario) REFERENCES cenario(id_cenario)
);

CREATE TABLE acao_preventiva (
    id_acao INT AUTO_INCREMENT PRIMARY KEY,
    id_instituicao INT NOT NULL,
    id_tipo_acao INT NOT NULL,
    descricao TEXT,
    impacto_esperado TEXT,
    FOREIGN KEY (id_instituicao) REFERENCES instituicao(id_instituicao),
    FOREIGN KEY (id_tipo_acao) REFERENCES tipo_acao(id_tipo_acao)
);

CREATE TABLE resumo_instituicao_ano (
    id_resumo INT AUTO_INCREMENT PRIMARY KEY,
    id_instituicao INT NOT NULL,
    ano_letivo YEAR NOT NULL,
    total_alunos INT,
    total_evadidos INT,
    taxa_evasao DECIMAL(5,2),
    custo_total DECIMAL(12,2),
    perda_financeira DECIMAL(12,2),
    data_atualizacao DATETIME,
    FOREIGN KEY (id_instituicao) REFERENCES instituicao(id_instituicao)
);

CREATE TABLE resumo_serie (
    id_resumo_serie INT AUTO_INCREMENT PRIMARY KEY,
    id_instituicao INT NOT NULL,
    serie VARCHAR(20),
    ano_letivo YEAR,
    total_alunos INT,
    total_evadidos INT,
    taxa_evasao DECIMAL(5,2),
    FOREIGN KEY (id_instituicao) REFERENCES instituicao(id_instituicao)
);

CREATE TABLE resumo_financeiro (
    id_resumo_financeiro INT AUTO_INCREMENT PRIMARY KEY,
    id_instituicao INT NOT NULL,
    ano_referencia YEAR,
    custo_medio_aluno DECIMAL(10,2),
    custo_total DECIMAL(12,2),
    repasse_total DECIMAL(12,2),
    perda_por_evasao DECIMAL(12,2),
    FOREIGN KEY (id_instituicao) REFERENCES instituicao(id_instituicao)
);

CREATE TABLE historico_indicador (
    id_historico_indicador INT AUTO_INCREMENT PRIMARY KEY,
    id_instituicao INT NOT NULL,
    id_tipo_indicador INT NOT NULL,
    valor DECIMAL(6,2),
    periodo YEAR,
    data_registro DATE,
    FOREIGN KEY (id_instituicao) REFERENCES instituicao(id_instituicao),
    FOREIGN KEY (id_tipo_indicador) REFERENCES tipo_indicador(id_tipo_indicador)
);