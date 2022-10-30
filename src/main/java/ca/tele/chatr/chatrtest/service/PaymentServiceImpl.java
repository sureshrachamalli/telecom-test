package ca.tele.chatr.chatrtest.service;

import ca.tele.chatr.chatrtest.exception.BadRequestException;
import ca.tele.chatr.chatrtest.exception.NoRecordsFoundException;
import ca.tele.chatr.chatrtest.exception.UnHandeledEntityException;
import ca.tele.chatr.chatrtest.model.ResponseDto;
import ca.tele.chatr.chatrtest.model.RewardsAndPaymentRequest;
import ca.tele.chatr.chatrtest.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Value("${reward.threshold.min:50}")
    private Integer rewardThresholdMin;

    @Value("${reward.threshold.max:100}")
    private Integer rewardThresholdMax;

    @Autowired
    private RewardsAndPaymentsRepository rewardsAndPaymentsRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     *  Description: Allows RewardsAndPaymentRequest after all validations from controller and fetch the userId from user table, also calculates the rewards for given entity
     *               finally if all above two scenarios executes successful saves entity into DB.
     * @param rewardsAndPaymentRequest
     * @return ResponseDto<RewardsAndPaymentsEntity>
     * @throws Error
     */
    @Override
    public ResponseDto<RewardsAndPaymentsEntity> saveRewardsAndPayments(RewardsAndPaymentRequest rewardsAndPaymentRequest) throws Error{
        ResponseDto<RewardsAndPaymentsEntity> response = null;
        if(null == rewardsAndPaymentRequest){
            throw new BadRequestException("Request body is null");
        }
        try {
            RewardsAndPaymentsEntity rewardsAndPaymentsEntity = buildPaymentEntity(rewardsAndPaymentRequest,provideUserId(rewardsAndPaymentRequest.getUserName(), SAVE_OPERATION));
            Integer rewardPoints = calculateRewardsOnPurchase(rewardsAndPaymentRequest);
            rewardsAndPaymentsEntity.setRewardPoints(rewardPoints.longValue());
            response = ResponseDto.forSuccess(rewardsAndPaymentsRepository.save(rewardsAndPaymentsEntity));
        }catch (Exception e){
            log.error("Exception occured while save the transaction, exception is::  {}", e.fillInStackTrace());
            throw new UnHandeledEntityException("New Exception Occured"+e.getMessage());
        }

        return response;
    }

    /**
     *  Description: Returns the RewardsAndPaymentEntity for given userName if exists else throws NoRecordFoundException.
     * @param userName
     * @return ResponseDto<List<RewardsAndPaymentsEntity>>
     * @throws Error
     */
    @Override
    public ResponseDto<List<RewardsAndPaymentsEntity>> getRewardsAndPayments(String userName) {
        List<RewardsAndPaymentsEntity> rewardsAndPaymentsEntityList = new ArrayList();
        try {
            rewardsAndPaymentsEntityList= rewardsAndPaymentsRepository.findByUserId(provideUserId(userName, FETCH_OPERATION));

            if(rewardsAndPaymentsEntityList.size() == 0){
                throw new NoRecordsFoundException("No Records Found, with User :: "+userName);
            }
        }catch (Exception e){
            log.error("Exception occured while retrieving the rewardsAndPaymentsEntityList, exception is::  {}", e.fillInStackTrace());
            throw new UnHandeledEntityException("New Exception Occured"+e.getMessage());
        }
        return ResponseDto.forSuccess(rewardsAndPaymentsEntityList);
    }

    /**
     *  Description: Returns the userId for given userName & operationName.
     *              If userName exists returns it irrespective of Operation else if save operation creates userId and returns ID or else throw BadRequestException.
     * @param userName, operationName(FETCH/SAVE)
     * @return userId
     * @throws Error
     */
    @Override
    public Long provideUserId(String userName, String operationName){
        UserEntity userExistsEntity = userRepository.findByUserName(userName.toLowerCase());
        Long userId = null;
        try{
            if(null != userExistsEntity){
                userId= userExistsEntity.getId();
            }else if(operationName.equalsIgnoreCase(SAVE_OPERATION)){
                UserEntity userEntity = new UserEntity();
                userEntity.setUserName(userName.toLowerCase());
                userId= userRepository.save(userEntity).getId();
            }else if(operationName.equalsIgnoreCase(FETCH_OPERATION)){
                throw new BadRequestException("No user found with name :: "+userName);
            }
        }catch (BadRequestException e){
            log.error("BadRequest exception:: {}", e.fillInStackTrace());
            throw new BadRequestException("No user found with name :: "+userName);
        }catch (Exception e){
            log.error("Exception Occured at provideUserId () :: {}", e.fillInStackTrace());
            throw new UnHandeledEntityException("New Exception Occured"+e.getMessage());
        }

        return userId;

    }

    /**
     * Description: bind the RewardsAndPaymentRequest & userId to RewardsAndPaymentsEntity and returns the object.
     * @param rewardsAndPaymentRequest
     * @param userId
     * @return RewardsAndPaymentsEntity
     */
    private RewardsAndPaymentsEntity buildPaymentEntity(RewardsAndPaymentRequest rewardsAndPaymentRequest, Long userId){
        RewardsAndPaymentsEntity rewardsAndPaymentsEntity = new RewardsAndPaymentsEntity();
        rewardsAndPaymentsEntity.setUserId(userId);
        rewardsAndPaymentsEntity.setProductName(rewardsAndPaymentRequest.getProductName());
        rewardsAndPaymentsEntity.setProductQuantity(rewardsAndPaymentRequest.getProductQuantity());
        rewardsAndPaymentsEntity.setProductPrice(rewardsAndPaymentRequest.getProductPrice());
        rewardsAndPaymentsEntity.setCreatedDate(LocalDate.now());
        return rewardsAndPaymentsEntity;
    }

    /**
     * Description: Calculates 2 points for every dollar spent over $100 in each transaction, plus 1 point for every dollar spent over $50 in each transaction.
     * @param rewardsAndPaymentRequest
     * @return RewardPoints
     */
    private Integer calculateRewardsOnPurchase(RewardsAndPaymentRequest rewardsAndPaymentRequest){
        Integer rewardPoints = 0;
        if(rewardsAndPaymentRequest.getProductPrice() > 0.0 && rewardsAndPaymentRequest.getProductQuantity() > 0 ){
            try{
                Double totalPurchaseAmount = rewardsAndPaymentRequest.getProductPrice() * rewardsAndPaymentRequest.getProductQuantity();
                if( totalPurchaseAmount > rewardThresholdMin && totalPurchaseAmount <= rewardThresholdMax){
                    rewardPoints = totalPurchaseAmount.intValue();
                }else if(totalPurchaseAmount > rewardThresholdMax){
                    rewardPoints = ((totalPurchaseAmount.intValue() - rewardThresholdMax) * 2) + rewardThresholdMin;
                }
            }catch (Exception e){
                log.error("Unable to calculate Rewards :: "+e.fillInStackTrace());
                throw new UnHandeledEntityException("Unable to calculate Rewards :: "+e.getMessage());
            }

            return  rewardPoints;
        }
        return rewardPoints;
    }
}
