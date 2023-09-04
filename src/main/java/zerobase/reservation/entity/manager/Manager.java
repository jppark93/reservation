package zerobase.reservation.entity.manager;


import lombok.*;
import org.springframework.data.annotation.Id;
import zerobase.reservation.entity.BaseEntity;
import zerobase.reservation.entity.dto.SignUpDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * 점장 엔티티
 * <p>
 * 정보 : 이름, 이메일, 패스워드, 전화번호
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Manager extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String name;
    private String password;
    private String phone;


    // DTO -> Entity
    public static Manager from(SignUpDto signUpDto) {
        return Manager.builder()
            .email(signUpDto.getEmail())
            .name(signUpDto.getName())
            .password(signUpDto.getPassword())
            .phone(signUpDto.getPhone())
            .build();
    }
}
