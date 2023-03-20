/**
 * Copyright (c) 2016-2023 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package de.csdev.ebus.wip;

import static org.junit.Assert.*;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.csdev.ebus.cfg.EBusConfigurationReaderException;
import de.csdev.ebus.client.EBusClient;
import de.csdev.ebus.command.EBusCommandRegistry;
import de.csdev.ebus.configuration.EBusConfigurationReaderExt;
import de.csdev.ebus.core.EBusEbusdController;
import de.csdev.ebus.core.IEBusController.ConnectionStatus;

/**
 * @author Christian Sowada - Initial contribution
 *
 */
public class EBusdControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(EBusdControllerTest.class);

    EBusCommandRegistry commandRegistry;

    // @Before
    public void before() throws IOException, EBusConfigurationReaderException {
        commandRegistry = new EBusCommandRegistry(EBusConfigurationReaderExt.class, true);
    }

    // @Test
    public void testIdentification() throws InterruptedException {

        EBusEbusdController controller = new EBusEbusdController("openhab", 8888);
        assertNotNull(commandRegistry);

        EBusClient client = new EBusClient(commandRegistry);

        client.connect(controller, (byte) 0x00);

        // assertEquals(controller.getConnectionStatus(), ConnectionStatus.DISCONNECTED);

        // start thread
        controller.start();

        // assertEquals(controller.getConnectionStatus(), ConnectionStatus.DISCONNECTED);

        Thread.sleep(1000);

        assertEquals(controller.getConnectionStatus(), ConnectionStatus.CONNECTED);

        // client should be up and running

        // let it work for some seconds
        Thread.sleep(60000);

        logger.info("Dispose client ...");
        client.dispose();

        Thread.sleep(1500);

        assertEquals(controller.getConnectionStatus(), ConnectionStatus.DISCONNECTED);

        logger.info("Finished ...");
    }

}
