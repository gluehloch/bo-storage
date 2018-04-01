/*
 * $Id: BetofficeValidationMessageTest.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

package de.winkler.betoffice.validation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import de.winkler.betoffice.validation.BetofficeValidationMessage.Severity;

/**
 * Test for class {@link BetofficeValidationException}.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32
 *          +0200 (Sat, 27 Jul 2013) $
 */
public class BetofficeValidationMessageTest {

    @Test
    public void testBetofficeValidationMessage() {
        BetofficeValidationMessage ex = new BetofficeValidationMessage(
                "message", "propertyName", Severity.ERROR);
        assertThat(ex.getMessage()).isEqualTo("message");
        assertThat(ex.getPropertyName()).isEqualTo("propertyName");
        assertThat(ex.getSeverity()).isEqualTo(Severity.ERROR);
    }

}
