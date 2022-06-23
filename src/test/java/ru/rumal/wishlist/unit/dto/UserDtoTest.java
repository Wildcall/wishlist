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
import ru.rumal.wishlist.model.AuthType;
import ru.rumal.wishlist.model.Role;
import ru.rumal.wishlist.model.dto.UserDto;
import ru.rumal.wishlist.model.dto.View;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoTest {

    private final Validator validator = Validation
            .buildDefaultValidatorFactory()
            .getValidator();

    @Autowired
    private JacksonTester<UserDto> jacksonTester;

    @Autowired
    private ObjectMapper mapper;

    private static Map<String, Object> getCorrectResponseViewMap(UserDto userDto) {
        Map<String, Object> responseView = new HashMap<>();
        responseView.put("id", userDto.getId());
        responseView.put("email", userDto.getEmail());
        responseView.put("name", userDto.getName());
        responseView.put("picture", userDto.getPicture());
        responseView.put("authType", userDto.getAuthType().name());
        responseView.put("role", userDto.getRole().name());
        return responseView;
    }

    private static Map<String, Object> getCorrectNewViewMap() {
        Map<String, Object> requestView = new HashMap<>();
        requestView.put("email", "email@email.com");
        requestView.put("password", "password");
        requestView.put("name", "name");
        return requestView;
    }

    private static Map<String, Object> getCorrectUpdateViewMap() {
        Map<String, Object> requestView = new HashMap<>();
        requestView.put("email", "email@email.com");
        requestView.put("name", "name");
        return requestView;
    }

    private static Map<String, Object> getCorrectUpdatePasswordViewMap() {
        Map<String, Object> requestView = new HashMap<>();
        requestView.put("password", "password");
        requestView.put("newPassword", "newPassword");
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
        map.put("email", "wrongEmail");
        String invalidEmail = mapper.writeValueAsString(map);
        sb.add(Arguments.of(invalidEmail, "email", false));

        map = getCorrectNewViewMap();
        map.put("password", generateString(7));
        String shortPassword = mapper.writeValueAsString(map);
        sb.add(Arguments.of(shortPassword, "password", false));

        map = getCorrectNewViewMap();
        map.put("password", generateString(25));
        String longPassword = mapper.writeValueAsString(map);
        sb.add(Arguments.of(longPassword, "password", false));

        map = getCorrectNewViewMap();
        map.put("name", generateString(1));
        String shortName = mapper.writeValueAsString(map);
        sb.add(Arguments.of(shortName, "name", false));

        map = getCorrectNewViewMap();
        map.put("name", generateString(25));
        String longName = mapper.writeValueAsString(map);
        sb.add(Arguments.of(longName, "name", false));

        map = getCorrectNewViewMap();
        map.put("name", "&MyName");
        String wrongName = mapper.writeValueAsString(map);
        sb.add(Arguments.of(wrongName, "name", false));

        //  @formatter:off
        Stream.of("email", "password", "name")
                .forEach(field -> Stream.of("", "  ", null)
                        .forEach(value -> {
                            try {
                                Map<String, Object> correctNewViewMap = getCorrectNewViewMap();
                                correctNewViewMap.put(field, value);
                                String json = mapper.writeValueAsString(correctNewViewMap);
                                sb.add(Arguments.of(json, field, false));
                            } catch (JsonProcessingException ignored) {}
                        }));

        Stream.of("email", "password", "name")
                .forEach(field -> {
                    try {
                        Map<String, Object> correctNewViewMap = getCorrectNewViewMap();
                        correctNewViewMap.remove(field);
                        String json = mapper.writeValueAsString(correctNewViewMap);
                        sb.add(Arguments.of(json, field, false));
                    } catch (JsonProcessingException ignored) {}
                });
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
        map.put("email", "wrongEmail");
        String invalidEmail = mapper.writeValueAsString(map);
        sb.add(Arguments.of(invalidEmail, "email", false));

        map = getCorrectUpdateViewMap();
        map.put("name", "s");
        String shortName = mapper.writeValueAsString(map);
        sb.add(Arguments.of(shortName, "name", false));

        map = getCorrectUpdateViewMap();
        map.put("name", "too_1234567891234567891234_long");
        String longName = mapper.writeValueAsString(map);
        sb.add(Arguments.of(longName, "name", false));

        map = getCorrectUpdateViewMap();
        map.put("name", "&MyName");
        String wrongName = mapper.writeValueAsString(map);
        sb.add(Arguments.of(wrongName, "name", false));

        //  @formatter:off
        Stream.of("email", "name")
                .forEach(field -> Stream.of("", "  ", null)
                        .forEach(value -> {
                            try {
                                Map<String, Object> correctNewViewMap = getCorrectUpdateViewMap();
                                correctNewViewMap.put(field, value);
                                String json = mapper.writeValueAsString(correctNewViewMap);
                                sb.add(Arguments.of(json, field, false));
                            } catch (JsonProcessingException ignored) {}
                        }));

        Stream.of("email", "name")
                .forEach(field -> {
                    try {
                        Map<String, Object> correctNewViewMap = getCorrectUpdateViewMap();
                        correctNewViewMap.remove(field);
                        String json = mapper.writeValueAsString(correctNewViewMap);
                        sb.add(Arguments.of(json, field, false));
                    } catch (JsonProcessingException ignored) {}
                });
        //  @formatter:on
        return sb.build();
    }

    @SneakyThrows
    private static Stream<Arguments> update_password_view_invalid_json_stream() {
        ObjectMapper mapper = new ObjectMapper();
        Stream.Builder<Arguments> sb = Stream.builder();

        Map<String, Object> map = getCorrectUpdatePasswordViewMap();
        String valid = mapper.writeValueAsString(map);
        sb.add(Arguments.of(valid, null, true));

        map = getCorrectUpdatePasswordViewMap();
        map.put("password", "s");
        String shortPassword = mapper.writeValueAsString(map);
        sb.add(Arguments.of(shortPassword, "password", false));

        map = getCorrectUpdatePasswordViewMap();
        map.put("password", "too_1234567891234567891234_long");
        String longPassword = mapper.writeValueAsString(map);
        sb.add(Arguments.of(longPassword, "password", false));

        map = getCorrectUpdatePasswordViewMap();
        map.put("newPassword", "s");
        String shortNewPassword = mapper.writeValueAsString(map);
        sb.add(Arguments.of(shortNewPassword, "newPassword", false));

        map = getCorrectUpdatePasswordViewMap();
        map.put("newPassword", "too_1234567891234567891234_long");
        String longNewPassword = mapper.writeValueAsString(map);
        sb.add(Arguments.of(longNewPassword, "newPassword", false));

        //  @formatter:off
        Stream.of("password", "newPassword")
                .forEach(field -> Stream.of("", "  ", null)
                        .forEach(value -> {
                            try {
                                Map<String, Object> correctNewViewMap = getCorrectUpdatePasswordViewMap();
                                correctNewViewMap.put(field, value);
                                String json = mapper.writeValueAsString(correctNewViewMap);
                                sb.add(Arguments.of(json, field, false));
                            } catch (JsonProcessingException ignored) {}
                        }));

        Stream.of("password", "newPassword")
                .forEach(field -> {
                    try {
                        Map<String, Object> correctNewViewMap = getCorrectUpdatePasswordViewMap();
                        correctNewViewMap.remove(field);
                        String json = mapper.writeValueAsString(correctNewViewMap);
                        sb.add(Arguments.of(json, field, false));
                    } catch (JsonProcessingException ignored) {}
                });
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
        UserDto userDto = new UserDto("id",
                                      "email",
                                      "password",
                                      "new password",
                                      "name",
                                      "picture",
                                      AuthType.APPLICATION,
                                      true,
                                      Role.USER);

        JsonContent<UserDto> result = this.jacksonTester
                .forView(View.Response.class)
                .write(userDto);

        String expectedJson = mapper.writeValueAsString(getCorrectResponseViewMap(userDto));

        assertThat(result).isEqualToJson(expectedJson);

        assertThat(result).doesNotHaveJsonPath("$.password");
        assertThat(result).doesNotHaveJsonPath("$.newPassword");
        assertThat(result).doesNotHaveJsonPath("$.enable");
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("new_view_invalid_json_stream")
    void newView(String jsonRequest,
                 String propertyPath,
                 boolean expected) {
        UserDto result = this.jacksonTester
                .parse(jsonRequest)
                .getObject();

        Set<ConstraintViolation<UserDto>> violations = validator.validate(result, View.New.class);
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
        UserDto result = this.jacksonTester
                .parse(jsonRequest)
                .getObject();

        Set<ConstraintViolation<UserDto>> violations = validator.validate(result, View.Update.class);
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
    @MethodSource("update_password_view_invalid_json_stream")
    void updatePasswordView(String jsonRequest,
                            String propertyPath,
                            boolean expected) {
        UserDto result = this.jacksonTester
                .parse(jsonRequest)
                .getObject();

        Set<ConstraintViolation<UserDto>> violations = validator.validate(result, View.UpdatePassword.class);
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