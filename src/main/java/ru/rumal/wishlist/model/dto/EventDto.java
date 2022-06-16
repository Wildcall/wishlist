package ru.rumal.wishlist.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.rumal.wishlist.model.entity.BaseEntity;
import ru.rumal.wishlist.model.entity.Event;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventDto implements BaseDto {

    @Null(groups = {View.New.class, View.Update.class})
    @JsonView(View.Response.class)
    private Long id;

    @NotNull(groups = {View.New.class, View.Update.class})
    @NotBlank(groups = {View.New.class, View.Update.class})
    @NotEmpty(groups = {View.New.class, View.Update.class})
    @Length(groups = {View.New.class, View.Update.class}, min = 2, max = 255)
    @JsonView(View.Response.class)
    private String name;

    @Length(groups = {View.New.class, View.Update.class}, max = 255)
    @JsonView(View.Response.class)
    private String description;

    @NotNull(groups = {View.New.class, View.Update.class})
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonView(View.Response.class)
    private LocalDateTime date;

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
