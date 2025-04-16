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

    public Ingredient getById(Long id) {
        Optional<Ingredient> ingredient = ingredientRepository.findById(id);
        return ingredient.orElse(null);
    }

    public Ingredient update(Long id, Ingredient ingredient) {
        Optional<Ingredient> ingredientFound = ingredientRepository.findById(id);
        if (ingredientFound.isPresent()) {
            Ingredient ingredientToUpdate = ingredientFound.get();
            ingredientToUpdate.setName(ingredient.getName());
            ingredientToUpdate.setCategory(ingredient.getCategory());
            ingredientToUpdate.setQuantity(ingredient.getQuantity());
            ingredientToUpdate.setValidate(ingredient.getValidate());
            return ingredientRepository.save(ingredientToUpdate);
        }
        return null;
    }

    public void delete(Long id) {
        Optional<Ingredient> ingredientExist = ingredientRepository.findById(id);
        if (ingredientExist.isPresent()) {
            ingredientRepository.deleteById(id);
        }
    }
}
