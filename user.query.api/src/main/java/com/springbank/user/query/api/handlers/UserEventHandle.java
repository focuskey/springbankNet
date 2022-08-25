package com.springbank.user.query.api.handlers;

import com.springbank.user.core.events.UserRegisteredEvent;
import com.springbank.user.core.events.UserRemovedEvent;
import com.springbank.user.core.events.UserUpdatedEvent;

// In this query.api also handler the same typeEvent. which handed handled at the cmd.api UserAggregated.java class
// So that is means after the com.api Event finished (in fact in that @EventSourcingHandler  only pass the data property.
public interface UserEventHandle {
    void on(UserRegisteredEvent event);
    void on(UserUpdatedEvent event);
    void on(UserRemovedEvent event);
}
