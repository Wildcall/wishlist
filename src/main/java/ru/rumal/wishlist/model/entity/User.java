package ru.rumal.wishlist.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UserDetails;
import ru.rumal.wishlist.model.dto.BaseDto;
import ru.rumal.wishlist.model.dto.UserDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "_user")
public class User implements BaseEntity {

    @Id
    private String email;
    private String password;
    private String name;
    private String picture;
    private Boolean enable;

    @ToString.Exclude
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Event> events;

    @ToString.Exclude
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Gift> gifts;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "_user_giving_gift",
            joinColumns = @JoinColumn(name = "user_email"),
            inverseJoinColumns = @JoinColumn(name = "gift_id")
    )
    private Set<Gift> givingGiftsSet;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return email != null && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public BaseDto toBaseDto() {
        return new UserDto(this.email,
                           this.password,
                           this.name,
                           this.picture);
    }

    public UserDetails toUserDetails() {
        return new org.springframework.security.core.userdetails.User(
                this.email,
                this.password,
                this.enable,
                true,
                true,
                true,
                new ArrayList<>()
        );
    }
}
