package com.carloslonghi.ChefIA.model;

import com.carloslonghi.ChefIA.model.enums.IngredientCategory;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "ingredient")
@RequiredArgsConstructor
@Data
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IngredientCategory category;

    private Double quantity;

    private LocalDate validate;
}
