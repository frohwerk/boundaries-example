package de.frohwerk.spring.demo;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.frohwerk.spring.demo.business.BusinessObject;
import de.frohwerk.complex.library.RequestObject;
import de.frohwerk.spring.demo.business.BusinessObjectService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
// API accepts and returns all attributes supported on API level
public class DemoController {

    private final ObjectMapper objectMapper;

    private final BusinessObjectService businessObjectService;

    @PostMapping(path = "/objects", consumes = MediaType.APPLICATION_JSON_VALUE)
    public RequestObject createObject(@RequestBody final RequestObject requestObject) {
        final var businessObject = objectMapper.convertValue(requestObject, BusinessObjectImpl.class);
        final var savedObject = businessObjectService.save(businessObject);
        return objectMapper.convertValue(savedObject, RequestObject.class);
    }

    @GetMapping("/objects/{id}")
    public RequestObject getObject(@PathVariable final String id) {
        return objectMapper.convertValue(businessObjectService.getById(id), RequestObject.class);
    }

    @Data
    // Relaxed binding at the business level
    private static class BusinessObjectImpl implements BusinessObject {

        private String name;

        private final Map<String, Object> additionalAttributes = new HashMap<>();

        @JsonAnyGetter
        public Map<String, Object> getAdditionalAttributes() {
            return Map.copyOf(this.additionalAttributes);
        }

        @JsonAnySetter
        public void setAdditionalAttribute(final String attributeName, final Object attributeValue) {
            this.additionalAttributes.put(attributeName, attributeValue);
        }

    }
}
