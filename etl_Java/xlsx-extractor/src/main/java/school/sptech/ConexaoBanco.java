package school.sptech;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;


public class ConexaoBanco {

    private final DataSource dataSource;

    private static final ConexaoBanco CONEXAO_BANCO = new ConexaoBanco();

    public static final JdbcTemplate CONEXAO = CONEXAO_BANCO.getConnection();

    public ConexaoBanco() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(System.getenv("DB_HOST"));
        basicDataSource.setUsername(System.getenv("DB_USER"));
        basicDataSource.setPassword(System.getenv("DB_PASSWORD"));

        this.dataSource = basicDataSource;
    }

    public JdbcTemplate getConnection() {
        return new JdbcTemplate(dataSource);
    }





}
