package zerobase.reservation.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zerobase.reservation.encryption.domain.UserType;
import zerobase.reservation.encryption.service.JwtAuthService;
import zerobase.reservation.encryption.util.CryptoUtil;
import zerobase.reservation.entity.Customer;
import zerobase.reservation.entity.dto.SignInDto;
import zerobase.reservation.entity.manager.Manager;
import zerobase.reservation.entity.repository.CustomerRepository;
import zerobase.reservation.entity.repository.ManagerRepository;
import zerobase.reservation.exception.ReservationServiceException;

import static zerobase.reservation.exception.ErrorCode.USER_NOT_EXIST;
import static zerobase.reservation.exception.ErrorCode.WRONG_PASSWORD;


/**
 * 로그인(점장, 고객 공통) 서비스 레이어
 */
@Service
@RequiredArgsConstructor
public class SignInService {

    private final ManagerRepository managerRepository;
    private final CustomerRepository customerRepository;
    private final JwtAuthService authService;

    /**
     * 점장 로그인(토큰 발급)
     */
    public String getManagerToken(SignInDto signInDto) {
        // 이메일로 해당 유저를 찾을 수 없는 경우
        Manager manager = managerRepository.findByEmail(signInDto.getEmail())
            .orElseThrow(() -> new ReservationServiceException(USER_NOT_EXIST));

        // 비밀번호가 틀린 경우
        if (!signInDto.getPassword().equals(CryptoUtil.decrypt(manager.getPassword()))) {
            throw new ReservationServiceException(WRONG_PASSWORD);
        }

        return authService.createToken(manager.getId(), manager.getEmail(), UserType.MANAGER);
    }

    /**
     * 고객 로그인(토큰 발급)
     */
    public String getCustomerToken(SignInDto signInDto) {
        // 이메일로 해당 유저를 찾을 수 없는 경우
        Customer customer = customerRepository.findByEmail(signInDto.getEmail())
            .orElseThrow(() -> new ReservationServiceException(USER_NOT_EXIST));

        // 비밀번호가 틀린 경우
        if (!signInDto.getPassword().equals(CryptoUtil.decrypt(customer.getPassword()))) {
            throw new ReservationServiceException(WRONG_PASSWORD);
        }

        return authService.createToken(customer.getId(), customer.getEmail(), UserType.CUSTOMER);
    }
}
