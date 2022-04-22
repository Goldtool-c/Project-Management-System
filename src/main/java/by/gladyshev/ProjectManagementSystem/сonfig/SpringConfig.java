package by.gladyshev.ProjectManagementSystem.сonfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.sql.DataSource;

@Configuration
@ComponentScan("by.gladyshev.ProjectManagementSystem")
@EnableWebMvc
public class SpringConfig implements WebMvcConfigurer {
    private final ApplicationContext context;
    @Autowired
    public SpringConfig(ApplicationContext context)
    {
        this.context = context;
    }
    @Bean
    public SpringResourceTemplateResolver templateResolver()
    {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(context);
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".html");
        return resolver;
    }
    @Bean
    public SpringTemplateEngine templateEngine()
    {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        engine.setEnableSpringELCompiler(true);
        return engine;
    }
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry)
    {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        registry.viewResolver(resolver);
    }
    @Bean
    public DataSource dataSource()
    {
        DriverManagerDataSource dmds= new DriverManagerDataSource();
        dmds.setDriverClassName("org.postgresql.Driver");
        dmds.setUrl("jdbc:postgresql://localhost:5432/ProjectDB");
        dmds.setUsername("postgres");
        dmds.setPassword("123456");

        return dmds;
    }
    @Bean
    public JdbcTemplate jdbcTemplate()
    {
        return new JdbcTemplate(dataSource());
    }
}
