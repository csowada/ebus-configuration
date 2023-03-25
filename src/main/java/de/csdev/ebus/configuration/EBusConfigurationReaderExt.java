/**
 * Copyright (c) 2016-2023 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package de.csdev.ebus.configuration;

import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.csdev.ebus.cfg.std.EBusConfigurationReader;
import de.csdev.ebus.command.IEBusCommandCollection;

@NonNullByDefault
public class EBusConfigurationReaderExt extends EBusConfigurationReader {

    @SuppressWarnings({"null"})
    private static final Logger logger = LoggerFactory.getLogger(EBusConfigurationReaderExt.class);

    @Override
    @SuppressWarnings({"null"})
    public List<IEBusCommandCollection> loadBuildInConfigurationCollections() {
        try {
            URL url = EBusConfigurationReaderExt.class.getResource("/index-configuration.json");
            if (url != null) {
                return loadConfigurationCollectionBundle(url);
            }
        } catch (Exception e) {
            logger.error("error!", e);
        }
        return Collections.emptyList();
    }

}
