package com.springbank.user.cmd.api.controllers;

import com.springbank.user.cmd.api.commands.RegisterUserCommand;
import com.springbank.user.cmd.api.dto.RegisterUserResponse;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController   //indicate that this is our full controller
@RequestMapping(path = "/api/v1/registerUser")
public class RegisterUserController {

    private final CommandGateway commandGateway;
    //CommandGateway is a command dispatching mechanism that will dispatch the registered user command
    //Once dispatched, it will invoke the command handler constructor for the register  user command.

    @Autowired
    public RegisterUserController(CommandGateway commandGateway){
        this.commandGateway = commandGateway;
    }

    //We are going to return Response entity, return a response Dto as well as HTTP status code.
    // we will pass the RegisterUserCommand
    @PostMapping
    public ResponseEntity<RegisterUserResponse> registerUser(@Valid @RequestBody RegisterUserCommand command){
        var id = UUID.randomUUID().toString();
        command.setId(id);

        try{
//            commandGateway.sendAndWait(command);
            commandGateway.send(command);
            return new ResponseEntity<>(new RegisterUserResponse(id, "User successfully registered!"), HttpStatus.CREATED);

        } catch (Exception e) {
            var safeErrorMessage = "Error while processing register user request for id --: " + command.getId();
            System.out.println(e.toString());

            //return the information
            return new ResponseEntity<>(new RegisterUserResponse(id, safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
