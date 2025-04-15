package com.carloslonghi.ChefIA.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IngredientCategory {

    VEGETABLE("Vegetal", Unit.KILOGRAM),
    FRUIT("Fruta", Unit.KILOGRAM),
    MEAT("Carne", Unit.KILOGRAM),
    FISH("Peixe", Unit.KILOGRAM),
    GRAIN("Grão / Cereal", Unit.KILOGRAM),
    SPICE("Tempero", Unit.KILOGRAM),
    OIL("Óleo", Unit.LITER),
    SAUCE("Molho", Unit.LITER),
    BEVERAGE("Bebida", Unit.LITER),
    OTHER("Outro", Unit.UNIT);

    private final String label;
    private final Unit unit;
}
