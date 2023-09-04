package zerobase.reservation.entity.dto.manager;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zerobase.reservation.entity.manager.Manager;

/**
 * 점장 정보 수정 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateManagerDto {

    private String email;
    private String name;
    private String phone;

    // Entity -> DTO
    public static UpdateManagerDto from(Manager manager) {
        return UpdateManagerDto.builder()
            .email(manager.getEmail())
            .name(manager.getName())
            .phone(manager.getPhone())
            .build();
    }
}
