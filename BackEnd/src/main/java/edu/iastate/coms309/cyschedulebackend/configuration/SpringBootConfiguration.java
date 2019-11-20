package edu.iastate.coms309.cyschedulebackend.configuration;

import com.google.gson.*;
import edu.iastate.coms309.cyschedulebackend.Utils.SpringfoxJsonToGsonAdapter;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;

@Configuration
@EnableCaching
@EnableSwagger2
public class SpringBootConfiguration {

    @Bean
    public GsonHttpMessageConverter gsonHttpMessageConverter() {
        GsonHttpMessageConverter converter = new GsonHttpMessageConverter();

        Gson gson = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new JsonDeserializer<ZonedDateTime>() {
            @Override
            public ZonedDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString());
            }
        }).create();
        converter.setGson(gson);

        return converter;
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * @return 生成文档说明信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Sanchi test API文档")
                .description("接口文档")
                .version("1.0.0").build();
    }

    private Gson gson() {
        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Json.class, new SpringfoxJsonToGsonAdapter());
        return builder.excludeFieldsWithoutExposeAnnotation().create();
    }
}
