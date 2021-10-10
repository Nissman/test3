package pz;

import io.restassured.response.Response;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class AccountTests extends BaseTest {
    @Test
    void gatAccountInfoTest(){
        given()
                .header("Authorization", token)
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .then()
                .statusCode(200);
    }

    @Test
    void getAccountInfoWithLoggingTest(){
        given()
                .header("Authorization", "Bearer 9d2306f677fa45ecbbe39df15c86f710fb9692fc")
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .prettyPeek()
                .then()
                .statusCode(200);

    }
    @Test
    void getAccountInfoWithAssertionsInGivenTest(){
        given()
                .header("Authorization", token)
                .log()
                .method()
                .log()
                .uri()
                .expect()
                .statusCode(200)
                .body("data.url", equalTo(username))
                .body("success",equalTo(true) )
                .body("status",equalTo(200) )
                .contentType("application/json")
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .prettyPeek();
    }
    @Test
    void getAccountInfoWithAssertionsAfterTest(){
       Response response =  given()
                .header("Authorization", token)
                .log()
                .method()
                .log()
                .uri()
               .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .prettyPeek();

       assertThat(response.jsonPath().get("data.url"), equalTo(username));
    }
}
