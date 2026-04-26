package school.sptech;

import org.springframework.jdbc.core.JdbcTemplate;
import school.sptech.enums.SiglaEnum;


public class TabelasBanco {

    public static void tabelasBanco() {

        ConexaoBanco dbConnectionProvider = new ConexaoBanco();
        JdbcTemplate connection = dbConnectionProvider.getConnection();


        connection.execute("""
            CREATE TABLE IF NOT EXISTS notaMunicipal (
              id                 INT            NOT NULL AUTO_INCREMENT,
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

        connection.execute("""
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

        connection.execute("""
            CREATE TABLE IF NOT EXISTS logAcesso (
              id          INT         NOT NULL AUTO_INCREMENT,
              ip          VARCHAR(45) NULL,
              dataCriacao DATETIME    NULL,
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
              id         INT            NOT NULL ,
              nivel      VARCHAR(45)    NULL,
              parametroA DECIMAL(5,2)  NULL,
              parametroB DECIMAL(5,2)  NULL,
              parametroC DECIMAL(5,2)  NULL,
              PRIMARY KEY (id)
            )
        """);

        connection.execute("""
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
        System.out.println("[] - Tabelas criadas com sucesso!");


        System.out.println("Banco de dados lido com sucesso!");
    }

    public void inserirNotasMunicipio() {

    }



}
