package art.galushko.api.specs;

import art.galushko.config.Configs.UNIVERSITY;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class UniversitySpecs {
    public static RequestSpecification universityApi(){
        return new RequestSpecBuilder()
                .setBaseUri(UNIVERSITY.HOST)
                .setContentType("application/json")
                .build().filter(new AllureRestAssured());
    }
}
