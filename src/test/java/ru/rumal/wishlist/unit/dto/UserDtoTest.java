package ru.rumal.wishlist.unit.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.context.annotation.Import;
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

@Import(Utils.class)
@JsonTest
class UserDtoTest {

    private final Validator validator = Validation
            .buildDefaultValidatorFactory()
            .getValidator();

    @Autowired
    private Utils utils;

    @Autowired
    private JacksonTester<UserDto> jacksonTester;

    @Autowired
    private ObjectMapper mapper;

    public static Map<String, Object> getCorrectNewViewMap() {
        Map<String, Object> requestView = new HashMap<>();
        requestView.put("email", "email@email.com");
        requestView.put("password", "password");
        requestView.put("name", "name");
        return requestView;
    }

    @SneakyThrows
    @Test
    public void responseView() {
        UserDto userDto = utils.getUserDto();

        JsonContent<UserDto> result = this.jacksonTester
                .forView(View.Response.class)
                .write(userDto);

        Map<String, Object> responseView = new HashMap<>();
        responseView.put("id", userDto.getId());
        responseView.put("email", userDto.getEmail());
        responseView.put("name", userDto.getName());
        responseView.put("picture", userDto.getPicture());
        responseView.put("authType", userDto
                .getAuthType()
                .name());
        responseView.put("role", userDto
                .getRole()
                .name());
        String expectedJson = mapper.writeValueAsString(responseView);

        assertThat(result).isEqualToJson(expectedJson);

        assertThat(result).doesNotHaveJsonPath("$.password");
        assertThat(result).doesNotHaveJsonPath("$.newPassword");
        assertThat(result).doesNotHaveJsonPath("$.enable");
    }

    @SneakyThrows
    @Test
    public void NewView_ValidJson() {
        String jsonRequest = mapper.writeValueAsString(getCorrectNewViewMap());

        UserDto result = this.jacksonTester
                .parse(jsonRequest)
                .getObject();

        Set<ConstraintViolation<UserDto>> violations = validator.validate(result, View.New.class);
        Assertions.assertTrue(violations.isEmpty());
    }

    @SneakyThrows
    @ParameterizedTest
    @MethodSource("new_view_invalid_json_stream")
    public void NewView_InvalidJson(String jsonRequest) {
        UserDto result = this.jacksonTester
                .parse(jsonRequest)
                .getObject();

        Set<ConstraintViolation<UserDto>> violations = validator.validate(result, View.New.class);
        Assertions.assertFalse(violations.isEmpty());
    }

    @SneakyThrows
    public static Stream<String> new_view_invalid_json_stream() {
        ObjectMapper mapper = new ObjectMapper();
        Stream.Builder<String> sb = Stream.builder();

        Map<String, Object> map = getCorrectNewViewMap();
        map.put("email", "wrongEmail");
        String invalidEmail = mapper.writeValueAsString(map);
        sb.add(invalidEmail);

        map = getCorrectNewViewMap();
        map.put("password", "short");
        String shortPassword = mapper.writeValueAsString(map);
        sb.add(shortPassword);

        map = getCorrectNewViewMap();
        map.put("password", "too_1234567891234567891234_long");
        String longPassword = mapper.writeValueAsString(map);
        sb.add(longPassword);

        map = getCorrectNewViewMap();
        map.put("name", "s");
        String shortName = mapper.writeValueAsString(map);
        sb.add(shortName);

        map = getCorrectNewViewMap();
        map.put("name", "too_1234567891234567891234_long");
        String longName = mapper.writeValueAsString(map);
        sb.add(longName);

        map = getCorrectNewViewMap();
        map.put("name", "&MyName");
        String wrongName = mapper.writeValueAsString(map);
        sb.add(wrongName);

        //  @formatter:off
        Stream.of("email", "password", "name")
                .forEach(field -> Stream.of("", "  ", null)
                        .forEach(value -> {
                            try {
                                Map<String, Object> correctNewViewMap = getCorrectNewViewMap();
                                correctNewViewMap.put(field, value);
                                String json = mapper.writeValueAsString(correctNewViewMap);
                                sb.add(json);
                            } catch (JsonProcessingException ignored) {}
                        }));

        Stream.of("email", "password", "name")
                .forEach(field -> {
                    try {
                        Map<String, Object> correctNewViewMap = getCorrectNewViewMap();
                        correctNewViewMap.remove(field);
                        String json = mapper.writeValueAsString(correctNewViewMap);
                        sb.add(json);
                    } catch (JsonProcessingException ignored) {}
                });
        //  @formatter:on
        return sb.build();
    }
}