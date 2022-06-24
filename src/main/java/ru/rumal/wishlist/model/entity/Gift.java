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
    @ManyToMany(mappedBy = "givingGiftsSet", fetch = FetchType.LAZY)
    private Set<User> giversSet;

    @ToString.Exclude
    @ManyToMany(mappedBy = "gifts", fetch = FetchType.LAZY)
    private Set<Event> eventsSet;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    private Tag tag;

    public Gift(Long id) {
        this.id = id;
    }

    public void setEvent(Event event) {
        this.eventsSet = new HashSet<>(1);
        eventsSet.add(event);
    }

    public void setGiver(User user) {
        this.giversSet = new HashSet<>(1);
        giversSet.add(user);
    }

    @Override
    public BaseDto toBaseDto() {
        //  @formatter:off
        return new GiftDto(
                this.id,
                this.name,
                this.link,
                this.picture,
                this.description,
                this.status != null ? this.status.name() : null,
                this.tag != null ? this.tag.getId() : null);
        //  @formatter:on
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
