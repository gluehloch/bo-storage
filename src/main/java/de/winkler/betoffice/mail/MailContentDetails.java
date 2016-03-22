/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2016 by Andre Winkler. All rights reserved.
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

package de.winkler.betoffice.mail;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Hält die Daten die aus der Klasse {@link MailContent} extrahiert wurden.
 *
 * @author by Andre Winkler
 */
public class MailContentDetails {

    private boolean evaluated;

	// Allgemeine Informationen
    private String date;
	private String using;
	private String formInfo;
	private String userIP;
	private String browser;
	private String time;
	private String user;
	private String pwdA;

	// Anmeldung Sportwetten
	private String userCall;
	private String nickName;
	private String pwdB;
	private String mail;
	private String phone;

	// Abgabe Wett-Tipp
	private String homeGoals;
	private String guestGoals;
	private String round;
	private String championship;

    /**
     * @param evaluated the evaluated to set
     */
    public void setEvaluated(boolean evaluated) {
        this.evaluated = evaluated;
    }

    /**
     * @return the evaluated
     */
    public boolean isEvaluated() {
        return evaluated;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

	/**
	 * @return the using
	 */
	public String getUsing() {
		return using;
	}

	/**
	 * @param using the using to set
	 */
	public void setUsing(String using) {
		this.using = using;
	}

	/**
	 * @return the formInfo
	 */
	public String getFormInfo() {
		return formInfo;
	}

	/**
	 * @param formInfo the formInfo to set
	 */
	public void setFormInfo(String formInfo) {
		this.formInfo = formInfo;
	}

	/**
	 * @return the userIP
	 */
	public String getUserIP() {
		return userIP;
	}

	/**
	 * @param userIP the userIP to set
	 */
	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}

	/**
	 * @return the browser
	 */
	public String getBrowser() {
		return browser;
	}

	/**
	 * @param browser the browser to set
	 */
	public void setBrowser(String browser) {
		this.browser = browser;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the pwdA
	 */
	public String getPwdA() {
		return pwdA;
	}

	/**
	 * @param pwdA the pwdA to set
	 */
	public void setPwdA(String pwdA) {
		this.pwdA = pwdA;
	}

	/**
	 * @return the userCall
	 */
	public String getUserCall() {
		return userCall;
	}

	/**
	 * @param userCall the userCall to set
	 */
	public void setUserCall(String userCall) {
		this.userCall = userCall;
	}

	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * @return the pwdB
	 */
	public String getPwdB() {
		return pwdB;
	}

	/**
	 * @param pwdB the pwdB to set
	 */
	public void setPwdB(String pwdB) {
		this.pwdB = pwdB;
	}

	/**
	 * @return the mail
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * @param mail the mail to set
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the homeGoals
	 */
	public String getHomeGoals() {
		return homeGoals;
	}

	/**
	 * @param homeGoals the homeGoals to set
	 */
	public void setHomeGoals(String homeGoals) {
		this.homeGoals = homeGoals;
	}

	/**
	 * @return the guestGoals
	 */
	public String getGuestGoals() {
		return guestGoals;
	}

	/**
	 * @param guestGoals the guestGoals to set
	 */
	public void setGuestGoals(String guestGoals) {
		this.guestGoals = guestGoals;
	}

	/**
	 * @return the round
	 */
	public String getRound() {
		return round;
	}

	/**
	 * @param round the round to set
	 */
	public void setRound(String round) {
		this.round = round;
	}

    /**
     * @return the championship
     */
    public String getChampionship() {
        return championship;
    }

    /**
     * @param championship the championship to set
     */
    public void setChampionship(String championship) {
        this.championship = championship;
    }

	@Override
	public String toString() {
		return new ToStringBuilder(this).
		    append("evaluated", evaluated).
		    append("date", date).
			append("using", using).
			append("formInfo", formInfo).
			append("userIP", userIP).
			append("browser", browser).
			append("time", time).
			append("user", user).
			append("pwdA", pwdA).
			append("userCall", userCall).
			append("nickName", nickName).
			append("pwdB", pwdB).
			append("mail", mail).
			append("phone", phone).
			append("homeGoals", homeGoals).
			append("guestGoals", guestGoals).
			append("round", round).toString();
	}

}
