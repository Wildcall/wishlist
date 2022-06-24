package ru.rumal.wishlist.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rumal.wishlist.model.GiftStatus;
import ru.rumal.wishlist.model.entity.BaseEntity;
import ru.rumal.wishlist.model.entity.Gift;
import ru.rumal.wishlist.validation.CustomEnum;
import ru.rumal.wishlist.validation.CustomLong;
import ru.rumal.wishlist.validation.CustomString;

import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GiftDto implements BaseDto {

    @Null(groups = {View.New.class, View.Update.class})
    @JsonView(View.Response.class)
    private Long id;

    @CustomString(groups = {View.New.class, View.Update.class}, min = 2)
    @JsonView(View.Response.class)
    private String name;

    @CustomString(groups = {View.New.class, View.Update.class}, min = 2, nullable = true)
    @JsonView(View.Response.class)
    private String link;

    @CustomString(groups = {View.New.class, View.Update.class}, min = 2, nullable = true)
    @JsonView(View.Response.class)
    private String picture;

    @CustomString(groups = {View.New.class, View.Update.class}, min = 2, nullable = true)
    @JsonView(View.Response.class)
    private String description;

    @Null(groups = {View.New.class})
    @CustomEnum(groups = {View.Update.class}, enumClass = GiftStatus.class, nullable = true)
    @JsonView(View.Response.class)
    private String status;

    @CustomLong(groups = {View.New.class}, min = 0, nullable = true)
    @JsonView(View.Private.class)
    private Long eventId;

    @CustomLong(groups = {View.New.class, View.Update.class}, min = 0, nullable = true)
    @JsonView(View.Response.class)
    private Long tagId;

    public GiftDto(Long id,
                   String name,
                   String link,
                   String picture,
                   String description,
                   String status,
                   Long tagId) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.picture = picture;
        this.description = description;
        this.status = status;
        this.tagId = tagId;
    }

    @Override
    public BaseEntity toBaseEntity() {
        Gift gift = new Gift();
        gift.setName(this.name);
        gift.setLink(this.link);
        gift.setPicture(this.picture);
        gift.setDescription(this.description);
        gift.setStatus(status != null ? GiftStatus.valueOf(status) : null);
        return gift;
    }
}
