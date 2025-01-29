/**
 * Copyright (c) 2016-2025 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package de.csdev.ebus.configuration;

import de.csdev.ebus.core.EBusVersion;

public class EBusConfigurationVersion extends EBusVersion {

    private EBusConfigurationVersion() {
        throw new IllegalStateException("Utility class");
    }

    public static String getVersion() {
        return getVersion(EBusConfigurationVersion.class);
    }

    public static String getBuildCommit() {
        return getBuildCommit(EBusConfigurationVersion.class);
    }

    public static String getBuildTimestamp() {
        return getBuildTimestamp(EBusConfigurationVersion.class);
    }

    public static String getBuildNumber() {
        return getBuildNumber(EBusConfigurationVersion.class);
    }
}
