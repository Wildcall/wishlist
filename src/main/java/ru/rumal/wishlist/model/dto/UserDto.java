package ru.rumal.wishlist.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.rumal.wishlist.model.AuthType;
import ru.rumal.wishlist.model.Role;
import ru.rumal.wishlist.model.entity.BaseEntity;
import ru.rumal.wishlist.model.entity.User;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto implements BaseDto {

    @Null(groups = {View.New.class})
    @NotNull(groups = {View.Update.class, View.UpdatePassword.class})
    @NotBlank(groups = {View.Update.class, View.UpdatePassword.class})
    @NotEmpty(groups = {View.Update.class, View.UpdatePassword.class})
    @JsonView(View.Response.class)
    private String id;

    @Null(groups = {View.UpdatePassword.class})
    @NotNull(groups = {View.New.class, View.Update.class})
    @NotBlank(groups = {View.New.class, View.Update.class})
    @NotEmpty(groups = {View.New.class, View.Update.class})
    @Email(groups = {View.New.class, View.Update.class}, regexp = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$")
    @JsonView(View.Response.class)
    private String email;

    @Null(groups = {View.Update.class})
    @NotNull(groups = {View.New.class, View.UpdatePassword.class})
    @NotBlank(groups = {View.New.class, View.UpdatePassword.class})
    @NotEmpty(groups = {View.New.class, View.UpdatePassword.class})
    @Length(groups = {View.New.class, View.UpdatePassword.class}, min = 8, max = 24)
    @JsonView(View.Private.class)
    private String password;

    @Null(groups = {View.New.class, View.Update.class})
    @NotNull(groups = {View.UpdatePassword.class})
    @NotBlank(groups = {View.UpdatePassword.class})
    @NotEmpty(groups = {View.UpdatePassword.class})
    @Length(groups = {View.UpdatePassword.class}, min = 8, max = 24)
    @JsonView(View.Private.class)
    private String newPassword;

    @Null(groups = {View.UpdatePassword.class})
    @NotNull(groups = {View.New.class, View.Update.class})
    @NotBlank(groups = {View.New.class, View.Update.class})
    @NotEmpty(groups = {View.New.class, View.Update.class})
    @Pattern(groups = {View.New.class, View.Update.class}, regexp = "^[\\s.0-9A-zА-я]*$")
    @Length(groups = {View.New.class, View.Update.class}, min = 2, max = 24)
    @JsonView(View.Response.class)
    private String name;

    @Null(groups = {View.New.class, View.Update.class, View.UpdatePassword.class})
    @JsonView(View.Response.class)
    private String picture;

    @Null(groups = {View.New.class, View.Update.class, View.UpdatePassword.class})
    @JsonView(View.Response.class)
    private AuthType authType;

    @Null(groups = {View.New.class, View.Update.class, View.UpdatePassword.class})
    @JsonView(View.Private.class)
    private Boolean enable;

    @Null(groups = {View.New.class, View.Update.class, View.UpdatePassword.class})
    @JsonView(View.Response.class)
    private Role role;

    @Override
    public BaseEntity toBaseEntity() {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setPicture(picture);
        user.setAuthType(authType);
        user.setEnable(enable);
        user.setRole(role);
        return user;
    }
}
