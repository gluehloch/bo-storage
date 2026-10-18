package de.betoffice.service.request;

import de.betoffice.storage.user.entity.Nickname;

/**
 * User Update Command
 * 
 * @param adminOperation    Administrator Aktion? Dieser kann die Mail-Adresse beliebig ändern.
 * @param nickname          Nutzerkürzel
 * @param name              Name
 * @param surname           Vorname
 * @param mail              Mail Adresse
 * @param emailNotification Email Benachrichtigung einschalten?
 * @param phone             Telefonnummer
 * 
 */
public record UserUpdateCommand(
        boolean adminOperation,
        Nickname nickname,
        String name,
        String surname,
        String mail,
        boolean emailNotification,
        String phone) {

}
