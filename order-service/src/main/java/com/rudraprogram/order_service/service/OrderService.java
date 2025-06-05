package com.rudraprogram.order_service.service;

import com.rudraprogram.order_service.dto.InventoryResponse;
import com.rudraprogram.order_service.dto.OrderLineItemsDto;
import com.rudraprogram.order_service.dto.OrderRequest;
import com.rudraprogram.order_service.model.Order;
import com.rudraprogram.order_service.model.OrderLineItems;
import com.rudraprogram.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    private   final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest){

        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderLineItemsList(orderRequest.getOrderLineItemsDtoList()
                .stream().map(this::mapToDTO).toList());

        //Call inventory service and place order if product is in stock
        //block make webclient to make syncronous call
        List<String> skuCodes= order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();

        InventoryResponse [] inventoryResponseArray  = webClient.get().uri("http://localhost:8082/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                        .retrieve()
                                .bodyToMono(InventoryResponse[].class).block();
        boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);

        if(allProductsInStock){
            orderRepository.save(order);
            log.info("Order {} saved successfully",order.getId());

        }else{
            throw  new IllegalArgumentException("Product is not in stock,Please try again later");
        }
    }

    private OrderLineItems mapToDTO(OrderLineItemsDto orderLineItemsDto) {

        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return  orderLineItems;
    }
}
