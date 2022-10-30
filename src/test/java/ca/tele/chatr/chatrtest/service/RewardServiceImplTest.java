package ca.tele.chatr.chatrtest.service;

import ca.tele.chatr.chatrtest.ChatrTestApplication;
import ca.tele.chatr.chatrtest.model.ResponseDto;
import ca.tele.chatr.chatrtest.model.ResponseState;
import ca.tele.chatr.chatrtest.model.Rewards;
import ca.tele.chatr.chatrtest.persistence.RewardsAndPaymentsRepository;
import ca.tele.chatr.chatrtest.utility.RewardsUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ChatrTestApplication.class})
class RewardServiceImplTest {

    @Autowired
    private RewardService rewardService;

    @MockBean
    private RewardsAndPaymentsRepository rewardsAndPaymentsRepository;

    @MockBean
    private PaymentService paymentService;

    @Test
    public void test_getTotalRewardForUserPerDay_success_scenario(){
        Mockito.when(paymentService.provideUserId("suresh", paymentService.FETCH_OPERATION)).thenReturn(1l);
        Mockito.when(rewardsAndPaymentsRepository.findTotalRewardPoints(1l, LocalDate.now())).thenReturn(150l);

        ResponseDto<Rewards> responseDto= rewardService.getTotalRewardForUserPerDay("suresh");
        assertEquals(ResponseState.SUCCESS, responseDto.getResponseStatus());
        assertEquals(150, responseDto.getData().getRewardPoints());
        assertEquals(RewardsUtil.DAILY, responseDto.getData().getRewardFrequency());
    }

    @Test
    public void test_getTotalRewardForUserPerDay_error_scenario(){
        ResponseDto<Rewards> responseDto= rewardService.getTotalRewardForUserPerDay("suresh");
        assertEquals(ResponseState.SUCCESS, responseDto.getResponseStatus());
        assertEquals(0, responseDto.getData().getRewardPoints());
        assertEquals(RewardsUtil.DAILY, responseDto.getData().getRewardFrequency());
    }

    @Test
    public void test_getTotalRewardForUserPerMonth_success_scenario(){
        Mockito.when(paymentService.provideUserId("suresh", paymentService.FETCH_OPERATION)).thenReturn(1l);
        Mockito.when(rewardsAndPaymentsRepository.findTotalFrequencyRewardPoints(1l, LocalDate.now(), LocalDate.now().minusMonths(1))).thenReturn(150l);

        ResponseDto<Rewards> responseDto= rewardService.getMonthlyRewardsForUser("suresh");
        assertEquals(ResponseState.SUCCESS, responseDto.getResponseStatus());
        assertEquals(150, responseDto.getData().getRewardPoints());
        assertEquals(RewardsUtil.MONTHLY, responseDto.getData().getRewardFrequency());
    }

    @Test
    public void test_getTotalRewardForUserPerMonth_error_scenario(){
        ResponseDto<Rewards> responseDto= rewardService.getMonthlyRewardsForUser("suresh");
        assertEquals(ResponseState.SUCCESS, responseDto.getResponseStatus());
        assertEquals(0, responseDto.getData().getRewardPoints());
        assertEquals(RewardsUtil.MONTHLY, responseDto.getData().getRewardFrequency());
    }

    @Test
    public void test_getTotalRewardForUserPerQuarter_success_scenario(){
        Mockito.when(paymentService.provideUserId("suresh", paymentService.FETCH_OPERATION)).thenReturn(1l);
        Mockito.when(rewardsAndPaymentsRepository.findTotalFrequencyRewardPoints(1l, LocalDate.now(), LocalDate.now().minusMonths(3))).thenReturn(150l);

        ResponseDto<Rewards> responseDto= rewardService.getQuarterlyRewardsForUser("suresh");
        assertEquals(ResponseState.SUCCESS, responseDto.getResponseStatus());
        assertEquals(150, responseDto.getData().getRewardPoints());
        assertEquals(RewardsUtil.QUARTERLY, responseDto.getData().getRewardFrequency());
    }

    @Test
    public void test_getTotalRewardForUserPerQuarter_error_scenario(){
        ResponseDto<Rewards> responseDto= rewardService.getQuarterlyRewardsForUser("suresh");
        assertEquals(ResponseState.SUCCESS, responseDto.getResponseStatus());
        assertEquals(0, responseDto.getData().getRewardPoints());
        assertEquals(RewardsUtil.QUARTERLY, responseDto.getData().getRewardFrequency());
    }


}