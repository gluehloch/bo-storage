package de.betoffice.service.request;

import de.betoffice.storage.user.entity.Nickname;

public record UserCreateCommand(String nickname, String firstName, String lastName, String email, String password, String phone) {

    public Nickname toNickname() {
        return Nickname.of(nickname);
    }

}
