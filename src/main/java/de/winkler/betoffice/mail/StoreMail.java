/*
 * $Id: StoreMail.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2013 by Andre Winkler. All rights reserved.
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

/**
 * Store the xml converted tipp mail to the file system.
 * 
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2008-06-08 12:34:41
 *          +0200 (So, 08 Jun 2008) $
 */
public class StoreMail {

    public void store(TippMailParameter tipp, File file) throws IOException {
        TippToXmlConverter converter = new TippToXmlConverter();
        TippMail createTippMail = converter.createTippMail(tipp);
        String content = createTippMail.getContent();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            IOUtils.write(content, fos, "UTF-8");
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    /*
     * public TippMailParameter load(File file) throws IOException,
     * MailException { FileInputStream fis = new FileInputStream(file);
     * 
     * String tippMail = IOUtils.toString(fis); MailContent mailContent = new
     * MailContent(null, null, tippMail, null);
     * 
     * MailXMLParser mailXmlParser = new MailXMLParser(); MailContentDetails
     * mailContentDetails = mailXmlParser .parse(mailContent);
     * 
     * 
     * // TippMailParameter tippMailParameter = new
     * TippMailParameter(mailContentDetails.getNickName(), //
     * mailContentDetails.getHomeGoals(), mailContentDetails.getGuestGoals(), //
     * null , null , Integer.parseInt(mailContentDetails.getRound()), //
     * mailContentDetails.getPwdA(),
     * Long.parseLong(mailContentDetails.getChampionship()), // null , null ,
     * null );
     * 
     * }
     */

}
