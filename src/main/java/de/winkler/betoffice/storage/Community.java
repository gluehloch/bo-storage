/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2023 by Andre Winkler. All
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

package de.winkler.betoffice.storage;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * Defines a group of players.
 *
 * @author Andre Winkler
 */
@Entity
@Table(name = "bo_community")
public class Community extends AbstractStorageObject {

    private static final long serialVersionUID = -7239278975374588294L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Embedded
    private CommunityReference reference;

    @NotNull
    @Column(name = "bo_name")
    private String name;
    
    @NotNull
    @Column(name = "bo_year")
    private String year;

    @ManyToOne
    @JoinColumn(name = "bo_user_ref")
    private User communityManager;

    @ManyToOne
    @JoinColumn(name = "bo_season_ref")
    private Season season;
    
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.LAZY)
    @JoinTable(name = "bo_community_user",
        joinColumns = @JoinColumn(name = "bo_community_ref"),
        inverseJoinColumns = @JoinColumn(name = "bo_user_ref"))
    private Set<User> users = new HashSet<>();

    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    public CommunityReference getReference() {
        return reference;
    }
    
    public void setReference(CommunityReference reference) {
        this.reference = reference;
    }

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

    public User getCommunityManager() {
        return communityManager;
    }

    public void setCommunityManager(User user) {
        this.communityManager = user;
    }
    
    public Season getSeason() {
        return season;
    }
    
    public void setSeason(Season season) {
        this.season = season;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void addMember(User user) {
        users.add(user);
    }
    
    public void removeMember(User user) {
        users.remove(user);
    }
    
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) {
            return false;
        } else if (!(object instanceof Group)) {
            return false;
        } else {
            Community community = (Community) object;
            return community.getId().equals(getId());           
        }
    }

    @Override
    public int hashCode() {
        return 37;
    }

}
