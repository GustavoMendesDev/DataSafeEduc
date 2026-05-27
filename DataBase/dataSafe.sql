CREATE DATABASE IF NOT EXISTS dataSafe;
USE dataSafe;
-- --------------------------------------------------------
-- Script gerado a partir da modelagem DataSafe
-- --------------------------------------------------------

CREATE TABLE areaConhecimento (
    id    INT PRIMARY KEY,
    nome  VARCHAR(45),
    sigla CHAR(10)
);

CREATE TABLE habilidade (
    id                INT PRIMARY KEY,
    numero            CHAR(20),
    descricao         VARCHAR(455),
    fkAreaConhecimento INT,
    FOREIGN KEY (fkAreaConhecimento) REFERENCES areaConhecimento(id)
);

CREATE TABLE parametroTri (
    id         INT PRIMARY KEY,
    nivel      VARCHAR(45),
    parametroA DECIMAL(5,2),
    parametroB DECIMAL(5,2),
    parametroC DECIMAL(5,2)
);

CREATE TABLE questao (
    codigoItem    VARCHAR(20) PRIMARY KEY,
    anoExame      YEAR,
    fkHabilidade  INT,
    fkParametroTri INT,
    FOREIGN KEY (fkHabilidade)   REFERENCES habilidade(id),
    FOREIGN KEY (fkParametroTri) REFERENCES parametroTri(id)
);

CREATE TABLE notaMunicipal (
    id                   INT PRIMARY KEY,
    matematica           DECIMAL(5,2),
    codigosELinguagens   DECIMAL(5,2),
    cienciasDaNatureza   DECIMAL(5,2),
    cienciasHumanas      DECIMAL(5,2)
);

CREATE TABLE municipio (
    id              INT PRIMARY KEY,
    nome            VARCHAR(45),
    estado          CHAR(2),
    fkNotaMunicipal INT,
    FOREIGN KEY (fkNotaMunicipal) REFERENCES notaMunicipal(id)
);

CREATE TABLE nivelAcesso (
    id   INT PRIMARY KEY,
    nome VARCHAR(45)
);

CREATE TABLE cursinho (
    id   INT PRIMARY KEY,
    nome VARCHAR(45),
    cnpj VARCHAR(45)
);

CREATE TABLE usuario (
    id           INT PRIMARY KEY,
    nome         VARCHAR(80),
    senha        VARCHAR(255),
    dataCriacao  DATETIME,
    fkNivelAcesso INT,
    cursinho_id  INT,
    municipio_id INT,
    FOREIGN KEY (fkNivelAcesso) REFERENCES nivelAcesso(id),
    FOREIGN KEY (cursinho_id)   REFERENCES cursinho(id),
    FOREIGN KEY (municipio_id)  REFERENCES municipio(id)
);

CREATE TABLE simulado (
    id                INT PRIMARY KEY,
    nomeSimulado      VARCHAR(45),
    quantidadeQuestoes INT,
    fkUsuario         INT,
    FOREIGN KEY (fkUsuario) REFERENCES usuario(id)
);

CREATE TABLE questaoSimulado (
    fkQuestao  VARCHAR(20),
    fkSimulado INT,
    PRIMARY KEY (fkQuestao, fkSimulado),
    FOREIGN KEY (fkQuestao)  REFERENCES questao(codigoItem),
    FOREIGN KEY (fkSimulado) REFERENCES simulado(id)
);

CREATE TABLE controleNotificacao (
    slack_channel_id      VARCHAR(100),
    periodo               INT,
    receberNotificacao    VARCHAR(45),
    tipoNotificacao       TINYINT(1),
    usuario_id            INT,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE logAcesso (
    id          INT PRIMARY KEY,
    mensagem    VARCHAR(255),
    nivel       VARCHAR(20),
    ip          VARCHAR(45),
    dataCriacao DATETIME
);