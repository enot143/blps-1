package coursera.config.bitronix;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.TransactionManagerServices;
import bitronix.tm.resource.jdbc.PoolingDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class BitronixConfig{

    //
    @Bean(name = "bitronixTransactionManager")
    @DependsOn
    public BitronixTransactionManager bitronixTransactionManager() throws Throwable {
        bitronix.tm.Configuration configuration = TransactionManagerServices.getConfiguration();
        configuration.setJournal(null);
        BitronixTransactionManager bitronixTransactionManager = TransactionManagerServices.getTransactionManager();
        bitronixTransactionManager.setTransactionTimeout(10000);
        return bitronixTransactionManager;
    }
    @Bean(name = "transactionManager")
    @DependsOn({"bitronixTransactionManager"})
    public PlatformTransactionManager transactionManager(TransactionManager bitronixTransactionManager) throws Throwable {
        return new JtaTransactionManager(bitronixTransactionManager);
    }
    @Bean(name = "PgDataSource")
    public DataSource primaryMySqlDataSource() {
        String user = "";
        String password = "";
        String url = "jdbc:postgresql://pg:5432/studs";
        PoolingDataSource bitronixDataSourceBean = new PoolingDataSource();
        bitronixDataSourceBean.setMaxPoolSize(5);
        bitronixDataSourceBean.setUniqueName("primaryPgDataSourceResource");
        bitronixDataSourceBean.setClassName("org.postgresql.xa.PGXADataSource");
        bitronixDataSourceBean.setAllowLocalTransactions(true);
        Properties properties = new Properties();
        properties.put("user",  user);
        properties.put("password",  password);
        properties.put("url", url);
        bitronixDataSourceBean.setDriverProperties(properties);
        return bitronixDataSourceBean;
    }

}