package ppzeff.securing.autsber.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServiceObjectMapper {
    private final ObjectMapper objectMapper;

    private static ServiceObjectMapper instance;

    public static synchronized ObjectMapper getObjectMapper() {
        if (instance == null) {
            synchronized (ServiceObjectMapper.class) {
                if (instance == null) {
                    instance = new ServiceObjectMapper();
                }
            }
        }
        return instance.objectMapper;
    }

    private ServiceObjectMapper() {
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}

