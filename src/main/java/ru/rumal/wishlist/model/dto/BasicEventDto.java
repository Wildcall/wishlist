package ru.rumal.wishlist.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rumal.wishlist.model.entity.BaseEntity;
import ru.rumal.wishlist.model.entity.BasicEvent;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicEventDto implements BaseDto {

    @JsonView({View.Response.class})
    private Long id;

    @JsonView({View.Response.class})
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonView({View.Response.class})
    private LocalDateTime date;

    @Override
    public BaseEntity toBaseEntity() {
        return new BasicEvent(this.id, this.name, this.date);
    }
}
