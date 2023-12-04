package com.shop.controller;

import com.shop.dto.CartDetailDTO;
import com.shop.dto.CartItemDTO;
import com.shop.dto.CartOrderDTO;
import com.shop.service.CartService;
import lombok.RequiredArgsConstructor;
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

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping(value = "/cart")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid CartItemDTO cartItemDTO,
                                              BindingResult bindingResult, Principal principal){
        System.err.println("CartController.PostMapping/cart.order()");
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>
                    (sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();
        Long cartItemId;

        try{
            cartItemId = cartService.addCart(cartItemDTO,email);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("controller.CartController.order.try-catch구문에서 예외 발생");
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(cartItemId,HttpStatus.OK);


    }

    @GetMapping(value = "/cart")
    public String orderHist(Principal principal, Model model){
        System.err.println("CartController.GetMapping./cart.orderHist()");
        List<CartDetailDTO> cartDetailDTOList =
                cartService.getCartList(principal.getName());
        model.addAttribute("cartItems", cartDetailDTOList);
        return "cart/cartList";

    }
    @PatchMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity updateCartItem
            (@PathVariable("cartItemId") Long cartItemId, int count, Principal principal){

        if(count <= 0 ){
            return new ResponseEntity<String>("최소 1개 이상 담아주세요", HttpStatus.BAD_REQUEST);
        }else if(!cartService.validateCartItem(cartItemId, principal.getName())){
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.updateCartItemCount(cartItemId, count);
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);

    }
    @DeleteMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity deleteCartItem
            (@PathVariable("cartItemId") Long cartItemId, Principal principal){

        if(!cartService.validateCartItem(cartItemId,principal.getName())){
            return new ResponseEntity<String>("수정 권한이 없습니다.",HttpStatus.FORBIDDEN);
        }
        cartService.deleteCartItem(cartItemId);
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);

    }

    @PostMapping(value = "/cart/orders")
    public @ResponseBody ResponseEntity orderCartItem
            (@RequestBody CartOrderDTO cartOrderDTO, Principal principal){

        List<CartOrderDTO> cartOrderDTOList =
                cartOrderDTO.getCartOrderDTOList();

        if(cartOrderDTOList == null || cartOrderDTOList.size() == 0){
            return new ResponseEntity<String>("주문할 상품을 선택해주세요.",
                    HttpStatus.FORBIDDEN);
        }
        for(CartOrderDTO cartOrder : cartOrderDTOList){
            if(!cartService.validateCartItem(cartOrder.getCartItemId(),
            principal.getName())){
                return new ResponseEntity<String>("주문 권한이 없습니다.",HttpStatus.FORBIDDEN);
            }
        }
        Long orderId = cartService.orderCartItem(cartOrderDTOList,principal.getName());
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

}
