package com.carloslonghi.ChefIA.service;

import com.carloslonghi.ChefIA.model.Ingredient;
import com.carloslonghi.ChefIA.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public Ingredient create(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    public List<Ingredient> getAll() {
        return ingredientRepository.findAll();
    }

    public Optional<Ingredient> getById(Long id) {
        return ingredientRepository.findById(id);
    }

    public Ingredient update(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    public void delete(Long id) {
        Optional<Ingredient> ingredientExist = ingredientRepository.findById(id);
        if (ingredientExist.isPresent()) {
            ingredientRepository.deleteById(id);
        }
    }
}
