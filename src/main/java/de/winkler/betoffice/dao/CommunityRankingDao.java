package de.winkler.betoffice.dao;

import de.winkler.betoffice.storage.*;

import java.util.Collection;
import java.util.List;

public interface CommunityRankingDao {

    /**
     * Berechnet das Tipper-Ranking für eine Meisterschaft.
     *
     * @param  users       Diese Teilnehmer werden im Ergebnis erwartet.
     * @param  season      Die betreffende Meisterschaft.
     * @param  seasonRange Season Range.
     * @return             Das Ranking der Tipper.
     */
    List<UserResult> calculateUserRanking(Collection<User> users, Season season, SeasonRange seasonRange);

    /**
     * Berechnet das Tipper-Ranking für eine Meisterschaft.
     *
     * @param  users  Diese Teilnehmer werden im Ergebnis erwartet.
     * @param  season Die betreffende Meisterschaft.
     * @return        Das Ranking der Tipper.
     */
    List<UserResult> calculateUserRanking(Collection<User> users, Season season);

    /**
     * Berechnet das Tipper-Ranking für einen Spieltag.
     *
     * @param  users Diese Teilnehmer werden im Ergebnis erwartet.
     * @param  round Der betreffende Spieltag.
     * @return       Das Ranking der Tipper.
     */
    List<UserResult> calculateUserRanking(Collection<User> users, GameList round);

}
