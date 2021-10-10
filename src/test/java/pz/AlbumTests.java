package pz;
import io.restassured.response.Response;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import static io.restassured.RestAssured.given;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.CoreMatchers.is;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AlbumTests extends BaseTest {
    static String albumId;
    static String createAlbumId;

    @Order(1)
    @Test
    void createAlbumWithoutParamTest() {

        Response response =   given()
                .header("Authorization", token)

                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post("https://api.imgur.com/3/album")
                .prettyPeek();

        albumId=response.jsonPath().get("data.id");
        createAlbumId=response.jsonPath().get("data.deletehash");
    }

    @Order(2)
    @Test
    void favoriteAlbumTest(){
        given()
                .header("Authorization", token)
                .expect()
                .body("success", is(true))
                .body("data", equalTo("favorited"))
                .body("status",equalTo(200) )
                .when()
                .post("https://api.imgur.com/3/album/{albumHash}/favorite",albumId)
                .prettyPeek();
    }

    @Order(3)
    @Test
    void unFavoriteAlbumTest(){
        given()
                .header("Authorization", token)
                .expect()
                .body("success", is(true))
                .body("data", equalTo("unfavorited"))
                .body("status",equalTo(200) )
                .when()
                .post("https://api.imgur.com/3/album/{albumHash}/favorite",albumId)
                .prettyPeek();
    }

    @Order(4)
    @Test
    void delAlbum() {
        given()
                .header("Authorization", token)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .delete("https://api.imgur.com/3/album/{albumHash}", createAlbumId)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Order(5)
    @Test
    void getNonExistenAlbumTest() {
        given()
                .header("Authorization", token)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/3/album/{albumHash}", createAlbumId)
                .prettyPeek()
                .then()
                .statusCode(404);
    }

    @Order(6)
    @Test
    void favoriteNonExistenTest(){
        given()
                .header("Authorization", token)
                .when()
                .post("https://api.imgur.com/3/image/L2AMlFb/favorite")
                .prettyPeek()
                .then()
                .statusCode(404);
    }

    @Order(7)
    @Test
    void unFavoriteNonExistenTest(){
        given()
                .header("Authorization", token)
                .when()
                .post("https://api.imgur.com/3/image/L2AMlFb/favorite")
                .prettyPeek()
                .then()
                .statusCode(404);
    }

    @Order(8)
    @Test
    void delAlbumAgain(){
        given()
                .header("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/album/{albumHash}", createAlbumId)
                .prettyPeek()
                .then()
                .statusCode(404);
    }
}
