package ca.tele.chatr.chatrtest.controller;

import ca.tele.chatr.chatrtest.model.RewardsAndPaymentRequest;
import ca.tele.chatr.chatrtest.persistence.ChatRTestRepository;
import ca.tele.chatr.chatrtest.persistence.ChatRTestUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentControllerIntegrationTest {

    private String baseUrl = "http://localhost";

    public static TestRestTemplate testRestTemplate;

    @LocalServerPort
    private Integer port;

    @Autowired
    private ChatRTestRepository chatRTestRepository;

    @Autowired
    private ChatRTestUserRepository chatRTestUserRepository;

    @BeforeAll
    public static void init(){
        testRestTemplate = new TestRestTemplate();
    }

    @BeforeEach
    public void setUp(){
        baseUrl= baseUrl.concat(":"+port).concat("/payment");
    }

    @Test
    @Sql(statements = "DELETE FROM PAYMENTS_AND_REWARDS", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM USER", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void test_success_scenario_savePayment(){
        RewardsAndPaymentRequest rewardsAndPaymentRequest = new RewardsAndPaymentRequest("Suresh", "Television", 150.0, 1);
        String url = baseUrl.concat("/savepayment");
        ResponseEntity<Object> responseEntity = testRestTemplate.postForEntity(url, rewardsAndPaymentRequest, Object.class);
        Map<String, Object> responseBody = (HashMap<String, Object>) responseEntity.getBody();
        Map<String, Object> responseObj = (HashMap<String, Object>)responseBody.get("data");
        Assertions.assertEquals(true, responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(202, responseEntity.getStatusCodeValue());
        Assertions.assertEquals("SUCCESS", responseBody.get("responseStatus"));
        Assertions.assertEquals(150, responseObj.get("rewardPoints"));
        Assertions.assertEquals(1, chatRTestRepository.findAll().size());
        Assertions.assertEquals(1, chatRTestUserRepository.findAll().size());
    }

    @Test
    @Sql(statements = "insert into USER (USER_ID, USER_NAME) values (1, 'suresh')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "insert into PAYMENTS_AND_REWARDS (ID, CREATED_DATE,PRODUCT_NAME, PRODUCT_PRICE,PRODUCT_QUANTITY, REWARD_POINTS,USER_ID) values (100, CURRENT_DATE, 'grapes',100.0, 100, 1000, 1)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM PAYMENTS_AND_REWARDS", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM USER", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void test_success_scenario_getPayment(){
        String url = baseUrl.concat("/getpayments");
        HttpHeaders headers = new HttpHeaders();
        headers.add("userName","suresh");
        HttpEntity<String> httpEntity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, Object.class);
        Map<String, Object> responseBody = (HashMap<String, Object>) responseEntity.getBody();
        List<Object> responseArrayObj = (ArrayList<Object>)responseBody.get("data");
        Map<String, Object> responseObj = (HashMap<String, Object>)responseArrayObj.get(0);
        Assertions.assertEquals(true, responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(200, responseEntity.getStatusCodeValue());
        Assertions.assertEquals("SUCCESS", responseBody.get("responseStatus"));
        Assertions.assertEquals(1000, responseObj.get("rewardPoints"));
        Assertions.assertEquals(1, chatRTestRepository.findAll().size());
        Assertions.assertEquals(1, chatRTestUserRepository.findAll().size());
    }

    @Test
    @Sql(statements = "insert into USER (USER_ID, USER_NAME) values (1, 'suresh')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "insert into PAYMENTS_AND_REWARDS (ID, CREATED_DATE,PRODUCT_NAME, PRODUCT_PRICE,PRODUCT_QUANTITY, REWARD_POINTS,USER_ID) values (100, CURRENT_DATE, 'grapes',100.0, 100, 1000, 1)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM PAYMENTS_AND_REWARDS", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(statements = "DELETE FROM USER", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void test_failed_with_new_user_no_records_found_exception_scenario_getPayment(){
        String url = baseUrl.concat("/getpayments");
        HttpHeaders headers = new HttpHeaders();
        headers.add("userName","naresh");
        HttpEntity<String> httpEntity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<Object> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, Object.class);
        Map<String, Object> responseBody = (HashMap<String, Object>) responseEntity.getBody();
        Assertions.assertEquals(true, responseEntity.getStatusCode().is4xxClientError());
        Assertions.assertEquals("ERROR", responseBody.get("responseStatus"));

    }
}