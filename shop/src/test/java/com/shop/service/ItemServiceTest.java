package com.shop.service;


import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDTO;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class ItemServiceTest {

    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemImgRepository itemImgRepository;

    List<MultipartFile> createMultipartFiles() throws Exception{
        List<MultipartFile> multipartFilesList = new ArrayList<>();
        for(int i = 0 ; i < 5 ; i ++){
            String path = "D;/shop/item/";
            String imgName = "image"+i+".jpg";
            MockMultipartFile multipartFile =
                    new MockMultipartFile(path,imgName, "image/jpg",new byte[]{1,2,3,4,5});
            multipartFilesList.add(multipartFile);
        }
        return multipartFilesList;
    }

   @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void saveItem() throws Exception{
       ItemFormDTO itemFormDTO = new ItemFormDTO();
       itemFormDTO.setItemName("test");
       itemFormDTO.setItemSellStatus(ItemSellStatus.SELL);
       itemFormDTO.setItemDetail("test product");
       itemFormDTO.setPrice(10000);
       itemFormDTO.setStockNumber(10);

       List<MultipartFile> multipartFileList = createMultipartFiles();
       Long itemId = itemService.saveItem(itemFormDTO, multipartFileList);

       List<ItemImg> itemImgList =
               itemImgRepository.findByItemIdOrderByIdAsc(itemId);
       Item item = itemRepository.findById(itemId)
                       .orElseThrow(EntityNotFoundException::new);

       assertEquals(itemFormDTO.getItemName(), item.getItemName());
       assertEquals(itemFormDTO.getItemSellStatus(), item.getItemSellStatus());
       assertEquals(itemFormDTO.getItemDetail(),item.getItemDetail());
       assertEquals(itemFormDTO.getPrice(),item.getPrice());
       assertEquals(itemFormDTO.getStockNumber(),item.getStockNumber());
       assertEquals(multipartFileList.get(0).getOriginalFilename(),itemImgList.get(0).getOriImgName());


   }

}
