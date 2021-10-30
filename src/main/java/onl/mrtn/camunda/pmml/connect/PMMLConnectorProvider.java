/*
 * Copyright (c) 2021. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package onl.mrtn.camunda.pmml.connect;

import org.camunda.connect.spi.Connector;
import org.camunda.connect.spi.ConnectorProvider;

public class PMMLConnectorProvider implements ConnectorProvider {
    @Override
    public String getConnectorId() {
        return PMMLConnector.ID;
    }

    @Override
    public Connector<?> createConnectorInstance() {
        return new PMMLConnector(PMMLConnector.ID);
    }
}
