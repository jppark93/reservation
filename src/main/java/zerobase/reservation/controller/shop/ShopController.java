package zerobase.reservation.controller.shop;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.reservation.entity.dto.shop.ShopDto;
import zerobase.reservation.service.ShopService;

import java.util.List;

/**
 * 매장 정보 api 컨트롤러
 */
@RestController
@RequestMapping("shop")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    /**
     * 특정 매장 조회
     */
    @GetMapping("{id}")
    public ResponseEntity<ShopDto> searchShop(@PathVariable Long id) {
        return ResponseEntity.ok(shopService.showShopById(id));
    }

    /**
     * 검색 옵션을 설정하여 매장 검색(타입, 이름에 키워드 포함)
     */
    @GetMapping("")
    public ResponseEntity<List<ShopDto>> searchShops(
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(required = false, defaultValue = "") String type,
        @RequestParam(required = false, defaultValue = "") String keyword
    ) {
        return ResponseEntity.ok(shopService.showShopsByTypeAndKeyword(page, type, keyword));
    }

    /**
     * 평점이 높은 순서대로 모든 매장 조회
     */
    @GetMapping("/rate")
    public ResponseEntity<List<ShopDto>> searchShopsByRate(
        @RequestParam(defaultValue = "1") Integer page
    ) {
        return ResponseEntity.ok(shopService.showAllShopsByRate(page));
    }
}
