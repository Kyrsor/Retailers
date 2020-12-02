package by.itechart.retailers.dto;

import by.itechart.retailers.entity.DeletedStatus;
import by.itechart.retailers.entity.LocationProduct;
import by.itechart.retailers.entity.LocationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LocationDto {

    private Long id;

    @NotBlank(message = "Location identifier can't be empty.")
    @Size(min = 1, max = 20, message = "Identifier can be from 1 to 20 symbols.")
    private String identifier;

    private CustomerDto customer;

    @Valid
    private AddressDto address;

    @Min(value = 1, message = "Total capacity must be greater than 0.")
    private Integer totalCapacity;

    @Min(value = 1, message = "Available capacity must be greater than 0.")
    private Integer availableCapacity;

    @NotBlank(message = "Location type can't be empty.")
    private LocationType locationType;

    @DecimalMin(value = "0", message = "Tax must be equals or greater than 0.")
    private BigDecimal locationTax;

    private DeletedStatus status;
}
