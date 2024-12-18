package com.shop.service;

import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemImgService {

    /* 이미지 저장경로 */
    @Value("${itemImgLocation}")
    private String itemImgLocation;

    /* 이미지 CRUD 구현 */
    private final ItemImgRepository itemImgRepository;
    
    /* 파일업로드 / 삭제처리 객체생성 */
    private final FileService fileService;

    /* 이미지 저장 로직 실행 START */
    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception{


        /* Thumvivil.png */
        String oriImgName = itemImgFile.getOriginalFilename();

        System.out.println("---------itemImgFile.getOriginalFilename()----------" + itemImgFile.getOriginalFilename());

        String imgName = "";
        String imgUrl = "";
        
        /* 이미지 파일이 업로드되었을 때,
        파일을 서버에 저장하고, 그 파일의 URL을 생성하여 반환하는 역할*/
        /* Thumvivil.png (oriImgName) 비어있지 않으면 */
        if(!StringUtils.isEmpty(oriImgName)){

            /* 이미지 파일이 존재하면 fileService 업로드로직 실행 */
            /* FileService = uuid+확장자로 새로운 파일 명 생성,
               상품업로드 전체경로 생성 및 파일저장 c:/shop/item/62ecf0dc-9513-4b86-abf2-d1a506f70864.png
               새로운 파일 명 리턴 */

            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            /* [imgName] => 62ecf0dc-9513-4b86-abf2-d1a506f70864.png*/

            imgUrl = "/images/item/" + imgName;
            /* [imgUrl] => /images/item/aa9148d3-e8c2-41a4-9e01-553a88daf5e9.png */

            /* 엔티티 =>  이미지 정보 업데이트 메서드 호출 */
            /* itemImgLocation(파일 저장할 경로) => c:/shop/item */
            /* oriImgName(원본 파일명) => Thumvivil.png */
            /* itemImgFile.getBytes(업로드할 파일) => 69957 */
            itemImg.updateItemImg(oriImgName, imgName, imgUrl);
            itemImgRepository.save(itemImg);
        }

    }
    /* 이미지 저장 로직 실행 END  */


    /* 상품 이미지 변경 START */
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
        
        /* 기존 이미지는 지우고 새 이미지 저장*/
        if(!itemImgFile.isEmpty()) {
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow( () -> new EntityNotFoundException(""));

            /* 기존 이미지 파일 삭제 (itemImgFile 이 NULL 아니면실행 )*/
            if(!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
            }

            /* 새로 등록한 이미지로 변경 */
            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imfUrl = "/images/item/" + imgName;
            savedItemImg.updateItemImg(oriImgName, imgName, imfUrl);
            /* savedItemImg = 영속상태, 데이터를 변경하는 것만으로 변경 감지 기능이 동작 
               트랜잭션이 끝날 때 update쿼리가 실행됨. 여기서 중요한 것은
               엔티티가 영속 상태여야 함
            */
            
        }
    }
    /* 상품 이미지 변경 END */
    
}
