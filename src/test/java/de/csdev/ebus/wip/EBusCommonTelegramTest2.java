/**
 * Copyright (c) 2016-2025 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package de.csdev.ebus.wip;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.csdev.ebus.TestUtils;
import de.csdev.ebus.cfg.EBusConfigurationReaderException;
import de.csdev.ebus.command.EBusCommandRegistry;
import de.csdev.ebus.command.EBusCommandUtils;
import de.csdev.ebus.command.IEBusCommand;
import de.csdev.ebus.command.IEBusCommandMethod;
import de.csdev.ebus.command.IEBusCommandMethod.Method;
import de.csdev.ebus.configuration.EBusConfigurationReaderExt;
import de.csdev.ebus.utils.EBusUtils;

/**
 * @author Christian Sowada - Initial contribution
 *
 */
public class EBusCommonTelegramTest2 {

    private static final Logger logger = LoggerFactory.getLogger(EBusCommonTelegramTest2.class);

    EBusCommandRegistry commandRegistry;

    // @Before
    public void before() throws IOException, EBusConfigurationReaderException {

        commandRegistry = new EBusCommandRegistry(EBusConfigurationReaderExt.class, true);
    }

    // @Test
    public void testResolveCommonTelegrams() {
        byte[] data = null;

        /*
         * 2014-10-23 16:10:30 - >>> Betriebsdaten des Feuerungsautomaten an den Regler - Block 1
         * 2014-10-23 16:10:30 - >>> auto_stroker.state_valve2 false Valve2
         * 2014-10-23 16:10:30 - >>> auto_stroker.state_ldw false LDW
         * 2014-10-23 16:10:30 - >>> auto_stroker.status_auto_stroker 0 Statusanzeige
         * 2014-10-23 16:10:30 - >>> auto_stroker.temp_return 23 Rücklauftemperatur
         * 2014-10-23 16:10:30 - >>> auto_stroker.state_uwp true UWP
         * 2014-10-23 16:10:30 - >>> auto_stroker.temp_boiler 48 Boilertemperatur
         * 2014-10-23 16:10:30 - >>> auto_stroker.temp_vessel 22.0 Kesseltemperatur
         * 2014-10-23 16:10:30 - >>> auto_stroker.state_alarm false Alarm
         * 2014-10-23 16:10:30 - >>> auto_stroker.state_ws false WS
         * 2014-10-23 16:10:30 - >>> auto_stroker.temp_outdoor 14 Außentemperatur
         * 2014-10-23 16:10:30 - >>> auto_stroker.state_gdw false GDW
         * 2014-10-23 16:10:30 - >>> auto_stroker.state_flame false Flame
         * 2014-10-23 16:10:30 - >>> auto_stroker.state_valve1 false Valve1
         * 2014-10-23 16:10:30 - >>> auto_stroker.performance_burner null Stellgrad MIN-MAX Kesselleistung in %
         */

        // 03 FE 05 03 08 01 00 48 FF 47 1D 2B 01 D2 AA
        data = EBusUtils.toByteArray("03 FE 05 03 08 01 00 40 FF 2D 17 30 0E C8 AA");
        IEBusCommand command = commandRegistry.getCommandById("std", "auto_stroker.op_data_bc2tc_b1");

        assertNotNull(command);

        IEBusCommandMethod commandMethod = command.getCommandMethod(Method.GET);

        assertNotNull(commandMethod);

        ByteBuffer masterTelegramMask = EBusCommandUtils.getMasterTelegramMask(commandMethod);

        // ByteBuffer masterTelegramMask2 =
        // EBusCommandUtils.getMasterTelegramMask(command.getCommandMethod(Method.SET));

        logger.debug("TELE {}", EBusUtils.toHexDumpString(data));
        logger.debug("MASK {}", EBusUtils.toHexDumpString(masterTelegramMask));

        // masterTelegramMask = EBusCommandUtils.getMasterTelegramMask(command.getCommandMethod(Method.BROADCAST));
        // logger.debug("TELE {}", EBusUtils.toHexDumpString(data));
        // logger.debug("MASK {}", EBusUtils.toHexDumpString(masterTelegramMask));

        @SuppressWarnings("unused")
        List<IEBusCommandMethod> find = commandRegistry.find(data);

        TestUtils.canResolve(commandRegistry, data);

    }

}
