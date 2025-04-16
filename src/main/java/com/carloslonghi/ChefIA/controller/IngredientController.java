package com.carloslonghi.ChefIA.controller;

import com.carloslonghi.ChefIA.model.Ingredient;
import com.carloslonghi.ChefIA.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ingredient")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @PostMapping
    public ResponseEntity<Ingredient> create(@RequestBody Ingredient ingredient) {
        Ingredient ingredientCreated = ingredientService.create(ingredient);
        return ResponseEntity.ok(ingredientCreated);
    }

    @GetMapping
    public ResponseEntity<List<Ingredient>> getAll() {
        List<Ingredient> ingredients = ingredientService.getAll();
        return ResponseEntity.ok(ingredients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> getById(@PathVariable Long id) {
        Ingredient ingredient = ingredientService.getById(id);
        return ResponseEntity.ok(ingredient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ingredient> update(@PathVariable Long id, @RequestBody Ingredient ingredient) {
        Ingredient ingredientUpdated = ingredientService.update(id, ingredient);
        if (ingredientUpdated != null){
            return ResponseEntity.ok(ingredientUpdated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        Ingredient ingredientFound = ingredientService.getById(id);
        if (ingredientFound != null) {
            ingredientService.delete(id);
            return ResponseEntity.ok("Ingrediente deletado");
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/test")
    public Ingredient testMapping(@RequestBody Ingredient ingredient) {
        System.out.println("Category: " + ingredient.getCategory());
        return ingredient;
    }
}
