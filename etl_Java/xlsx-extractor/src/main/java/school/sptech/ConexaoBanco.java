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
        basicDataSource.setUrl("jdbc:h2:mem:datasafe;DB_CLOSE_DELAY=-1;MODE=MySQL");
        basicDataSource.setUsername("datasafe");
        basicDataSource.setPassword("ds@121314");

        this.dataSource = basicDataSource;
    }

    public JdbcTemplate getConnection() {
        return new JdbcTemplate(dataSource);
    }





}
