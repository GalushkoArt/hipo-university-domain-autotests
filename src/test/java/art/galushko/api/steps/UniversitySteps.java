package art.galushko.api.steps;

import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;

import java.util.Map;
import java.util.function.Consumer;

import static art.galushko.api.specs.UniversitySpecs.universityApi;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.hamcrest.Matchers.notNullValue;

@Slf4j
public class UniversitySteps {
    @Step("Send GET request to {endpoint}")
    public static Response sendGetRequest(String endpoint) {
        log.info("Send GET request to " + endpoint);
        return given().spec(universityApi())
                .when()
                .get(endpoint);
    }
    @Step("Send GET request to {endpoint} with parameters")
    public static Response sendGetRequestWithParams(String endpoint, Map<String, Object> params) {
        log.info("Send GET request to " + endpoint + " with params: " + params);
        return given().spec(universityApi())
                .params(params)
                .when()
                .get(endpoint);
    }

    @Step("Response status code should be equal to {statusCode}")
    public static ValidatableResponse expectStatusCode(Response response, int statusCode) {
        log.info("Check response status code is equal to " + statusCode);
        return response.then()
                .statusCode(statusCode)
                .log().all();
    }

    @Step("Check that {name} is not empty")
    public static String checkNotEmpty(String name, String actual) {
        log.info("Checking that " + name + " is not empty");
        assertThat(actual).as(name + " should not be empty").isNotEmpty();
        return actual;
    }

    @Step("Check that {name} has at least {expected} elements")
    public static void checkThatHasAtLeast(String name, JsonPath jsonPath, int expected) {
        log.info("Checking size of " + name + " greater or equal to " + expected);
        assertThat((int) jsonPath.get("size()")).as(name + " should have at least " + expected + " elements").isGreaterThanOrEqualTo(expected);
    }

    @Step("Check that {name} is valid")
    public static void checkThatValidSoftly(String name, Consumer<SoftAssertions> softly) {
        log.info("Checking that " + name + " is valid");
        assertSoftly(softly);
    }

    @Step("Check that university responses have not null fields")
    public static void checkThatUniversityResponsesAreNotEmpty(ValidatableResponse validatableResponse) {
        log.info("Checking Universities from response don't have null in mandatory fields");
        validatableResponse.body("country", notNullValue())
                .body("name", notNullValue())
                .body("web_pages", notNullValue())
                .body("alpha_two_code", notNullValue())
                .body("domains", notNullValue());
    }
}
