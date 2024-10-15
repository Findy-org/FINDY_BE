package org.findy.findy_be.common.config;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.findy.findy_be.common.exception.ErrorResponse;
import org.findy.findy_be.common.meta.CustomApiResponse;
import org.findy.findy_be.common.meta.CustomApiResponses;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Value("${server.url}")
	private String serverUrl;

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
			.components(new Components()
				.addSecuritySchemes("bearerAuth", new SecurityScheme()
					.type(Type.HTTP)
					.scheme("bearer")
					.bearerFormat("JWT")
					.in(In.HEADER)
					.name("Authorization"))
				.addSchemas("ErrorResponse", createErrorResponseSchema()))
			.info(apiInfo())
			.addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
			.servers(List.of(new Server().url(serverUrl).description("Dynamic Server URL")));
	}

	private Info apiInfo() {
		return new Info()
			.title("Findy REST API")
			.description("Findy REST API 문서.")
			.version("1.0.0");
	}

	@Bean
	public OperationCustomizer operationCustomizer() {
		return (operation, handlerMethod) -> {
			ApiResponses apiResponses = operation.getResponses();
			if (apiResponses == null) {
				apiResponses = new ApiResponses();
				operation.setResponses(apiResponses);
			}
			handleCustomApiResponses(apiResponses, handlerMethod);
			return operation;
		};
	}

	private void handleCustomApiResponses(ApiResponses apiResponses, HandlerMethod handlerMethod) {
		Method method = handlerMethod.getMethod();
		CustomApiResponses customApiResponses = method.getAnnotation(CustomApiResponses.class);
		if (customApiResponses != null) {
			for (CustomApiResponse customApiResponse : customApiResponses.value()) {
				ApiResponse apiResponse = new ApiResponse();
				apiResponse.setDescription(customApiResponse.description());
				addContent(apiResponse, customApiResponse.error(), customApiResponse.status(),
					customApiResponse.message(), customApiResponse.isArray());
				apiResponses.addApiResponse(String.valueOf(customApiResponse.status()), apiResponse);
			}
		} else {
			CustomApiResponse customApiResponse = method.getAnnotation(CustomApiResponse.class);
			if (customApiResponse != null) {
				ApiResponse apiResponse = new ApiResponse();
				apiResponse.setDescription(customApiResponse.description());
				addContent(apiResponse, customApiResponse.error(), customApiResponse.status(),
					customApiResponse.message(), customApiResponse.isArray());
				apiResponses.addApiResponse(String.valueOf(customApiResponse.status()), apiResponse);
			}
		}
	}

	private void addContent(ApiResponse apiResponse, String error, int status, String message, boolean isArray) {
		Content content = new Content();
		MediaType mediaType = new MediaType();
		Schema<?> schema;
		if (isArray) {
			schema = new Schema<List<ErrorResponse>>()
				.type("array")
				.items(new Schema<ErrorResponse>().$ref("#/components/schemas/ErrorResponse"));
		} else {
			schema = new Schema<ErrorResponse>()
				.$ref("#/components/schemas/ErrorResponse");
		}
		mediaType.schema(schema)
			.example(isArray ? List.of(new ErrorResponse(error, status, message)) :
				new ErrorResponse(error, status, message));
		content.addMediaType("application/json", mediaType);
		apiResponse.setContent(content);
	}

	private Schema<ErrorResponse> createErrorResponseSchema() {
		return new Schema<ErrorResponse>()
			.type("object")
			.properties(Map.of(
				"error", new Schema<String>().type("string"),
				"status", new Schema<Integer>().type("integer"),
				"message", new Schema<String>().type("string")
			));
	}
}