package com.carloslonghi.ChefIA.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Unit {

    UNIT("Unidade"),
    KILOGRAM("Kg"),
    LITER("Litro");

    private final String label;
}
