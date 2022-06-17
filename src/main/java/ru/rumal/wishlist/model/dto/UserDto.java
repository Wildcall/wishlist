package ru.rumal.wishlist.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.rumal.wishlist.model.AuthType;
import ru.rumal.wishlist.model.entity.BaseEntity;
import ru.rumal.wishlist.model.entity.User;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto implements BaseDto {

    @Null(groups = {View.New.class})
    @NotNull(groups = {View.Update.class})
    @NotBlank(groups = {View.Update.class})
    @NotEmpty(groups = {View.Update.class})
    @JsonView(View.Response.class)
    private String id;

    @NotNull(groups = {View.New.class, View.Update.class})
    @NotBlank(groups = {View.New.class, View.Update.class})
    @NotEmpty(groups = {View.New.class, View.Update.class})
    @Email(groups = {View.New.class, View.Update.class}, regexp = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$")
    @JsonView(View.Response.class)
    private String email;

    @Null(groups = {View.Update.class})
    @NotNull(groups = {View.New.class})
    @NotBlank(groups = {View.New.class})
    @NotEmpty(groups = {View.New.class})
    @Length(groups = {View.New.class}, min = 8, max = 24)
    @JsonView(View.Private.class)
    private String password;

    @NotNull(groups = {View.New.class, View.Update.class})
    @NotBlank(groups = {View.New.class, View.Update.class})
    @NotEmpty(groups = {View.New.class, View.Update.class})
    @Length(groups = {View.New.class, View.Update.class}, min = 2, max = 24)
    @JsonView(View.Response.class)
    private String name;

    @Null(groups = {View.New.class})
    @NotNull(groups = {View.Update.class})
    @NotBlank(groups = {View.Update.class})
    @NotEmpty(groups = {View.Update.class})
    @JsonView(View.Response.class)
    private String picture;

    @Null(groups = {View.New.class, View.Update.class})
    @JsonView(View.Response.class)
    private AuthType authType;

    @Null(groups = {View.New.class, View.Update.class})
    @JsonView(View.Private.class)
    private Boolean enable;

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
        return user;
    }
}
