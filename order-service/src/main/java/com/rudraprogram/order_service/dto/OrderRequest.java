package com.rudraprogram.order_service.dto;

import com.rudraprogram.order_service.model.OrderLineItems;
import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    private List<OrderLineItemsDto> orderLineItemsDtoList;
}
