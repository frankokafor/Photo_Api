package com.frankokafor.rest.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
	
	 Contact contact = new Contact(
	            "Frank Okafor",
	            "07060999234",
	            "frankebukaokafor@gmail.com"
	    );

	    ApiInfo apiInfo = new ApiInfo(
	            "Frank Okafor Photo Application Api",
	            "This pages documents Frank's Photo Application RESTful Web Service endpoints",
	            "1.0",
	            "free to use",
	            contact,
	            "Apache 2.0",
	            "http://www.apache.org/licenses/LICENSE-2.0",
	            Collections.emptyList());

	    @Bean
	    public Docket api() {
	        return new Docket(DocumentationType.SWAGGER_2)
	                .protocols(new HashSet<>(Arrays.asList("HTTPs", "HTTP")))
	                .apiInfo(apiInfo)
	                .select()
	                .apis(RequestHandlerSelectors.any())
	                .paths(PathSelectors.any())
	                .build();
	    }

}
