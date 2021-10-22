/*
 * Copyright (c) 2021. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package onl.mrtn.pmml.camunda.pmml.connect;

import org.camunda.connect.spi.Connector;
import org.camunda.connect.spi.ConnectorProvider;

public class PmmlConnectorProvider implements ConnectorProvider {
    @Override
    public String getConnectorId() {
        return PmmlConnector.ID;
    }

    @Override
    public Connector<?> createConnectorInstance() {
        return new PmmlConnector(PmmlConnector.ID);
    }
}
