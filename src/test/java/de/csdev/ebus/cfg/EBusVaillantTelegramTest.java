/**
 * Copyright (c) 2016-2023 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package de.csdev.ebus.cfg;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeNoException;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.csdev.ebus.command.EBusCommandException;
import de.csdev.ebus.command.EBusCommandRegistry;
import de.csdev.ebus.command.EBusCommandUtils;
import de.csdev.ebus.command.IEBusCommandMethod;
import de.csdev.ebus.command.IEBusCommandMethod.Method;
import de.csdev.ebus.command.datatypes.EBusTypeException;
import de.csdev.ebus.configuration.EBusConfigurationReaderExt;
import de.csdev.ebus.utils.EBusUtils;

/**
 * @author Christian Sowada - Initial contribution
 *
 */
public class EBusVaillantTelegramTest {

    private static final Logger logger = LoggerFactory.getLogger(EBusVaillantTelegramTest.class);

    static EBusCommandRegistry commandRegistry;

    @BeforeClass
    public static void before() throws IOException, EBusConfigurationReaderException {
        commandRegistry = new EBusCommandRegistry(EBusConfigurationReaderExt.class, true);
    }

    /**
     * Simple helper to resolve a telegram
     *
     * @param data
     */
    private void checkResolveTelegram(String data) {
        byte[] byteArray = EBusUtils.toByteArray(data);

        // vrc700_general.gen.sf_mode
        List<IEBusCommandMethod> find = commandRegistry.find(byteArray);

        assertFalse(find.isEmpty());

        IEBusCommandMethod method = find.get(0);

        assertNotNull(method);

        logger.debug("Successful resolved: {}", EBusCommandUtils.getFullId(method));
    }

    @Test
    public void testSetVaillantMasterSlave() {
        IEBusCommandMethod method = commandRegistry.getCommandMethodById("vrc700_general", "gen.operation_mode",
                Method.SET);
        assertNotNull(method);
        assertEquals(IEBusCommandMethod.Type.MASTER_SLAVE, method.getType());
    }

    @Test
    public void testVaillantTelegrams() {

        // bai.boiler.control.getopdataSGET
        checkResolveTelegram("10 08 B5 10 09 00 00 4E FF FF FF 00 00 00 74 00 01 01 9A 00 AA");

        // bai.boiler.control.getopdata:GET
        checkResolveTelegram("10 08 B5 11 01 01 89 00 09 50 46 00 80 FF 60 01 00 FF 97 00 AA");

        // vrc700_general.gen.sf_mode:SET
        checkResolveTelegram("FF 15 B5 24 07 02 01 00 00 74 00 05 9B 00 02 00 00 2C 00");
    }

    @Test
    public void testVaillantSetOperationMode() {

        byte[] byteArray = EBusUtils.toByteArray("31 15 B5 24 08 02 01 00 00 7B 00 00 00 E1");

        // vrc700_general gen.operation_mode
        List<IEBusCommandMethod> find = commandRegistry.find(byteArray);

        for (IEBusCommandMethod method : find) {

            Map<String, Object> values = new HashMap<>();
            values.put("value", BigDecimal.ZERO);

            try {

                assertEquals(IEBusCommandMethod.Type.MASTER_SLAVE, method.getType());

                ByteBuffer buildMasterTelegram = EBusCommandUtils.buildMasterTelegram(method, (byte) 0x31, (byte) 0x15,
                        values);

                Map<String, Object> map = EBusCommandUtils.decodeTelegram(method, byteArray);
                Object object = map.get("value");

                assertEquals(BigDecimal.ZERO, object);

                assertEquals(EBusUtils.toHexDumpString(byteArray).toString(),
                        EBusUtils.toHexDumpString(buildMasterTelegram).toString());

            } catch (EBusTypeException e) {
                e.printStackTrace();
                assumeNoException(e);
            } catch (EBusCommandException e) {
                e.printStackTrace();
                assumeNoException(e);
            }
        }
    }

}
