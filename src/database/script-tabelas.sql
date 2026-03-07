-- =====================================================
-- BANCO DE DADOS
-- =====================================================
CREATE DATABASE datasafe_enem
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE datasafe_enem;

-- =====================================================
-- EDIÇÃO DO ENEM
-- =====================================================
CREATE TABLE edicao_enem (
    id_edicao INT AUTO_INCREMENT PRIMARY KEY,
    ano SMALLINT NOT NULL,
    descricao VARCHAR(100),
    data_aplicacao DATE,
    UNIQUE (ano)
);

-- =====================================================
-- ÁREA DO CONHECIMENTO
-- =====================================================
CREATE TABLE area_conhecimento (
    id_area INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE
);

-- =====================================================
-- PROVA (CO_PROVA)
-- =====================================================
CREATE TABLE prova (
    id_prova INT AUTO_INCREMENT PRIMARY KEY,
    codigo_prova VARCHAR(10) NOT NULL,
    id_edicao INT NOT NULL,
    id_area INT NOT NULL,

    CONSTRAINT fk_prova_edicao
        FOREIGN KEY (id_edicao) REFERENCES edicao_enem(id_edicao),

    CONSTRAINT fk_prova_area
        FOREIGN KEY (id_area) REFERENCES area_conhecimento(id_area),

    UNIQUE (codigo_prova, id_edicao)
);

CREATE INDEX idx_prova_edicao ON prova(id_edicao);
CREATE INDEX idx_prova_area ON prova(id_area);

-- =====================================================
-- COMPETÊNCIA
-- =====================================================
CREATE TABLE competencia (
    id_competencia INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(10) NOT NULL,
    descricao TEXT NOT NULL,
    id_area INT NOT NULL,

    CONSTRAINT fk_competencia_area
        FOREIGN KEY (id_area) REFERENCES area_conhecimento(id_area),

    UNIQUE (codigo, id_area)
);

-- =====================================================
-- HABILIDADE
-- =====================================================
CREATE TABLE habilidade (
    id_habilidade INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(10) NOT NULL,
    descricao TEXT NOT NULL,
    id_competencia INT NOT NULL,

    CONSTRAINT fk_habilidade_competencia
        FOREIGN KEY (id_competencia) REFERENCES competencia(id_competencia),

    UNIQUE (codigo, id_competencia)
);

-- =====================================================
-- QUESTÃO (ITEM DO ENEM)
-- =====================================================
CREATE TABLE questao (
    id_questao INT AUTO_INCREMENT PRIMARY KEY,
    codigo_item VARCHAR(20) NOT NULL,
    id_prova INT NOT NULL,
    id_habilidade INT NOT NULL,
    enunciado TEXT,

    CONSTRAINT fk_questao_prova
        FOREIGN KEY (id_prova) REFERENCES prova(id_prova),

    CONSTRAINT fk_questao_habilidade
        FOREIGN KEY (id_habilidade) REFERENCES habilidade(id_habilidade),

    UNIQUE (codigo_item)
);

CREATE INDEX idx_questao_prova ON questao(id_prova);
CREATE INDEX idx_questao_habilidade ON questao(id_habilidade);

-- =====================================================
-- PARÂMETRO A — DISCRIMINAÇÃO
-- =====================================================
CREATE TABLE parametro_discriminacao (
    id_discriminacao INT AUTO_INCREMENT PRIMARY KEY,
    id_questao INT NOT NULL,
    valor DECIMAL(6,4) NOT NULL,

    CONSTRAINT fk_discriminacao_questao
        FOREIGN KEY (id_questao) REFERENCES questao(id_questao),

    UNIQUE (id_questao)
);

-- =====================================================
-- PARÂMETRO B — DIFICULDADE
-- =====================================================
CREATE TABLE parametro_dificuldade (
    id_dificuldade INT AUTO_INCREMENT PRIMARY KEY,
    id_questao INT NOT NULL,
    valor DECIMAL(6,4) NOT NULL,

    CONSTRAINT fk_dificuldade_questao
        FOREIGN KEY (id_questao) REFERENCES questao(id_questao),

    UNIQUE (id_questao)
);

-- =====================================================
-- PARÂMETRO C — ACERTO AO ACASO
-- =====================================================
CREATE TABLE parametro_acerto_ao_acaso (
    id_acerto INT AUTO_INCREMENT PRIMARY KEY,
    id_questao INT NOT NULL,
    valor DECIMAL(6,4),

    CONSTRAINT fk_acerto_questao
        FOREIGN KEY (id_questao) REFERENCES questao(id_questao),

    UNIQUE (id_questao)
);

-- =====================================================
-- INDICADORES ANALÍTICOS (AGREGAÇÕES)
-- =====================================================
CREATE TABLE indicador_analitico (
    id_indicador INT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL,
    nivel VARCHAR(30) NOT NULL, -- PROVA, AREA, HABILIDADE
    referencia_id INT NOT NULL,
    valor DECIMAL(10,4) NOT NULL,
    descricao VARCHAR(255),
    data_calculo TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_indicador_tipo ON indicador_analitico(tipo);
CREATE INDEX idx_indicador_nivel ON indicador_analitico(nivel);