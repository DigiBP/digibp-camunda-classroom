/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.endpoint;

import ch.fhnw.digibp.classroom.service.SPARQLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/sparql")
public class SPARQLAPI {

    @Autowired
    private SPARQLService sparqlService;

    @PostMapping(path = "/evaluate", consumes = "application/json", produces = "application/json")
    public EvaluateDto evaluate(@RequestParam("selectQuery") String selectQuery, @RequestParam(value = "reasoningFlag", defaultValue = "true") boolean reasoningFlag, @RequestParam(value = "dbName") String dbName, @RequestParam(value = "serverURL") String serverURL, @RequestParam(value = "username") String username, @RequestParam(value = "password") String password, @RequestBody(required = false) EvaluateDto queryParameter) throws Exception {
        return new EvaluateDto(sparqlService.evaluate(selectQuery, queryParameter.getParameter(), reasoningFlag, dbName, serverURL, username, password));
    }

    public static class EvaluateDto {

        protected Map<String, EvaluateValue> parameter = new HashMap<>();

        private EvaluateDto() {}

        public EvaluateDto(Map<String, ?> parameter) {
            setParameter(parameter);
        }

        public Map<String, ?> getParameter() {
            Map<String, Object> parameter = new HashMap<>();
            for (Map.Entry<String, EvaluateValue> value : this.parameter.entrySet()) {
                parameter.put(value.getKey(), value.getValue().getValue());
            }
            return parameter;
        }

        public void setParameter(Map<String, ?> map) {
            if (map != null) {
                if (map instanceof EvaluateDto) {
                    this.parameter.putAll(((EvaluateDto)map).parameter);
                } else {
                    for (Map.Entry<String, ?> value : map.entrySet()) {
                        this.parameter.put(value.getKey(), new EvaluateValue(value.getValue()));
                    }
                }
            }
        }

        public static class EvaluateValue {
            protected Object value;

            public EvaluateValue() {}

            public EvaluateValue(Object value) {
                this.value = value;
            }

            public Object getValue() {
                return value;
            }

            public void setValue(Object value) {
                this.value = value;
            }
        }

    }

}
