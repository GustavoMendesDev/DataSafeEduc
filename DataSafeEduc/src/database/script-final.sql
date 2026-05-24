
-- =============================================
-- CRIAÇÃO DAS TABELAS - school.sptech
-- =============================================

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

CREATE TABLE IF NOT EXISTS usuario (
  id            INT          NOT NULL AUTO_INCREMENT,
  nome          VARCHAR(80)  NULL,
  senha         VARCHAR(255) NULL,
  dataCriacao   DATETIME     NULL,
  fkNivelAcesso INT          NOT NULL,
  fkMunicipio   INT          NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fkNivelAcesso
    FOREIGN KEY (fkNivelAcesso)
    REFERENCES nivelAcesso (id),
  CONSTRAINT fkMunicipio
    FOREIGN KEY (fkMunicipio)
    REFERENCES municipio (id)
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
  PRIMARY KEY (id)
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




















-- =============================================
-- INSERTS ENEM 2024 - Gerado a partir de dados reais do INEP
-- 381 questões únicas | Matriz de Referência Oficial
-- =============================================
 
-- ---------------------------------------------
-- areaConhecimento
-- ---------------------------------------------
INSERT INTO areaConhecimento (id, nome, sigla) VALUES (1, 'Linguagens, Códigos e suas Tecnologias', 'LC');
INSERT INTO areaConhecimento (id, nome, sigla) VALUES (2, 'Ciências Humanas e suas Tecnologias', 'CH');
INSERT INTO areaConhecimento (id, nome, sigla) VALUES (3, 'Ciências da Natureza e suas Tecnologias', 'CN');
INSERT INTO areaConhecimento (id, nome, sigla) VALUES (4, 'Matemática e suas Tecnologias', 'MT');
 
-- ---------------------------------------------
-- habilidade (120 habilidades - 30 por área)
-- ---------------------------------------------
-- Linguagens, Códigos e suas Tecnologias
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (101, 'H01', 'Identificar as diferentes linguagens e seus recursos expressivos como elementos de caracterização dos sistemas de comunicação.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (102, 'H02', 'Recorrer aos conhecimentos sobre as linguagens dos sistemas de comunicação e informação para resolver problemas sociais.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (103, 'H03', 'Relacionar informações geradas nos sistemas de comunicação e informação, considerando a função social desses sistemas.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (104, 'H04', 'Reconhecer posições críticas aos usos sociais que são feitos das linguagens e dos sistemas de comunicação e informação.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (105, 'H05', 'Associar vocábulos e expressões de um texto em LEM ao seu tema.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (106, 'H06', 'Utilizar os conhecimentos da LEM e de seus mecanismos como meio de ampliar as possibilidades de acesso a informações, tecnologias e culturas.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (107, 'H07', 'Relacionar um texto em LEM, as estruturas linguísticas, sua função e seu uso social.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (108, 'H08', 'Reconhecer a importância da produção cultural em LEM como representação da diversidade cultural e linguística.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (109, 'H09', 'Reconhecer as manifestações corporais de movimento como originárias de necessidades cotidianas de um grupo social.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (110, 'H10', 'Reconhecer a necessidade de transformação de hábitos corporais em função das necessidades cinestésicas.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (111, 'H11', 'Reconhecer a linguagem corporal como meio de interação social, considerando os limites de desempenho e as alternativas de adaptação para diferentes indivíduos.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (112, 'H12', 'Reconhecer diferentes funções da arte, do trabalho da produção dos artistas em seus meios culturais.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (113, 'H13', 'Analisar as diversas produções artísticas como meio de explicar diferentes culturas, padrões de beleza e preconceitos.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (114, 'H14', 'Reconhecer o valor da diversidade artística e das inter-relações de elementos que se apresentam nas manifestações de vários grupos sociais e étnicos.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (115, 'H15', 'Estabelecer relações entre o texto literário e o momento de sua produção, situando aspectos do contexto histórico, social e político.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (116, 'H16', 'Relacionar informações sobre concepções artísticas e procedimentos de construção do texto literário.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (117, 'H17', 'Reconhecer a presença de valores sociais e humanos atualizáveis e permanentes no patrimônio literário nacional.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (118, 'H18', 'Identificar os elementos que concorrem para a progressão temática e para a organização e estruturação de textos de diferentes gêneros e tipos.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (119, 'H19', 'Analisar a função da linguagem predominante nos textos em situações específicas de interlocução.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (120, 'H20', 'Reconhecer a importância do patrimônio linguístico para a preservação da memória e da identidade nacional.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (121, 'H21', 'Reconhecer em textos de diferentes gêneros, recursos verbais e não-verbais utilizados com a finalidade de criar e mudar comportamentos e hábitos.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (122, 'H22', 'Relacionar, em diferentes textos, opiniões, temas, assuntos e recursos linguísticos.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (123, 'H23', 'Inferir em um texto quais são os objetivos de seu produtor e quem é seu público alvo, pela análise dos procedimentos argumentativos utilizados.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (124, 'H24', 'Reconhecer no texto estratégias argumentativas empregadas para o convencimento do público, tais como a intimidação, sedução, comoção, chantagem, entre outras.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (125, 'H25', 'Identificar, em textos de diferentes gêneros, as marcas linguísticas que singularizam as variedades linguísticas sociais, regionais e de registro.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (126, 'H26', 'Relacionar as variedades linguísticas a situações específicas de uso social.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (127, 'H27', 'Reconhecer os usos da norma padrão da língua portuguesa nas diferentes situações de comunicação.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (128, 'H28', 'Reconhecer a função e o impacto social das diferentes tecnologias da comunicação e informação.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (129, 'H29', 'Identificar pela análise de suas linguagens, as tecnologias da comunicação e informação.', 1);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (130, 'H30', 'Relacionar as tecnologias de comunicação e informação ao desenvolvimento das sociedades e ao conhecimento que elas produzem.', 1);
 
-- Ciências Humanas e suas Tecnologias
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (201, 'H01', 'Interpretar historicamente e/ou geograficamente fontes documentais acerca de aspectos da cultura.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (202, 'H02', 'Analisar a produção da memória pelas sociedades humanas.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (203, 'H03', 'Associar as manifestações culturais do presente aos seus processos históricos.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (204, 'H04', 'Comparar pontos de vista expressos em diferentes fontes sobre determinado aspecto da cultura.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (205, 'H05', 'Identificar as manifestações ou representações da diversidade do patrimônio cultural e artístico em diferentes sociedades.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (206, 'H06', 'Interpretar diferentes representações gráficas e cartográficas dos espaços geográficos.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (207, 'H07', 'Identificar os significados histórico-geográficos das relações de poder entre as nações.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (208, 'H08', 'Analisar a ação dos estados nacionais no que se refere à dinâmica dos fluxos populacionais e no enfrentamento de problemas de ordem econômico-social.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (209, 'H09', 'Comparar o significado histórico-geográfico das organizações políticas e socioeconômicas em escala local, regional ou mundial.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (210, 'H10', 'Reconhecer a dinâmica da organização dos movimentos sociais e a importância da participação da coletividade na transformação histórico-geográfica.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (211, 'H11', 'Identificar registros de práticas de grupos sociais no tempo e no espaço.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (212, 'H12', 'Analisar o papel da justiça como instituição na organização das sociedades.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (213, 'H13', 'Analisar a atuação dos movimentos sociais que contribuíram para mudanças ou rupturas em processos de disputa pelo poder.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (214, 'H14', 'Comparar diferentes pontos de vista, presentes em textos analíticos e interpretativos, sobre situação ou fatos de natureza histórico-geográfica acerca das instituições sociais, políticas e econômicas.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (215, 'H15', 'Avaliar criticamente conflitos culturais, sociais, políticos, econômicos ou ambientais ao longo da história.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (216, 'H16', 'Identificar registros sobre o papel das técnicas e tecnologias na organização do trabalho e/ou da vida social.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (217, 'H17', 'Analisar fatores que explicam o impacto das novas tecnologias no processo de territorialização da produção.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (218, 'H18', 'Analisar diferentes processos de produção ou circulação de riquezas e suas implicações sócio-espaciais.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (219, 'H19', 'Reconhecer as transformações técnicas e tecnológicas que determinam as várias formas de uso e apropriação dos espaços rural e urbano.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (220, 'H20', 'Selecionar argumentos favoráveis ou contrários às modificações impostas pelas novas tecnologias à vida social e ao mundo do trabalho.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (221, 'H21', 'Identificar o papel dos meios de comunicação na construção da vida social.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (222, 'H22', 'Analisar as lutas sociais e conquistas obtidas no que se refere às mudanças nas legislações ou nas políticas públicas.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (223, 'H23', 'Analisar a importância dos valores éticos na estruturação política das sociedades.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (224, 'H24', 'Relacionar cidadania e democracia na organização das sociedades.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (225, 'H25', 'Identificar estratégias que promovam formas de inclusão social.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (226, 'H26', 'Identificar em fontes diversas o processo de ocupação dos meios físicos e as relações da vida humana com a paisagem.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (227, 'H27', 'Analisar de maneira crítica as interações da sociedade com o meio físico, levando em consideração aspectos históricos e/ou geográficos.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (228, 'H28', 'Relacionar o uso das tecnologias com os impactos sócio-ambientais em diferentes contextos histórico-geográficos.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (229, 'H29', 'Reconhecer a função dos recursos naturais na produção do espaço geográfico, relacionando-os com as mudanças provocadas pelas ações humanas.', 2);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (230, 'H30', 'Avaliar as relações entre preservação e degradação da vida no planeta nas diferentes escalas.', 2);
 
-- Ciências da Natureza e suas Tecnologias
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (301, 'H01', 'Reconhecer características ou propriedades de fenômenos ondulatórios ou oscilatórios, relacionando-os a seus usos em diferentes contextos.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (302, 'H02', 'Associar a solução de problemas de comunicação, transporte, saúde ou outro com o correspondente desenvolvimento científico e tecnológico.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (303, 'H03', 'Confrontar interpretações científicas com interpretações baseadas no senso comum, ao longo do tempo ou em diferentes culturas.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (304, 'H04', 'Avaliar propostas de intervenção no ambiente, considerando a qualidade de vida humana, ética, conservação ambiental e o uso sustentável dos recursos naturais.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (305, 'H05', 'Caracterizar causas ou efeitos dos movimentos de partículas, substâncias, objetos ou corpos celestes.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (306, 'H06', 'Compreender fenômenos decorrentes da interação entre a radiação e a matéria em suas manifestações em processos naturais ou tecnológicos.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (307, 'H07', 'Identificar diferentes padrões de energia e as formas de transferência de energia nos processos físicos, químicos ou biológicos.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (308, 'H08', 'Identificar a presença e aplicar as tecnologias associadas às ciências naturais em diferentes contextos.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (309, 'H09', 'Entender o papel da evolução na diversificação dos seres vivos.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (310, 'H10', 'Reconhecer mecanismos de transmissão da vida, valorizar e propor ações de qualidade de vida individual, coletiva ou do planeta.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (311, 'H11', 'Reconhecer benefícios, limitações e aspectos éticos da biotecnologia, considerando estruturas e processos biológicos envolvidos em produtos biotecnológicos.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (312, 'H12', 'Avaliar as implicações sociais, ambientais e/ou econômicas na produção ou no consumo de recursos energéticos ou minerais, identificando transformações químicas ou de energia envolvidas nesses processos.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (313, 'H13', 'Avaliar o uso de diferentes energias na realidade brasileira, considerando fontes, transformações, eficiência e impacto ambiental.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (314, 'H14', 'Avaliar o uso de diferentes energias para o desenvolvimento humano, socioeconômico e ambiental com critérios de sustentabilidade.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (315, 'H15', 'Usar o conhecimento químico para explicar fenômenos naturais e processos industriais ou cotidianos.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (316, 'H16', 'Reconhecer propriedades e usos dos materiais, considerando a estrutura microscópica e as interações físico-químicas que determinam essas propriedades.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (317, 'H17', 'Relacionar informações apresentadas em diferentes formas de linguagem e representação usadas nas ciências físicas, químicas ou biológicas.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (318, 'H18', 'Relacionar propriedades físicas, químicas ou biológicas de produtos, sistemas ou procedimentos tecnológicos às finalidades a que se destinam.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (319, 'H19', 'Avaliar métodos, processos ou procedimentos das ciências naturais que contribuam para diagnosticar ou solucionar problemas de ordem social, econômica ou ambiental.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (320, 'H20', 'Articular informações de natureza científica para obter, analisar e apresentar conclusões sobre fenômenos físicos, químicos ou biológicos.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (321, 'H21', 'Reconhecer situações de saúde individual, coletiva ou animal, identificando determinantes, reconhecendo sintomas e propondo ações preventivas ou curativas.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (322, 'H22', 'Compreender as ações e transformações ambientais identificando processos degradantes e propondo estratégias de conservação ambiental.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (323, 'H23', 'Avaliar os riscos individuais ou coletivos ao escolher determinado produto, procedimento ou ação relacionado a condições de saúde, segurança ou conservação ambiental.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (324, 'H24', 'Identificar as relações entre organismos e ambiente, e os impactos dessas relações na dinâmica das populações e nas cadeias alimentares.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (325, 'H25', 'Avaliar o impacto das transformações ambientais causadas por atividades humanas na biodiversidade e nos ecossistemas.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (326, 'H26', 'Analisar os processos químicos associados a fenômenos atmosféricos ou geológicos, identificando as reações e substâncias envolvidas.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (327, 'H27', 'Avaliar os efeitos de substâncias sobre a saúde humana, identificando riscos e benefícios do uso de medicamentos, agrotóxicos, aditivos alimentares e outros.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (328, 'H28', 'Reconhecer transformações físico-químicas que ocorrem em fenômenos cotidianos, associando-as a propriedades e reações de substâncias.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (329, 'H29', 'Analisar os impactos das transformações da matéria e da energia nos processos biológicos, físicos e químicos nos organismos e nos ecossistemas.', 3);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (330, 'H30', 'Relacionar conhecimentos de ciências naturais com o desenvolvimento tecnológico e seus impactos no ambiente e na sociedade.', 3);
 
-- Matemática e suas Tecnologias
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (401, 'H01', 'Reconhecer, no contexto social, diferentes significados e representações dos números e operações - naturais, inteiros, racionais ou reais.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (402, 'H02', 'Identificar padrões numéricos ou princípios de contagem.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (403, 'H03', 'Resolver situação-problema envolvendo conhecimentos numéricos.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (404, 'H04', 'Avaliar a razoabilidade de um resultado numérico na construção de argumentos sobre afirmações quantitativas.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (405, 'H05', 'Avaliar propostas de intervenção na realidade utilizando conhecimentos numéricos.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (406, 'H06', 'Interpretar a localização e a movimentação de pessoas/objetos no espaço tridimensional e sua representação no espaço bidimensional.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (407, 'H07', 'Identificar características de figuras planas ou espaciais.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (408, 'H08', 'Resolver situação-problema que envolva conhecimentos geométricos de espaço e forma.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (409, 'H09', 'Utilizar conhecimentos geométricos de espaço e forma na seleção de argumentos propostos como solução de problemas do cotidiano.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (410, 'H10', 'Avaliar propostas de intervenção na realidade utilizando conhecimentos geométricos.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (411, 'H11', 'Construir noções de grandezas e medidas para a compreensão da realidade e a solução de problemas do cotidiano.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (412, 'H12', 'Utilizar o conhecimento de grandezas e medidas para resolver situação-problema no cotidiano.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (413, 'H13', 'Avaliar o resultado de uma medição na construção de um argumento consistente.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (414, 'H14', 'Resolver situação-problema que envolva medidas de grandezas.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (415, 'H15', 'Avaliar proposta de intervenção na realidade utilizando conhecimentos de grandezas e medidas.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (416, 'H16', 'Identificar variação entre grandezas no contexto de situações reais.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (417, 'H17', 'Utilizar o princípio da proporcionalidade para resolver problemas em contextos cotidianos.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (418, 'H18', 'Aplicar conceitos de proporcionalidade em situações da vida cotidiana, incluindo regra de três, porcentagem e juros.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (419, 'H19', 'Identificar representações algébricas que expressem a relação entre grandezas.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (420, 'H20', 'Interpretar gráfico cartesiano que represente relações entre grandezas.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (421, 'H21', 'Resolver situação-problema cuja modelagem envolva conhecimentos algébricos.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (422, 'H22', 'Utilizar conhecimentos algébricos/geométricos como recurso para a construção de argumentação.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (423, 'H23', 'Avaliar propostas de intervenção na realidade utilizando conhecimentos algébricos.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (424, 'H24', 'Utilizar informações expressas em gráficos ou tabelas para fazer inferências.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (425, 'H25', 'Resolver problema com dados apresentados em tabelas ou gráficos.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (426, 'H26', 'Analisar informações expressas em gráficos ou tabelas como recurso para a construção de argumentos.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (427, 'H27', 'Calcular medidas de tendência central ou de dispersão de um conjunto de dados expressos em uma tabela de frequências.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (428, 'H28', 'Resolver situação-problema que envolva conhecimentos de estatística e probabilidade.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (429, 'H29', 'Utilizar conhecimentos de estatística e probabilidade como recurso para a construção de argumentação.', 4);
INSERT INTO habilidade (id, numero, descricao, fkAreaConhecimento) VALUES (430, 'H30', 'Avaliar propostas de intervenção na realidade utilizando conhecimentos de estatística e probabilidade.', 4);
 
-- ---------------------------------------------
-- parametroTri (380 únicos + 1 sem parâmetros)
-- ---------------------------------------------
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (1, 'Difícil', 2.26176, 1.33672, 0.2149);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (2, 'Médio', 1.402, 0.401, 0.215);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (3, 'Muito Difícil', 1.60709, 2.49443, 0.22653);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (4, 'Médio', 1.57651, 0.53632, 0.17771);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (5, 'Difícil', 2.46865, 1.73917, 0.23827);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (6, 'Muito Difícil', 1.89157, 2.761, 0.19855);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (7, 'Difícil', 2.86412, 1.37123, 0.25332);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (8, 'Fácil', 2.3698, -0.23487, 0.12324);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (9, 'Médio', 1.93962, 0.73478, 0.12274);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (10, 'Não classificado', NULL, NULL, NULL);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (11, 'Muito Difícil', 2.77224, 2.00426, 0.04097);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (12, 'Muito Difícil', 3.90485, 2.50442, 0.11173);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (13, 'Muito Difícil', 3.04396, 2.33757, 0.08577);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (14, 'Fácil', 1.68081, -0.15612, 0.19974);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (15, 'Muito Difícil', 2.59004, 2.41256, 0.20225);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (16, 'Muito Difícil', 2.18588, 2.06747, 0.20325);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (17, 'Muito Difícil', 1.97145, 2.29841, 0.14642);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (18, 'Difícil', 3.53861, 1.72953, 0.05329);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (19, 'Difícil', 2.31189, 1.58736, 0.11137);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (20, 'Difícil', 1.88685, 1.03171, 0.15614);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (21, 'Difícil', 1.68425, 1.70099, 0.10013);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (22, 'Muito Difícil', 1.39291, 2.46888, 0.17809);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (23, 'Difícil', 2.03866, 1.24664, 0.21583);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (24, 'Difícil', 2.10805, 1.4948, 0.14338);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (25, 'Difícil', 2.46504, 1.99892, 0.18993);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (26, 'Médio', 1.49597, 0.94258, 0.18539);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (27, 'Muito Difícil', 3.29564, 2.98287, 0.14454);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (28, 'Muito Difícil', 2.79303, 2.04988, 0.25041);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (29, 'Difícil', 2.86713, 1.62276, 0.15323);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (30, 'Difícil', 2.87, 1.54072, 0.12062);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (31, 'Difícil', 2.52245, 1.60614, 0.23639);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (32, 'Difícil', 2.24996, 1.35811, 0.22169);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (33, 'Médio', 2.33519, 0.67545, 0.20585);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (34, 'Difícil', 2.90615, 1.73585, 0.09467);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (35, 'Difícil', 1.73929, 1.76867, 0.18629);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (36, 'Muito Difícil', 2.25486, 2.9863, 0.19011);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (37, 'Difícil', 2.15309, 1.47257, 0.11281);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (38, 'Difícil', 2.21296, 1.16464, 0.2156);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (39, 'Difícil', 2.70251, 1.04041, 0.08979);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (40, 'Difícil', 2.18515, 1.28849, 0.1245);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (41, 'Difícil', 2.22599, 1.37853, 0.21026);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (42, 'Difícil', 1.40036, 1.16171, 0.17727);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (43, 'Difícil', 1.48715, 1.71916, 0.18593);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (44, 'Difícil', 3.23326, 1.35497, 0.14941);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (45, 'Difícil', 2.20493, 1.1269, 0.183);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (46, 'Difícil', 3.29248, 1.93864, 0.13627);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (47, 'Difícil', 2.66954, 1.71786, 0.17943);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (48, 'Difícil', 3.09197, 1.13019, 0.19954);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (49, 'Médio', 1.91272, 0.6447, 0.18144);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (50, 'Difícil', 1.96491, 1.28856, 0.18913);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (51, 'Muito Difícil', 1.03037, 2.2554, 0.22682);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (52, 'Difícil', 1.76236, 1.43583, 0.2737);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (53, 'Muito Difícil', 3.12311, 2.42519, 0.11331);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (54, 'Muito Difícil', 2.40682, 2.16964, 0.19129);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (55, 'Médio', 1.75758, 0.70231, 0.15325);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (56, 'Difícil', 3.87983, 1.96308, 0.20715);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (57, 'Médio', 5.00865, 0.50119, 0.13688);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (58, 'Difícil', 2.51129, 1.55249, 0.04279);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (59, 'Médio', 3.42357, 0.49941, 0.11756);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (60, 'Médio', 2.25936, 0.86844, 0.2292);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (61, 'Difícil', 2.44456, 1.79477, 0.19792);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (62, 'Médio', 0.57062, 0.78773, 0.0228);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (63, 'Difícil', 2.73571, 1.22835, 0.14646);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (64, 'Difícil', 1.89479, 1.79703, 0.07532);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (65, 'Muito Difícil', 2.88558, 2.28909, 0.19954);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (66, 'Médio', 3.40083, 0.78927, 0.1278);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (67, 'Médio', 2.26694, 0.92667, 0.27632);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (68, 'Difícil', 3.20531, 1.24481, 0.27737);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (69, 'Muito Difícil', 1.0066, 2.12389, 0.21728);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (70, 'Difícil', 1.70701, 1.34875, 0.15396);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (71, 'Médio', 2.18202, 0.92105, 0.18528);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (72, 'Muito Difícil', 1.34998, 2.01873, 0.21909);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (73, 'Difícil', 3.68007, 1.99027, 0.07257);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (74, 'Médio', 1.70988, 0.56843, 0.12157);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (75, 'Médio', 1.16405, 0.1391, 0.23831);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (76, 'Médio', 1.42716, 0.47356, 0.22465);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (77, 'Difícil', 1.12236, 1.94205, 0.1949);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (78, 'Muito Difícil', 2.07038, 2.11919, 0.04391);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (79, 'Médio', 2.11332, 0.37902, 0.20512);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (80, 'Difícil', 2.35211, 1.61749, 0.08654);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (81, 'Difícil', 3.46803, 1.44675, 0.27231);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (82, 'Médio', 1.80537, 0.46767, 0.00115);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (83, 'Muito Difícil', 1.40499, 2.56924, 0.29516);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (84, 'Difícil', 2.81885, 1.47104, 0.15626);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (85, 'Muito Difícil', 3.11951, 2.03277, 0.14869);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (86, 'Médio', 1.04682, 0.19023, 0.21346);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (87, 'Médio', 2.73828, 0.66579, 0.17472);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (88, 'Médio', 2.30137, 0.9951, 0.05607);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (89, 'Médio', 1.54385, 0.18646, 0.02403);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (90, 'Difícil', 3.26263, 1.04212, 0.20017);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (91, 'Difícil', 1.23581, 1.19631, 0.17298);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (92, 'Difícil', 1.90197, 1.42904, 0.17325);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (93, 'Médio', 1.8603, 0.23132, 0.12911);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (94, 'Difícil', 1.47338, 1.91066, 0.1946);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (95, 'Difícil', 2.17473, 1.29429, 0.09133);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (96, 'Médio', 3.366, 0.70227, 0.21496);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (97, 'Difícil', 1.82455, 1.01771, 0.09973);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (98, 'Médio', 1.64523, 0.58391, 0.19253);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (99, 'Médio', 3.85743, 0.08809, 0.5);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (100, 'Médio', 1.83319, 0.61131, 0.16629);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (101, 'Difícil', 3.98036, 1.04132, 0.06184);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (102, 'Médio', 1.22652, 0.72244, 0.21354);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (103, 'Fácil', 0.90246, -0.70921, 0.20607);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (104, 'Médio', 1.50992, 0.75485, 0.13474);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (105, 'Médio', 1.27565, 0.64626, 0.11824);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (106, 'Difícil', 2.54816, 1.50923, 0.25926);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (107, 'Difícil', 3.60975, 1.52395, 0.3178);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (108, 'Médio', 1.44913, 0.90324, 0.29682);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (109, 'Muito Difícil', 5.44912, 2.872, 0.15431);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (110, 'Difícil', 2.13198, 1.15028, 0.16957);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (111, 'Muito Difícil', 2.13415, 2.32504, 0.17243);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (112, 'Médio', 1.63204, 0.67676, 0.16999);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (113, 'Difícil', 3.052, 1.11022, 0.25363);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (114, 'Médio', 2.85265, 0.96695, 0.12627);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (115, 'Difícil', 3.56451, 1.17043, 0.07963);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (116, 'Difícil', 1.70031, 1.02545, 0.21971);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (117, 'Muito Difícil', 3.58253, 2.29438, 0.31171);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (118, 'Difícil', 2.53134, 1.99645, 0.13947);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (119, 'Médio', 3.48167, 0.81823, 0.23768);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (120, 'Médio', 3.84493, 0.88646, 0.10345);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (121, 'Muito Difícil', 2.27226, 2.66298, 0.35485);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (122, 'Difícil', 1.35644, 1.59204, 0.16417);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (123, 'Difícil', 1.81691, 1.37451, 0.12064);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (124, 'Difícil', 2.60531, 1.94764, 0.17863);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (125, 'Difícil', 2.98857, 1.93426, 0.22115);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (126, 'Médio', 3.32906, 0.86547, 0.16688);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (127, 'Médio', 2.32009, 0.55237, 0.13443);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (128, 'Muito Difícil', 0.82553, 2.21651, 0.16426);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (129, 'Médio', 1.98922, 0.99384, 0.18343);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (130, 'Difícil', 1.11115, 1.46269, 0.22184);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (131, 'Difícil', 1.53146, 1.27326, 0.12431);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (132, 'Muito Difícil', 2.76472, 2.5307, 0.08433);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (133, 'Difícil', 1.96986, 1.25923, 0.16841);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (134, 'Muito Difícil', 2.59016, 2.32557, 0.13444);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (135, 'Muito Difícil', 2.08549, 2.83027, 0.2966);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (136, 'Muito Difícil', 2.1156, 2.03104, 0.04375);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (137, 'Difícil', 1.48916, 1.59773, 0.21462);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (138, 'Muito Difícil', 3.37882, 2.15318, 0.10317);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (139, 'Difícil', 4.24356, 1.09968, 0.28019);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (140, 'Muito Difícil', 2.46809, 3.26728, 0.24774);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (141, 'Muito Difícil', 1.49785, 2.86851, 0.21175);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (142, 'Muito Difícil', 1.02692, 3.36222, 0.18689);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (143, 'Muito Difícil', 1.75413, 2.39153, 0.28016);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (144, 'Difícil', 1.22427, 1.34947, 0.17872);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (145, 'Médio', 1.34953, 0.50738, 0.16017);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (146, 'Difícil', 2.05227, 1.04175, 0.1734);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (147, 'Fácil', 1.34861, -0.66075, 0.0051);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (148, 'Médio', 0.88567, 0.32823, 0.01968);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (149, 'Médio', 3.8859, 0.87601, 0.19923);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (150, 'Médio', 3.52605, 0.16469, 0.18195);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (151, 'Médio', 2.8747, 0.24823, 0.23747);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (152, 'Difícil', 2.02503, 1.88946, 0.19048);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (153, 'Médio', 3.84247, 0.91232, 0.20573);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (154, 'Difícil', 2.10598, 1.42213, 0.10091);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (155, 'Muito Difícil', 1.34516, 2.49622, 0.24066);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (156, 'Difícil', 3.17804, 1.61233, 0.086);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (157, 'Difícil', 3.37555, 1.84456, 0.1926);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (158, 'Difícil', 1.56842, 1.95154, 0.23233);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (159, 'Médio', 2.2696, 0.50185, 0.10032);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (160, 'Médio', 1.78653, 0.83974, 0.15945);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (161, 'Difícil', 2.46593, 1.78408, 0.21541);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (162, 'Muito Difícil', 2.19608, 2.24658, 0.27848);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (163, 'Muito Difícil', 2.79451, 2.06903, 0.29389);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (164, 'Médio', 2.53287, 0.60753, 0.28042);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (165, 'Médio', 1.16425, 0.88519, 0.20873);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (166, 'Muito Difícil', 3.14218, 2.04254, 0.13487);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (167, 'Muito Difícil', 1.5236, 3.09917, 0.1505);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (168, 'Muito Difícil', 1.87853, 2.91962, 0.20882);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (169, 'Difícil', 1.89829, 1.63461, 0.19327);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (170, 'Médio', 3.60586, 0.48338, 0.14782);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (171, 'Difícil', 2.05051, 1.77607, 0.18888);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (172, 'Médio', 1.7697, 0.3763, 0.20963);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (173, 'Muito Difícil', 1.18974, 2.2259, 0.21918);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (174, 'Difícil', 3.26013, 1.57311, 0.15019);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (175, 'Difícil', 2.01857, 1.22451, 0.12408);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (176, 'Muito Difícil', 3.25419, 2.00748, 0.23654);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (177, 'Médio', 2.43297, 0.04851, 0.36134);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (178, 'Difícil', 3.88018, 1.84281, 0.25883);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (179, 'Difícil', 0.75872, 1.29973, 0.00551);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (180, 'Difícil', 1.93635, 1.77293, 0.24579);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (181, 'Difícil', 1.58473, 1.27479, 0.12442);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (182, 'Médio', 2.74054, 0.68765, 0.19316);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (183, 'Fácil', 3.6852, -0.20488, 0.20362);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (184, 'Fácil', 2.9048, -0.61866, 0.20054);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (185, 'Médio', 2.78496, 0.52101, 0.15014);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (186, 'Médio', 4.67112, 0.81082, 0.33341);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (187, 'Médio', 1.47453, 0.40487, 0.18029);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (188, 'Médio', 2.59075, 0.81709, 0.21069);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (189, 'Médio', 1.6887, 0.38261, 0.17288);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (190, 'Fácil', 2.1269, -0.37854, 0.00587);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (191, 'Médio', 2.4521, 0.40325, 0.20257);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (192, 'Médio', 2.32301, 0.39677, 0.16215);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (193, 'Muito Difícil', 0.49366, 2.05523, 0.09974);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (194, 'Médio', 2.6215, 0.71695, 0.1198);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (195, 'Médio', 1.93512, 0.23569, 0.19848);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (196, 'Médio', 4.08733, 0.26147, 0.21651);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (197, 'Difícil', 2.24834, 1.81798, 0.17832);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (198, 'Médio', 2.26937, 0.68094, 0.2747);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (199, 'Difícil', 1.56271, 1.15577, 0.1328);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (200, 'Médio', 2.18685, 0.19158, 0.20225);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (201, 'Difícil', 2.82782, 1.20607, 0.17666);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (202, 'Fácil', 1.08881, -0.92571, 0.01007);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (203, 'Difícil', 2.80943, 1.22263, 0.3742);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (204, 'Fácil', 1.39576, -0.86517, 0.20417);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (205, 'Difícil', 2.56039, 1.36529, 0.22214);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (206, 'Difícil', 1.13982, 1.73496, 0.10129);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (207, 'Muito Difícil', 2.76697, 2.68891, 0.14606);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (208, 'Muito Difícil', 1.26643, 2.72688, 0.13414);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (209, 'Difícil', 3.40587, 1.26456, 0.13332);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (210, 'Muito Difícil', 2.16007, 2.55251, 0.12173);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (211, 'Muito Difícil', 1.92613, 2.49551, 0.13769);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (212, 'Muito Difícil', 2.96006, 2.23749, 0.16489);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (213, 'Difícil', 2.19756, 1.63229, 0.13191);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (214, 'Médio', 3.28393, 0.79725, 0.151);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (215, 'Muito Difícil', 2.49341, 2.13268, 0.15163);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (216, 'Muito Difícil', 1.49745, 2.60757, 0.14156);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (217, 'Difícil', 2.78314, 1.11704, 0.11259);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (218, 'Difícil', 2.27814, 1.63004, 0.09638);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (219, 'Difícil', 1.46288, 1.83821, 0.27312);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (220, 'Difícil', 2.08753, 1.72911, 0.18351);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (221, 'Difícil', 2.15211, 1.80485, 0.13117);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (222, 'Fácil', 0.90764, -0.16726, 0.20002);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (223, 'Muito Difícil', 1.48367, 2.43527, 0.15643);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (224, 'Difícil', 0.92744, 1.27494, 0.08115);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (225, 'Muito Difícil', 2.46852, 2.16372, 0.13378);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (226, 'Difícil', 2.77926, 1.21189, 0.19623);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (227, 'Médio', 2.40504, 0.7279, 0.14722);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (228, 'Médio', 3.45275, 0.54931, 0.16635);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (229, 'Difícil', 2.59479, 1.17618, 0.31316);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (230, 'Difícil', 3.05364, 1.97296, 0.35804);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (231, 'Fácil', 1.63763, -0.46573, 0.19507);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (232, 'Fácil', 2.59082, -0.08357, 0.18726);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (233, 'Muito Difícil', 2.56254, 3.01139, 0.03752);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (234, 'Médio', 1.65663, 0.56156, 0.19846);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (235, 'Médio', 1.62073, 0.14243, 0.18986);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (236, 'Médio', 1.82693, 0.87369, 0.16157);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (237, 'Difícil', 1.59365, 1.18465, 0.23723);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (238, 'Difícil', 1.12353, 1.02169, 0.12343);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (239, 'Fácil', 1.58965, -0.54366, 0.20012);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (240, 'Fácil', 0.52413, -0.75088, 0.22138);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (241, 'Difícil', 0.73965, 1.57862, 0.13905);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (242, 'Difícil', 1.59952, 1.88032, 0.12125);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (243, 'Médio', 2.94433, 0.90437, 0.1784);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (244, 'Difícil', 3.03669, 1.97948, 0.09728);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (245, 'Difícil', 1.2195, 1.21829, 0.22976);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (246, 'Médio', 1.84217, 0.95735, 0.20836);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (247, 'Médio', 1.90346, 0.06765, 0.17804);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (248, 'Médio', 2.84933, 0.97227, 0.14988);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (249, 'Difícil', 2.60033, 1.62055, 0.49999);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (250, 'Difícil', 1.68516, 1.29852, 0.17632);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (251, 'Difícil', 1.94088, 1.08527, 0.16079);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (252, 'Difícil', 1.30844, 1.25071, 0.18571);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (253, 'Médio', 2.58687, 0.11877, 0.21034);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (254, 'Difícil', 2.384, 1.68908, 0.25667);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (255, 'Médio', 4.7046, 0.72073, 0.22345);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (256, 'Médio', 1.89984, 0.61185, 0.2417);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (257, 'Difícil', 2.24213, 1.40974, 0.18204);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (258, 'Médio', 0.40526, 0.65018, 0.30076);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (259, 'Difícil', 2.41, 1.38763, 0.25068);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (260, 'Difícil', 2.48077, 1.75332, 0.23665);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (261, 'Fácil', 0.58481, -0.38987, 0.01152);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (262, 'Difícil', 3.03049, 1.00964, 0.2486);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (263, 'Difícil', 1.38987, 1.48831, 0.2771);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (264, 'Difícil', 1.21803, 1.38698, 0.10064);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (265, 'Médio', 1.81122, 0.89892, 0.14279);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (266, 'Difícil', 1.71111, 1.59401, 0.12056);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (267, 'Médio', 1.95249, 0.25646, 0.21057);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (268, 'Médio', 1.98242, 0.67856, 0.24558);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (269, 'Médio', 1.47602, 0.11469, 0.20588);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (270, 'Médio', 2.77004, 0.18063, 0.18169);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (271, 'Difícil', 1.98686, 1.43691, 0.21777);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (272, 'Médio', 2.66541, 0.60552, 0.12265);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (273, 'Médio', 1.4122, 0.27283, 0.0807);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (274, 'Muito Difícil', 0.20704, 11.14224, 0.17137);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (275, 'Difícil', 2.46856, 1.24642, 0.17816);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (276, 'Médio', 2.34154, 0.71996, 0.12841);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (277, 'Médio', 3.27746, 0.7899, 0.16002);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (278, 'Médio', 3.33008, 0.45298, 0.15146);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (279, 'Difícil', 3.40007, 1.69903, 0.13015);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (280, 'Médio', 2.58128, 0.13172, 0.18248);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (281, 'Difícil', 3.0737, 1.11287, 0.1741);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (282, 'Médio', 1.7665, 0.28216, 0.126);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (283, 'Difícil', 2.58772, 1.21731, 0.1766);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (284, 'Médio', 2.08344, 0.95536, 0.22914);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (285, 'Médio', 2.7327, 0.5031, 0.08917);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (286, 'Médio', 0.84167, 0.36015, 0.23496);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (287, 'Fácil', 2.11831, -0.44202, 0.19567);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (288, 'Médio', 1.72449, 0.64259, 0.26155);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (289, 'Fácil', 2.44925, -0.00932, 0.19459);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (290, 'Médio', 3.92192, 0.61079, 0.24818);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (291, 'Difícil', 1.92079, 1.62104, 0.1821);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (292, 'Muito Difícil', 1.30507, 2.24497, 0.3219);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (293, 'Médio', 4.0067, 0.04613, 0.2554);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (294, 'Difícil', 2.2051, 1.8988, 0.16464);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (295, 'Médio', 2.44437, 0.98014, 0.20713);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (296, 'Difícil', 1.50359, 1.18451, 0.25552);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (297, 'Médio', 1.798, 0.77439, 0.19123);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (298, 'Muito Difícil', 0.44561, 2.71266, 0.00977);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (299, 'Médio', 3.15857, 0.08755, 0.18772);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (300, 'Difícil', 0.77659, 1.10707, 0.24218);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (301, 'Fácil', 1.48442, -0.752, 0.00999);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (302, 'Médio', 3.6485, 0.27468, 0.20745);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (303, 'Difícil', 1.77787, 1.44612, 0.15906);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (304, 'Difícil', 2.46539, 1.08536, 0.27044);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (305, 'Médio', 1.79583, 0.3193, 0.19696);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (306, 'Médio', 2.10871, 0.63797, 0.12831);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (307, 'Médio', 1.53078, 0.23278, 0.20717);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (308, 'Médio', 0.97308, 0.68535, 0.20836);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (309, 'Médio', 2.39673, 0.03767, 0.26467);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (310, 'Difícil', 2.56709, 1.87677, 0.11755);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (311, 'Muito Difícil', 1.56806, 2.3502, 0.29201);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (312, 'Muito Difícil', 1.84675, 2.38735, 0.24264);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (313, 'Médio', 1.69238, 0.89087, 0.11112);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (314, 'Fácil', 1.27914, -0.47051, 0.21183);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (315, 'Difícil', 2.38298, 1.45126, 0.18421);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (316, 'Difícil', 3.0182, 1.22062, 0.11641);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (317, 'Muito Difícil', 1.24643, 2.317, 0.08752);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (318, 'Difícil', 3.05121, 1.6307, 0.24882);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (319, 'Médio', 2.90113, 0.65327, 0.11183);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (320, 'Difícil', 1.21508, 1.69932, 0.15114);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (321, 'Médio', 2.82798, 0.81272, 0.10833);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (322, 'Muito Difícil', 2.39398, 2.01893, 0.17156);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (323, 'Médio', 1.90192, 0.6706, 0.16827);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (324, 'Difícil', 2.09622, 1.56504, 0.45158);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (325, 'Fácil', 0.93211, -0.69435, 0.20425);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (326, 'Difícil', 3.00357, 1.09314, 0.11952);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (327, 'Médio', 2.34265, 0.76737, 0.19913);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (328, 'Médio', 1.63931, 0.12149, 0.19472);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (329, 'Muito Difícil', 1.56293, 2.49679, 0.14573);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (330, 'Difícil', 3.50874, 1.41332, 0.28921);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (331, 'Médio', 0.97991, 0.88701, 0.02074);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (332, 'Difícil', 2.38414, 1.01911, 0.12279);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (333, 'Difícil', 1.68259, 1.77477, 0.07803);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (334, 'Muito Difícil', 1.47108, 2.41049, 0.27168);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (335, 'Difícil', 2.30989, 1.07008, 0.19248);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (336, 'Difícil', 1.8508, 1.48328, 0.34371);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (337, 'Médio', 1.29902, 0.25116, 0.01814);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (338, 'Médio', 3.39952, 0.32395, 0.19849);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (339, 'Médio', 2.42441, 0.08078, 0.19163);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (340, 'Médio', 3.72659, 0.66069, 0.1705);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (341, 'Fácil', 1.15112, -0.24753, 0.19867);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (342, 'Médio', 1.05033, 0.20527, 0.00887);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (343, 'Fácil', 1.6146, -0.07254, 0.20394);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (344, 'Médio', 2.78643, 0.48704, 0.1756);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (345, 'Difícil', 1.82739, 1.49102, 0.20499);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (346, 'Médio', 1.26372, 0.92757, 0.22995);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (347, 'Fácil', 3.51616, -0.2635, 0.19228);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (348, 'Médio', 2.1539, 0.20071, 0.20024);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (349, 'Médio', 1.59442, 0.91705, 0.18709);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (350, 'Médio', 4.01406, 0.2854, 0.20514);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (351, 'Fácil', 2.30804, -0.071, 0.20113);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (352, 'Médio', 2.24622, 0.87521, 0.18171);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (353, 'Difícil', 2.47659, 1.41111, 0.21138);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (354, 'Difícil', 2.63266, 1.23509, 0.15044);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (355, 'Médio', 2.19541, 0.46219, 0.12159);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (356, 'Fácil', 1.45822, -0.78889, 0.01333);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (357, 'Difícil', 1.10756, 1.57176, 0.27729);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (358, 'Médio', 1.07077, 0.04632, 0.21574);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (359, 'Médio', 1.68854, 0.00982, 0.20499);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (360, 'Fácil', 3.14973, -0.03426, 0.20588);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (361, 'Médio', 1.32912, 0.33346, 0.01256);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (362, 'Fácil', 4.32713, -0.03216, 0.14822);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (363, 'Médio', 1.63483, 0.22791, 0.18384);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (364, 'Médio', 5.12643, 0.99473, 0.15516);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (365, 'Médio', 1.30407, 0.74117, 0.04096);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (366, 'Difícil', 3.03065, 1.04759, 0.22947);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (367, 'Difícil', 1.94371, 1.34885, 0.14161);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (368, 'Médio', 3.54677, 0.31786, 0.12491);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (369, 'Médio', 2.90014, 0.58457, 0.17607);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (370, 'Fácil', 2.79235, -0.3231, 0.16431);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (371, 'Fácil', 1.5787, -0.00945, 0.05369);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (372, 'Médio', 4.19734, 0.50532, 0.21518);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (373, 'Médio', 3.15898, 0.16783, 0.17207);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (374, 'Difícil', 1.56187, 1.78435, 0.1507);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (375, 'Médio', 1.05578, 0.18306, 0.20969);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (376, 'Fácil', 1.85835, -0.12011, 0.08169);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (377, 'Médio', 5.24443, 0.2992, 0.19478);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (378, 'Médio', 2.13734, 0.72568, 0.23628);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (379, 'Fácil', 1.3153, -0.18943, 0.19915);
INSERT INTO parametroTri (id, nivel, parametroA, parametroB, parametroC) VALUES (380, 'Fácil', 2.88059, -0.27388, 0.19156);
 
-- ---------------------------------------------
-- questao (381 itens únicos do ENEM 2024)
-- ---------------------------------------------
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('7685', 2024, 408, 1);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('10593', 2024, 119, 2);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('12071', 2024, 419, 3);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('12135', 2024, 329, 4);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('12877', 2024, 425, 5);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('13141', 2024, 421, 6);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('13702', 2024, 305, 7);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('14475', 2024, 101, 8);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('14995', 2024, 427, 9);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('15097', 2024, 112, 10);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('15207', 2024, 419, 11);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('15394', 2024, 411, 12);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('16575', 2024, 116, 13);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('16650', 2024, 313, 14);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('16874', 2024, 421, 15);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('17182', 2024, 115, 16);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('17539', 2024, 412, 17);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('17549', 2024, 408, 18);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('17572', 2024, 317, 19);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('17580', 2024, 317, 20);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('18078', 2024, 416, 21);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('26485', 2024, 425, 22);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('26631', 2024, 115, 23);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('27182', 2024, 311, 24);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('27253', 2024, 421, 25);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('27840', 2024, 412, 26);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('27923', 2024, 419, 27);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('29021', 2024, 403, 28);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('29740', 2024, 428, 29);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('32627', 2024, 420, 30);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('35949', 2024, 404, 31);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('37525', 2024, 314, 32);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('38487', 2024, 329, 33);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('39631', 2024, 419, 34);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('39880', 2024, 403, 35);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('40363', 2024, 411, 36);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('40658', 2024, 312, 37);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('40924', 2024, 113, 38);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('42136', 2024, 425, 39);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('42680', 2024, 308, 40);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('43095', 2024, 412, 41);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('44318', 2024, 218, 42);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('45163', 2024, 318, 43);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('46766', 2024, 114, 44);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('47987', 2024, 106, 45);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('49701', 2024, 412, 46);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('50176', 2024, 420, 47);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('55423', 2024, 408, 48);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('55806', 2024, 427, 49);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('56084', 2024, 328, 50);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('59767', 2024, 306, 51);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('60313', 2024, 404, 52);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('60335', 2024, 402, 53);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('60414', 2024, 403, 54);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('60453', 2024, 402, 55);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('60791', 2024, 324, 56);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('61076', 2024, 306, 10);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('61929', 2024, 106, 57);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('63960', 2024, 325, 58);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('67577', 2024, 217, 59);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('67681', 2024, 401, 60);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('68506', 2024, 318, 61);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('68866', 2024, 321, 62);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('76177', 2024, 401, 63);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('76197', 2024, 318, 64);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('76674', 2024, 321, 65);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('77606', 2024, 303, 66);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('78441', 2024, 329, 67);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('78580', 2024, 319, 68);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('79213', 2024, 408, 69);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('81442', 2024, 404, 70);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('81454', 2024, 310, 71);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('81496', 2024, 403, 72);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('81798', 2024, 307, 73);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('82093', 2024, 210, 74);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('82598', 2024, 327, 75);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('82747', 2024, 328, 76);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('82981', 2024, 308, 77);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('83076', 2024, 404, 78);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('83554', 2024, 315, 79);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('83799', 2024, 402, 80);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('84020', 2024, 316, 81);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('84075', 2024, 318, 82);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('84219', 2024, 417, 83);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('84423', 2024, 429, 84);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('84940', 2024, 414, 85);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('85312', 2024, 314, 86);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('85551', 2024, 207, 87);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('85697', 2024, 314, 88);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('86002', 2024, 328, 89);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('86727', 2024, 303, 90);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('86829', 2024, 202, 91);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('86898', 2024, 309, 92);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('86938', 2024, 202, 93);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('86955', 2024, 427, 94);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('87137', 2024, 302, 95);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('87263', 2024, 315, 96);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('87519', 2024, 401, 97);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('87525', 2024, 426, 98);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('87876', 2024, 107, 99);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('88196', 2024, 330, 100);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('88293', 2024, 322, 101);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('88330', 2024, 326, 102);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('88476', 2024, 303, 103);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('88676', 2024, 311, 104);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('88833', 2024, 203, 105);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('89141', 2024, 308, 106);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('89543', 2024, 425, 107);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('95504', 2024, 304, 108);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('95687', 2024, 422, 109);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('95755', 2024, 403, 110);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('95775', 2024, 209, 111);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('96042', 2024, 307, 112);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('96219', 2024, 201, 113);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('96456', 2024, 122, 114);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('96550', 2024, 313, 115);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('96632', 2024, 428, 116);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('96636', 2024, 421, 117);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('96768', 2024, 412, 118);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('97126', 2024, 215, 119);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('97312', 2024, 107, 120);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('97327', 2024, 324, 121);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('97503', 2024, 405, 122);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('97926', 2024, 211, 123);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('98061', 2024, 325, 124);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('98114', 2024, 312, 125);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('98152', 2024, 208, 126);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('111400', 2024, 301, 127);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('111416', 2024, 325, 128);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('111460', 2024, 314, 129);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('111475', 2024, 319, 130);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('111515', 2024, 302, 131);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('111547', 2024, 323, 132);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('111549', 2024, 415, 133);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('111611', 2024, 301, 134);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('111621', 2024, 305, 135);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('111650', 2024, 311, 136);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('111657', 2024, 312, 137);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('111658', 2024, 315, 138);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('111693', 2024, 324, 139);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('111695', 2024, 414, 140);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('111706', 2024, 430, 141);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('111708', 2024, 413, 142);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('111726', 2024, 327, 143);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('111764', 2024, 430, 144);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('111768', 2024, 426, 145);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('111807', 2024, 210, 146);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('111879', 2024, 222, 147);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('111888', 2024, 105, 148);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('111901', 2024, 101, 149);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('111965', 2024, 222, 150);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('111991', 2024, 203, 151);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117615', 2024, 423, 152);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117618', 2024, 419, 153);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117622', 2024, 422, 154);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117673', 2024, 323, 155);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117707', 2024, 327, 156);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117712', 2024, 416, 157);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117717', 2024, 413, 158);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117723', 2024, 426, 159);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117745', 2024, 429, 160);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117747', 2024, 410, 161);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117759', 2024, 414, 162);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117774', 2024, 416, 163);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117780', 2024, 312, 164);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117781', 2024, 302, 165);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117785', 2024, 418, 166);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117796', 2024, 422, 167);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117797', 2024, 418, 168);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117808', 2024, 416, 169);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117814', 2024, 317, 170);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117817', 2024, 322, 171);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117837', 2024, 323, 172);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117859', 2024, 305, 173);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117865', 2024, 415, 174);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117878', 2024, 330, 175);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117888', 2024, 409, 176);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117915', 2024, 328, 177);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117932', 2024, 320, 178);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('117963', 2024, 309, 179);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('118000', 2024, 306, 180);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('118023', 2024, 215, 181);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('118064', 2024, 216, 182);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('118098', 2024, 111, 183);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('118138', 2024, 123, 184);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('118157', 2024, 108, 185);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('118194', 2024, 105, 186);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('118208', 2024, 108, 187);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('118217', 2024, 109, 188);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('118219', 2024, 109, 189);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('118254', 2024, 111, 190);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('118285', 2024, 108, 191);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('118297', 2024, 117, 192);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('119743', 2024, 113, 193);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('120220', 2024, 126, 194);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('120370', 2024, 113, 195);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('120372', 2024, 111, 196);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('120624', 2024, 122, 197);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('125682', 2024, 220, 198);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('125718', 2024, 230, 199);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('125746', 2024, 225, 200);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('125769', 2024, 221, 201);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('125794', 2024, 221, 202);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('125832', 2024, 227, 203);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('125853', 2024, 217, 204);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('125887', 2024, 407, 205);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('125890', 2024, 417, 206);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('125917', 2024, 411, 207);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('125923', 2024, 428, 208);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('125946', 2024, 216, 209);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('125949', 2024, 413, 210);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('125950', 2024, 406, 211);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('125957', 2024, 409, 212);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('125961', 2024, 410, 213);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('125978', 2024, 411, 214);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('125982', 2024, 419, 215);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('125992', 2024, 405, 216);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('125993', 2024, 423, 217);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('125996', 2024, 406, 218);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('126004', 2024, 407, 219);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('126027', 2024, 407, 220);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('126034', 2024, 418, 221);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('126548', 2024, 310, 222);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('126577', 2024, 320, 223);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('126592', 2024, 307, 224);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140159', 2024, 201, 225);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140161', 2024, 202, 226);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140185', 2024, 210, 227);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140199', 2024, 214, 228);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140213', 2024, 209, 229);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140379', 2024, 204, 230);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140423', 2024, 204, 231);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140450', 2024, 205, 232);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140471', 2024, 223, 233);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140475', 2024, 210, 234);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140508', 2024, 216, 235);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140552', 2024, 212, 236);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140564', 2024, 213, 237);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140574', 2024, 211, 238);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140591', 2024, 107, 239);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140616', 2024, 203, 240);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140621', 2024, 226, 241);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140663', 2024, 105, 242);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140673', 2024, 107, 243);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140684', 2024, 223, 244);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140710', 2024, 107, 245);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140714', 2024, 106, 246);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140727', 2024, 106, 247);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140760', 2024, 106, 248);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140769', 2024, 105, 249);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140787', 2024, 108, 250);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140789', 2024, 108, 251);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140794', 2024, 226, 252);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140807', 2024, 130, 253);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140816', 2024, 119, 254);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140822', 2024, 219, 255);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140824', 2024, 226, 256);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140831', 2024, 116, 257);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140836', 2024, 102, 258);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140881', 2024, 118, 259);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140886', 2024, 115, 260);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140892', 2024, 115, 261);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140894', 2024, 125, 262);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140935', 2024, 116, 263);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140961', 2024, 117, 264);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140980', 2024, 118, 265);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('140982', 2024, 117, 266);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141010', 2024, 224, 267);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141016', 2024, 221, 268);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141025', 2024, 214, 269);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141031', 2024, 213, 270);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141032', 2024, 203, 271);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141033', 2024, 220, 272);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141035', 2024, 211, 273);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141045', 2024, 204, 274);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141067', 2024, 226, 275);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141068', 2024, 229, 276);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141073', 2024, 207, 277);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141105', 2024, 227, 278);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141127', 2024, 207, 279);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141143', 2024, 215, 280);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141147', 2024, 223, 281);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141161', 2024, 227, 282);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141163', 2024, 220, 283);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141166', 2024, 228, 284);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141202', 2024, 112, 285);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141205', 2024, 109, 286);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141206', 2024, 110, 287);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141207', 2024, 113, 288);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141218', 2024, 114, 289);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141221', 2024, 113, 290);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141236', 2024, 112, 291);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141238', 2024, 113, 292);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141244', 2024, 109, 293);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141263', 2024, 110, 294);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141308', 2024, 218, 295);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141310', 2024, 216, 296);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141320', 2024, 224, 297);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141338', 2024, 205, 298);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141348', 2024, 230, 299);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141365', 2024, 201, 300);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141374', 2024, 225, 301);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141379', 2024, 208, 302);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141399', 2024, 130, 303);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141401', 2024, 209, 304);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141402', 2024, 219, 305);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141444', 2024, 118, 306);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141446', 2024, 127, 307);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141447', 2024, 104, 308);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141460', 2024, 102, 309);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141472', 2024, 203, 310);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141537', 2024, 415, 311);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141550', 2024, 415, 312);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141558', 2024, 323, 313);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141571', 2024, 424, 314);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141579', 2024, 424, 315);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141591', 2024, 326, 316);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141594', 2024, 305, 317);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141598', 2024, 326, 318);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141612', 2024, 321, 319);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141621', 2024, 320, 320);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141661', 2024, 317, 321);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141677', 2024, 420, 322);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141730', 2024, 108, 323);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141743', 2024, 311, 324);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141747', 2024, 330, 325);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141753', 2024, 310, 326);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141775', 2024, 313, 327);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141782', 2024, 311, 328);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141789', 2024, 316, 329);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141791', 2024, 304, 330);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141799', 2024, 116, 331);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141803', 2024, 110, 332);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141807', 2024, 316, 333);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('141808', 2024, 312, 334);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('149741', 2024, 205, 335);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('149788', 2024, 205, 336);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('149789', 2024, 203, 337);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('149791', 2024, 215, 338);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('149795', 2024, 206, 339);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('149806', 2024, 205, 340);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('149807', 2024, 224, 341);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('149808', 2024, 201, 342);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('149809', 2024, 206, 343);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('149816', 2024, 229, 344);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('149830', 2024, 228, 345);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('149844', 2024, 212, 346);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('149847', 2024, 206, 347);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('149860', 2024, 229, 348);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('149862', 2024, 223, 349);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150425', 2024, 129, 350);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150441', 2024, 120, 351);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150460', 2024, 125, 352);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150521', 2024, 117, 353);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150523', 2024, 128, 354);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150540', 2024, 130, 355);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150588', 2024, 121, 356);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150600', 2024, 121, 357);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150602', 2024, 102, 358);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150605', 2024, 129, 359);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150621', 2024, 119, 360);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150622', 2024, 118, 361);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150627', 2024, 123, 362);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150629', 2024, 118, 363);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150640', 2024, 122, 364);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150645', 2024, 124, 365);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150662', 2024, 118, 366);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150665', 2024, 125, 367);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150666', 2024, 127, 368);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150668', 2024, 121, 369);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150675', 2024, 103, 370);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150676', 2024, 104, 371);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150677', 2024, 103, 372);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150678', 2024, 120, 373);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150680', 2024, 126, 374);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150684', 2024, 128, 375);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150747', 2024, 104, 376);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150766', 2024, 120, 377);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150767', 2024, 115, 378);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150768', 2024, 129, 379);
INSERT INTO questao (codigoItem, anoExame, fkHabilidade, fkParametroTri) VALUES ('150780', 2024, 124, 380);





-- Inserir um nível de acesso
INSERT INTO nivelAcesso (nome) VALUES ('Admin');

-- Inserir uma nota municipal (obrigatória para município)
INSERT INTO notaMunicipal (matematica, codigosELinguagens, cienciasDaNatureza, cienciasHumanas) 
VALUES (0, 0, 0, 0);

-- Inserir um município
INSERT INTO municipio (nome, estado, fkNotaMunicipal) VALUES ('São Paulo', 'SP', 1);

-- Inserir o usuário
INSERT INTO usuario (nome, senha, dataCriacao, fkNivelAcesso, fkMunicipio)
VALUES ('user@gmail.com', '123', NOW(), 1, 1);


