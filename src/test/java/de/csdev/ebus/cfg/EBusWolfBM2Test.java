/**
 * Copyright (c) 2016-2020 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package de.csdev.ebus.cfg;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.csdev.ebus.command.EBusCommandRegistry;
import de.csdev.ebus.command.EBusCommandUtils;
import de.csdev.ebus.command.IEBusCommand;
import de.csdev.ebus.command.IEBusCommandMethod;
import de.csdev.ebus.command.IEBusCommandMethod.Method;
import de.csdev.ebus.command.datatypes.EBusTypeException;
import de.csdev.ebus.configuration.EBusConfigurationReaderExt;
import de.csdev.ebus.utils.EBusUtils;

/**
 * @author Christian Sowada - Initial contribution
 *
 */
public class EBusWolfBM2Test {

    EBusCommandRegistry commandRegistry;

    @Before
    public void before() throws IOException, EBusConfigurationReaderException {
        commandRegistry = new EBusCommandRegistry(EBusConfigurationReaderExt.class, true);
    }

    @Test
    public void testGetDHWProgram() {
        IEBusCommand command = commandRegistry.getCommandById("bm2", "dhw.program_dhw_circuit");

        assertNotNull(command);

        IEBusCommandMethod commandMethod = command.getCommandMethod(Method.GET);

        HashMap<String, Object> params = new HashMap<>();
        params.put("program", 1);

        try {
            ByteBuffer buffer = EBusCommandUtils.buildMasterTelegram(commandMethod, (byte) 0x00, (byte) 0x35, params);
            assertEquals(EBusUtils.toHexDumpString(buffer).toString(), "00 35 50 22 03 A3 75 27 79");

        } catch (EBusTypeException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void testSetDHWProgram() {
        IEBusCommand command = commandRegistry.getCommandById("bm2", "dhw.program_dhw_circuit");
        assertNotNull(command);

        IEBusCommandMethod commandMethod = command.getCommandMethod(Method.SET);

        HashMap<String, Object> params = new HashMap<>();
        params.put("program", 1);

        try {
            ByteBuffer buffer = EBusCommandUtils.buildMasterTelegram(commandMethod, (byte) 0x00, (byte) 0x30, params);
            assertEquals(EBusUtils.toHexDumpString(buffer).toString(), "00 30 50 23 09 D8 75 27 01 00 5D 01 00 00 BF");

        } catch (EBusTypeException e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void testDecodeDHWProgram() {
        byte[] byteArray = EBusUtils.toByteArray("00 35 50 22 03 A3 75 27 79 00 02 01 00 B7 00 AA");
        List<IEBusCommandMethod> find = commandRegistry.find(byteArray);

        assertFalse(find.isEmpty());

        for (IEBusCommandMethod method : find) {
            try {
                Map<String, Object> map = EBusCommandUtils.decodeTelegram(method, byteArray);

                assertNotNull(map.get("program"));

                assertEquals(map.get("program"), BigDecimal.valueOf(1));

            } catch (EBusTypeException e) {
                e.printStackTrace();
                fail();
            }
        }
    }

}
