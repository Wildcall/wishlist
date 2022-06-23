package ru.rumal.wishlist.model.entity;

import lombok.*;
import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.BasicEventDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_basic_event")
public class BasicEvent implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDateTime date;

    @Override
    public BaseDto toBaseDto() {
        return new BasicEventDto(this.id,
                                 this.name,
                                 this.date);
    }
}
