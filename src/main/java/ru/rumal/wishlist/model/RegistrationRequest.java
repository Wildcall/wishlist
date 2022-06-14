package ru.rumal.wishlist.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationRequest {
    @NotNull
    @NotBlank
    @NotEmpty
    @Email(regexp = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$")
    private String email;
    @NotNull
    @NotBlank
    @NotEmpty
    @Length(min = 8, max = 24)
    private String password;
    @NotNull
    @NotBlank
    @NotEmpty
    @Length(min = 2, max = 24)
    private String name;
}
