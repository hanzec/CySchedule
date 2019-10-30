package edu.iastate.coms309.cyschedulebackend.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.iastate.coms309.cyschedulebackend.Service.AccountService;
import edu.iastate.coms309.cyschedulebackend.Service.EventService;
import edu.iastate.coms309.cyschedulebackend.Service.PermissionService;
import edu.iastate.coms309.cyschedulebackend.Service.UserTokenService;
import edu.iastate.coms309.cyschedulebackend.Utils.SpringfoxJsonToGsonAdapter;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.PushBuilder;

@EnableAsync
@Configuration
@EnableCaching
public class SpringBootConfiguration {

    @Bean
    public EventService eventService(){return new EventService();}

    @Bean
    public AccountService accountService(){ return new AccountService(); }

    @Bean
    public UserTokenService userTokenService(){return new UserTokenService();}

    @Bean
    public PermissionService permissionService(){return new PermissionService();}

    @Bean
    public GsonHttpMessageConverter gsonHttpMessageConverter() {
        GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
        converter.setGson(gson());
        return converter;
    }

    private Gson gson() {
        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Json.class, new SpringfoxJsonToGsonAdapter());
        return builder.excludeFieldsWithoutExposeAnnotation().create();
    }
}
