package com.springbank.user.cmd.api.aggregats;
// Maybe the most important class in  this bank project. Because it hander the command hander
// And it can also create the Event and send the event to the Event Bus


import com.springbank.user.cmd.api.commands.RegisterUserCommand;
import com.springbank.user.cmd.api.commands.RemoveUserCommand;
import com.springbank.user.cmd.api.commands.UpdateUserCommand;
import com.springbank.user.cmd.api.security.PasswordEncoder;
import com.springbank.user.cmd.api.security.PasswordEncoderImpl;
import com.springbank.user.core.events.UserRegisteredEvent;
import com.springbank.user.core.events.UserRemovedEvent;
import com.springbank.user.core.events.UserUpdatedEvent;
import com.springbank.user.core.models.User;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.UUID;

@Aggregate   //to tell the Axon framework that this is an aggregate for the userCommandAPI that is means this object can handler the command.
public class UserAggregate {
    @AggregateIdentifier
    private String id;
    private User user;

    private final PasswordEncoder passwordEncoder;

    //Axon required that each aggregate object contains two constructor, one take no arguments,
    public UserAggregate(){
        passwordEncoder = new PasswordEncoderImpl();

    }

    // another one take the Command Object as a parameter.
    @CommandHandler
    public UserAggregate(RegisterUserCommand command){
        var newUser = command.getUser();
        newUser.setId(command.getId());
        var password = newUser.getAccount().getPassword();

        passwordEncoder = new PasswordEncoderImpl();

        //Hash the passwd, Can't save it into the database as plain text.
        var hashedPassword = passwordEncoder.hashPassword(password);

        //replace the plaintext password with the hash password and then we need to
        newUser.getAccount().setPassword(hashedPassword);

        // instantiate a new event using a fluid builder
        //raise the event
        var event = UserRegisteredEvent.builder()
                .id(command.getId())
                .user(newUser)
                .build(); //return the constructed user registered event,

        //send event to the eventbus, will be handler by somehandler functio
        // it will store the event in the eventStore and publish it into the event bus
        AggregateLifecycle.apply(event);
    }

    // to handle the other type of command
    @CommandHandler
    public void handle(UpdateUserCommand command){
        var updatedUser = command.getUser();  //define a new updateUser to transer the data
        updatedUser.setId(command.getId());
        var password = updatedUser.getAccount().getPassword();
        // Get the plain passwd and changed to the hashed passed
        var hashedPassword = passwordEncoder.hashPassword(password);
        updatedUser.getAccount().setPassword(hashedPassword);

        //raise the update event with random updateID
        var event = UserUpdatedEvent.builder()
                .id(UUID.randomUUID().toString())
                .user(updatedUser)
                .build();

        AggregateLifecycle.apply(event);
    }


    @CommandHandler
    public void handle(RemoveUserCommand command){
        var event = new UserRemovedEvent();
        event.setId(command.getId());

        AggregateLifecycle.apply(event);
    }

    // Told the Axon framework that this function should be called when the aggregate is sourced from the events
    // As all the events sourcing handlers combined will from the aggregate. this is where all the state changes are going to happen.
    // Because all the command changed the state. Command  -> Event ( Happened)
    @EventSourcingHandler
    public void on(UserRegisteredEvent event){
        this.id = event.getId();
        this.user = event.getUser();
        //Event & Command has the same property.
    }

    @EventSourcingHandler
    public void on(UserUpdatedEvent event){
        this.user = event.getUser();
        // this time we just update the user field of the aggregate since the correct aggregate instance was  already selected when the update user command was handled.
        // this is the @TargetAggregateIdentifier annotation on the command object into play.

    }

    @EventSourcingHandler
    public void on(UserRemovedEvent event){
        AggregateLifecycle.markDeleted();
    }

}
