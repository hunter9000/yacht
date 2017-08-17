package yacht;

import yacht.interceptor.JWTInterceptor;
import yacht.interceptor.RolePermissionInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import yacht.interceptor.YahtzeeGameOwnerInterceptor;

@EnableJpaRepositories(basePackages = "yacht.repository")
@EntityScan(basePackages = "yacht")
@SpringBootApplication
public class Application extends WebMvcConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public JWTInterceptor JWTInterceptor() {
        return new JWTInterceptor();
    }

    @Bean
    public RolePermissionInterceptor RolePermissionInterceptor() { return new RolePermissionInterceptor(); }

    @Bean
    public YahtzeeGameOwnerInterceptor YahtzeeGameOwnerInterceptor() {
        return new YahtzeeGameOwnerInterceptor();
    }

//    @Bean
//    public SecurityManager SecurityManager() {
//        return new SecurityManager();
//    }

    /** Creates the password encoder object. This is used to .encode() a raw string. The results of that are a hashed
     *  string that includes a random salt. That is what's stored in the user table. To compare a raw input password
     *  to the stored hash, use .matches(raw, hashed) */
    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder(10/*, random*/);
        return encoder;
    }

    /** Adds JWTInterceptor to the list of interceptors. The handler methods of JWTInterceptor will be called for any
     *  api calls, except those in excludePathPatterns().
     *  AdminInterceptor verifies that the user has the admin role before accessing the given paths. */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(JWTInterceptor()).addPathPatterns("/api/**").excludePathPatterns("/api/authenticate/");
        registry.addInterceptor(RolePermissionInterceptor()).addPathPatterns("/api/**");        // intercepts all paths, only applies to methods with the @RolePermissions annotation
        registry.addInterceptor(YahtzeeGameOwnerInterceptor()).addPathPatterns("/api/**");        // intercepts all paths, only applies to methods with the @YahtzeeGameOwnerRequired annotation
    }

    /** Swagger info */
    @Bean
    public Docket newsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("api")
                .apiInfo(apiInfo())
                .select()
                .paths((str) -> { return str.matches("/api/.*"); })
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Pathfinder Toolkit")
                .description("API documentation")
//                .termsOfServiceUrl("http://www-03.ibm.com/software/sla/sladb.nsf/sla/bm?Open")
//                .contact("Niklas Heidloff")
//                .license("Apache License Version 2.0")
//                .licenseUrl("https://github.com/IBM-Bluemix/news-aggregator/blob/master/LICENSE")
                .version("1.0")
                .build();
    }

}
