package zerobase.reservation.controller;



import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zerobase.reservation.entity.dto.SignUpDto;
import zerobase.reservation.service.SignUpService;

/**
 * 회원가입 api 컨트롤러
 */
@RestController
@RequestMapping("signup")
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;

    /**
     * 점장 회원가입
     */
    @PostMapping("manager")
    public ResponseEntity<String> signUpManager(
        @RequestBody SignUpDto signUpDto
    ) {
        return ResponseEntity.ok(signUpService.signUpManager(signUpDto));
    }

    /**
     * 고객 회원가입
     */
    @PostMapping("customer")
    public ResponseEntity<String> signUpCustomer(
        @RequestBody SignUpDto signUpDto
    ) {
        return ResponseEntity.ok(signUpService.signUpCustomer(signUpDto));
    }
}
