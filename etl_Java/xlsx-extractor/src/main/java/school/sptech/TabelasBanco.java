package school.sptech;

import org.springframework.jdbc.core.JdbcTemplate;
import school.sptech.enums.SiglaEnum;
import static school.sptech.Log.*;


public class TabelasBanco {

    public static void tabelasBanco() {

        ConexaoBanco dbConnectionProvider = new ConexaoBanco();
        JdbcTemplate connection = dbConnectionProvider.getConnection();


        connection.execute("""
            CREATE TABLE IF NOT EXISTS notaMunicipal (
              id                 INT           NOT NULL AUTO_INCREMENT,
              matematica         DECIMAL(5,2)  NULL,
              codigosELinguagens DECIMAL(5,2)  NULL,
              cienciasDaNatureza DECIMAL(5,2)  NULL,
              cienciasHumanas    DECIMAL(5,2)  NULL,
              PRIMARY KEY (id)
            )
        """);

        connection.execute("""
            CREATE TABLE IF NOT EXISTS municipio (
              id              INT         NOT NULL AUTO_INCREMENT,
              nome            VARCHAR(45) NULL,
              estado          CHAR(2)     NULL,
              fkNotaMunicipal INT         NOT NULL,
              PRIMARY KEY (id),
              CONSTRAINT fkNotaMunicipal
                FOREIGN KEY (fkNotaMunicipal)
                REFERENCES notaMunicipal (id)
            )
        """);

        connection.execute("""
            CREATE TABLE IF NOT EXISTS nivelAcesso (
              id   INT         NOT NULL AUTO_INCREMENT,
              nome VARCHAR(45) NULL,
              PRIMARY KEY (id)
            )
        """);

        // NOVA TABELA: cursinho
        connection.execute("""
            CREATE TABLE IF NOT EXISTS cursinho (
              id            INT         NOT NULL AUTO_INCREMENT,
              nome          VARCHAR(45) NOT NULL,
              cnpj          VARCHAR(45) NOT NULL,
              codigoConvite CHAR(4),
              fkMunicipio   INT,
              PRIMARY KEY (id),
              CONSTRAINT fkMunicipio
                FOREIGN KEY (fkMunicipio)
                REFERENCES municipio (id)
            )
        """);

        // TABELA ALTERADA: usuario — adicionados campos email e fkCursinho
        connection.execute("""
            CREATE TABLE IF NOT EXISTS usuario (
              id            INT          NOT NULL AUTO_INCREMENT,
              nome          VARCHAR(80)  NULL,
              email         VARCHAR(120) NULL,
              senha         VARCHAR(255) NULL,
              dataCriacao   DATETIME     NULL,
              fkNivelAcesso INT          NOT NULL,
              fkCursinho    INT          NOT NULL,
              PRIMARY KEY (id),
              CONSTRAINT fkNivelAcesso
                FOREIGN KEY (fkNivelAcesso)
                REFERENCES nivelAcesso (id),
              CONSTRAINT fkCursinho
                FOREIGN KEY (fkCursinho)
                REFERENCES cursinho (id)
            )
        """);

        // NOVA TABELA: controleNotificacao
        connection.execute("""
            CREATE TABLE IF NOT EXISTS controleNotificacao (
              id                 INT          NOT NULL AUTO_INCREMENT,
              slack_channel_id   VARCHAR(100) NOT NULL,
              periodo            INT          NOT NULL,
              receberNotificacao VARCHAR(3)   NOT NULL,
              tipoNotificacao    TINYINT(1)   NOT NULL,
              notificarSistema   TINYINT(1)   NOT NULL,
              notificarEmail     TINYINT(1)   NOT NULL,
              encerrarSessao     TINYINT(1)   NOT NULL,
              ativo              TINYINT(1)   NOT NULL DEFAULT 1,
              usuario_id         INT          NULL,
              PRIMARY KEY (id)
            )
        """);

        // NOVA TABELA: controleNotificacaoEnvio
        connection.execute("""
            CREATE TABLE IF NOT EXISTS controleNotificacaoEnvio (
              id                    INT         NOT NULL AUTO_INCREMENT,
              fkControleNotificacao INT         NOT NULL,
              tipo                  VARCHAR(80) NOT NULL,
              dataEnvio             DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
              PRIMARY KEY (id),
              UNIQUE KEY uk_controle_tipo (fkControleNotificacao, tipo)
            )
        """);

        connection.execute("""
            CREATE TABLE IF NOT EXISTS simulado (
              id                 INT         NOT NULL AUTO_INCREMENT,
              nomeSimulado       VARCHAR(45) NULL,
              quantidadeQuestoes INT         NULL,
              fkUsuario          INT         NOT NULL,
              PRIMARY KEY (id),
              CONSTRAINT fkUsuario
                FOREIGN KEY (fkUsuario)
                REFERENCES usuario (id)
            )
        """);

        // NOVA TABELA: logAcesso (substituiu a tabela log genérica)
        connection.execute("""
            CREATE TABLE IF NOT EXISTS logAcesso (
              id          INT         NOT NULL AUTO_INCREMENT,
              ip          VARCHAR(45) NULL,
              dataCriacao DATETIME    NULL,
              fkUsuario   INT         NOT NULL,
              PRIMARY KEY (id),
              CONSTRAINT fkLogAcessoUsuario
                FOREIGN KEY (fkUsuario)
                REFERENCES usuario (id)
            )
        """);

        connection.execute("""
          CREATE TABLE IF NOT EXISTS log (
            id          INT          NOT NULL AUTO_INCREMENT,
            mensagem    VARCHAR(999) NULL,
            nivel       VARCHAR(20)  NULL,
            ip          VARCHAR(45)  NULL,
            dataCriacao DATETIME     NULL,
            PRIMARY KEY (id)
          )
        """);

        connection.execute("""
            CREATE TABLE IF NOT EXISTS areaConhecimento (
              id    INT         NOT NULL,
              nome  VARCHAR(45) NULL,
              sigla CHAR(10)    NULL,
              PRIMARY KEY (id)
            )
        """);

        connection.execute("""
            CREATE TABLE IF NOT EXISTS habilidade (
              id                 INT          NOT NULL,
              numero             CHAR(20)     NULL,
              descricao          VARCHAR(455) NULL,
              fkAreaConhecimento INT          NOT NULL,
              PRIMARY KEY (id),
              CONSTRAINT fkAreaConhecimento
                FOREIGN KEY (fkAreaConhecimento)
                REFERENCES areaConhecimento (id)
            )
        """);

        connection.execute("""
            CREATE TABLE IF NOT EXISTS parametroTri (
              id         INT           NOT NULL,
              nivel      VARCHAR(45)   NULL,
              parametroA DECIMAL(10,2) NULL,
              parametroB DECIMAL(10,2) NULL,
              parametroC DECIMAL(10,2) NULL,
              PRIMARY KEY (id)
            )
        """);

        connection.execute("""
            CREATE TABLE IF NOT EXISTS questao (
              codigoItem     VARCHAR(20) NOT NULL,
              anoExame       YEAR        NOT NULL,
              fkHabilidade   INT         NOT NULL,
              fkParametroTri INT         NOT NULL,
              PRIMARY KEY (codigoItem, anoExame),
              CONSTRAINT fkHabilidade
                FOREIGN KEY (fkHabilidade)
                REFERENCES habilidade (id),
              CONSTRAINT fkParametroTri
                FOREIGN KEY (fkParametroTri)
                REFERENCES parametroTri (id)
            )
        """);

        connection.execute("""
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
            )
        """);

        info("[] - Tabelas criadas com sucesso!");

        // -------------------------------------------------------
        // INSERTS
        // -------------------------------------------------------

        connection.execute("""
            INSERT INTO notaMunicipal (matematica, codigosELinguagens, cienciasDaNatureza, cienciasHumanas)
            SELECT 400, 350, 300, 200
            WHERE NOT EXISTS (SELECT 1 FROM notaMunicipal WHERE id = 1)
        """);

        connection.execute("""
            INSERT INTO municipio (nome, fkNotaMunicipal)
            SELECT 'São Paulo', 1
            WHERE NOT EXISTS (SELECT 1 FROM municipio WHERE id = 1)
        """);

        connection.execute("""
            INSERT INTO nivelAcesso (nome)
            SELECT 'Administrador'
            WHERE NOT EXISTS (SELECT 1 FROM nivelAcesso WHERE id = 1)
        """);

        connection.execute("""
            INSERT INTO nivelAcesso (nome)
            SELECT 'Coordenador'
            WHERE NOT EXISTS (SELECT 1 FROM nivelAcesso WHERE id = 2)
        """);

        connection.execute("""
            INSERT INTO nivelAcesso (nome)
            SELECT 'Professor'
            WHERE NOT EXISTS (SELECT 1 FROM nivelAcesso WHERE id = 3)
        """);

        connection.execute("""
            INSERT INTO cursinho (nome, cnpj, codigoConvite, fkMunicipio)
            SELECT 'Cursinho Progressão', '12345678000199', 'XPTO', 1
            WHERE NOT EXISTS (SELECT 1 FROM cursinho WHERE id = 1)
        """);

        connection.execute("""
            INSERT INTO cursinho (nome, cnpj, codigoConvite, fkMunicipio)
            SELECT 'Academia do Saber', '98765432000188', 'XPTI', 1
            WHERE NOT EXISTS (SELECT 1 FROM cursinho WHERE id = 2)
        """);

        connection.execute("""
            INSERT INTO usuario (nome, email, senha, dataCriacao, fkNivelAcesso, fkCursinho)
            SELECT 'Carlos Silva', 'carlos@gmail.com', 'senhaCriptografada123', NOW(), 3, 1
            WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'carlos@gmail.com')
        """);

        connection.execute("""
            INSERT INTO usuario (nome, senha, dataCriacao, fkNivelAcesso, fkCursinho)
            SELECT 'Ana Souza', 'senhaCriptografada456', NOW(), 1, 1
            WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE nome = 'Ana Souza')
        """);

        connection.execute("""
            INSERT INTO usuario (nome, senha, dataCriacao, fkNivelAcesso, fkCursinho)
            SELECT 'Marcos Oliveira', 'senhaCriptografada789', NOW(), 2, 1
            WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE nome = 'Marcos Oliveira')
        """);

        connection.execute("""
            INSERT INTO usuario (nome, senha, dataCriacao, fkNivelAcesso, fkCursinho)
            SELECT 'Fernanda Lima', 'senhaCriptografada999', NOW(), 3, 2
            WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE nome = 'Fernanda Lima')
        """);

        info("Banco de dados lido com sucesso!");
    }
}