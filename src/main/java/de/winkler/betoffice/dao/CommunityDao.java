/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2022 by Andre Winkler. All
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

package de.winkler.betoffice.dao;

import java.util.List;
import java.util.Optional;

import de.winkler.betoffice.storage.Community;

/**
 * Community DAO.
 * 
 * @author Andre Winkler
 */
public interface CommunityDao extends CommonDao<Community> {

	/**
	 * Find community by short name.
	 * 
	 * @param shortName short name of the community
	 * @return
	 */
	Optional<Community> findByShortName(String shortName);

	/**
	 * Find a community by name.
	 * 
	 * @param name the community name
	 * @return the community
	 */
	Optional<Community> find(String name);

	/**
	 * Find all communities.
	 * 
	 * @param nameFilter a filter for the community name
	 * @return all communities
	 */
	List<Community> findAll(String nameFilter);

	/**
	 * Are there still any community members?
	 *
	 * @param community Find members of this community
	 * @return <code>true</code> if community has members
	 */
	boolean hasMembers(Community community);

}
