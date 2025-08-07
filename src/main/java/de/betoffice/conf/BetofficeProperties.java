/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2024 by Andre Winkler. All
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

package de.betoffice.conf;

public class BetofficeProperties {

    private final String driverClassName;
    private final String url;
    private final String username;
    private final String password;

    private final boolean mailEnabled;
    private final String mailUsername;
    private final String mailPassword;
    private final String mailHost;
    private final String mailPort;
    private final boolean mailSmtpAuth;
    private final boolean mailStartTlsEnable;

    public BetofficeProperties(
            String driverClassName,
            String url,
            String username,
            String password,
            boolean mailEnabled,
            String mailUsername,
            String mailPassword,
            String mailHost,
            String mailPort,
            boolean mailSmtpAuth,
            boolean mailStartTlsEnable) {
        this.driverClassName = driverClassName;
        this.url = url;
        this.username = username;
        this.password = password;
        this.mailEnabled = mailEnabled;
        this.mailUsername = mailUsername;
        this.mailPassword = mailPassword;
        this.mailHost = mailHost;
        this.mailPort = mailPort;
        this.mailSmtpAuth = mailSmtpAuth;
        this.mailStartTlsEnable = mailStartTlsEnable;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isMailEnabled() {
        return mailEnabled;
    }

    public String getMailUsername() {
        return mailUsername;
    }

    public String getMailPassword() {
        return mailPassword;
    }

    public String getMailHost() {
        return mailHost;
    }

    public String getMailPort() {
        return mailPort;
    }

    public boolean getMailSmtpAuth() {
        return mailSmtpAuth;
    }

    public boolean getMailStartTlsEnable() {
        return mailStartTlsEnable;
    }

}
