/*
 * Copyright (c) 2021. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package onl.mrtn.camunda.sparql;

import com.complexible.stardog.api.ConnectionConfiguration;
import com.complexible.stardog.api.SelectQuery;
import com.complexible.stardog.api.reasoning.ReasoningConnection;
import com.stardog.stark.query.Binding;
import com.stardog.stark.query.BindingSet;
import com.stardog.stark.query.SelectQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SPARQLEvaluator {
    private final Logger logger = LoggerFactory.getLogger(SPARQLEvaluator.class);

    public Map<String, ?> evaluate(String selectQuery, Map<String, ?> queryParameter, boolean reasoningFlag, String dbName, String serverURL, String username, String password){
        Map<String, List<String>> results = new HashMap<>();

        ReasoningConnection aReasoningConn = ConnectionConfiguration
                .to(dbName)
                .server(serverURL)
                .credentials(username, password)
                .reasoning(reasoningFlag)
                .connect()
                .as(ReasoningConnection.class);

        SelectQuery aQuery = aReasoningConn.select(selectQuery);

        for (Map.Entry<String,?> entry : queryParameter.entrySet()) {
            Object value = entry.getValue();
            if( value instanceof String) {
                try {
                    value = Long.parseLong((String) value);
                } catch (NumberFormatException e) {
                    try {
                        value = Double.parseDouble((String) value);
                    } catch (NumberFormatException ignored) {}
                }
            }
            aQuery.parameter(entry.getKey(), value);
        }

        try(SelectQueryResult aResult = aQuery.execute()) {
            while(aResult.hasNext()){
                BindingSet bindingSet = aResult.next();
                for(Binding binding : bindingSet){
                    String value = "";
                    if(binding.literal().isPresent()) {
                        value = binding.literal().get().label();
                    }
                    String name = binding.name();
                    if(!results.containsKey(name)){
                        List<String> values = new ArrayList<>();
                        values.add(value);
                        results.put(name, values);
                    } else {
                        results.get(name).add(value);
                    }
                }
            }
        }

        return results;
    }
}

