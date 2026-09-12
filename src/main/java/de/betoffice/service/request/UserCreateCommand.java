package de.betoffice.service.request;

public record UserCreateCommand(String nickname, String firstName, String lastName, String email, String password) {

}
