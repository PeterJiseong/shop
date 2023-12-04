package com.shop.dto;

import com.shop.constant.OrderStatus;
import com.shop.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderHisDTO {
    private Long orderId;
    private String orderDate;
    private OrderStatus orderStatus;
    private List<OrderItemDTO> orderItemDTOList = new ArrayList<>();

    public OrderHisDTO(Order order){
        System.err.println("dto.OrderHisDTO.생성자");
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }

    public void addOrderItemDTO(OrderItemDTO orderItemDTO){
        System.err.println("dto.OrderHisDTO.addOrderItemDTO");
        orderItemDTOList.add(orderItemDTO);
    }

}
