package com.ivanskyi.service.impl;

import com.ivanskyi.dto.DataAboutOrderStatusDTO;
import com.ivanskyi.dto.ProductDTO;
import com.ivanskyi.dto.PurchasedProductDTO;
import com.ivanskyi.enumeration.OrderStatus;
import com.ivanskyi.model.Product;
import com.ivanskyi.model.User;
import com.ivanskyi.model.UserRole;
import com.ivanskyi.repository.OrderRepository;
import com.ivanskyi.repository.ProductRepository;
import com.ivanskyi.repository.PurchasedProductRepository;
import com.ivanskyi.service.OrderService;
import com.ivanskyi.service.ProductService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderServiceImplTest {

    private static final String TEST_PRODUCT_DESCRIPTION = "Test description";
    private static final double TEST_PRODUCT_PRICE = 15.0;
    private static final int TEST_PRODUCT_QTY = 100;
    private static final long TEST_PURCHASED_AMOUNT = 100L;
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserSecurityServiceImpl userSecurityService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchasedProductRepository purchasedProductRepository;

    private static final String TEST_USER_USERNAME = "TEST-USER";
    private static final String TEST_USER_PASSWORD = "TEST_PASSWORD";
    private static final String TEST_USER_EMAIL = "TEST_EMAIL@EMAIL.COM";
    private static final String TEST_PRODUCT_PRODUCT_NAME = "Test product name.";

    private Long orderId;
    private Long productId;

    @BeforeAll
    void setupTestUser() {
        User user = new User();
        user.setUsername(TEST_USER_USERNAME);
        user.setPassword(TEST_USER_PASSWORD);
        user.setEmail(TEST_USER_EMAIL);
        user.setRoles(Collections.singletonList(UserRole.ROLE_MANAGER));
        userSecurityService.signup(user);
    }

    @AfterAll
    void cleanupTestData() {
        userSecurityService.delete(TEST_USER_USERNAME);

        orderRepository.findById(orderId).ifPresent(order -> {
            orderRepository.delete(order);
        });

        productRepository.findById(productId).ifPresent(product -> {
            purchasedProductRepository.findAllByOrderIdAndProduct(orderId, product).stream()
                    .forEach(purchased -> purchasedProductRepository.delete(purchased));
            productRepository.delete(product);
        });
    }

    @Test
    void testUserExist() {
        User user = userSecurityService.search(TEST_USER_USERNAME);
        assertNotNull(user, "User should exist");
        assertEquals(TEST_USER_USERNAME, user.getUsername(), "Usernames should match");
    }

    @Test
    @WithUserDetails(TEST_USER_USERNAME)
    public void createTestProductAndAddToOrder() {
        ProductDTO dummyProductDTO = getDummyProductDTO();
        ProductDTO savedProductDto = productService.createNewProduct(dummyProductDTO);
        Product product = productService.getProductById(savedProductDto.getId()).orElse(null);
        assertNotNull(product);
        assertEquals(savedProductDto.getId(), product.getId());

        PurchasedProductDTO purchasedProductDTO = getPurchasedProductDTO(savedProductDto);
        PurchasedProductDTO purchased = orderService.addNewProductToOrder(purchasedProductDTO);
        productId = purchased.getProduct().getId();

        assertEquals(TEST_PRODUCT_PRODUCT_NAME, purchased.getProduct().getName(), "Product name isn't the same");
        orderId = purchased.getOrderId();
        DataAboutOrderStatusDTO status = new DataAboutOrderStatusDTO(orderId, OrderStatus.ORDERED);
        boolean isPaymentWasDid = orderService.executePaymentForOrder(status);
        assertTrue(isPaymentWasDid, "The payment was not completed");
    }

    private ProductDTO getDummyProductDTO() {
        ProductDTO productDto = new ProductDTO();
        productDto.setName(TEST_PRODUCT_PRODUCT_NAME);
        productDto.setDescription(TEST_PRODUCT_DESCRIPTION);
        productDto.setPrice(TEST_PRODUCT_PRICE);
        productDto.setQuantity(TEST_PRODUCT_QTY);
        return productDto;
    }

    private PurchasedProductDTO getPurchasedProductDTO(final ProductDTO product) {
        PurchasedProductDTO dto = new PurchasedProductDTO();
        dto.setProduct(product);
        dto.setAmount(TEST_PURCHASED_AMOUNT);
        return dto;
    }
}
