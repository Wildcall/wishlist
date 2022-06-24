package ru.rumal.wishlist.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
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
    @JsonView(View.Private.class)
    private Set<Long> giftsIdSet;

    @Null(groups = {View.New.class, View.Update.class})
    @JsonView(View.Response.class)
    private Set<GiftDto> giftsSet;

    public EventDto(Long id,
                    String name,
                    String description,
                    LocalDateTime date,
                    Set<GiftDto> giftsSet) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.giftsSet = giftsSet;
    }

    @Override
    public BaseEntity toBaseEntity() {
        Event event = new Event();
        event.setName(this.name);
        event.setDescription(this.description);
        event.setDate(this.date);
        return event;
    }
}
