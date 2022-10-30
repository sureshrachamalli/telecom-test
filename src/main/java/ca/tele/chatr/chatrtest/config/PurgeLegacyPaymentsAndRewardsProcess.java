package ca.tele.chatr.chatrtest.config;

import ca.tele.chatr.chatrtest.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class PurgeLegacyPaymentsAndRewardsProcess {

    @Autowired
    private RewardService rewardService;

    @Scheduled(cron = "0 0 0 * * *")
    public void purgeOlderThanQuarterRewardsAndPayments() {
        rewardService.purgeOlderThanQuarterPaymentAndRewardTxns();
    }
}
