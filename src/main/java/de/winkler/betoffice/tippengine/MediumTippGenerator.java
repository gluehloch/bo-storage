/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2016 by Andre Winkler. All rights reserved.
 * ============================================================================
 *          GNU GENERAL PUBLIC LICENSE
 *  TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package de.winkler.betoffice.tippengine;

import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameResult;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.enums.TippStatusType;

import java.util.Iterator;
import java.util.List;

/**
 * Erster Automat des Wettbüros. Der Automat simuliert einen
 * Teilnehmer. Aus allen Tipps zu einem Spiel ermittelt der
 * Automat den Durchschnitt, rundet das Ergebnis ab und verwendet
 * das Ergebnis als eigenen Tipp.
 *
 * @author Andre Winkler
 */
public class MediumTippGenerator implements TippGenerator {

    private final String BOT_MEDIUM_TIPP = "#BOT_MEDIUM_TIPP#";
    
	/** Der Teilnehmer für den die Tipps generiert werden. */
	private final User user;

	/**
	 * Konstruktor.
	 *
	 * @param _user Der zu tippende Teilnehmer.
	 */
	public MediumTippGenerator(final User _user) {
		user = _user;
	}

	public void generateTipp(final Season season) {
		List<GameList> gameDays = season.unmodifiableGameList();
		for (Iterator<GameList> i = gameDays.listIterator(); i.hasNext();) {
			generateTipp(i.next());
		}
	}

	public void generateTipp(final GameList round) {
		List<Game> games = round.unmodifiableList();
		for (Game game : games) {
			GameResult gr = InfoCenter.getMediumTipp(game);
			if (gr != null) {
				game.addTipp(BOT_MEDIUM_TIPP, user, gr, TippStatusType.AUTO);
			}
		}
	}

}
