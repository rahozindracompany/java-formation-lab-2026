package com.indra.jpa.inventory.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @DisplayName("Busqueda de productos sin nombre retorna productos paginados y total")
    void searchWithoutNameReturnsPaginatedProductsAndTotal() throws Exception {
     
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(Math.min(10, 50))))
                .andExpect(jsonPath("$.totalElements").value(50));
    }


    @Test
    @DisplayName("Busqueda de productos con nombre filtra y pagina los resultados")
    void searchWithNameFiltersAndPaginatesProducts() throws Exception {
        mockMvc.perform(get("/products").param("name", "mechanical keyboard")
                 )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.content[0].name").value(org.hamcrest.Matchers.containsString("Mechanical Keyboard")));
    }

   

  


  
}