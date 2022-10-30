package ca.tele.chatr.chatrtest.service;

import ca.tele.chatr.chatrtest.exception.NoRecordsFoundException;
import ca.tele.chatr.chatrtest.model.ResponseDto;
import ca.tele.chatr.chatrtest.model.Rewards;
import ca.tele.chatr.chatrtest.persistence.RewardsAndPaymentsRepository;
import ca.tele.chatr.chatrtest.utility.RewardsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class RewardServiceImpl implements RewardService{

    @Autowired
    private RewardsAndPaymentsRepository rewardsAndPaymentsRepository;

    @Autowired
    private PaymentService paymentService;

    @Override
    public ResponseDto<Rewards> getTotalRewardForUserPerDay(String userName) {
        Long totalRewards = null;
        try{
            totalRewards = rewardsAndPaymentsRepository.findTotalRewardPoints(paymentService.provideUserId(userName, paymentService.FETCH_OPERATION), LocalDate.now());
        }catch (Exception e){
            log.error("getTotalRewardForUserPerDay(), No records Found with UserName :: {}",userName);
            throw new NoRecordsFoundException("No records Found with UserName"+userName);
        }
        return ResponseDto.forSuccess(new Rewards(RewardsUtil.DAILY, totalRewards, userName));
    }

    @Override
    public ResponseDto<Rewards> getMonthlyRewardsForUser(String userName) {
        Long totalRewards = null;
        try{
         totalRewards = rewardsAndPaymentsRepository.findTotalFrequencyRewardPoints(paymentService.provideUserId
                (userName, paymentService.FETCH_OPERATION), LocalDate.now(), LocalDate.now().minusMonths(1));
        }catch (Exception e){
            log.error("getMonthlyRewardsForUser(), No records Found with UserName :: {}",userName);
            throw new NoRecordsFoundException("No records Found with UserName"+userName);
        }
        return ResponseDto.forSuccess(new Rewards(RewardsUtil.MONTHLY, totalRewards, userName));
    }

    @Override
    public ResponseDto<Rewards> getQuarterlyRewardsForUser(String userName) {
        Long totalRewards = null;
        try{
          totalRewards = rewardsAndPaymentsRepository.findTotalFrequencyRewardPoints(paymentService.provideUserId(userName, paymentService.FETCH_OPERATION), LocalDate.now(), LocalDate.now().minusMonths(3));
        }catch (Exception e){
            log.error("getQuarterlyRewardsForUser(), No records Found with UserName :: {}",userName);
            throw new NoRecordsFoundException("No records Found with UserName"+userName);
        }
        return ResponseDto.forSuccess(new Rewards(RewardsUtil.QUARTERLY, totalRewards, userName));
    }

    @Override
    public void purgeOlderThanQuarterPaymentAndRewardTxns(){
        log.info("Purge process for the older than date {} was triggered..",LocalDate.now().minusMonths(3));
        rewardsAndPaymentsRepository.deleteByCreatedDateBefore(LocalDate.now().minusMonths(3));
    }
}
