/*
 * $Id: MailDownloadAndEvaluationTest.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2010 by Andre Winkler. All rights reserved.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import de.awtools.mail.POP3Receiver;

/**
 * Testet einen fiktiven Mail-Download mit anschließender Ergebnisauswertung.
 * 
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2009-04-11 21:38:02
 *          +0200 (Sa, 11 Apr 2009) $
 */
public class MailDownloadAndEvaluationTest {

	private POP3Receiver pop3Receiver;
	private Message[] messages;
	private List<PropertyChangeEvent> events;

	@Test
	public void testMailDownloadAndEvaluation() throws Exception {
		MailDownloadManager mailManager = new MailDownloadManager(pop3Receiver);
		mailManager.addPropertyChangeListener(new MyPropertyChangeListener());
		List<MailContent> mails = mailManager.readMails();

		verify(pop3Receiver).download();
		verify(pop3Receiver).messagesToArray();

		// Erstes Event liefert die Anzahl der Mails, die runtergeladen wurden.
		assertEquals(MailDownloadManager.PROPERTY_MAIL_COUNT, events.get(0)
				.getPropertyName());
		assertEquals(4, events.get(0).getNewValue());

		// Event oldValue, newValue, Mail
		// -1, 0 => Mail 0 ausgewertet
		//  0, 1 => Mail 1 ausgewertet
		//  1, 2 => Mail 2 ausgewertet
	    //  2, 3 => Mail 3 ausgewertet
		for (int i = 1; i < mails.size() + 1; i++) {
			assertEquals(i - 2, events.get(i).getOldValue());
			assertEquals(i - 1, events.get(i).getNewValue());
			assertEquals(MailDownloadManager.PROPERTY_MAIL_PARSING, events.get(
					i).getPropertyName());
		}

		//
		// Prüft den Eingang der EMail und die korrekte Zuordnung als
		// Tipp- oder Registrierungsmail, das Versendedatum und das Extrahieren
		// der XML-Nachricht.
		//
		for (int i = 0; i < mails.size(); i++) {
			assertEquals("24.03.1971", mails.get(i).sDate);
			assertEquals("andre.winkler@web.de", mails.get(i).sFrom);
			assertTrue(mails.get(i).sMessageText.startsWith("<FORM>"));
			if (i == messages.length - 1) {
				assertEquals(MailXMLParser.REGISTRATION, mails.get(i).sSubject);
			} else {
				assertEquals(MailXMLParser.TIPP, mails.get(i).sSubject);
			}
		}

		//
		// Prüft die XML Auswertung der EMail.
		//
		MailXMLParser mailXMLParser = new MailXMLParser();
		int index = 0;
		for (MailContent mail : mails) {
			MailContentDetails detail = mailXMLParser.parse(mail);
			assertEquals("25.03.1971", detail.getDate());
			assertEquals("TEST-BROWSER", detail.getBrowser());
			assertEquals("TEST-REMOTE-HOST", detail.getUserIP());
			assertEquals("##ENV_HTTP_REFERER##", detail.getFormInfo());
			assertEquals("17:55", detail.getTime());
			assertEquals("andre.winkler", detail.getNickName());
			assertEquals("Elch71", detail.getPwdA());
			if (detail.getUsing().startsWith("Tipp")) {
				assertEquals("Bundesliga 2008/09", detail.getChampionship());
				assertEquals(Integer.toString(index), detail.getRound());
				assertEquals("0,1,2,3,4,5,6,7,8", detail.getHomeGoals());
				assertEquals("8,7,6,5,4,3,2,1,0", detail.getGuestGoals());
			} else if (detail.getUsing().startsWith("Anmeldung")) {
				assertEquals("Winkler", detail.getUser());
				assertEquals("Elch71", detail.getPwdB());
				assertEquals("4711", detail.getPhone());
				assertEquals("andre.winkler@web.de", detail.getMail());
			} else {
				fail("Unexpected mail type: " + detail.getUsing());
			}
			index++;
		}
	}

	/**
	 * 4 Nachrichten präparieren: 3 x Tipp und 1 x Anmeldung.
	 */
	@Before
	public void setUp() throws Exception {
		DateFormat dateFormatter = DateFormat.getDateInstance(
				DateFormat.DEFAULT, new Locale("de", "DE"));

		String[] content = new String[4];
		content[0] = IOUtils.toString(this.getClass().getResourceAsStream(
				"test-mail-tipp-01.txt"));
		content[1] = IOUtils.toString(this.getClass().getResourceAsStream(
				"test-mail-tipp-02.txt"));
		content[2] = IOUtils.toString(this.getClass().getResourceAsStream(
				"test-mail-tipp-03.txt"));
		content[3] = IOUtils.toString(this.getClass().getResourceAsStream(
				"test-mail-registration-04.txt"));

		InternetAddress[] address = new InternetAddress[] { new InternetAddress(
				"andre.winkler@web.de") };

		messages = new Message[4];
		for (int i = 0; i < messages.length; i++) {
			messages[i] = mock(Message.class);
		}
		pop3Receiver = mock(POP3Receiver.class);

		for (int i = 0; i < messages.length; i++) {
			when(messages[i].getFrom()).thenReturn(address);
			when(messages[i].getContentType()).thenReturn("text/plain");
			when(messages[i].getContent()).thenReturn(content[i]);
			when(messages[i].getSentDate()).thenReturn(
					dateFormatter.parse("24.03.1971"));

			if (i == messages.length - 1) {
				when(messages[i].getSubject()).thenReturn(
						MailXMLParser.REGISTRATION);
			} else {
				when(messages[i].getSubject()).thenReturn(MailXMLParser.TIPP);
			}
		}

		when(pop3Receiver.messagesToArray()).thenReturn(messages);
		events = new ArrayList<PropertyChangeEvent>();
	}

	private class MyPropertyChangeListener implements PropertyChangeListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @seejava.beans.PropertyChangeListener#propertyChange(java.beans.
		 * PropertyChangeEvent)
		 */
		@Override
		public void propertyChange(final PropertyChangeEvent _event) {
			events.add(_event);
		}

	}

}
