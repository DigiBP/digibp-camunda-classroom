/*
 * Copyright (c) 2021. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package onl.mrtn.camunda.sparql.connect;

import org.camunda.connect.spi.Connector;
import org.camunda.connect.spi.ConnectorProvider;

public class SPARQLConnectorProvider implements ConnectorProvider {
    @Override
    public String getConnectorId() {
        return SPARQLConnector.ID;
    }

    @Override
    public Connector<?> createConnectorInstance() {
        return new SPARQLConnector(SPARQLConnector.ID);
    }
}
