package ca.tele.chatr.chatrtest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RewardsAndPaymentRequest {
    @NotNull(message = "UserId should not be empty.")
    @NotEmpty(message = "UserId should not be empty.")
    private String userName;

    @NotNull(message = "ProductName should not be empty.")
    @NotEmpty(message = "ProductName should not be empty.")
    private String productName;

    @NotNull
    @Min(value = 1, message = "Enter Product Price more than 0")
    private Double productPrice;

    @NotNull
    @Min(value = 1, message = "Enter Product Quantity more than 0")
    private Integer productQuantity;
}
