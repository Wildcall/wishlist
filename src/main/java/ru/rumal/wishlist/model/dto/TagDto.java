package ru.rumal.wishlist.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rumal.wishlist.model.entity.BaseEntity;
import ru.rumal.wishlist.model.entity.Tag;
import ru.rumal.wishlist.validation.CustomString;

import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TagDto implements BaseDto {

    @Null(groups = {View.New.class, View.Update.class})
    @JsonView(View.Response.class)
    private Long id;

    @CustomString(groups = {View.New.class, View.Update.class}, min = 2)
    @JsonView(View.Response.class)
    private String name;

    @Override
    public BaseEntity toBaseEntity() {
        return new Tag(this.id,
                       this.name);
    }
}
