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

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SpringDocConfig {

    @Bean
    public OperationCustomizer resolveGenericRequestBody() {
        return (operation, handlerMethod) -> {
            Class<?> controllerClass = handlerMethod.getBeanType();
            Type superclass = controllerClass.getGenericSuperclass();

            if (!(superclass instanceof ParameterizedType pt)) return operation;
            Type[] args = pt.getActualTypeArguments();
            if (args.length == 0) return operation;

            Schema<?> requestSchema = buildRequestSchema(controllerClass, args);
            if (requestSchema == null) return operation;

            operation.setRequestBody(buildRequestBody(requestSchema));
            return operation;
        };
    }

    private Schema<?> buildRequestSchema(Class<?> controllerClass, Type[] args) {
        if (AbstractBatchController.class.isAssignableFrom(controllerClass)) {
            return buildBatchSchema(args);
        } else if (AbstractNestedBatchController.class.isAssignableFrom(controllerClass)) {
            return buildNestedBatchSchema(args);
        }
        return null;
    }

    private Schema<?> buildBatchSchema(Type[] args) {
        if (!(args[0] instanceof Class<?> entityClass)) return null;

        Schema<?> entityRef = new Schema<>().$ref("#/components/schemas/" + entityClass.getSimpleName());
        Schema<?> snapshotSchema = new Schema<>()
                .type("object")
                .addProperty("corporateNumber", new Schema<>().type("string").minLength(13).maxLength(13)
                        .description("corporateNumber must be 13 characters long"))
                .addProperty("entities", new ArraySchema().items(entityRef));

        return new Schema<>().type("object").addProperty("companies", new ArraySchema().items(snapshotSchema));
    }

    private Schema<?> buildNestedBatchSchema(Type[] args) {
        if (args.length < 2) return null;
        if (!(args[0] instanceof Class<?> snapshotClass)) return null;
        if (!(args[1] instanceof Class<?> dtoClass)) return null;

        Schema<?> dtoRef = new Schema<>().$ref("#/components/schemas/" + dtoClass.getSimpleName());
        Schema<?> snapshotSchema = new Schema<>().type("object");

        for (Field field : getAllFields(snapshotClass)) {
            addFieldToSchema(snapshotSchema, field, dtoRef);
        }

        return new Schema<>().type("object").addProperty("snapshots", new ArraySchema().items(snapshotSchema));
    }

    private void addFieldToSchema(Schema<?> snapshotSchema, Field field, Schema<?> dtoRef) {
        field.setAccessible(true);
        String fieldName = field.getName();
        Class<?> fieldType = field.getType();

        if (fieldType == String.class) {
            if (fieldName.equals("corporateNumber")) {
                snapshotSchema.addProperty(fieldName, new Schema<>().type("string").minLength(13).maxLength(13)
                        .description("corporateNumber must be 13 characters long"));
            } else {
                snapshotSchema.addProperty(fieldName, new Schema<>().type("string"));
            }
        } else if (fieldType == List.class) {
            snapshotSchema.addProperty(fieldName, new ArraySchema().items(dtoRef));
        }
    }

    private RequestBody buildRequestBody(Schema<?> requestSchema) {
        return new RequestBody()
                .required(true)
                .content(new Content().addMediaType("application/json", new MediaType().schema(requestSchema)));
    }

    private List<Field> getAllFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields()).toList();
    }
}
