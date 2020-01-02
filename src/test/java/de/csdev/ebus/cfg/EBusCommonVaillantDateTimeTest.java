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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.csdev.ebus.command.EBusCommandRegistry;
import de.csdev.ebus.command.EBusCommandUtils;
import de.csdev.ebus.command.IEBusCommandMethod;
import de.csdev.ebus.command.datatypes.EBusTypeException;
import de.csdev.ebus.configuration.EBusConfigurationReaderExt;
import de.csdev.ebus.utils.EBusDateTime;
import de.csdev.ebus.utils.EBusUtils;

/**
 * @author Christian Sowada - Initial contribution
 *
 */
public class EBusCommonVaillantDateTimeTest {

    private static final Logger logger = LoggerFactory.getLogger(EBusCommonVaillantDateTimeTest.class);

    EBusCommandRegistry commandRegistry;

    @Before
    public void before() throws IOException, EBusConfigurationReaderException {
        commandRegistry = new EBusCommandRegistry(EBusConfigurationReaderExt.class, true);
    }

    private void checkTime(String strBuffer, Map<String, Object> expectedResults) throws EBusTypeException {

        logger.info("Resolve: " + strBuffer);

        byte[] byteArray = EBusUtils.toByteArray(strBuffer);
        List<IEBusCommandMethod> list = commandRegistry.find(byteArray);
        assertNotNull(list);
        assertFalse(list.isEmpty());

        for (IEBusCommandMethod method : list) {
            logger.info("ID: {}.{}", method.getParent().getParentCollection().getId(), method.getParent().getId());
            Map<String, Object> map = EBusCommandUtils.decodeTelegram(method, byteArray);

            for (Entry<String, Object> entry : map.entrySet()) {

                if (expectedResults.containsKey(entry.getKey())) {
                    assertEquals(expectedResults.get(entry.getKey()), entry.getValue());
                }

                logger.info("{} -> {}", entry.getKey(), entry.getValue());
            }
        }
    }

    @Test
    public void testTime() throws EBusTypeException {

        Calendar time = Calendar.getInstance();
        time.set(Calendar.MILLISECOND, 0);

        Map<String, Object> expectedResults = new HashMap<String, Object>();
        expectedResults.put("time", new EBusDateTime(time, true, false));

        time.set(1970, 0, 1, 20, 57, 8);
        checkTime("FF 15 B5 09 03 0D 60 00 25 00 03 08 39 14 92 00", expectedResults);

        time.set(1970, 0, 1, 03, 27, 5);
        checkTime("FF 15 B5 09 03 0D 60 00 25 00 03 05 1B 03 B2 00", expectedResults);

        time.set(1970, 0, 1, 21, 22, 7);
        checkTime("FF 15 B5 09 03 0D 60 00 25 00 03 07 16 15 CB 00", expectedResults);
    }
}