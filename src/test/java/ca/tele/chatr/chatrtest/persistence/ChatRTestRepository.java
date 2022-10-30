package ca.tele.chatr.chatrtest.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ChatRTestRepository extends JpaRepository<RewardsAndPaymentsEntity, Long> {
    @Query(value = "SELECT * FROM PAYMENTS_AND_REWARDS p where p.USER_ID = :userId", nativeQuery = true)
    List<RewardsAndPaymentsEntity> findByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT SUM(p.REWARD_POINTS) FROM PAYMENTS_AND_REWARDS p where p.USER_ID = :userId and p.CREATED_DATE = :createdDate group by p.USER_ID", nativeQuery = true)
    Long findTotalRewardPoints(@Param("userId") Long userId, @Param("createdDate") LocalDate createdDate);

    @Query(value = "SELECT SUM(p.REWARD_POINTS) FROM PAYMENTS_AND_REWARDS p where p.USER_ID = :userId and p.CREATED_DATE >= :toDate and p.CREATED_DATE <= :fromDate group by p.USER_ID", nativeQuery = true)
    Long findTotalFrequencyRewardPoints(@Param("userId") Long userId,
                                        @Param("fromDate") LocalDate fromDate,
                                        @Param("toDate") LocalDate toDate);
}
