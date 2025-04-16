package com.carloslonghi.ChefIA.controller;

import com.carloslonghi.ChefIA.model.Ingredient;
import com.carloslonghi.ChefIA.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Optional<Ingredient>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ingredientService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ingredient> update(@PathVariable Long id, @RequestBody Ingredient ingredient) {
        return ingredientService.getById(id)
                .map(ingredientFound -> {
                    ingredient.setId(ingredientFound.getId());
                    Ingredient ingredientUpdated = ingredientService.update(ingredient);
                    return ResponseEntity.ok(ingredientUpdated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        Optional<Ingredient> ingredientFound = ingredientService.getById(id);
        if (ingredientFound.isPresent()) {
            ingredientService.delete(id);
            return ResponseEntity.ok("Ingrediente deletado");
        }
        return ResponseEntity.notFound().build();
    }
}
