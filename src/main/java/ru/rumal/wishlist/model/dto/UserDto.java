package ru.rumal.wishlist.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rumal.wishlist.model.AuthType;
import ru.rumal.wishlist.model.Role;
import ru.rumal.wishlist.model.entity.BaseEntity;
import ru.rumal.wishlist.model.entity.User;
import ru.rumal.wishlist.validation.CustomString;

import javax.validation.constraints.Email;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto implements BaseDto {

    @Null(groups = {View.New.class})
    @CustomString(groups = {View.Update.class, View.UpdatePassword.class})
    @JsonView(View.Response.class)
    private String id;

    @Null(groups = {View.UpdatePassword.class})
    @CustomString(groups = {View.New.class, View.Update.class})
    @Email(groups = {View.New.class, View.Update.class}, regexp = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$")
    @JsonView(View.Response.class)
    private String email;

    @Null(groups = {View.Update.class})
    @CustomString(groups = {View.New.class, View.UpdatePassword.class}, min = 8, max = 24)
    @JsonView(View.Private.class)
    private String password;

    @Null(groups = {View.New.class, View.Update.class})
    @CustomString(groups = {View.UpdatePassword.class}, min = 8, max = 24)
    @JsonView(View.Private.class)
    private String newPassword;

    @Null(groups = {View.UpdatePassword.class})
    @CustomString(groups = {View.New.class, View.Update.class}, min = 2, max = 24)
    @Pattern(groups = {View.New.class, View.Update.class}, regexp = "^[\\s.0-9A-zА-я]*$")
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
