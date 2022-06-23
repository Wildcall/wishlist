package ru.rumal.wishlist.unit.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.util.Assert;
import ru.rumal.wishlist.model.dto.EventDto;
import ru.rumal.wishlist.model.dto.View;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class EventDtoTest {

    private final Validator validator = Validation
            .buildDefaultValidatorFactory()
            .getValidator();

    @Autowired
    private JacksonTester<EventDto> jacksonTester;

    @Autowired
    private ObjectMapper mapper;

    private static Map<String, Object> getCorrectResponseViewMap(EventDto eventDto) {
        Map<String, Object> requestView = new HashMap<>();
        requestView.put("id", eventDto.getId());
        requestView.put("name", eventDto.getName());
        requestView.put("description", eventDto.getDescription());
        requestView.put("date", eventDto
                .getDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        return requestView;
    }

    private static Map<String, Object> getCorrectNewViewMap() {
        Map<String, Object> requestView = new HashMap<>();
        requestView.put("name", "New event");
        requestView.put("date", LocalDateTime
                .now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        return requestView;
    }

    private static Map<String, Object> getCorrectUpdateViewMap() {
        Map<String, Object> requestView = new HashMap<>();
        requestView.put("name", "New event");
        requestView.put("date", LocalDateTime
                .now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        requestView.put("giftsSet", new HashSet<>());
        return requestView;
    }

    @SneakyThrows
    private static Stream<Arguments> new_view_invalid_json_stream() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        Stream.Builder<Arguments> sb = Stream.builder();

        Map<String, Object> map = getCorrectNewViewMap();
        String valid = mapper.writeValueAsString(map);
        sb.add(Arguments.of(valid, null, true));

        map = getCorrectNewViewMap();
        map.remove("name");
        String nameNotPresent = mapper.writeValueAsString(map);
        sb.add(Arguments.of(nameNotPresent, "name", false));

        map = getCorrectNewViewMap();
        map.remove("date");
        String dateNotPresent = mapper.writeValueAsString(map);
        sb.add(Arguments.of(dateNotPresent, "date", false));

        //  @formatter:off
        Stream.of("name", "description")
                .forEach(field -> Stream.of(generateString(1), generateString(256), "", "   ")
                        .forEach(value -> {
                            try {
                                Map<String, Object> correctNewViewMap = getCorrectNewViewMap();
                                correctNewViewMap.put(field, value);
                                String json = mapper.writeValueAsString(correctNewViewMap);
                                sb.add(Arguments.of(json, field, false));
                            } catch (JsonProcessingException ignored) {}
                        }));
        //  @formatter:on
        return sb.build();
    }

    @SneakyThrows
    private static Stream<Arguments> update_view_invalid_json_stream() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        Stream.Builder<Arguments> sb = Stream.builder();

        Map<String, Object> map = getCorrectUpdateViewMap();
        String valid = mapper.writeValueAsString(map);
        sb.add(Arguments.of(valid, null, true));

        map = getCorrectUpdateViewMap();
        map.remove("giftsSet");
        String giftsSetNotPresent = mapper.writeValueAsString(map);
        sb.add(Arguments.of(giftsSetNotPresent, "giftsSet", true));

        map = getCorrectUpdateViewMap();
        map.remove("date");
        String dateNotPresent = mapper.writeValueAsString(map);
        sb.add(Arguments.of(dateNotPresent, "date", true));

        map = getCorrectUpdateViewMap();
        map.remove("name");
        String nameNotPresent = mapper.writeValueAsString(map);
        sb.add(Arguments.of(nameNotPresent, "name", false));

        //  @formatter:off
        Stream.of("name", "description")
                .forEach(field -> Stream.of(generateString(1), generateString(256), "", "   ")
                        .forEach(value -> {
                            try {
                                Map<String, Object> correctNewViewMap = getCorrectUpdateViewMap();
                                correctNewViewMap.put(field, value);
                                String json = mapper.writeValueAsString(correctNewViewMap);
                                sb.add(Arguments.of(json, field, false));
                            } catch (JsonProcessingException ignored) {}
                        }));
        //  @formatter:on
        return sb.build();
    }

    public static String generateString(int size) {
        Assert.isTrue(size > 0, "Size must be more then 0");
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < size; i++) {
            str.append("A");
        }
        return str.toString();
    }

    @SneakyThrows
    @DisplayName("Pass if returned json correct")
    @Test
    void responseView() {
        EventDto eventDto = new EventDto(33L,
                                         "Event name",
                                         "description",
                                         LocalDateTime.of(2022, 6, 19, 10, 10, 10),
                                         new HashSet<>());

        JsonContent<EventDto> result = this.jacksonTester
                .forView(View.Response.class)
                .write(eventDto);

        String expectedJson = mapper.writeValueAsString(getCorrectResponseViewMap(eventDto));

        assertThat(result).isEqualToJson(expectedJson);
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("new_view_invalid_json_stream")
    void newView(String jsonRequest,
                 String propertyPath,
                 boolean expected) {
        EventDto result = this.jacksonTester
                .parse(jsonRequest)
                .getObject();

        Set<ConstraintViolation<EventDto>> violations = validator.validate(result, View.New.class);
        Assertions.assertEquals(violations.isEmpty(), expected);
        if (!expected) {
            long count = violations
                    .stream()
                    .filter(v -> !v
                            .getPropertyPath()
                            .toString()
                            .equals(propertyPath))
                    .count();
            Assertions.assertEquals(count, 0, "Wrong field throw exception");
        }
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("update_view_invalid_json_stream")
    void updateView(String jsonRequest,
                    String propertyPath,
                    boolean expected) {
        EventDto result = this.jacksonTester
                .parse(jsonRequest)
                .getObject();

        Set<ConstraintViolation<EventDto>> violations = validator.validate(result, View.Update.class);
        Assertions.assertEquals(violations.isEmpty(), expected);
        if (!expected) {
            long count = violations
                    .stream()
                    .filter(v -> !v
                            .getPropertyPath()
                            .toString()
                            .equals(propertyPath))
                    .count();
            Assertions.assertEquals(count, 0, "Wrong field throw exception");
        }
    }
}