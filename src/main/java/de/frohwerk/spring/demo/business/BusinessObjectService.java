package de.frohwerk.spring.demo.business;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
// Business service depends on attributes defined by the interface
public class BusinessObjectService {

    private final AtomicInteger sequence = new AtomicInteger(0);

    private final Map<String, BusinessObject> repo = new HashMap<>();

    public BusinessObject save(final BusinessObject businessObject) {
        repo.put(Integer.toString(sequence.incrementAndGet()), businessObject);
        return businessObject;
    }

    public BusinessObject getById(final String id) {
        return repo.get(id);
    }

}
