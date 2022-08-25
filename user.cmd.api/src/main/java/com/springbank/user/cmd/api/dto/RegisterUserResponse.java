package com.springbank.user.cmd.api.dto;

//Because we also want to return a message when register a user
//Return a ID to show that the user had registered.
public class RegisterUserResponse extends BaseResponse{
    private String id;
    public RegisterUserResponse(String id, String message) {
        super(message);
        this.id=id;
    }
}
