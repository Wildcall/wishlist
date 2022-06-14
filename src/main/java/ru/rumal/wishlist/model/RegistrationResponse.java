package ru.rumal.wishlist.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationResponse {
    @NotNull
    @NotBlank
    @NotEmpty
    @Email(regexp = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$")
    private String email;
    @NotNull
    @NotBlank
    @NotEmpty
    @Min(value = 8)
    private String password;
}
