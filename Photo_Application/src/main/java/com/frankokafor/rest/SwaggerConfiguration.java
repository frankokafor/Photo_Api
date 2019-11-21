package com.frankokafor.rest;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfiguration implements WebMvcConfigurer  {

	Contact contact = new Contact("Frank Okafor"
								, "07060999234"
								, "frankebukaokafor@gmail.com"
								);

	ApiInfo apiInfo = 
			new ApiInfo("Frank Okafor Photo Application Api"
					,"This pages documents Frank's Photo Application RESTful Web Service endpoints"
					, "1.0"
					, "free to use"
					,contact
					, "Apache 2.0"
					, "http://www.apache.org/licenses/LICENSE-2.0"
					, Collections.emptyList());

	@Bean
	public Docket api() {
		return new 
				Docket(DocumentationType.SWAGGER_2)
				.protocols(new HashSet<>(Arrays.asList("HTTPs", "HTTP")))
				.apiInfo(apiInfo)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.frankokafor.rest"))
				.paths(PathSelectors.any())
				.build();
	}
	
	 @Override
	    public void addCorsMappings(CorsRegistry registry) {

	        registry
	                .addMapping("/**")
	                .allowedMethods("*")
	                .allowedOrigins("*");

	    }
	    
	    @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry
	                .addResourceHandler("/profile_image/**")
	             //  .addResourceLocations("file:/opt/profile_image/")
	                .addResourceLocations("file:///C:/enterprise_uploads/profile_image/")
	                .setCachePeriod(3600)
	                .resourceChain(true);
	        registry
            .addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/");

	        registry
            .addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/");
	    }

}
