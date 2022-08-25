package com.springbank.user.cmd.api.commands;


import com.springbank.user.core.models.User;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class RegisterUserCommand {
    @TargetAggregateIdentifier   //which instance of an aggregate type should handle the command message
    private String id;
    @NotNull(message = "no user details were supplied")
    @Valid
    private User user;



}
