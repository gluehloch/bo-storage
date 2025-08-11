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

package de.betoffice.storage.season.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import de.betoffice.storage.AbstractStorageObject;
import de.betoffice.storage.season.GoalType;

/**
 * GOAL!!!
 *
 * @author by Andre Winkler
 */
@Entity
@Table(name = "bo_goal")
public class Goal extends AbstractStorageObject {

    private static final long serialVersionUID = -3103409341667493346L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "bo_index")
    private int index;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(column = @Column(name = "bo_homegoals"), name = "homeGoals"),
            @AttributeOverride(column = @Column(name = "bo_guestgoals"), name = "guestGoals")
    })
    private GameResult result;

    @Column(name = "bo_minute")
    private Integer minute;

    @ManyToOne
    @JoinColumn(name = "bo_game_ref")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "bo_player_ref")
    private Player player;

    @Enumerated
    @Column(name = "bo_goaltype")
    private GoalType goalType;

    @Column(name = "bo_comment")
    private String comment;

    @Column(name = "bo_openligaid")
    private Long openligaid;

    // -- id ------------------------------------------------------------------

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

    // ------------------------------------------------------------------------

    public int getIndex() {
        return index;
    }

    /**
     * The index is under Hibernate control. Don´t use this method.
     * 
     * @param index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    // -- result --------------------------------------------------------------

    public GameResult getResult() {
        return result;
    }

    public void setResult(GameResult result) {
        this.result = result;
    }

    // -- minute --------------------------------------------------------------

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

    // -- game ----------------------------------------------------------------

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

    // -- player --------------------------------------------------------------

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

    // -- goalType ------------------------------------------------------------

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

    // -- comment -------------------------------------------------------------

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

    // -- openligaid ----------------------------------------------------------

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

    // ------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
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
