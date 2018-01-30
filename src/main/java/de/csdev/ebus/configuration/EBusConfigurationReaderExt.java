package de.csdev.ebus.configuration;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.csdev.ebus.cfg.std.EBusConfigurationReader;
import de.csdev.ebus.command.IEBusCommandCollection;

public class EBusConfigurationReaderExt extends EBusConfigurationReader {

    private static final Logger logger = LoggerFactory.getLogger(EBusConfigurationReaderExt.class);

    @Override
    public List<IEBusCommandCollection> loadBuildInConfigurationCollections() {
        try {
            return loadConfigurationCollectionBundle(
                    EBusConfigurationReaderExt.class.getResource("/index-configuration.json"));
        } catch (Exception e) {
            logger.error("error!", e);
        }
        return null;
    }

}
