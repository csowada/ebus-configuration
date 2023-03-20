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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import de.csdev.ebus.command.EBusCommandRegistry;
import de.csdev.ebus.command.EBusCommandUtils;
import de.csdev.ebus.command.IEBusCommandMethod;
import de.csdev.ebus.command.datatypes.EBusTypeException;
import de.csdev.ebus.configuration.EBusConfigurationReaderExt;
import de.csdev.ebus.utils.EBusUtils;

/**
 * @author Christian Sowada - Initial contribution
 *
 */
public class EBusWolfCWLTest {

    static EBusCommandRegistry commandRegistry;

    @BeforeClass
    public static void before() throws IOException, EBusConfigurationReaderException {
        commandRegistry = new EBusCommandRegistry(EBusConfigurationReaderExt.class, true);
    }

    @Test
    public void test1() {
        Map<String, Object> map = check("31 3C 40 22 01 08 95 00 02 00 15 39 00 15 29 AA");

        assertNotNull(map);
        assertFalse(map.isEmpty());
        assertTrue(map.containsKey("temp_ouside"));
        assertEquals(new BigDecimal("2.1"), map.get("temp_ouside"));
    }

    @Test
    public void test2() {
        Map<String, Object> map = check("31 3C 40 22 01 07 9A 00 02 00 BE 92 00 AA");

        assertNotNull(map);
        assertFalse(map.isEmpty());
        assertTrue(map.containsKey("temp_inside"));
        assertEquals(new BigDecimal("19.0"), map.get("temp_inside"));
    }

    public Map<String, Object> check(String byteString) {
        byte[] byteArray = EBusUtils.toByteArray(byteString);
        List<IEBusCommandMethod> find = commandRegistry.find(byteArray);

        for (IEBusCommandMethod method : find) {

            assertNotNull(method);

            try {
                Map<String, Object> map = EBusCommandUtils.decodeTelegram(method, byteArray);
                return map;

            } catch (EBusTypeException e) {
                e.printStackTrace();
                fail();
            }
        }

        return new HashMap<String, Object>();
    }

}
