package ru.rumal.wishlist.facade;

import ru.rumal.wishlist.model.RegistrationRequest;
import ru.rumal.wishlist.model.RegistrationResponse;

public interface AuthFacade {
    RegistrationResponse save(RegistrationRequest request);
}
