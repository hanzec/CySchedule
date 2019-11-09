package edu.iastate.coms309.cyschedulebackend.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.iastate.coms309.cyschedulebackend.Utils.SpringfoxJsonToGsonAdapter;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import springfox.documentation.spring.web.json.Json;

@Configuration
@EnableCaching
public class SpringBootConfiguration {

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
