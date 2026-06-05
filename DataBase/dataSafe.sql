CREATE DATABASE IF NOT EXISTS dataSafe;
USE dataSafe;
-- --------------------------------------------------------
-- Script gerado a partir da modelagem DataSafe
-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS notaMunicipal (
  id                 INT           NOT NULL AUTO_INCREMENT,
  matematica         DECIMAL(5,2)  NULL,
  codigosELinguagens DECIMAL(5,2)  NULL,
  cienciasDaNatureza DECIMAL(5,2)  NULL,
  cienciasHumanas    DECIMAL(5,2)  NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS municipio (
  id              INT         NOT NULL AUTO_INCREMENT,
  nome            VARCHAR(45) NULL,
  estado          CHAR(2)     NULL,
  fkNotaMunicipal INT         NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fkNotaMunicipal
    FOREIGN KEY (fkNotaMunicipal)
    REFERENCES notaMunicipal (id)
);

CREATE TABLE IF NOT EXISTS nivelAcesso (
  id   INT         NOT NULL AUTO_INCREMENT,
  nome VARCHAR(45) NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS cursinho (
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(45) NOT NULL,
    cnpj VARCHAR(45) NOT NULL,
    codigoConvite CHAR(4),
    fkMunicipio INT,
    PRIMARY KEY (id),
    CONSTRAINT fkMunicipio
    FOREIGN KEY (fkMunicipio)
    REFERENCES municipio (id)
);

CREATE TABLE IF NOT EXISTS usuario (
  id            INT          NOT NULL AUTO_INCREMENT,
  nome          VARCHAR(80)  NULL,
  email         VARCHAR(120) NULL,
  senha         VARCHAR(255) NULL,
  dataCriacao   DATETIME     NULL,
  fkNivelAcesso INT          NOT NULL,
  fkCursinho INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fkNivelAcesso
    FOREIGN KEY (fkNivelAcesso)
    REFERENCES nivelAcesso (id),
  CONSTRAINT fkCursinho
    FOREIGN KEY(fkCursinho)
    REFERENCES cursinho(id)
);

CREATE TABLE IF NOT EXISTS simulado (
  id                 INT         NOT NULL AUTO_INCREMENT,
  nomeSimulado       VARCHAR(45) NULL,
  quantidadeQuestoes INT         NULL,
  fkUsuario          INT         NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fkUsuario
    FOREIGN KEY (fkUsuario)
    REFERENCES usuario (id)
);

CREATE TABLE IF NOT EXISTS logAcesso (
  id          INT         NOT NULL AUTO_INCREMENT,
  ip          VARCHAR(45) NULL,
  dataCriacao DATETIME    NULL,
  fkUsuario   INT         NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fkLogAcessoUsuario
    FOREIGN KEY (fkUsuario)
    REFERENCES usuario (id)
);

CREATE TABLE IF NOT EXISTS areaConhecimento (
  id    INT         NOT NULL,
  nome  VARCHAR(45) NULL,
  sigla CHAR(10)    NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS habilidade (
  id                 INT          NOT NULL,
  numero             CHAR(20)     NULL,
  descricao          VARCHAR(455) NULL,
  fkAreaConhecimento INT          NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fkAreaConhecimento
    FOREIGN KEY (fkAreaConhecimento)
    REFERENCES areaConhecimento (id)
);

CREATE TABLE IF NOT EXISTS parametroTri (
  id         INT           NOT NULL,
  nivel      VARCHAR(45)   NULL,
  parametroA DECIMAL(5,2)  NULL,
  parametroB DECIMAL(5,2)  NULL,
  parametroC DECIMAL(5,2)  NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS questao (
  codigoItem     VARCHAR(20) NOT NULL,
  anoExame       YEAR        NULL,
  fkHabilidade   INT         NOT NULL,
  fkParametroTri INT         NOT NULL,
  PRIMARY KEY (codigoItem),
  CONSTRAINT fkHabilidade
    FOREIGN KEY (fkHabilidade)
    REFERENCES habilidade (id),
  CONSTRAINT fkParametroTri
    FOREIGN KEY (fkParametroTri)
    REFERENCES parametroTri (id)
);

CREATE TABLE IF NOT EXISTS questaoSimulado (
  fkQuestao  VARCHAR(20) NOT NULL,
  fkSimulado INT         NOT NULL,
  PRIMARY KEY (fkQuestao, fkSimulado),
  CONSTRAINT fkQuestao
    FOREIGN KEY (fkQuestao)
    REFERENCES questao (codigoItem),
  CONSTRAINT fkSimulado
    FOREIGN KEY (fkSimulado)
    REFERENCES simulado (id)
);

INSERT INTO notaMunicipal (matematica, codigosELinguagens, cienciasDaNatureza, cienciasHumanas) 
VALUES (400, 350, 300, 200);

INSERT INTO municipio (nome, fkNotaMunicipal) VALUES ('São Paulo', 1);


-- 1. INSERIR OS NÍVEIS DE ACESSO (Conforme solicitado)
INSERT INTO nivelAcesso (nome) VALUES ('Administrador'); -- Geralmente assume o ID 1
INSERT INTO nivelAcesso (nome) VALUES ('Coordenador');   -- Geralmente assume o ID 2
INSERT INTO nivelAcesso (nome) VALUES ('Professor');     -- Geralmente assume o ID 3

-- 2. INSERIR ALGUNS CURSINHOS DE EXEMPLO
INSERT INTO cursinho (nome, cnpj, codigoConvite, fkMunicipio) VALUES ('Cursinho Progressão', '12345678000199', "XPTO", 1); -- ID 1
INSERT INTO cursinho (nome, cnpj, codigoConvite, fkMunicipio) VALUES ('Academia do Saber', '98765432000188', "XPTI", 1);   -- ID 2

-- 3. INSERIR ALGUNS MUNICÍPIOS DE EXEMPLO
-- (Nota: Como a tabela municipio não estava no script original mas é referenciada, 
-- certifique-se de que estes IDs existem na sua tabela de municípios)
 


-- 4. INSERIR OS USUÁRIOS (Fazendo as associações)
-- Utilizador 1: Professor no Cursinho Progressão (fkNivelAcesso = 3, cursinho_id = 1)
INSERT INTO usuario (nome, email, senha, dataCriacao, fkNivelAcesso, fkCursinho) 
VALUES ('Carlos Silva','carlos@gmail.com', 'senhaCriptografada123', NOW(), 3, 1);

-- Utilizador 2: Administrador no Cursinho Progressão (fkNivelAcesso = 1, cursinho_id = 1)
INSERT INTO usuario (nome, senha, dataCriacao, fkNivelAcesso, fkCursinho) 
VALUES ('Ana Souza', 'senhaCriptografada456', NOW(), 1, 1);

-- Utilizador 3: Coordenador na Academia do Saber (fkNivelAcesso = 2, cursinho_id = 2)
INSERT INTO usuario (nome, senha, dataCriacao, fkNivelAcesso, fkCursinho) 
VALUES ('Marcos Oliveira', 'senhaCriptografada789', NOW(), 2, 1);

-- Utilizador 4: Outro Professor, agora na Academia do Saber (fkNivelAcesso = 3, cursinho_id = 2)
INSERT INTO usuario (nome, senha, dataCriacao, fkNivelAcesso, fkCursinho) 
VALUES ('Fernanda Lima', 'senhaCriptografada999', NOW(), 3, 2);