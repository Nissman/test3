package pz;


import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class ImageTests extends BaseTest{


    @Test
    void uploadFileNoDataTest() {
         given()
                .header("Authorization", token)
                .expect()
                .body("success", is(false))
                .body("data.error", equalTo("No image data was sent to the upload api"))
                 .body("status",equalTo(400) )
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek();
    }

    @Test
    void getNonExistenImageTest() {
        given()
                .header("Authorization", token)
                .when()
                .get("https://api.imgur.com/3/image/m5t78NO")
                .prettyPeek()
                .then()
                .statusCode(404);
    }
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

}
