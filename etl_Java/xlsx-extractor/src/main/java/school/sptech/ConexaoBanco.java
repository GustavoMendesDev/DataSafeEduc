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
        basicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        basicDataSource.setUrl("jdbc:mysql://localhost:3306/datasafe?useSSL=true&serverTimezone=America/Sao_Paulo&allowPublicKeyRetrieval=true");
        basicDataSource.setUsername("root");
        basicDataSource.setPassword("#0612@Gm");

        this.dataSource = basicDataSource;
    }

    public JdbcTemplate getConnection() {
        return new JdbcTemplate(dataSource);
    }





}
