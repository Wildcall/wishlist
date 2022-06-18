package ru.rumal.wishlist.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.rumal.wishlist.model.GiftStatus;
import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.GiftDto;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "_gift")
public class Gift implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String link;
    private String picture;
    private String description;
    @Enumerated(EnumType.STRING)
    private GiftStatus status;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "givingGiftsSet")
    private Set<User> giversSet;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "gifts")
    private Set<Event> eventsSet;

    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "tag_id", referencedColumnName = "id")
    private Tag tag;

    @Override
    public BaseDto toBaseDto() {
        return new GiftDto(
                this.id,
                this.name,
                this.link,
                this.picture,
                this.description,
                this.status,
                this.tag.getId());
    }
}
