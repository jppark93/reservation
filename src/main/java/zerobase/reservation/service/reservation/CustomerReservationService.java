package zerobase.reservation.service.reservation;


import com.amerikano.reservation.type.ReserveTime;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.reservation.entity.Customer;
import zerobase.reservation.entity.Reservation;
import zerobase.reservation.entity.dto.customer.ReservationForm;
import zerobase.reservation.entity.dto.customer.VisitShopForm;
import zerobase.reservation.entity.dto.reservation.ReservationDto;
import zerobase.reservation.entity.manager.Shop;
import zerobase.reservation.entity.repository.CustomerRepository;
import zerobase.reservation.entity.repository.ReservationRepository;
import zerobase.reservation.entity.repository.ShopRepository;
import zerobase.reservation.exception.ReservationServiceException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static zerobase.reservation.exception.ErrorCode.*;


/**
 * 예약(고객) 서비스 레이어
 */
@Service
@RequiredArgsConstructor
public class CustomerReservationService {

    private final ReservationRepository reservationRepository;
    private final ShopRepository shopRepository;
    private final CustomerRepository customerRepository;

    /**
     * 서비스 처리 전 해당 고객 유저가 존재하는지 확인
     */
    private boolean isCustomerExists(Long customerId) {
        return customerRepository.existsById(customerId);
    }

    /**
     * 예약 정보를 바탕으로 예약 진행
     */
    public ReservationForm.ResponseDto registerReservation(
        Long customerId, ReservationForm.RequestDto requestDto
    ) {
        // 해당 유저가 존재하지 않는 경우
        if (!isCustomerExists(customerId)) {
            throw new ReservationServiceException(RESERVATION_CUSTOMER_NOT_EXIST);
        }


        Shop shop = shopRepository.findById(requestDto.getShopId())
            .orElseThrow(() -> new ReservationServiceException(SHOP_NOT_EXIST));



        // 예약 정보 생성 후 저장
        // 예약 승인은 기본값을 null 로 설정(점장 미확인)
        Reservation newReservation = getNewReservation(customerId, shop, requestDto);

        reservationRepository.save(newReservation);

        return ReservationForm.ResponseDto.builder()
            .shopId(shop.getId())
            .code(newReservation.getCode())
            .reservedDate(newReservation.getReservedDate())
            .build();
    }

    /**
     * 새로운 예약 정보 생성 후 저장
     * <p>
     * 예약 승인(accepted)은 기본값을 null 로 설정(점장 미확인 상태)
     */
    private Reservation getNewReservation(Long customerId, Shop shop, ReservationForm.RequestDto requestDto) {
        return Reservation.builder()
            .customerId(customerId)
            .shop(shop)
            .code(getRandomCode())
            .reservedDate(requestDto.getReservedDate())
            .accepted(null)
            .visited(false)
            .canceled(false)
            .build();
    }

    /**
     * 해당 고객의 모든 예약 조회
     */
    public List<ReservationDto> getCustomerReservations(Long customerId) {
        return reservationRepository.findAllByCustomerId(customerId)
            .stream()
            .map(ReservationDto::from)
            .collect(Collectors.toList());
    }


    /**
     * 예약 정보 취소
     */
    @Transactional
    public void cancelReservation(Long customerId, String code) {
        // 코드에 해당하는 예약이 존재하는지 확인
        Reservation reservation = reservationRepository.findByCode(code)
            .orElseThrow(() -> new ReservationServiceException(RESERVATION_NOT_EXIST));

        validateReservationCommon(reservation);

        matchCustomerId(customerId, reservation);

        reservation.setCanceled(true);
    }

    /**
     * 예약한 매장 방문 처리
     */
    @Transactional
    public String visitReservedShop(String code, VisitShopForm form) {
        // 코드에 해당하는 예약이 존재하는지 확인
        Reservation reservation = reservationRepository.findByCode(code)
            .orElseThrow(() -> new ReservationServiceException(RESERVATION_NOT_EXIST));

        Customer customer = customerRepository.findById(reservation.getCustomerId())
            .orElseThrow(() -> new ReservationServiceException(RESERVATION_CUSTOMER_NOT_EXIST));

        validateReservationCommon(reservation);

        checkVisitForm(customer, form);

        // 예약을 확인하지 않았다면 방문할 수 없음
        if (reservation.getAccepted() == null) {
            throw new ReservationServiceException(RESERVATION_NOT_PROCESSED);
        }



        reservation.setVisited(true);
        reservation.setVisitedAt(LocalDateTime.now());

        return "예약 방문 처리가 완료되었습니다";
    }

    /**
     * 예약의 기본 검증
     */
    private void validateReservationCommon(Reservation reservation) {
        // 이미 취소된 예약인 경우
        if (reservation.getCanceled()) {
            throw new ReservationServiceException(RESERVATION_ALREADY_CANCELED);
        }

        // 이미 완료 처리된 예약인 경우
        if (reservation.getVisited()) {
            throw new ReservationServiceException(RESERVATION_ALREADY_VISITED);
        }

        // 이미 거절된 예약인 경우
        if (reservation.getAccepted() != null &&
            !reservation.getAccepted()) {
            throw new ReservationServiceException(RESERVATION_ALREADY_REFUSED);
        }
    }



    /**
     * 예약한 고객과 요청한 고객이 같은지 확인
     */
    private void matchCustomerId(Long customerId, Reservation reservation) {
        if (!Objects.equals(reservation.getCustomerId(), customerId)) {
            throw new ReservationServiceException(RESERVATION_WRONG_CUSTOMER);
        }
    }



    /**
     * 예약 정보와 방문자가 입력한 정보 체크
     * <p>
     * (두 가지 다 일치해야 방문 확인 가능)
     */
    private void checkVisitForm(Customer customer, VisitShopForm form) {
        if (!customer.getName().equals(form.getName()) ||
            !customer.getPhone().equals(form.getPhone())
        ) {
            throw new ReservationServiceException(WRONG_VISIT_FORM);
        }
    }


}
