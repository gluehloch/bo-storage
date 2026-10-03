/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2013-2026 by Andre Winkler. All
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

package de.betoffice.storage.community;

import java.io.Serializable;

import de.betoffice.storage.AbstractIdentifier;
import de.betoffice.storage.community.entity.CommunityReference;
import de.betoffice.storage.season.SeasonDto;
import de.betoffice.storage.user.PartyDto;

public class CommunityDto extends AbstractIdentifier implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String year;
    private String shortName;
    private PartyDto communityManager;
    private SeasonDto season;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public PartyDto getCommunityManager() {
        return communityManager;
    }

    public void setCommunityManager(PartyDto communityManager) {
        this.communityManager = communityManager;
    }

    public SeasonDto getSeason() {
        return season;
    }

    public void setSeason(SeasonDto season) {
        this.season = season;
    }

    public CommunityReference toCommunityReference() {
        return CommunityReference.of(shortName);
    }

    @Override
    public String toString() {
        return "CommunityDto{" +
                "name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", communityManager=" + communityManager +
                ", season=" + season +
                '}';
    }

}
