package com.java.companyhouse.config;

import com.java.companyhouse.controller.AbstractBatchController;
import com.java.companyhouse.controller.AbstractNestedBatchController;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Configuration
public class SpringDocConfig {

    @Bean
    public OperationCustomizer resolveGenericRequestBody() {
        return (operation, handlerMethod) -> {
            Class<?> controllerClass = handlerMethod.getBeanType();
            Type superclass = controllerClass.getGenericSuperclass();

            if (!(superclass instanceof ParameterizedType pt)) return operation;

            Type[] args = pt.getActualTypeArguments();
            if (args.length == 0 || !(args[0] instanceof Class<?> entityClass)) return operation;

            String entityName = entityClass.getSimpleName();

            Schema<?> entityRef = new Schema<>().$ref("#/components/schemas/" + entityName);

            Schema<?> snapshotSchema = new Schema<>().type("object").addProperty("corporateNumber", new Schema<>().type("string").minLength(13).maxLength(13).description("corporateNumber must be 13 characters long")).addProperty("entities", new ArraySchema().items(entityRef));

            Schema<?> requestSchema;

            if (AbstractBatchController.class.isAssignableFrom(controllerClass)) {
                requestSchema = new Schema<>().type("object").addProperty("companies", new ArraySchema().items(snapshotSchema));
            } else if (AbstractNestedBatchController.class.isAssignableFrom(controllerClass)) {
                requestSchema = new Schema<>().type("object").addProperty("snapshots", new ArraySchema().items(snapshotSchema));
            } else {
                return operation;
            }

            operation.setRequestBody(new RequestBody().required(true).content(new Content().addMediaType("application/json", new MediaType().schema(requestSchema))));

            return operation;
        };
    }
}