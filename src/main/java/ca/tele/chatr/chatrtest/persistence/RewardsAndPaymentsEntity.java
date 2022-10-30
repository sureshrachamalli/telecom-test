package ca.tele.chatr.chatrtest.persistence;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PAYMENTS_AND_REWARDS")
public class RewardsAndPaymentsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USER_ID")
    private  Long userId;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "PRODUCT_QUANTITY")
    private Integer productQuantity;

    @Column(name = "PRODUCT_PRICE")
    private Double productPrice;

    @Column(name = "REWARD_POINTS")
    private Long rewardPoints;

    @Column(name = "CREATED_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdDate;
}
