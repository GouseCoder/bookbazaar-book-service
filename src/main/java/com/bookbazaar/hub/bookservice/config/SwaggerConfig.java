package com.bookbazaar.hub.bookservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bookbazaar.hub.bookservice.utils.SwaggerConstants;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {
	
	@Bean
	public OpenAPI springOpenAPI() {

		return new OpenAPI()

				.info(new Info().title(SwaggerConstants.TITLE)
						.description(SwaggerConstants.DESCRIPTION)
						.version(SwaggerConstants.VERSION1)
						.termsOfService(SwaggerConstants.TERMS_OF_SERVICE)
						.contact(new Contact().name(SwaggerConstants.CONTACT_NAME).email(SwaggerConstants.CONTACT_EMAIL)
								.url(SwaggerConstants.CONTACT_URL))
						.license(new License().name(SwaggerConstants.LISCENCE_NAME).url(SwaggerConstants.LISCENCE_URL)));

	}

}
