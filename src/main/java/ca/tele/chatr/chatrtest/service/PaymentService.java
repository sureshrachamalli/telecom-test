package ca.tele.chatr.chatrtest.service;

import ca.tele.chatr.chatrtest.model.ResponseDto;
import ca.tele.chatr.chatrtest.model.RewardsAndPaymentRequest;
import ca.tele.chatr.chatrtest.persistence.RewardsAndPaymentsEntity;

import java.util.List;

public interface PaymentService {

   public final static String SAVE_OPERATION = "SAVE";
   public final static String FETCH_OPERATION = "FETCH";

   ResponseDto<RewardsAndPaymentsEntity> saveRewardsAndPayments(RewardsAndPaymentRequest rewardsAndPaymentRequest) throws Error;

   ResponseDto<List<RewardsAndPaymentsEntity>> getRewardsAndPayments(String userName);

   Long provideUserId(String userName, String operation);


}
