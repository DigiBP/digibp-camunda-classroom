/*
 * Copyright (c) 2019. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package ch.fhnw.digibp.classroom.endpoint;

import ch.fhnw.digibp.classroom.service.PMMLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/pmml")
public class PMMLAPI {

    @Autowired
    private PMMLService pmmlService;

    @PostMapping(path = "/{deployment-id}/{file-name}/evaluate", consumes = "application/json", produces = "application/json")
    public EvaluateDto evaluate(@PathVariable("deployment-id") String deploymentId, @PathVariable("file-name") String fileName, @RequestParam(value = "model-name", required = false, defaultValue = "") String modelName, @RequestBody EvaluateDto evaluateDto) throws Exception {
        return new EvaluateDto(pmmlService.evaluate(fileName, modelName, evaluateDto.getVariables(), deploymentId));
    }

    @GetMapping(path = "/{deployment-id}/{file-name}/evaluate/info", produces = "application/json")
    public Map<String, List<?>> evaluateInfo(@PathVariable("deployment-id") String deploymentId, @PathVariable("file-name") String fileName, @RequestParam(value = "model-name", required = false, defaultValue = "") String modelName) throws Exception {
        return pmmlService.evaluateInfo(fileName, modelName, deploymentId);
    }

    @GetMapping(path = "/{deployment-id}/{file-name}/evaluate/generate-input", produces = "application/json")
    public EvaluateDto evaluateGenerateInput(@PathVariable("deployment-id") String deploymentId, @PathVariable("file-name") String fileName, @RequestParam(value = "model-name", required = false, defaultValue = "") String modelName) throws Exception {
        return new EvaluateDto(pmmlService.evaluateGenerateInput(fileName, modelName, deploymentId));
    }

    public static class EvaluateDto {

        protected Map<String, EvaluateValue> variables = new HashMap<>();

        private EvaluateDto() {}

        public EvaluateDto(Map<String, ?> variables) {
            setVariables(variables);
        }

        public Map<String, ?> getVariables() {
            Map<String, Object> variables = new HashMap<>();
            for (Map.Entry<String, EvaluateValue> value : this.variables.entrySet()) {
                variables.put(value.getKey(), value.getValue().getValue());
            }
            return variables;
        }

        public void setVariables(Map<String, ?> map) {
            if (map != null) {
                if (map instanceof EvaluateDto) {
                    this.variables.putAll(((EvaluateDto)map).variables);
                } else {
                    for (Map.Entry<String, ?> value : map.entrySet()) {
                        this.variables.put(value.getKey(), new EvaluateValue(value.getValue()));
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
