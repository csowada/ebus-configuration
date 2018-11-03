/**
 * Copyright (c) 2016-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package de.csdev.ebus.cfg;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
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

    EBusCommandRegistry commandRegistry;

    @Before
    public void before() throws IOException, EBusConfigurationReaderException {

        commandRegistry = new EBusCommandRegistry(EBusConfigurationReaderExt.class, true);
    }

    @Test
    public void test1() {
        check("31 3C 40 22 01 08 95 00 02 00 15 39 00 15 29 AA");
    }

    @Test
    public void test2() {
        check("31 3C 40 22 01 07 9A 00 02 00 BE 92 00 AA");
    }

    public void check(String byteString) {
        byte[] byteArray = EBusUtils.toByteArray(byteString);
        List<IEBusCommandMethod> find = commandRegistry.find(byteArray);

        for (IEBusCommandMethod method : find) {
            try {
                Map<String, Object> map = EBusCommandUtils.decodeTelegram(method, byteArray);

                for (Entry<String, Object> eBusCommand2 : map.entrySet()) {
                    System.out.println("ConfigurationReaderTest.testIsMasterAddress()" + eBusCommand2.getKey() + " > "
                            + eBusCommand2.getValue());
                }

                // Object object = map.get("datetime");
                //
                // assertNotNull(object);

            } catch (EBusTypeException e) {
                e.printStackTrace();
            }
        }
    }

    // @Test
    // public void testDateTimeBroadcast() {
    //
    // // 22:00:34 06.11.2017 (Monday)
    // byte[] byteArray = EBusUtils.toByteArray("31 3C 40 22 01 07 9A 00 02 00 BE 92 00 AA");
    //
    // }

}
