package com.tensing.boot.common;

import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.snippet.Attributes.Attribute;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

// rest-doc 필드 벨리데이션을 붙여주는 클래스
public class ConstrainedFields {

    private ConstraintDescriptions constraintDescriptions;

    public ConstrainedFields(Class<?> clazz) {
        this.constraintDescriptions = new ConstraintDescriptions(clazz);
    }

    public FieldDescriptor withPath(String path) {
        return fieldWithPath(path)
                .attributes(new Attribute("constraint", this.constraintDescriptions.descriptionsForProperty(path)));

    }
}
