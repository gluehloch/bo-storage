/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2025 by Andre Winkler. All
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

package de.winkler.betoffice.conf;

import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractPropertiesConfiguration {

    // -- database settings

    @Value("${betoffice.persistence.classname}")
    private String driverClassName;

    @Value("${betoffice.persistence.url}")
    private String url;

    @Value("${betoffice.persistence.username}")
    private String username;

    @Value("${betoffice.persistence.password}")
    private String password;

    // -- mail settings

    @Value("${betoffice.mail.username")
    private String mailUsername;

    @Value("${betoffice.mail.password")
    private String mailPassword;

    @Value("${betoffice.mail.host")
    private String mailHost;

    @Value("${betoffice.mail.port")
    private String mailPort;

    @Value("${betoffice.mail.smtpauth")
    private String mailSmtpAuth;

    @Value("${betoffice.mail.starttlsenable")
    private String mailStartTlsEnable;

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getMailSmtpAuth() {
        return mailSmtpAuth;
    }

    public String getMailStartTlsEnable() {
        return mailStartTlsEnable;
    }

    BetofficeProperties getProperties() {
        return new BetofficeProperties(driverClassName, url, username, password);
    }

}
