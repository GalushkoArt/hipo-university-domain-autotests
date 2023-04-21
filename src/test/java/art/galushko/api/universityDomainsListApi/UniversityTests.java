package art.galushko.api.universityDomainsListApi;

import art.galushko.api.ApiTests;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyExtractionOptions;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static art.galushko.api.steps.UniversitySteps.*;
import static art.galushko.config.Configs.UNIVERSITY.*;
import static java.util.function.Predicate.isEqual;
import static java.util.function.Predicate.not;

@DisplayName("University API Tests")
@Feature("University API Tests")
public class UniversityTests extends ApiTests {
    @Test
    @DisplayName("Info Endpoint Test")
    @Description("Checking that info endpoint returns HTML with API information")
    public void checkInfoEndpoint() {
        Response response = sendGetRequest(INFO_ENDPOINT);
        ValidatableResponse validatableResponse = expectStatusCode(response, 200);
        XmlPath html = validatableResponse.extract().body().htmlPath();
        String body = checkNotEmpty("HTML body", html.get("html.body"));
        JsonPath json = JsonPath.from(body);
        checkThatValidSoftly("Json from HTML body", assertions -> {
            assertions.assertThat((String) json.get("author.name")).as("Check author name").isEqualTo("hipo");
            assertions.assertThat((String) json.get("author.website")).as("Check author website").isEqualTo("http://hipolabs.com");
            assertions.assertThat((String) json.get("example")).as("Check example link").isEqualTo("http://universities.hipolabs.com/search?name=middle&country=Turkey");
            assertions.assertThat((String) json.get("github")).as("Check github link").isEqualTo("https://github.com/Hipo/university-domains-list");
        });
    }

    @Test
    @DisplayName("Search Endpoint Test Without Parameters")
    @Description("Checking that search endpoint returns all universities with correct format")
    public void checkSearchWithoutParameters() {
        Response response = sendGetRequest(SEARCH_ENDPOINT);
        ValidatableResponse validatableResponse = expectStatusCode(response, 200);
        checkThatUniversityResponsesAreNotEmpty(validatableResponse);
        checkThatHasAtLeast("Response Json", validatableResponse.extract().jsonPath(), 9980);
    }

    @Test
    @DisplayName("Search Endpoint Test with domain parameter")
    @Description("Checking that search endpoint with domain parameter will return only universities with specified domain")
    public void checkSearchWithDomainParameters() {
        Response response = sendGetRequestWithParams(SEARCH_ENDPOINT, Map.of("domain", "http://www.cnu.edu.cn/"));
        ValidatableResponse validatableResponse = expectStatusCode(response, 200);
        ResponseBodyExtractionOptions body = validatableResponse.extract().body();
        checkNotEmpty("JSON body", body.asString());
        JsonPath json = body.jsonPath();
        checkThatValidSoftly("Json from body", assertions -> {
            assertions.assertThat((List<?>) json.getList("domain")).as("Check countries of universities").allMatch(isEqual("http://www.cnu.edu.cn"));
        });
    }

    @Test
    @DisplayName("Search Endpoint Test with name, country and domain parameter dont consider domain parameter")
    @Description("Checking that search endpoint with name, country and domain parameter will return only universities with specified name part and country but don't consider domain")
    public void checkSearchWithDomainAndNameParameters() {
        Response response = sendGetRequestWithParams(
                SEARCH_ENDPOINT,
                Map.of(
                        "name", "india",
                        "country", "United States",
                        "domain", "http://www.cnu.edu.cn/"
                )
        );
        ValidatableResponse validatableResponse = expectStatusCode(response, 200);
        ResponseBodyExtractionOptions body = validatableResponse.extract().body();
        checkNotEmpty("JSON body", body.asString());
        JsonPath json = body.jsonPath();
        checkThatValidSoftly("Json body", assertions -> {
            assertions.assertThat((List<?>) json.getList("country")).as("Check countries of universities").allMatch(isEqual("United States"));
            assertions.assertThat((List<?>) json.getList("name")).as("Check names of universities").allMatch(string -> string.toString().toLowerCase().contains("india"));
            assertions.assertThat((List<?>) json.getList("domain")).as("Check countries of universities").allMatch(not(isEqual("http://www.cnu.edu.cn")));
        });
    }

    @Test
    @DisplayName("Search Endpoint Test Empty List Parameters")
    @Description("Checking that search endpoint can return empty list")
    public void checkSearchWithParameters() {
        Response response = sendGetRequestWithParams(SEARCH_ENDPOINT, Map.of("name", "i think this should not be found"));
        ValidatableResponse validatableResponse = expectStatusCode(response, 200);
        JsonPath json = validatableResponse.extract().body().jsonPath();
        checkThatValidSoftly("Json body", assertions -> {
            assertions.assertThat((int) json.get("size()")).as("Check countries of universities").isEqualTo(0);
        });
    }

    @Test
    @DisplayName("Update Endpoint Test")
    @Description("Checking that update endpoint shows that dataset was updated")
    @TmsLink("UNI-45")
    public void checkUpdateEndpoint() {
        Response response = sendGetRequest(UPDATE_ENDPOINT);
        ValidatableResponse validatableResponse = expectStatusCode(response, 200);
        JsonPath json = validatableResponse.extract().jsonPath();
        checkThatValidSoftly("Json body", assertions -> {
            assertions.assertThat((String) json.get("status")).as("Check author name").isEqualTo("error");
            assertions.assertThat((String) json.get("message")).as("Check author website").isEqualTo("Dataset had been updated recently. Try again later.");
        });
    }
}
