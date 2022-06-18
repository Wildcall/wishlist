package ru.rumal.wishlist.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.rumal.wishlist.model.GiftStatus;
import ru.rumal.wishlist.model.entity.BaseEntity;
import ru.rumal.wishlist.model.entity.Gift;
import ru.rumal.wishlist.validation.ValueOfEnum;

import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GiftDto implements BaseDto {

    @Null(groups = {View.New.class, View.Update.class})
    @JsonView(View.Response.class)
    private Long id;


    @NotNull(groups = {View.New.class, View.Update.class})
    @NotBlank(groups = {View.New.class, View.Update.class})
    @NotEmpty(groups = {View.New.class, View.Update.class})
    @Length(groups = {View.New.class, View.Update.class}, min = 2, max = 255)
    @JsonView(View.Response.class)
    private String name;

    @Null(groups = {View.New.class, View.Update.class})
    @NotBlank(groups = {View.New.class, View.Update.class}, payload = Payload.class)
    @NotEmpty(groups = {View.New.class, View.Update.class})
    @Length(groups = {View.New.class, View.Update.class}, min = 2, max = 255)
    @JsonView(View.Response.class)
    private String link;

    @Null(groups = {View.New.class, View.Update.class})
    @NotBlank(groups = {View.New.class, View.Update.class})
    @NotEmpty(groups = {View.New.class, View.Update.class})
    @Length(groups = {View.New.class, View.Update.class}, min = 2, max = 255)
    @JsonView(View.Response.class)
    private String picture;

    @Null(groups = {View.New.class, View.Update.class})
    @NotBlank(groups = {View.New.class, View.Update.class})
    @NotEmpty(groups = {View.New.class, View.Update.class})
    @Length(groups = {View.New.class, View.Update.class}, min = 2, max = 255)
    @JsonView(View.Response.class)
    private String description;

    @Null(groups = {View.New.class})
    @NotNull(groups = {View.Update.class})
    @ValueOfEnum(groups = {View.Update.class}, enumClass = GiftStatus.class)
    @JsonView(View.Response.class)
    private GiftStatus status;

    @JsonView(View.Response.class)
    private Long tagId;

    @Override
    public BaseEntity toBaseEntity() {
        Gift gift = new Gift();
        gift.setId(this.id);
        gift.setName(this.name);
        gift.setLink(this.link);
        gift.setPicture(this.picture);
        gift.setDescription(this.description);
        gift.setStatus(this.status);
        return gift;
    }
}
