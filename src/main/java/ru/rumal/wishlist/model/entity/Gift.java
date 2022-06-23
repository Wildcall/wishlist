package ru.rumal.wishlist.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.rumal.wishlist.model.GiftStatus;
import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.GiftDto;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
    private Set<User> giversSet = new HashSet<>();

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "gifts")
    private Set<Event> eventsSet = new HashSet<>();

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    private Tag tag;

    public boolean addEvent(Event event) {
        return this.eventsSet.add(event);
    }

    @Override
    public BaseDto toBaseDto() {
        return new GiftDto(
                this.id,
                this.name,
                this.link,
                this.picture,
                this.description,
                this.status != null ? this.status.name() : null,
                this.eventsSet
                        .stream()
                        .map(Event::getId)
                        .collect(Collectors.toSet()),
                this.tag != null ? this.tag.getId() : null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Gift gift = (Gift) o;

        return id.equals(gift.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
