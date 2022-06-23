package ru.rumal.wishlist.integration.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Map;

@Getter
@RequiredArgsConstructor
@TestConfiguration
public class ResultActionUtils {

    private final ObjectMapper mapper;

    public Long extractLongId(ResultActions resultActions) {
        try {
            Map map = mapper.readValue(resultActions
                                               .andReturn()
                                               .getResponse()
                                               .getContentAsString(), Map.class);
            Object id = map.get("id");
            return ((Number) id).longValue();
        } catch (Exception e) {
            return null;
        }
    }

    public String extractStringId(ResultActions resultActions) {
        try {
            Map map = mapper.readValue(resultActions
                                               .andReturn()
                                               .getResponse()
                                               .getContentAsString(), Map.class);
            Object id = map.get("id");
            return (String) id;
        } catch (Exception e) {
            return null;
        }
    }
}
