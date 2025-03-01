package com.example.teamcity.api;

import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static com.example.teamcity.api.enums.Endpoint.*;
import static com.example.teamcity.api.generators.TestDataGenerator.generate;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.hamcrest.Matchers.containsString;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.AssertJUnit.assertEquals;

@Test(groups = {"Regression"})
public class ProjectTest extends BaseApiTest {


    @Test(description = "User should be able to create a unique project", groups = {"Positive", "CRUD"})
    public void userCreatesProjectTest() {
        // Step 1: Create a super user
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());

        // Step 2: Initialize user-authenticated requests
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        var createProject = userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        assertNotNull(createProject, "Created project should not be null");
        assertNotNull(createProject.getId(), "Project ID should not be null");
        assertNotNull(createProject.getName(), "Project name should not be null");
        assertEquals(createProject.getStatusCode(), HttpStatus.SC_CREATED, 201);
    }

    @Test(description = "Projects can not be created with the same ID", groups = {"Negative", "CRUD"})
    public void userCreatesProjectWithExistingIDTest() {

        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        var createProject = userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject()); //Project 1

        new UncheckedBase(Specifications.authSpec(testData.getUser()), PROJECTS)
                .create(createProject) //Projetc wtih same ID
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("Project with this name already exists:".formatted(testData.getProject().getName())));

    }

    //Tests with empty IDs or invalid Locator doesn't work, as create Model already validates that. I don't know how to build it correctly without changing the whole modelbase

    @Test(description = "User should be able to create a project with empty id", groups = {"Negative", "CRUD"})
    public void userCreatesProjectWithEpmtyIDTest() {

        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        testData.getProject().setId("");
        var createProject = userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        assertEquals(createProject.getStatusCode(), HttpStatus.SC_INTERNAL_SERVER_ERROR, 500);
    }

    @Test(description = "User should be able to create a prokect with empty name", groups = {"Negative", "CRUD"})
    public void userCreatesProjectWithEpmtyNameTest() {

        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        testData.getProject().setName("");
        var createProject = userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        assertEquals(createProject.getStatusCode(), HttpStatus.SC_INTERNAL_SERVER_ERROR, 500);

    }

    @Test(description = "Projects can not be created with invalid auth", groups = {"Negative", "CRUD"})
    public void userCreatesProjectWithInvalidAuthTest() {
        step("Create user");
        step("Create project");
        step("Check Project was not created successfully, check Response and Message");
    }

}

