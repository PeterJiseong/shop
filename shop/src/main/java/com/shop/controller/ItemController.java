package com.shop.controller;

import com.shop.dto.ItemFormDTO;
import com.shop.dto.ItemImgDTO;
import com.shop.dto.ItemSearchDTO;
import com.shop.entity.Item;
import com.shop.service.ItemImgService;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemImgService itemImgService;

    //Optional : 선택적으로 받아옴
    @GetMapping(value = {"/admin/items","/admin/items/{page}"})
    public String itemManage(ItemSearchDTO itemSearchDTO,
    @PathVariable("page")Optional<Integer>page,Model model ){
        System.err.println("controller.ItemController.itemManage");
        Pageable pageable = PageRequest.of(page.isPresent()?page.get():0,3);
        //request.of(조회할 페이지 번호, 한번에 가져올 데이터의 수)
        //present() : optional객체에 값이 존재하는지 여부를 확인
        Page<Item> items =
                itemService.getAdminItemPage(itemSearchDTO,pageable);
       //조회할 페이지에 대한 조건과 페이지에 대한 정보를 매개변수로 전달하여
        //page 객체를 반환받음

        //조회한 상품 데이터, 검색 조건에 대한 내용, 최대 페이지의 번호 설정하여 리턴
        model.addAttribute("items",items);
        model.addAttribute("itemSearchDTO", itemSearchDTO);
        model.addAttribute("maxPage", 5);
        return "item/itemMng";

    }


    @GetMapping(value = "/admin/item/{itemId}")
    //@PathBariable : URI에 전달된 변수값을 매핑하는데 사용
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){

        System.err.println("controller.ItemController.itemDtl");
        try {
            ItemFormDTO itemFormDTO = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDTO", itemFormDTO);


        }catch (Exception e){
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
            System.err.println("itemcontroller. itemDtl에서 예외 발생");
            e.printStackTrace();
            return "item/itemForm";
        }
        return "item/itemForm";


    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(Model model, @Valid ItemFormDTO itemFormDTO,BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList){

        System.err.println("controller.ItemController.itemUpdate");
        itemFormDTO = itemService.getItemDtl(itemFormDTO.getId());

        ItemFormDTO requestOne =(ItemFormDTO) model.getAttribute("itemFormDTO");
        requestOne.setItemImgDTOList(itemFormDTO.getItemImgDTOList());
        //model.addAttribute("itemFormDTO",requestOne);

        if(bindingResult.hasErrors()){
            return"item/itemForm";
        }


        if(itemImgFileList.get(0).isEmpty() && itemFormDTO.getId() == null){
            model.addAttribute("errorMessage","첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }
        try {
            itemService.updateItem(itemFormDTO,itemImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage","상품 수정 중 에러가 발생 하였습니다.");
            System.err.println("@Controller. updateItem중 예외 발생");
            e.printStackTrace();
            return "item/itemForm";
        }
        //itemImgService.saveItemImg();


        model.addAttribute("sucMessage","상품 수정이 완료 되었습니다.");//안됨, redirect 라서
        return "redirect:/";
    }

    @GetMapping("/admin/item/new")
    public String itemForm(Model model){
        System.err.println("controller.ItemController.itemForm");
        model.addAttribute("itemFormDTO",new ItemFormDTO());
        return "/item/itemForm";
    }

    @PostMapping(value = "/admin/item/new")
    public String itemSave(Model model, @Valid ItemFormDTO itemFormDTO, BindingResult bindingResult,
                           @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList){

        System.err.println("controller.ItemController.itemSave");
        if(bindingResult.hasErrors()){
            System.err.println("hasError 진입");
            return"/item/itemForm";
        }


        if(itemImgFileList.get(0).isEmpty() && itemFormDTO.getId() == null){
            model.addAttribute("errorMessage","첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }
        try {
            itemService.saveItem(itemFormDTO,itemImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage","상품 등록 중 에러가 발생 하였습니다.");
            System.err.println("@Controller. saveItem중 예외 발생");
            e.printStackTrace();
            return "item/itemForm";
        }
        //itemImgService.saveItemImg();


        model.addAttribute("sucMessage","상품 등록이 완료 되었습니다.");
        return "redirect:/";
    }
    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId){
        System.err.println("controller.ItemController.itemDtl");
        ItemFormDTO itemFormDTO = itemService.getItemDtl(itemId);
        model.addAttribute("item",itemFormDTO);
        return"/item/itemDtl";
    }


}
