package ca.tele.chatr.chatrtest.controller;

import ca.tele.chatr.chatrtest.model.ResponseDto;
import ca.tele.chatr.chatrtest.model.Rewards;
import ca.tele.chatr.chatrtest.service.RewardService;
import ca.tele.chatr.chatrtest.utility.RewardsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/rewards")
public class RewardController {

    @Autowired
    private RewardService rewardService;

    @GetMapping( "/gettotalrewards")
    public ResponseEntity<Object> getTotalRewards(@NotNull @RequestHeader(name = RewardsUtil.USER_NAME) String userName){
        ResponseDto<Rewards> totalRewardForUserPerDay = rewardService.getTotalRewardForUserPerDay(userName);
        return ResponseEntity.status(HttpStatus.OK).body(totalRewardForUserPerDay);
    }

    @GetMapping( "/getmonthlyrewards")
    public ResponseEntity<Object> getMonthlyPayments(@NotNull @RequestHeader(name = RewardsUtil.USER_NAME) String userName){
        ResponseDto<Rewards> totalRewardForUserPerDay = rewardService.getMonthlyRewardsForUser(userName);
        return ResponseEntity.status(HttpStatus.OK).body(totalRewardForUserPerDay);
    }

    @GetMapping( "/getquarterlyRewards")
    public ResponseEntity<Object> getQuarterlyPayments(@NotNull @RequestHeader(name = RewardsUtil.USER_NAME) String userName){
        ResponseDto<Rewards> totalRewardForUserPerDay = rewardService.getQuarterlyRewardsForUser(userName);
        return ResponseEntity.status(HttpStatus.OK).body(totalRewardForUserPerDay);
    }
}
