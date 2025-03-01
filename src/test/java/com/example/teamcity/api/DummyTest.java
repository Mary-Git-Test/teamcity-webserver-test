package com.example.teamcity.api;

import com.example.teamcity.api.models.User;
import com.example.teamcity.api.spec.Specifications;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static java.nio.file.Paths.get;

public class DummyTest extends BaseApiTest {
    @Test
    public void userShouldBeAbleToGetAllProjects() {
        RestAssured
                .given()
                .spec(Specifications
                        .authSpec(User.builder()
                                .username("admin") // Fixed field name
                                .password("admin")
                                .build()))
                .get("/app/rest/projects");
    }
}