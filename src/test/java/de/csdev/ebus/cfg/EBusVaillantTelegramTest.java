/**
 * Copyright (c) 2016-2020 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package de.csdev.ebus.cfg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeNoException;

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

    EBusCommandRegistry commandRegistry;

    @Before
    public void before() throws IOException, EBusConfigurationReaderException {

        commandRegistry = new EBusCommandRegistry(EBusConfigurationReaderExt.class, true);
    }

    @Test
    public void testSetVaillantMasterSlave() {
        IEBusCommandMethod method = commandRegistry.getCommandMethodById("vrc700_general", "gen.operation_mode",
                Method.SET);
        assertEquals(IEBusCommandMethod.Type.MASTER_SLAVE, method.getType());
    }

    @Test
    public void testDateTimeBroadcast() {

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
            }
        }
    }

}
