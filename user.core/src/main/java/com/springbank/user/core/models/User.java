package com.springbank.user.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder //complext builder createre
@Document(collation = "users")  //mongo DB to dabase jason not only sql user documents
public class User {
    @Id  // map the mongo DB field
    private String id;

    @NotEmpty(message="firstname is mandatory")
    private String firstname;

    @NotEmpty(message = "lastname is mandatory")
    private String lastname;

    @Email(message = "Please provide a vaild email address")
    private String emailAddress;
    @NotNull(message = "Please provide the account credentials")
    @Valid
    private Account account;

}
