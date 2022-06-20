package ru.rumal.wishlist.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.TagDto;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "_tag")
public class Tag implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ToString.Exclude
    @OneToOne(mappedBy = "tag")
    private Gift gift;

    public Tag(Long id,
               String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public BaseDto toBaseDto() {
        return new TagDto(this.id,
                          this.name);
    }
}
