package school.sptech;

import org.springframework.jdbc.core.JdbcTemplate;
import school.sptech.dto.NotaMunicipal;
import school.sptech.dto.Habilidade;
import school.sptech.dto.Questao;
import school.sptech.dto.Dificuldade;



public class TabelasBanco {

    public static void tabelasBanco() {
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        JdbcTemplate connection = conexaoBanco.getConnection();


        connection.execute("""
                    CREATE TABLE IF NOT EXISTS `Nota_Municipal` (
                      `id`                    INT            NOT NULL AUTO_INCREMENT,
                      `matematica`            DECIMAL(10,2)  NULL,
                      `codigosELinguagens`    DECIMAL(10,2)  NULL,
                      `cienciasDaNatureza`    DECIMAL(10,2)  NULL,
                      `cienciasHumanas`       DECIMAL(10,2)  NULL,
                      PRIMARY KEY (`id`)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """);

        connection.execute("""
                    CREATE TABLE IF NOT EXISTS `municipio` (
                      `idMunicipio`           INT            NOT NULL AUTO_INCREMENT,
                      `nome`                  VARCHAR(45)    NULL,
                      `estado`                CHAR(2)        NULL,
                      `Nota_Municipal_id`     INT            NOT NULL,
                      PRIMARY KEY (`idMunicipio`),
                      CONSTRAINT `fk_municipio_nota`
                        FOREIGN KEY (`Nota_Municipal_id`)
                        REFERENCES `Nota_Municipal` (`id`)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """);

        connection.execute("""
                    CREATE TABLE IF NOT EXISTS `nivel_acesso` (
                      `idnivel_acesso`        INT            NOT NULL AUTO_INCREMENT,
                      `nome`                  VARCHAR(45)    NULL,
                      PRIMARY KEY (`idnivel_acesso`)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """);

        connection.execute("""
                    CREATE TABLE IF NOT EXISTS `usuario` (
                      `idUsuario`                   INT            NOT NULL AUTO_INCREMENT,
                      `nome`                        VARCHAR(80)    NULL,
                      `senha`                       VARCHAR(255)   NULL,
                      `dataCriacao`                 DATETIME       NULL,
                      `nivel_acesso_idnivel_acesso` INT            NOT NULL,
                      `municipio_idMunicipio`       INT            NOT NULL,
                      PRIMARY KEY (`idUsuario`),
                      CONSTRAINT `fk_usuario_nivel`
                        FOREIGN KEY (`nivel_acesso_idnivel_acesso`)
                        REFERENCES `nivel_acesso` (`idnivel_acesso`),
                      CONSTRAINT `fk_usuario_municipio`
                        FOREIGN KEY (`municipio_idMunicipio`)
                        REFERENCES `municipio` (`idMunicipio`)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """);

        connection.execute("""
                    CREATE TABLE IF NOT EXISTS `simulado` (
                      `idSimulado`          INT            NOT NULL AUTO_INCREMENT,
                      `quantidadeQuestoes`  INT            NULL,
                      `nomeSimulado`        VARCHAR(45)    NULL,
                      `usuario_idUsuario`   INT            NOT NULL,
                      PRIMARY KEY (`idSimulado`),
                      CONSTRAINT `fk_simulado_usuario`
                        FOREIGN KEY (`usuario_idUsuario`)
                        REFERENCES `usuario` (`idUsuario`)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """);

        connection.execute("""
                    CREATE TABLE IF NOT EXISTS `log_acesso` (
                      `idlog_acesso`  INT            NOT NULL AUTO_INCREMENT,
                      `ip`            VARCHAR(45)    NULL,
                      `dataCriacao`   DATETIME       NULL,
                      PRIMARY KEY (`idlog_acesso`)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """);

        connection.execute("""
                    CREATE TABLE IF NOT EXISTS `habilidade` (
                      `idHabilidade`                    INT          NOT NULL AUTO_INCREMENT,
                      `numero`                          CHAR(20)     NULL,
                      `descricao`                       VARCHAR(455) NULL,
                      `area_conhecimento_id_AreaConhecimento` INT    NOT NULL,
                      PRIMARY KEY (`idHabilidade`),
                      CONSTRAINT `fk_habilidade_area`
                        FOREIGN KEY (`area_conhecimento_id_AreaConhecimento`)
                        REFERENCES `area_conhecimento` (`id_AreaConhecimento`)
                    )
                """);

        connection.execute("""
                    CREATE TABLE IF NOT EXISTS `dificuldade` (
                      `idDificuldade`  INT            NOT NULL AUTO_INCREMENT,
                      `nivel`          VARCHAR(45)    NULL,
                      `parametro_a`    DECIMAL(10,4)  NULL,
                      `parametro_b`    DECIMAL(10,4)  NULL,
                      `parametro_c`    DECIMAL(10,4)  NULL,
                      PRIMARY KEY (`idDificuldade`)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """);

        connection.execute("""
                    CREATE TABLE IF NOT EXISTS `habilidade` (
                      `idHabilidade`  INT            NOT NULL AUTO_INCREMENT,
                      `numero`        CHAR(20)       NULL,
                      `descricao`     VARCHAR(455)   NULL,
                      PRIMARY KEY (`idHabilidade`)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """);

        connection.execute("""
                    CREATE TABLE IF NOT EXISTS `questao` (
                      `idQuestao`                     INT            NOT NULL AUTO_INCREMENT,
                      `codigoItem`                    VARCHAR(20)    NULL,
                      `ano_exame`                     YEAR           NULL,
                      `habilidade_idHabilidade`       INT            NOT NULL,
                      `dificuldade_idDificuldade`     INT            NOT NULL,
                      PRIMARY KEY (`idQuestao`),
                      CONSTRAINT `fk_questao_habilidade`
                        FOREIGN KEY (`habilidade_idHabilidade`)
                        REFERENCES `habilidade` (`idHabilidade`),
                      CONSTRAINT `fk_questao_dificuldade`
                        FOREIGN KEY (`dificuldade_idDificuldade`)
                        REFERENCES `dificuldade` (`idDificuldade`)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """);

        connection.execute("""
                    CREATE TABLE IF NOT EXISTS `questao_has_simulado` (
                      `questao_idQuestao`   INT  NOT NULL,
                      `simulado_idSimulado` INT  NOT NULL,
                      PRIMARY KEY (`questao_idQuestao`, `simulado_idSimulado`),
                      CONSTRAINT `fk_qhs_questao`
                        FOREIGN KEY (`questao_idQuestao`)
                        REFERENCES `questao` (`idQuestao`),
                      CONSTRAINT `fk_qhs_simulado`
                        FOREIGN KEY (`simulado_idSimulado`)
                        REFERENCES `simulado` (`idSimulado`)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """);

        System.out.println("Banco de dados lido com sucesso!");
    }

    public void inserirNotasMunicipio() {

    }



}
