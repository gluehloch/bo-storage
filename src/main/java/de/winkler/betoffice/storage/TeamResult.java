/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2017 by Andre Winkler. All rights reserved.
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

package de.winkler.betoffice.storage;

import org.apache.commons.lang.Validate;

/**
 * TeamResult verwaltet die errechneten Daten einer Mannschaft für
 * eine Saison und Gruppe. Zwei TeamResults, die im Sinne von
 * <code>compareTo()</code> gleich sind, sind ebenfalls im Sinne
 * der Methode <code>equals()</code> gleich. Das Attribut tabPos
 * wird berechnet und wird für die Auswertung von <code>equals()</code>
 * und <code>hashCode()</code> nicht verwendet.
 *
 * @author Andre Winkler
 */
public class TeamResult implements Comparable<TeamResult> {

    /** serial version id */
    private static final long serialVersionUID = -6690928605407554417L;

    private final Season season;
    
    private final GroupType groupType;

    private final Team team;

    private int posGoals;

    private int negGoals;

    private int win;

    private int lost;

    private int remis;

    private int tablePosition;

    public TeamResult(Season _season, GroupType _groupType, Team _team) {
        season = _season;
        groupType = _groupType;
        team = _team;
    }

    public Season getSeason() {
        return season;
    }

    public GroupType getGroupType() {
        return groupType;
    }

    public Team getTeam() {
        return team;
    }

    /**
     * @return the posGoals
     */
    public int getPosGoals() {
        return posGoals;
    }

    /**
     * @param posGoals the posGoals to set
     */
    public void setPosGoals(int posGoals) {
        this.posGoals = posGoals;
    }

    /**
     * @return the negGoals
     */
    public int getNegGoals() {
        return negGoals;
    }

    /**
     * @param negGoals the negGoals to set
     */
    public void setNegGoals(int negGoals) {
        this.negGoals = negGoals;
    }

    /**
     * @return Berechnet die Tordifferenz.
     */
    private int getGoalDiff() {
        return posGoals - negGoals;
    }

    /**
     * @return the win
     */
    public int getWin() {
        return win;
    }

    /**
     * @param win the win to set
     */
    public void setWin(int win) {
        this.win = win;
    }

    /**
     * @return the lost
     */
    public int getLost() {
        return lost;
    }

    /**
     * @param lost the lost to set
     */
    public void setLost(int lost) {
        this.lost = lost;
    }

    /**
     * @return the remis
     */
    public int getRemis() {
        return remis;
    }

    /**
     * @param remis the remis to set
     */
    public void setRemis(int remis) {
        this.remis = remis;
    }

    public int getPoints() {
        return getWin() * Game.WIN + getRemis() * Game.REMIS;
    }

    public int getGames() {
        return getWin() + getRemis() + getLost();
    }

    public void setTabPos(final int _tablePosition) {
        tablePosition = _tablePosition;
    }

    public int getTabPos() {
        return tablePosition;
    }

    private String getGoalsToString() {
        StringBuilder buf = new StringBuilder();
        buf.append(getPosGoals());
        buf.append(':');
        buf.append(getNegGoals());
        return buf.toString();
    }

    //
    // Comparator Methoden
    //

    /**
     * Vergleicht zwei Objekte team1 und team2 auf deren Rangfolge.
     * Rückgabe von -1, wenn team1 schlechter als team2; +1, wenn team1
     * besser als team2; 0, wenn beide gleich stark.
     * @param team1 Ein TeamResult.
     * @param team2 Ein TeamResult.
     * @return -1,0,+1
     * @throws IllegalArgumentException Null Parameter übergeben.
     */
    public static int isBetterAs(TeamResult team1, TeamResult team2) {
        Validate.notNull(team1, "team1 ist null");
        Validate.notNull(team2, "team2 ist null");

        //
        // Einfacher Punktevergleich
        //
        if (team1.getPoints() > team2.getPoints()) {
            // Team 1 hat mehr Punkte als Team 2.
            return 1;
        } else if (team1.getPoints() < team2.getPoints()) {
            // Team 1 hat weniger Punkte als Team 1.
            return -1;
        }

        //
        // Punktevergleich führte zu Gleichstand, also nun Vergleich des
        // Torverhältnisses.
        //
        if (team1.getGoalDiff() > team2.getGoalDiff()) {
            // Team 1 hat eine besseres Torverhältnis als Team 2.
            return 1;
        } else if (team1.getGoalDiff() < team2.getGoalDiff()) {
            // Team 1 hat ein schlechteres Torverhältnis als Team 1.
            return -1;
        }

        //
        // Vergleich der Tordifferenz führte zu Gleichstand, also nun Vergleich
        // wer die meisten Tore geschossen hat.
        //
        if (team1.getPosGoals() > team2.getPosGoals()) {
            // Team 1 hat mehr Tore geschossen als Team 1.
            return 1;
        } else if (team1.getPosGoals() < team2.getPosGoals()) {
            // Team 1 hat weniger Tore geschossen als Team 2.
            return -1;
        }

        //
        // Beide Mannschaften gleich erfolgreich.
        //
        return 0;
    }

    //
    // Methoden von Comparable
    //

    /**
     * Vergleicht diesen TeamResult mit einem anderen.
     *
     * @param obj Ein TeamResult.
     * @return Wenn '0': Objekte sind gleich.
     */
    public int compareTo(final TeamResult obj) {
        return isBetterAs(obj, this);
    }

    //
    // Methoden von Object
    //

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        if (getTeam() == null) {
            buf.append("<null>");
        } else {
            buf.append(getTeam().getName());
        }
        buf.append(" GroupType: ");
        buf.append(getGroupType().getName());
        buf.append(" Goals:");
        buf.append(getGoalsToString());
        buf.append(" Points:");
        buf.append(getPoints());
        return buf.toString();
    }

    /**
     * @see Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        } else if (!(object instanceof TeamResult)) {
            return false;
        } else {
            TeamResult result = (TeamResult) object;
            if ((result.getTeam().equals(getTeam()))
                    && (result.getSeason().equals(getSeason()))
                    && (result.getGroupType().equals(getGroupType()))
                    && (result.getPosGoals() == getPosGoals())
                    && (result.getNegGoals() == getNegGoals())
                    && (result.getWin() == getWin())
                    && (result.getRemis() == getRemis())
                    && (result.getLost() == getLost())) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + getTeam().hashCode();
        result = 37 * result + getSeason().hashCode();
        result = 37 * result + getGroupType().hashCode();
        result = 37 * result + posGoals;
        result = 37 * result + negGoals;
        result = 37 * result + win;
        result = 37 * result + lost;
        result = 37 * result + remis;
        return result;
    }

}
