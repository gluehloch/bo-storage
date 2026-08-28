/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2026 by Andre Winkler. All
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
 *
 */

package de.betoffice.storage.community.entity;

import de.betoffice.storage.community.CommunityDto;
import de.betoffice.storage.season.SeasonDto;
import de.betoffice.storage.season.entity.SeasonDtoMapper;
import de.betoffice.storage.user.PartyDto;
import de.betoffice.storage.user.entity.PartyDtoMapper;

public class CommunityDtoMapper {

    public static CommunityDto map(CommunityEntity community) {
        return map(community, new CommunityDto());
    }

    public static CommunityDto map(CommunityEntity community, CommunityDto json) {
        json.setId(community.getId());
        json.setName(community.getName());
        json.setYear(community.getYear());
        json.setShortName(community.getReference().getShortName());
        json.setCommunityManager(PartyDtoMapper.mapSmall(community.getCommunityManager(), new PartyDto()));
        json.setSeason(SeasonDtoMapper.map(community.getSeason(), new SeasonDto()));
        return json;
    }

}
