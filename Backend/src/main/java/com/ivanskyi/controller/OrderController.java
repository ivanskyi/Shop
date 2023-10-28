package com.ivanskyi.controller;

import com.ivanskyi.dto.OrderDTO;
import com.ivanskyi.dto.DataAboutOrderStatusDTO;
import com.ivanskyi.dto.PurchasedProductDTO;
import com.ivanskyi.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_MANAGER') or hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<PurchasedProductDTO> addProductToOrder(@RequestBody final PurchasedProductDTO purchasedProductDTO) {
        final PurchasedProductDTO savedPurchasedProductDTO = orderService.addNewProductToOrder(purchasedProductDTO);
        return (savedPurchasedProductDTO != null) ? new ResponseEntity<>(savedPurchasedProductDTO, HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_MANAGER') or hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<List<OrderDTO>> getAllOrdersForCurrentUser() {
        final List<OrderDTO> addedProduct = orderService.getAllOrdersForCurrentUser();
        return (addedProduct != null) ? new ResponseEntity<>(addedProduct, HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/buy")
    @PreAuthorize("hasAuthority('ROLE_MANAGER') or hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<Boolean> buyOrder(@RequestBody final DataAboutOrderStatusDTO dataAboutOrderStatusDTO) {
        Boolean isOrderBought = orderService.executePaymentForOrder(dataAboutOrderStatusDTO);
        return new ResponseEntity<>(isOrderBought, HttpStatus.CREATED);
    }
}
