package com.carloslonghi.ChefIA.controller;

import com.carloslonghi.ChefIA.model.Ingredient;
import com.carloslonghi.ChefIA.service.GeminiAIService;
import com.carloslonghi.ChefIA.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gemini")
@RequiredArgsConstructor
public class RecipeController {

    private final IngredientService ingredientService;
    private final GeminiAIService geminiAIService;

    @GetMapping
    public Mono<ResponseEntity<String>> generateRecipe() {
        List<Ingredient> ingredients = ingredientService.getAll();
        return geminiAIService.generateRecipe(ingredients)
                .map(recipe -> ResponseEntity.ok(recipe))
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }
}
