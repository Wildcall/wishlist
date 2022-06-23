package ru.rumal.wishlist.unit.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.rumal.wishlist.model.GiftStatus;
import ru.rumal.wishlist.model.dto.GiftDto;
import ru.rumal.wishlist.model.dto.View;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class GiftDtoTest {

    private final Validator validator = Validation
            .buildDefaultValidatorFactory()
            .getValidator();

    @Autowired
    private JacksonTester<GiftDto> jacksonTester;

    @Autowired
    private ObjectMapper mapper;

    private static Map<String, Object> getCorrectResponseViewMap(GiftDto giftDto) {
        Map<String, Object> requestView = new HashMap<>();
        requestView.put("id", giftDto.getId());
        requestView.put("name", giftDto.getName());
        requestView.put("link", giftDto.getLink());
        requestView.put("picture", giftDto.getPicture());
        requestView.put("description", giftDto.getDescription());
        requestView.put("status", giftDto.getStatus());
        requestView.put("eventsId", giftDto.getEventsId());
        requestView.put("tagId", giftDto.getTagId());
        return requestView;
    }

    private static Map<String, Object> getCorrectNewViewMap() {
        Map<String, Object> requestView = new HashMap<>();
        requestView.put("name", "New gift");
        return requestView;
    }

    private static Map<String, Object> getCorrectUpdateViewMap() {
        Map<String, Object> requestView = new HashMap<>();
        requestView.put("name", "New gift");
        requestView.put("status", GiftStatus.NEW);
        return requestView;
    }

    @SneakyThrows
    private static Stream<Arguments> new_view_invalid_json_stream() {
        ObjectMapper mapper = new ObjectMapper();
        Stream.Builder<Arguments> sb = Stream.builder();

        Map<String, Object> map = getCorrectNewViewMap();
        String valid = mapper.writeValueAsString(map);
        sb.add(Arguments.of(valid, null, true));

        map = getCorrectNewViewMap();
        map.put("tagId", -1L);
        String tagBelowZero = mapper.writeValueAsString(map);
        sb.add(Arguments.of(tagBelowZero, "tagId", false));

        //  @formatter:off
        Stream.of("name", "link", "picture", "description")
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
        Stream.Builder<Arguments> sb = Stream.builder();

        Map<String, Object> map = getCorrectUpdateViewMap();
        String valid = mapper.writeValueAsString(map);
        sb.add(Arguments.of(valid, null, true));

        map = getCorrectUpdateViewMap();
        map.put("status", GiftStatus.RECEIVED.name());
        String valid_RECEIVED_Status = mapper.writeValueAsString(map);
        sb.add(Arguments.of(valid_RECEIVED_Status, null, true));

        map = getCorrectUpdateViewMap();
        map.put("status", GiftStatus.RESERVED.name());
        String valid_RESERVED_Status = mapper.writeValueAsString(map);
        sb.add(Arguments.of(valid_RESERVED_Status, null, true));

        map = getCorrectUpdateViewMap();
        map.put("status", "WRONG");
        String wrongStatus = mapper.writeValueAsString(map);
        sb.add(Arguments.of(wrongStatus, "status", false));

        map = getCorrectUpdateViewMap();
        map.put("tagId", -1L);
        String tagBelowZero = mapper.writeValueAsString(map);
        sb.add(Arguments.of(tagBelowZero, "tagId", false));


        //  @formatter:off
        Stream.of("name", "link", "picture", "description")
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
    @DisplayName("Pass if returned json not contains private data")
    @Test
    void responseView() {
        GiftDto giftDto = new GiftDto(33L,
                                      "Gift name",
                                      "link to gift",
                                      "link to gift picture",
                                      "description",
                                      GiftStatus.NEW.name(),
                                      new HashSet<>(),
                                      22L);

        JsonContent<GiftDto> result = this.jacksonTester
                .forView(View.Response.class)
                .write(giftDto);

        String expectedJson = mapper.writeValueAsString(getCorrectResponseViewMap(giftDto));

        assertThat(result).isEqualToJson(expectedJson);
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("new_view_invalid_json_stream")
    void newView(String jsonRequest,
                 String propertyPath,
                 boolean expected) {
        GiftDto result = this.jacksonTester
                .parse(jsonRequest)
                .getObject();

        Set<ConstraintViolation<GiftDto>> violations = validator.validate(result, View.New.class);
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
        GiftDto result = this.jacksonTester
                .parse(jsonRequest)
                .getObject();

        Set<ConstraintViolation<GiftDto>> violations = validator.validate(result, View.Update.class);
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