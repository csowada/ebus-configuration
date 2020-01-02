/**
 * Copyright (c) 2016-2020 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package de.csdev.ebus.cfg;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.csdev.ebus.TestUtils;
import de.csdev.ebus.command.EBusCommandRegistry;
import de.csdev.ebus.configuration.EBusConfigurationReaderExt;
import de.csdev.ebus.utils.EBusUtils;

/**
 * @author Christian Sowada - Initial contribution
 *
 */
public class EBusWolfMMTelegramTest {

    EBusCommandRegistry commandRegistry;

    @Before
    public void before() throws IOException, EBusConfigurationReaderException {
        commandRegistry = new EBusCommandRegistry(EBusConfigurationReaderExt.class, true);
    }

    @Test
    public void testSolarCommands() {
        TestUtils.canResolve(commandRegistry, EBusUtils
                .toByteArray("70 51 50 14 07 00 00 2C 1A 14 00 14 58 00 09 00 00 E6 12 00 D8 14 64 00 42 00 AA"));

        TestUtils.canResolve(commandRegistry, EBusUtils
                .toByteArray("70 51 50 14 07 41 00 05 00 17 00 5A 0D 00 09 00 40 80 16 00 D8 14 64 00 4B 00 AA"));

    }

}
