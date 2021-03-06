package ru.rumal.wishlist.model.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.EventDto;
import ru.rumal.wishlist.model.dto.GiftDto;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "_event")
public class Event implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private LocalDateTime date;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "_event_gift",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "gift_id"))
    private Set<Gift> gifts = new HashSet<>();

    public Event(Long id) {
        this.id = id;
    }

    @Override
    public BaseDto toBaseDto() {
        //  @formatter:off
        return new EventDto(this.id,
                            this.name,
                            this.description,
                            this.date,
                            this.gifts
                                    .stream()
                                    .map(Gift::toBaseDto)
                                    .map(baseDto -> (GiftDto) baseDto)
                                    .collect(Collectors.toSet()));
        //  @formatter:on
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        return id.equals(event.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public void addGift(@NotNull Gift gift) {
        this.gifts.add(gift);
    }

    public void addGift(Set<Gift> gifts) {
        this.gifts = gifts;
    }
}
