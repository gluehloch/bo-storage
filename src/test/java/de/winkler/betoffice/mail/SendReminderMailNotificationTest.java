package de.winkler.betoffice.mail;

import java.util.ArrayList;
import java.util.List;

import de.winkler.betoffice.storage.GameTipp;

class SendReminderMailNotificationTest {

    void sortTipps() {
        GameTipp tipp1 = new GameTipp();

        final List<GameTipp> tipps = new ArrayList<>();
        SendReminderMailNotification.sort(tipps);
    }

}
