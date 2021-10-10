package pz;

import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ImageAndAlbumsTests extends BaseTest{
    static String encodedFile_1;
    static String encodedFile_2;
    static String encodedFile_3;
    static String albumId;
    static String createAlbumId;
    static String imageId_1;
    static String imageId_2;
    static String imageId_3;
    static String uploadedImageId_1;
    static String uploadedImageId_2;
    static String uploadedImageId_3;


    @BeforeAll
    static void beforeTest(){
     encodedFile_1= Base64.getEncoder().encodeToString(getFileContent("src/test/resources/revenant.jpg"));
     encodedFile_2= Base64.getEncoder().encodeToString(getFileContent("src/test/resources/revcat.PNG"));
     encodedFile_3= Base64.getEncoder().encodeToString(getFileContent("src/test/resources/omg.PNG"));

    }

    @Order(1)
    @Test
    void uploadFile1Test() {
        Response response =  given()
                .header("Authorization", token)
                .multiPart("image", encodedFile_1)
                .formParam("title", "ImageTitle")
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek();

        imageId_1=response.jsonPath().get("data.id");
        uploadedImageId_1=response.jsonPath().get("data.deletehash");
    }

    @Order(2)
    @Test
    void favoriteAnUploadImageTest(){
        given()
                .header("Authorization", token)
                .expect()
                .body("success", is(true))
                .body("data", equalTo("favorited"))
                .body("status",equalTo(200) )
                .when()
                .post("https://api.imgur.com/3/image/{imageHash}/favorite",uploadedImageId_1)
                .prettyPeek();
    }

    @Order(3)
    @Test
    void unFavoriteAnUploadImageTest(){
        given()
                .header("Authorization", token)
                .expect()
                .body("success", is(true))
                .body("data", equalTo("unfavorited"))
                .body("status",equalTo(200) )
                .when()
                .post("https://api.imgur.com/3/image/{imageHash}/favorite",uploadedImageId_1)
                .prettyPeek();
    }

    @Order(4)
    @Test
    void createAlbumTest() {
Response response =  given()
                .header("Authorization", token)
                .formParam("title", "My dank meme album")
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post("https://api.imgur.com/3/album")
                .prettyPeek();

        albumId=response.jsonPath().get("data.id");
        createAlbumId=response.jsonPath().get("data.deletehash");


    }

    @Order(5)
    @Test
    void getAlbumTest() {
        given()
                .header("Authorization", token)
                .log()
                .method()
                .log()
                .uri()
                .expect()
                .body("success", is(true))
                .body("data.id", equalTo(albumId))
                .body("status",equalTo(200) )
                .when()
                .get("https://api.imgur.com/3/album/{albumHash}", albumId)
                .prettyPeek();

    }



    @Order(6)
    @Test
    void uploadFile2ToAlbumTest() {
        Response response =  given()
                .header("Authorization", token)
                .multiPart("image", encodedFile_2)
                .formParam("title", "ImageTitle")
                .formParam("album", albumId)
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek();

        imageId_2=response.jsonPath().get("data.id");
        uploadedImageId_2=response.jsonPath().get("data.deletehash");

    }

    @Order(7)
    @Test
    void updateImage(){
        given()
                .header("Authorization", token)
                .formParam("title", "Heart")
                .formParam("description", "This is an image of a heart outline.")
                .expect()
                .body("success", is(true))
                .body("data", is(true))
                .when()
                .post("https://api.imgur.com/3/image/{imageHash}",imageId_1)
                .prettyPeek();
    }


    @Order(8)
    @Test
    void getImageTest() {
        given()
                .header("Authorization", token)
                .expect()
                .body("success", is(true))
                .body("status",equalTo(200) )
                .body("data.title",equalTo("Heart") )
                .body("data.description",equalTo("This is an image of a heart outline.") )
                .when()
                .get("https://api.imgur.com/3/image/{imageHash}",imageId_1)
                .prettyPeek();
    }

    @Order(9)
    @Test
    void uploadFile3Test() {
        Response response =  given()
                .header("Authorization", token)
                .multiPart("image", encodedFile_3)
                .formParam("title", "ImageTitle")
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek();

        imageId_3=response.jsonPath().get("data.id");
        uploadedImageId_3=response.jsonPath().get("data.deletehash");
    }

    @Order(10)
    @Test
    void  addImageToAnAlbum(){
        given()
                .header("Authorization", token)
                .formParam("ids[]", imageId_1)
                .expect()
                .body("success", is(true))
                .body("data", is(true))
                .body("status",equalTo(200) )
                .when()
                .post("https://api.imgur.com/3/album/{albumHash}/add",albumId)
                .prettyPeek();
    }

    @Order(11)
    @Test
    void setAlbumImage(){
        given()
                .header("Authorization", token)
                .formParam("ids[]", imageId_1)
                .formParam("ids[]", imageId_2)
                .formParam("ids[]", imageId_3)
                .expect()
                .body("success", is(true))
                .body("data", is(true))
                .body("status",equalTo(200) )
                .when()
                .post("https://api.imgur.com/3/album/{albumHash}",albumId)
                .prettyPeek();
    }

    @Order(12)
    @Test
    void removeImageFromAnAlbumTest(){
        given()
                .header("Authorization", token)
                .formParam("ids[]", imageId_1)
                .expect()
                .body("success", is(true))
                .body("data", is(true))
                .body("status",equalTo(200) )
                .when()
                .post("https://api.imgur.com/3/album/{albumHash}/remove_images",albumId)
                .prettyPeek();
    }

    @Order(13)
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


    @Order(14)
    @Test
    void DeleteImage1Test() {
        given()
                .header("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{imageDeleteHash}", username, uploadedImageId_1)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Order(15)
    @Test
    void DeleteImage2Test() {
        given()
                .header("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{imageDeleteHash}", username, uploadedImageId_2)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Order(16)
    @Test
    void DeleteImage3Test() {
        given()
                .header("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{imageDeleteHash}", username, uploadedImageId_3)
                .prettyPeek()
                .then()
                .statusCode(200);
    }


    private static byte[] getFileContent(String PATH_TO_IMAGE) {
        byte[] byteArray = new byte[0];
        try {
            byteArray = FileUtils.readFileToByteArray(new File(PATH_TO_IMAGE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }
}
