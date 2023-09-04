package zerobase.reservation.entity.dto.shop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zerobase.reservation.entity.manager.Shop;


/**
 * 매장 정보 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopDto {

    private Long id;
    private Long managerId;
    private String name;
    private String type;
    private String description;
    private String phone;
    private Double rate;

    // Entity -> DTO
    public static ShopDto from(Shop shop) {
        return ShopDto.builder()
            .id(shop.getId())
            .managerId(shop.getManagerId())
            .name(shop.getName())
            .type(shop.getType())
            .description(shop.getDescription())
            .phone(shop.getPhone())
            .rate(shop.getRate())
            .build();
    }


}
