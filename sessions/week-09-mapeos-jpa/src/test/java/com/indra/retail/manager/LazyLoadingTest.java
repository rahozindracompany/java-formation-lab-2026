package com.indra.retail.manager;

import com.indra.retail.entities.Customer;
import com.indra.retail.entities.Order;
import com.indra.retail.entities.OrderItem;
import com.indra.retail.entities.Product;
import org.springframework.dao.DataIntegrityViolationException;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class LazyLoadingTest {

    @Autowired
    private EntityManagerRecordManager manager;

    @Autowired
    private TransactionTemplate transactionTemplate;

    private Long orderId;

    @BeforeEach
    void createOrderWithItems() {
        // Todo en una transacción: las entidades quedan managed y el test no
        // depende de qué cascade tenga el mapeo.
        orderId = transactionTemplate.execute(status -> {
            Customer customer = manager.create(new Customer("Carla", UUID.randomUUID() + "@indra.com"));
            Product product = manager.create(new Product("Laptop", new BigDecimal("1500.00"), 4));
            Order order = new Order(customer);
            order.addItem(new OrderItem(product, 1, product.getPrice()));
            return manager.create(order).getId();
        });
    }

    @Test
    @DisplayName("Acceder a items fuera de la transacción lanza LazyInitializationException")
    void shouldThrowLazyInitializationExceptionOutsideTransaction() {
        // findById abre y cierra su transacción: la Order sale DETACHED
        Order order = manager.findById(Order.class, orderId).orElseThrow();

        assertThrows(LazyInitializationException.class, () -> order.getItems().size());
    }

    @Test
    @DisplayName("Acceder a items dentro de una transacción activa funciona")
    void shouldLoadItemsInsideTransaction() {
        int itemCount = transactionTemplate.execute(status -> {
            Order order = manager.findById(Order.class, orderId).orElseThrow();
            return order.getItems().size();
        });

        assertEquals(1, itemCount);
    }

    // ───────────────────────── Retos para participantes ─────────────────────────

    @Test
    @DisplayName("Customer de un Order no se carga hasta que se usa (LAZY)")
    void shouldLoadCustomerLazily() {
        Order order = manager.findById(Order.class, orderId).orElseThrow();

        assertThrows(LazyInitializationException.class,
                () -> order.getCustomer().getName());
    }

    @Test
    @DisplayName("Quitar un ítem de la lista elimina la fila (orphanRemoval)")
    void shouldRemoveOrphanItem() {
        Long itemId = transactionTemplate.execute(status -> {
            Order order = manager.findById(Order.class, orderId).orElseThrow();
            OrderItem item = order.getItems().get(0);
            order.removeItem(item);
            return item.getId();
        });

        assertTrue(manager.findById(OrderItem.class, itemId).isEmpty());
    }

    @Test
    @DisplayName("No se puede guardar un OrderItem sin Order")
    void shouldRejectOrphanOrderItem() {
        Product product = manager.create(new Product("Tablet", new BigDecimal("800.00"), 2));
        OrderItem orphan = new OrderItem(product, 1, product.getPrice());

        assertThrows(DataIntegrityViolationException.class, () -> manager.create(orphan));
    }

    @Test
    @DisplayName("Borrar un OrderItem no borra el Product")
    void shouldNotDeleteProductWhenDeletingItem() {
        Long productId = transactionTemplate.execute(status -> {
            Order order = manager.findById(Order.class, orderId).orElseThrow();
            OrderItem item = order.getItems().get(0);
            manager.delete(OrderItem.class, item.getId());
            return item.getProduct().getId();
        });

        assertTrue(manager.findById(Product.class, productId).isPresent());
    }
}
