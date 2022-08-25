package com.springbank.user.query.api.handlers;

import com.springbank.user.core.events.UserRegisteredEvent;
import com.springbank.user.core.events.UserRemovedEvent;
import com.springbank.user.core.events.UserUpdatedEvent;
import com.springbank.user.query.api.repositories.UserRepository;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//This is the detail implements the function.
//Axon basically manages the consumed offset for each processing group separately.


@Service //it markers the class as service or service provider
@ProcessingGroup("user-group") //means which basically means that when a consumer event handler in our case consumes an event.Axon will track the offset to make sure that with a given processing group you will always consume the latest event
public class UserEventHandlerImpl implements UserEventHandle{

    // The is the very important object for contacted with Database
    private final UserRepository userRepository;

    @Autowired  //so that the spring IOC container can make it available through dependency injection.
    public UserEventHandlerImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    //EventHandler defined the business logic to be performed when an event  is received or consumed from teh EventBus.
    @EventHandler
    @Override
    public void on(UserRegisteredEvent event) {
        userRepository.save(event.getUser()); //save method of the Mongo DB repository can be used for both insert and update data.
    }

    @EventHandler
    @Override
    public void on(UserUpdatedEvent event) {
        userRepository.save(event.getUser());
    }

    @EventHandler
    @Override
    public void on(UserRemovedEvent event) {
        userRepository.deleteById(event.getId());

    }
}
