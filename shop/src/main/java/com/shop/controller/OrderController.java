package com.shop.controller;


import com.shop.dto.OrderDTO;
import com.shop.dto.OrderHisDTO;
import com.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //Principal : 스프링 시큐리티. 로그인이 되어 있으면 로그인 계정에 대한 정보를 담음
    //@Controller 어노테이션이 적용된 클래스에서 메서드의 파라미터로 전달
    @PostMapping(value = "/order")
    public @ResponseBody ResponseEntity order(
            @RequestBody @Valid OrderDTO orderDTO, BindingResult bindingResult,
            Principal principal
            ){

        System.err.println("Controller.OrderController.order");
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(),
            HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();
        Long orderId;
        try{
            orderId = orderService.order(orderDTO,email);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("orderController.order() 중 예외 발생");
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);

    }

    @GetMapping(value = {"/orders","/orders/{page}"})
    public String orderHist(@PathVariable("page")Optional<Integer> page, Principal principal, Model model){
        System.err.println("Controller.OrderController.orderHist");
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);

        Page<OrderHisDTO> orderHisDTOList =
                orderService.getOrderList(principal.getName(), pageable);

        model.addAttribute("orders", orderHisDTOList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);
        return "order/orderHist";
    }
    @PostMapping("/order/{orderId}/cancel")
    public  @ResponseBody ResponseEntity cancelOrder(
            @PathVariable("orderId") Long orderId, Principal principal){
        System.err.println("Controller.OrderController.cancelOrder");
        if(!orderService.validateOrder(orderId, principal.getName())){
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);

    }

}
