package com.carloslonghi.ChefIA.service;

import com.carloslonghi.ChefIA.model.Ingredient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeminiAIService {

    private final WebClient webClient;

    public Mono<String> generateRecipe(List<Ingredient> ingredientList) {
        String ingredients = ingredientList.stream()
                .map(item -> String.format("%s (%s) - Quantidade: %2f, Validade: %s",
                        item.getName(), item.getCategory(), item.getQuantity(), item.getValidate())
                )
                .collect(Collectors.joining("\n"));
        
        String prompt = "Você é um chefe de cozinha, com base nesses ingredientes me sugira uma receita, detalhando a quantidade de cada ingrediente, e o modo de preparo passo a passo:\n" + ingredients;
        
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                )
        );

        return webClient.post()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class);
    }
}
