package com.beprocces.auth.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class PermissionsConverter  implements AttributeConverter<List<String> , String> {

    private static final String SEPARATOR = "," ;
    @Override
    public String convertToDatabaseColumn(List<String> permissions) {
        // Convertir la liste en une chaîne de caractères séparée par des virgules
        return permissions !=null ? String.join(SEPARATOR , permissions) : "";
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        // Convertir la chaîne en liste en la scindant par la virgule
        return dbData != null ? Arrays.stream(dbData.split(SEPARATOR)).collect(Collectors.toList()) : null;
    }
}
