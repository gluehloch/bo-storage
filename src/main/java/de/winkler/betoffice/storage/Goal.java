/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2014 by Andre Winkler. All
 * rights reserved.
 * ============================================================================
 * GNU GENERAL PUBLIC LICENSE TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND
 * MODIFICATION
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package de.winkler.betoffice.storage;

import de.winkler.betoffice.storage.enums.GoalType;

/**
 * GOAL!!!
 *
 * @author by Andre Winkler
 */
public class Goal extends AbstractStorageObject {

    private static final long serialVersionUID = -3103409341667493346L;

    private Long id;
    private int index;
    private GameResult result;
    private Integer minute;
    private Game game;
    private Player player;
    private GoalType goalType;
    private String comment;
    private Long openligaid;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    /**
     * The index is under Hibernate control. Don´t use this method.
     * 
     * @param _index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    public GameResult getResult() {
        return result;
    }

    public void setResult(GameResult result) {
        this.result = result;
    }

    /**
     * @return the minute
     */
    public Integer getMinute() {
        return minute;
    }

    /**
     * @param minute
     *            the minute to set
     */
    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    /**
     * @return the game
     */
    public Game getGame() {
        return game;
    }

    /**
     * @param game
     *            the game to set
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @param player
     *            the player to set
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * @return the goalType
     */
    public GoalType getGoalType() {
        return goalType;
    }

    /**
     * @param goalType
     *            the goalType to set
     */
    public void setGoalType(GoalType goalType) {
        this.goalType = goalType;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment
     *            the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the openligaid
     */
    public Long getOpenligaid() {
        return openligaid;
    }

    /**
     * @param openligaid
     *            the openligaid to set
     */
    public void setOpenligaid(Long openligaid) {
        this.openligaid = openligaid;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Goal [id=" + id + ", index=" + index + ", result=" + result
                + ", minute=" + minute + ", game=" + game + ", player="
                + player + ", goalType=" + goalType + ", comment=" + comment
                + ", openligaid=" + openligaid + "]";
    }

}
