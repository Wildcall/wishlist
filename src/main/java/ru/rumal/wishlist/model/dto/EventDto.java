package ru.rumal.wishlist.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rumal.wishlist.model.entity.BaseEntity;
import ru.rumal.wishlist.model.entity.Event;
import ru.rumal.wishlist.validation.CustomString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventDto implements BaseDto {

    @Null(groups = {View.New.class, View.Update.class})
    @JsonView(View.Response.class)
    private Long id;

    @CustomString(groups = {View.New.class, View.Update.class}, min = 2)
    @JsonView(View.Response.class)
    private String name;

    @CustomString(groups = {View.New.class, View.Update.class}, min = 2, nullable = true)
    @JsonView(View.Response.class)
    private String description;

    @NotNull(groups = {View.New.class})
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonView(View.Response.class)
    private LocalDateTime date;

    @Null(groups = {View.New.class})
    @JsonView(View.Response.class)
    private Set<Long> giftsSet;

    @Override
    public BaseEntity toBaseEntity() {
        Event event = new Event();
        event.setId(this.id);
        event.setName(this.name);
        event.setDescription(this.description);
        event.setDate(this.date);
        return event;
    }
}
