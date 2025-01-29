/**
 * Copyright (c) 2016-2025 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package de.csdev.ebus.cfg;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.junit.Before;
import org.junit.Test;

import de.csdev.ebus.command.EBusCommandException;
import de.csdev.ebus.command.EBusCommandRegistry;
import de.csdev.ebus.command.EBusCommandUtils;
import de.csdev.ebus.command.IEBusCommandMethod;
import de.csdev.ebus.command.datatypes.EBusTypeException;
import de.csdev.ebus.configuration.EBusConfigurationReaderExt;

/**
 * @author Christian Sowada - Initial contribution
 *
 */
public class KW_CRC_Test {

    EBusCommandRegistry commandRegistry;

    @Before
    public void before() throws IOException, EBusConfigurationReaderException {
        commandRegistry = new EBusCommandRegistry(EBusConfigurationReaderExt.class, true);
    }

    @Test
    public void testKWCrc() throws EBusTypeException, EBusCommandException {
        IEBusCommandMethod commandMethod = commandRegistry.getCommandMethodById("cgb2", "boiler.pressure",
                IEBusCommandMethod.Method.GET);

        assertNotNull("Unable to get command cgb2.boiler.pressure", commandMethod);

        ByteBuffer buffer = EBusCommandUtils.buildMasterTelegram(commandMethod, (byte) 0x00, (byte) 0x04, null);

        assertEquals("KW CRC Invalid", 0x77, buffer.get(5));
    }
}
