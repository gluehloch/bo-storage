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

package de.betoffice.storage.user.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import org.apache.commons.lang3.StringUtils;

@Embeddable
public class Nickname implements Comparable<Nickname> {

    /** Der Nickname des Users. */
    @Column(name = "bo_nickname")
    private String nickname;

    protected Nickname() {
    }

    private Nickname(String nickname) {
        this.nickname = nickname;
    }

    public static Nickname of(String nickname) {
        if (StringUtils.isBlank(nickname)) {
            throw new IllegalArgumentException("A nickname must be defined.");
        }

        return new Nickname(nickname);
    }

    public String getNickname() {
        return nickname;
    }

    public String value() {
        return nickname;
    }

    public void setNickname(String value) {
        nickname = value;
    }

    @Override
    public int compareTo(Nickname o) {
        return this.nickname.toLowerCase().compareTo(o.nickname.toLowerCase());
    }

    @Override
    public String toString() {
        return new StringBuilder("Nickname=[").append(nickname).append("]").toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname.toLowerCase());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Nickname other = (Nickname) obj;
        return Objects.equals(nickname, other.nickname);
    }

}
