package co.com.inventory.inventoryservice.services.impl;

import co.com.inventory.inventoryservice.entities.Product;
import co.com.inventory.inventoryservice.models.ProductDto;
import co.com.inventory.inventoryservice.repositories.ICatalogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private ICatalogRepository repository;

    @InjectMocks
    private InventoryService inventoryService;

    private ProductDto productDto;
    private Product product;

    @BeforeEach
    void setUp() {
        productDto = new ProductDto();
        productDto.setId("1");
        productDto.setName("Producto A");
        // ajusta setters según los campos reales de ProductDto

        product = new Product();
        product.setId("1");
        product.setName("Producto A");
        // ajusta setters según los campos reales de Product
    }

    // ----------------------------------------------------------------
    // CREATE
    // ----------------------------------------------------------------
    @Nested
    @DisplayName("create")
    class CreateTests {

        @Test
        @DisplayName("Debe retornar el id del producto cuando la creación es exitosa")
        void create_validProduct_returnsId() {
            given(repository.save(any(Product.class))).willReturn(product);

            String result = inventoryService.create(productDto);

            assertEquals("1", result);
            ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
            then(repository).should().save(productCaptor.capture());
            assertEquals("1", productCaptor.getValue().getId());
            assertEquals("Producto A", productCaptor.getValue().getName());
        }

        @Test
        @DisplayName("Debe lanzar IllegalStateException cuando el repositorio falla")
        void create_repositoryThrowsException_throwsIllegalStateException() {
            willThrow(new RuntimeException("db error"))
                    .given(repository)
                    .save(any(Product.class));

            IllegalStateException ex = assertThrows(IllegalStateException.class,
                    () -> inventoryService.create(productDto));

            assertEquals("Proceso de creación NO exitoso!", ex.getMessage());
            then(repository).should().save(any(Product.class));
        }

        @Test
        @DisplayName("Debe lanzar IllegalStateException cuando el mapeo falla (productDto null)")
        void create_nullProductDto_throwsIllegalStateException() {
            assertThrows(IllegalStateException.class,
                    () -> inventoryService.create(null));

            then(repository).should(never()).save(any());
        }
    }

    // ----------------------------------------------------------------
    // UPDATE
    // ----------------------------------------------------------------
    @Nested
    @DisplayName("update")
    class UpdateTests {

        @Test
        @DisplayName("Debe ejecutar la actualización sin lanzar excepción cuando es exitosa")
        void update_validProduct_completesSuccessfully() {
            given(repository.save(any(Product.class))).willReturn(product);

            assertDoesNotThrow(() -> inventoryService.update(productDto));

            ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
            then(repository).should().save(productCaptor.capture());
            assertEquals("1", productCaptor.getValue().getId());
            assertEquals("Producto A", productCaptor.getValue().getName());
        }

        @Test
        @DisplayName("Debe lanzar IllegalStateException cuando el repositorio falla")
        void update_repositoryThrowsException_throwsIllegalStateException() {
            willThrow(new RuntimeException("db error"))
                    .given(repository)
                    .save(any(Product.class));

            IllegalStateException ex = assertThrows(IllegalStateException.class,
                    () -> inventoryService.update(productDto));

            assertEquals("Proceso de actualizacion NO exitoso!", ex.getMessage());
            then(repository).should().save(any(Product.class));
        }

        @Test
        @DisplayName("Debe lanzar IllegalStateException cuando el mapeo falla (productDto null)")
        void update_nullProductDto_throwsIllegalStateException() {
            assertThrows(IllegalStateException.class,
                    () -> inventoryService.update(null));

            then(repository).should(never()).save(any());
        }
    }

    // ----------------------------------------------------------------
    // GET BY ID
    // ----------------------------------------------------------------
    @Nested
    @DisplayName("getById")
    class GetByIdTests {

        @Test
        @DisplayName("Debe retornar una lista con un elemento cuando el producto existe")
        void getById_existingId_returnsListWithOneElement() {
            given(repository.findById("1")).willReturn(Optional.of(product));

            List<ProductDto> result = inventoryService.getById("1");

            assertEquals(1, result.size());
            assertEquals("1", result.get(0).getId());
            then(repository).should().findById("1");
        }

        @Test
        @DisplayName("Debe retornar una lista vacía cuando el producto no existe")
        void getById_nonExistingId_returnsEmptyList() {
            given(repository.findById("999")).willReturn(Optional.empty());

            List<ProductDto> result = inventoryService.getById("999");

            assertTrue(result.isEmpty());
            then(repository).should().findById("999");
        }
    }

    // ----------------------------------------------------------------
    // GET BY NAME
    // ----------------------------------------------------------------
    @Nested
    @DisplayName("getByName")
    class GetByNameTests {

        @Test
        @DisplayName("Debe retornar la lista mapeada cuando existen resultados")
        void getByName_existingResults_returnsMappedList() {
            given(repository.findByName("Producto A")).willReturn(Arrays.asList(product));

            List<ProductDto> result = inventoryService.getByName("Producto A");

            assertEquals(1, result.size());
            assertEquals("Producto A", result.get(0).getName());
            then(repository).should().findByName("Producto A");
        }

        @Test
        @DisplayName("Debe retornar una lista vacía cuando no hay resultados")
        void getByName_noResults_returnsEmptyList() {
            given(repository.findByName("Inexistente")).willReturn(Collections.emptyList());

            List<ProductDto> result = inventoryService.getByName("Inexistente");

            assertTrue(result.isEmpty());
            then(repository).should().findByName("Inexistente");
        }
    }

    // ----------------------------------------------------------------
    // GET ALL
    // ----------------------------------------------------------------
    @Nested
    @DisplayName("getAll")
    class GetAllTests {

        @Test
        @DisplayName("Debe retornar la lista completa mapeada cuando hay productos")
        void getAll_withResults_returnsMappedList() {
            Product product2 = new Product();
            product2.setId("2");
            product2.setName("Producto B");

            given(repository.findAll()).willReturn(Arrays.asList(product, product2));

            List<ProductDto> result = inventoryService.getAll();

            assertEquals(2, result.size());
            then(repository).should().findAll();
        }

        @Test
        @DisplayName("Debe retornar una lista vacía cuando no hay productos")
        void getAll_noResults_returnsEmptyList() {
            given(repository.findAll()).willReturn(Collections.emptyList());

            List<ProductDto> result = inventoryService.getAll();

            assertTrue(result.isEmpty());
            then(repository).should().findAll();
        }
    }
}