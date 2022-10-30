package ca.tele.chatr.chatrtest.service;

import ca.tele.chatr.chatrtest.model.ResponseDto;
import ca.tele.chatr.chatrtest.model.Rewards;

public interface RewardService {
    ResponseDto<Rewards> getTotalRewardForUserPerDay(String userName);
    ResponseDto<Rewards> getMonthlyRewardsForUser(String userName);
    ResponseDto<Rewards> getQuarterlyRewardsForUser(String userName);
    void purgeOlderThanQuarterPaymentAndRewardTxns();

}
