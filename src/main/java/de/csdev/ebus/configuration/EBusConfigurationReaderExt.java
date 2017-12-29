package de.csdev.ebus.configuration;

import java.util.List;

import de.csdev.ebus.cfg.std.EBusConfigurationReader;
import de.csdev.ebus.command.IEBusCommandCollection;

public class EBusConfigurationReaderExt extends EBusConfigurationReader {

    @Override
    public List<IEBusCommandCollection> loadBuildInConfigurationCollections() {
        // TODO Auto-generated method stub
        // return super.loadBuildInConfigurationCollections();
        return loadConfigurationCollectionBundle(
                EBusConfigurationReaderExt.class.getResource("/index-configuration.json"));
    }

}
