package ru.rumal.wishlist.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rumal.wishlist.model.entity.BaseEntity;
import ru.rumal.wishlist.model.entity.BasicTag;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicTagDto implements BaseDto {

    @JsonView({View.Response.class})
    private Long id;

    @JsonView({View.Response.class})
    private String name;

    @Override
    public BaseEntity toBaseEntity() {
        return new BasicTag(this.id, this.name);
    }
}
