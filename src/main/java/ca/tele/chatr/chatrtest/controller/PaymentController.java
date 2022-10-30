package ca.tele.chatr.chatrtest.controller;

import ca.tele.chatr.chatrtest.model.ResponseDto;
import ca.tele.chatr.chatrtest.model.RewardsAndPaymentRequest;
import ca.tele.chatr.chatrtest.persistence.RewardsAndPaymentsEntity;
import ca.tele.chatr.chatrtest.service.PaymentService;
import ca.tele.chatr.chatrtest.utility.RewardsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/savepayment")
    public ResponseEntity<Object> savePayment(@Validated @RequestBody RewardsAndPaymentRequest rewardsAndPaymentRequest){
        ResponseDto<RewardsAndPaymentsEntity> paymentsAndRewardsEntity = paymentService.saveRewardsAndPayments(rewardsAndPaymentRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(paymentsAndRewardsEntity);
    }

    @GetMapping( "/getpayments")
    public ResponseEntity<Object> getPayments(@RequestHeader(name = RewardsUtil.USER_NAME, required = true) String userName) throws Exception{
        ResponseDto<List<RewardsAndPaymentsEntity>> paymentsAndRewardsEntityList = paymentService.getRewardsAndPayments(userName);
        return ResponseEntity.status(HttpStatus.OK).body(paymentsAndRewardsEntityList);
    }
}
