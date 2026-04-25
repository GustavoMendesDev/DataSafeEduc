CREATE DATABASE dataSafe;
USE dataSafe;

-- MUNICIPIO
CREATE TABLE municipio (
    idMunicipio INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(45) NOT NULL,
    estado CHAR(2) NOT NULL
);

-- NIVEL_ACESSO
CREATE TABLE nivel_acesso (
    idnivel_acesso INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(45) NOT NULL
);

-- USUARIO
CREATE TABLE usuario (
    idUsuario INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(80) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    nivel_acesso_idnivel_acesso INT,
    municipio_idMunicipio INT,
    
    FOREIGN KEY (nivel_acesso_idnivel_acesso)
        REFERENCES nivel_acesso(idnivel_acesso),
        
    FOREIGN KEY (municipio_idMunicipio)
        REFERENCES municipio(idMunicipio)
);



-- AREA_CONHECIMENTO
CREATE TABLE area_conhecimento (
    id_AreaConhecimento INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(45) NOT NULL,
    sigla CHAR(3)
);

-- DIFICULDADE
CREATE TABLE dificuldade (
    idDificuldade INT PRIMARY KEY AUTO_INCREMENT,
    nivel VARCHAR(45),
    parametro_a DECIMAL(5,2),
    parametro_b DECIMAL(5,2),
    parametro_c DECIMAL(5,2)
);

-- HABILIDADE
CREATE TABLE habilidade (
    idHabilidade INT PRIMARY KEY AUTO_INCREMENT,
    numero CHAR(5),
    descricao VARCHAR(45)
);

-- QUESTAO
CREATE TABLE questao (
    idQuestao INT PRIMARY KEY AUTO_INCREMENT,
    codigoItem VARCHAR(20),
    ano_exame YEAR,
    habilidade_idHabilidade INT,
    area_conhecimento_id_AreaConhecimento INT,
    dificuldade_idDificuldade INT,
    
    FOREIGN KEY (habilidade_idHabilidade)
        REFERENCES habilidade(idHabilidade),
        
    FOREIGN KEY (area_conhecimento_id_AreaConhecimento)
        REFERENCES area_conhecimento(id_AreaConhecimento),
        
    FOREIGN KEY (dificuldade_idDificuldade)
        REFERENCES dificuldade(idDificuldade)
);

-- SIMULADO
CREATE TABLE simulado (
    idSimulado INT PRIMARY KEY AUTO_INCREMENT,
    quantidadeQuestoes INT,
    usuario_idUsuario INT,
    nomeSimulado VARCHAR(45),
    
    FOREIGN KEY (usuario_idUsuario)
        REFERENCES usuario(idUsuario)
);

-- TABELA DE RELAÇÃO (N:N)
CREATE TABLE questao_has_simulado (
    questao_idQuestao INT,
    simulado_idSimulado INT,
    
    PRIMARY KEY (questao_idQuestao, simulado_idSimulado),
    
    FOREIGN KEY (questao_idQuestao)
        REFERENCES questao(idQuestao),
        
    FOREIGN KEY (simulado_idSimulado)
        REFERENCES simulado(idSimulado)
);

-- LOG_ACESSO
CREATE TABLE log_acesso (
    idlog_acesso INT PRIMARY KEY AUTO_INCREMENT,
    ip VARCHAR(45),
    dataCriacao DATETIME DEFAULT CURRENT_TIMESTAMP
);

SELECT * FROM usuario;