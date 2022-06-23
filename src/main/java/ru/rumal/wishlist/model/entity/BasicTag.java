package ru.rumal.wishlist.model.entity;

import lombok.*;
import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.BasicTagDto;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_basic_tag")
public class BasicTag implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Override
    public BaseDto toBaseDto() {
        return new BasicTagDto(this.id, this.name);
    }
}
