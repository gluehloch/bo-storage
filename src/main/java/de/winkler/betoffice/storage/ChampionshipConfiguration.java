/*
 * $Id: ChampionshipConfiguration.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

package de.winkler.betoffice.storage;

/**
 * Verwaltet die administrativen Daten einer Meisterschaft. Zum Beispiel die
 * Info, in welches Verzeichnis ein Export geht oder welches Export-Templates
 * zu verwenden sind.
 *
 * @author  $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public class ChampionshipConfiguration extends AbstractStorageObject {

    /** serial version id */
    private static final long serialVersionUID = 8221108184744478989L;

    // -- exportDirectory -----------------------------------------------------

    /**
     * Ein Exportverzeichnis für eine Meisterschaft. Ist i.d.R. eine relative
     * Pfadangabe.
     */
    private String exportDirectory;

    /**
     * Liefert das Exportverzeichnis.
     *
     * @return Das Exportverzeichnis.
     *
     * @hibernate.property column="bo_exportdirectory"
     */
    public String getExportDirectory() {
        return exportDirectory;
    }

    /**
     * Setzt ein neues Exportverzeichnis.
     *
     * @param value Das Exportverzeichnis.
     */
    public void setExportDirectory(final String value) {
        exportDirectory = value;
    }

    // -- exportTemplate ------------------------------------------------------

    /** Das zu verwendende Export-Template. */
    private String exportTemplate;

    /**
     * Liefert das Export-Template.
     *
     * @return Das Export-Template.
     *
     * @hibernate.property column="bo_exporttemplate"
     */
    public String getExportTemplate() {
        return exportTemplate;
    }

    /**
     * Setzt ein neues Export-Template.
     *
     * @param value Das Export-Template.
     */
    public void setExportTemplate(final String value) {
        exportTemplate = value;
    }

    // -- StorageObject -------------------------------------------------------

    /**
     * @see StorageObject#isValid()
     */
    public boolean isValid() {
        return true;
    }

}
