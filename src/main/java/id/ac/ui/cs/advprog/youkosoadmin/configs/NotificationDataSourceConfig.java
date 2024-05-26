package id.ac.ui.cs.advprog.youkosoadmin.configs;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableJpaRepositories(
        basePackages = "id.ac.ui.cs.advprog.youkosoadmin.modules.notification",
        entityManagerFactoryRef = "notificationEntityManagerFactory",
        transactionManagerRef = "notificationTransactionManager"
)
@EntityScan(basePackages = "id.ac.ui.cs.advprog.youkosoadmin.models.notification")
public class NotificationDataSourceConfig {

    @Bean(name = "notificationDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.notification")
    public DataSource notificationDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "notificationEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean notificationEntityManagerFactory(
            @Qualifier("notificationDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setDataSource(dataSource);
        em.setPackagesToScan("id.ac.ui.cs.advprog.youkosoadmin.models.notification");
        return em;
    }

    @Bean(name = "notificationTransactionManager")
    public PlatformTransactionManager notificationTransactionManager(
            @Qualifier("notificationEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactory.getObject()));
    }
}