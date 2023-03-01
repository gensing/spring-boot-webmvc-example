package com.tensing.boot.security.code;

import jakarta.persistence.AttributeConverter;

public class RoleConverter implements AttributeConverter<RoleCode, String> {
    @Override
    public String convertToDatabaseColumn(RoleCode attribute) {
        return attribute.getDesc();
    }

    @Override
    public RoleCode convertToEntityAttribute(String dbData) {
        return RoleCode.findByDesc(dbData);
    }
}
