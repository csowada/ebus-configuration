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
import static org.junit.Assume.assumeNoException;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

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
public class EBusVaillantVRC430TelegramTest {

    static EBusCommandRegistry commandRegistry;

    @BeforeClass
    public static void before() throws IOException, EBusConfigurationReaderException {
        commandRegistry = new EBusCommandRegistry(EBusConfigurationReaderExt.class, true);
    }

    @Test
    public void testDateTimeBroadcast() {

        // 22:00:34 06.11.2017 (Monday)
        byte[] byteArray = EBusUtils.toByteArray("10 FE B5 16 08 00 34 00 22 06 11 01 17 CE AA");

        List<IEBusCommandMethod> find = commandRegistry.find(byteArray);

        for (IEBusCommandMethod method : find) {

            assertNotNull(method);

            try {
                Map<String, Object> map = EBusCommandUtils.decodeTelegram(method, byteArray);
                Object object = map.get("datetime");

                assertTrue(object instanceof EBusDateTime);

                if (object instanceof EBusDateTime) {

                    EBusDateTime calendar = (EBusDateTime) map.get("datetime");

                    assertFalse(calendar.isAnyTime());
                    assertFalse(calendar.isAnyDate());

                    assertEquals(34, calendar.getCalendar().get(Calendar.SECOND));
                    assertEquals(00, calendar.getCalendar().get(Calendar.MINUTE));
                    assertEquals(22, calendar.getCalendar().get(Calendar.HOUR_OF_DAY));

                    assertEquals(6, calendar.getCalendar().get(Calendar.DAY_OF_MONTH));
                    assertEquals(11, calendar.getCalendar().get(Calendar.MONTH) + 1);
                    assertEquals(2017, calendar.getCalendar().get(Calendar.YEAR));
                }

            } catch (EBusTypeException e) {
                e.printStackTrace();
                assumeNoException(e);
            }
        }
    }

    @Test
    public void testDateTimeBroadcastWithNoDate() {

        // 22:00:34 01.01.1970 // No Date // CRC wrong
        byte[] byteArray = EBusUtils.toByteArray("10 FE B5 16 08 00 34 00 22 FF FF FF FF CE AA");

        List<IEBusCommandMethod> find = commandRegistry.find(byteArray);

        for (IEBusCommandMethod method : find) {

            assertNotNull(method);

            try {
                Map<String, Object> map = EBusCommandUtils.decodeTelegram(method, byteArray);
                Object object = map.get("datetime");

                assertTrue(object instanceof EBusDateTime);

                if (object instanceof EBusDateTime) {

                    EBusDateTime calendar = (EBusDateTime) map.get("datetime");

                    assertFalse(calendar.isAnyTime());
                    assertTrue(calendar.isAnyDate());

                    assertEquals(34, calendar.getCalendar().get(Calendar.SECOND));
                    assertEquals(00, calendar.getCalendar().get(Calendar.MINUTE));
                    assertEquals(22, calendar.getCalendar().get(Calendar.HOUR_OF_DAY));

                    assertEquals(1, calendar.getCalendar().get(Calendar.DAY_OF_MONTH));
                    assertEquals(1, calendar.getCalendar().get(Calendar.MONTH) + 1);
                    assertEquals(1970, calendar.getCalendar().get(Calendar.YEAR));
                }

            } catch (EBusTypeException e) {
                e.printStackTrace();
                assumeNoException(e);
            }
        }
    }

    @Test
    public void testDateTimeBroadcastX() {

        // Invalid vrc430 controller.date
        byte[] byteArray = EBusUtils.toByteArray("31 08 B5 09 03 0D 61 00 2A 00 02 00 00 2C 00 AA");

        List<IEBusCommandMethod> find = commandRegistry.find(byteArray);

        assertTrue(find.isEmpty());

    }

}
