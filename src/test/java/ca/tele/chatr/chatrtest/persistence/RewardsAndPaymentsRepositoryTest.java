package ca.tele.chatr.chatrtest.persistence;

import ca.tele.chatr.chatrtest.ChatrTestApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ChatrTestApplication.class})
class RewardsAndPaymentsRepositoryTest {

    @Autowired
    private RewardsAndPaymentsRepository rewardsAndPaymentsRepository;

    @Test
    public void test_findByUserId_success_scenario(){

        RewardsAndPaymentsEntity rewardsAndPaymentsEntity = new RewardsAndPaymentsEntity(1l, 1l, "watermelon", 1, 75.0, 25l, LocalDate.now());
        rewardsAndPaymentsRepository.save(rewardsAndPaymentsEntity);
        List<RewardsAndPaymentsEntity> returnresponse = rewardsAndPaymentsRepository.findByUserId(1l);
        assertEquals(25l, returnresponse.get(0).getRewardPoints());
    }

    @Test
    public void test_findByUserId_failure_scenario(){
        RewardsAndPaymentsEntity rewardsAndPaymentsEntity = new RewardsAndPaymentsEntity(1l, 1l, "watermelon", 1, 75.0, 25l, LocalDate.now());
        rewardsAndPaymentsRepository.save(rewardsAndPaymentsEntity);
        List<RewardsAndPaymentsEntity> returnresponse = rewardsAndPaymentsRepository.findByUserId(2l);
        assertEquals(0, returnresponse.size());
    }

    @Test
    public void test_findTotalRewardPoints_success_scenario(){
        RewardsAndPaymentsEntity rewardsAndPaymentsEntity = new RewardsAndPaymentsEntity(1l, 1l, "watermelon", 1, 75.0, 25l, LocalDate.now());
        rewardsAndPaymentsRepository.save(rewardsAndPaymentsEntity);
        Long rewardPoints = rewardsAndPaymentsRepository.findTotalRewardPoints(1l, LocalDate.now());
        assertEquals(25l, rewardPoints);
    }

    @Test
    public void test_findTotalRewardPoints_failure_scenario(){
        RewardsAndPaymentsEntity rewardsAndPaymentsEntity = new RewardsAndPaymentsEntity(1l, 1l, "watermelon", 1, 75.0, 25l, LocalDate.now());
        rewardsAndPaymentsRepository.save(rewardsAndPaymentsEntity);
        Long rewardPoints = rewardsAndPaymentsRepository.findTotalRewardPoints(2l, LocalDate.now());
        assertEquals(null, rewardPoints);
    }

    @Test
    public void test_findTotalFrequencyRewardPoints_success_scenario(){
        RewardsAndPaymentsEntity rewardsAndPaymentsEntity = new RewardsAndPaymentsEntity(1l, 1l, "watermelon", 1, 75.0, 25l, LocalDate.now());
        RewardsAndPaymentsEntity rewardsAndPaymentsEntity1 = new RewardsAndPaymentsEntity(2l, 1l, "Jackfruit", 1, 75.0, 25l, LocalDate.now().minusMonths(1));
        RewardsAndPaymentsEntity rewardsAndPaymentsEntity2 = new RewardsAndPaymentsEntity(3l, 1l, "Muskmelon", 1, 75.0, 25l, LocalDate.now().minusDays(15));
        rewardsAndPaymentsRepository.saveAll(Arrays.asList(rewardsAndPaymentsEntity, rewardsAndPaymentsEntity1, rewardsAndPaymentsEntity2));
        Long rewardPoints = rewardsAndPaymentsRepository.findTotalFrequencyRewardPoints(1l, LocalDate.now(), LocalDate.now().minusMonths(2));
        assertEquals(75l, rewardPoints);
    }

    @Test
    public void test_findTotalFrequencyRewardPoints_failure_scenario(){
        RewardsAndPaymentsEntity rewardsAndPaymentsEntity = new RewardsAndPaymentsEntity(1l, 1l, "watermelon", 1, 75.0, 25l, LocalDate.now());
        RewardsAndPaymentsEntity rewardsAndPaymentsEntity1 = new RewardsAndPaymentsEntity(2l, 1l, "Jackfruit", 1, 75.0, 25l, LocalDate.now().minusMonths(1));
        RewardsAndPaymentsEntity rewardsAndPaymentsEntity2 = new RewardsAndPaymentsEntity(3l, 1l, "Muskmelon", 1, 75.0, 25l, LocalDate.now().minusDays(15));
        rewardsAndPaymentsRepository.saveAll(Arrays.asList(rewardsAndPaymentsEntity, rewardsAndPaymentsEntity1, rewardsAndPaymentsEntity2));
        Long rewardPoints = rewardsAndPaymentsRepository.findTotalFrequencyRewardPoints(2l, LocalDate.now(), LocalDate.now().minusMonths(2));
        assertEquals(null, rewardPoints);
    }
}