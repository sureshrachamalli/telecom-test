package ca.tele.chatr.chatrtest.service;

import ca.tele.chatr.chatrtest.ChatrTestApplication;
import ca.tele.chatr.chatrtest.exception.BadRequestException;
import ca.tele.chatr.chatrtest.exception.UnHandeledEntityException;
import ca.tele.chatr.chatrtest.model.ResponseDto;
import ca.tele.chatr.chatrtest.model.ResponseState;
import ca.tele.chatr.chatrtest.model.RewardsAndPaymentRequest;
import ca.tele.chatr.chatrtest.persistence.RewardsAndPaymentsEntity;
import ca.tele.chatr.chatrtest.persistence.RewardsAndPaymentsRepository;
import ca.tele.chatr.chatrtest.persistence.UserEntity;
import ca.tele.chatr.chatrtest.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ChatrTestApplication.class})
class PaymentServiceImplTest {

    @Autowired
    private PaymentService paymentService;

    @MockBean
    private RewardsAndPaymentsRepository rewardsAndPaymentsRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void test_saveRewardsAndPayments_success_scenario() {
        UserEntity userEntity = new UserEntity(null, "suresh");
        UserEntity userEntityReturn = new UserEntity(1l, "suresh");
        Mockito.when(userRepository.save(userEntity)).thenReturn(userEntityReturn);
        RewardsAndPaymentRequest rewardsAndPaymentRequest = new RewardsAndPaymentRequest("Suresh", "Television", 150.0, 1);
        RewardsAndPaymentsEntity saveRewardsAndPaymentsEntity = new RewardsAndPaymentsEntity(null, 1l, "Television", 1, 150.0, 150l, LocalDate.now());
        RewardsAndPaymentsEntity returnRewardsAndPaymentsEntity = new RewardsAndPaymentsEntity(1l, 1l, "Television", 1, 150.0, 150l, LocalDate.now());
        Mockito.when(rewardsAndPaymentsRepository.save(saveRewardsAndPaymentsEntity)).thenReturn(returnRewardsAndPaymentsEntity);
        ResponseDto<RewardsAndPaymentsEntity> responseDto = paymentService.saveRewardsAndPayments(rewardsAndPaymentRequest);
        assertEquals(ResponseState.SUCCESS, responseDto.getResponseStatus());
        assertEquals(150, responseDto.getData().getRewardPoints());
    }

    @Test
    public void test_getRewardsAndPayments_request_parameter_null_error_scenario() {
        assertThrows(BadRequestException.class, () -> paymentService.saveRewardsAndPayments(null));
    }

    @Test
    public void test_getRewardsAndPayments_success_scenario() {
        UserEntity userEntity = new UserEntity(1l, "suresh");
        Mockito.when(userRepository.findByUserName("suresh")).thenReturn(userEntity);
        RewardsAndPaymentsEntity rewardsAndPaymentsEntity = new RewardsAndPaymentsEntity(1l, 1l, "watermelon", 1, 75.0, 25l, LocalDate.now());
        Mockito.when(rewardsAndPaymentsRepository.findByUserId(1l)).thenReturn(Arrays.asList(rewardsAndPaymentsEntity));
        ResponseDto<List<RewardsAndPaymentsEntity>> responseDto = paymentService.getRewardsAndPayments("Suresh");
        assertEquals(ResponseState.SUCCESS, responseDto.getResponseStatus());
        assertEquals(25, responseDto.getData().get(0).getRewardPoints());
    }

    @Test
    public void test_getRewardsAndPayments_error_scenario() {
        RewardsAndPaymentsEntity rewardsAndPaymentsEntity = new RewardsAndPaymentsEntity(1l, 1l, "watermelon", 1, 75.0, 25l, LocalDate.now());
        Mockito.when(rewardsAndPaymentsRepository.findByUserId(1l)).thenReturn(Arrays.asList(rewardsAndPaymentsEntity));
        assertThrows(UnHandeledEntityException.class, () -> paymentService.getRewardsAndPayments("Suresh"));
    }

    @Test
    public void test_provideUserId_fetch_operation_success_scenario() {
        UserEntity userEntity = new UserEntity(1l, "suresh");
        Mockito.when(userRepository.findByUserName("suresh")).thenReturn(userEntity);
        Long userId = paymentService.provideUserId("Suresh", PaymentService.FETCH_OPERATION);
        assertEquals(1, userId);
    }

    @Test
    public void test_provideUserId_save_operation_success_scenario() {
        UserEntity userEntity = new UserEntity(null, "suresh");
        UserEntity userEntityReturn = new UserEntity(1l, "suresh");
        Mockito.when(userRepository.save(userEntity)).thenReturn(userEntityReturn);
        Long userId = paymentService.provideUserId("Suresh", PaymentService.SAVE_OPERATION);
        assertEquals(1, userId);
    }

    @Test
    public void test_provideUserId_fetch_operation_error_scenario() {
        assertThrows(UnHandeledEntityException.class, () -> paymentService.provideUserId("Suresh", PaymentService.SAVE_OPERATION));
    }
}