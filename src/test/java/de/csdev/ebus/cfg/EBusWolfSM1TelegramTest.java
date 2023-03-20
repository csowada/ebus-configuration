/**
 * Copyright (c) 2016-2023 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package de.csdev.ebus.cfg;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import de.csdev.ebus.TestUtils;
import de.csdev.ebus.command.EBusCommandRegistry;
import de.csdev.ebus.configuration.EBusConfigurationReaderExt;
import de.csdev.ebus.utils.EBusUtils;

/**
 * @author Christian Sowada - Initial contribution
 *
 */
public class EBusWolfSM1TelegramTest {

    static EBusCommandRegistry commandRegistry;

    @BeforeClass
    public static void before() throws IOException, EBusConfigurationReaderException {
        commandRegistry = new EBusCommandRegistry(EBusConfigurationReaderExt.class, true);
    }

    @Test
    public void testSolarCommands() {
        TestUtils.canResolve(commandRegistry, EBusUtils.toByteArray("30 76 50 22 03 CC 2B 0A BF 00 02 07 01 DA AA"));
        TestUtils.canResolve(commandRegistry,
                EBusUtils.toByteArray("71 FE 50 18 0E 00 00 F9 00 07 00 3D 02 88 01 05 00 00 00 B8 AA"));
        TestUtils.canResolve(commandRegistry,
                EBusUtils.toByteArray("71 FE 50 17 10 08 91 F0 01 0A 04 00 80 00 80 00 80 00 80 00 80 F7 AA"));
    }

}
