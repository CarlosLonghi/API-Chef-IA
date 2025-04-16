package com.carloslonghi.ChefIA.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Unit {

    UNIT("Unidade"),
    KILOGRAM("Kg"),
    LITER("Litro");

    private final String label;
}
