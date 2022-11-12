package client;

import config.Config;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;

public class RestClient {

    public RequestSpecification getDefaultRequestSpec() {

        RestAssured.defaultParser = Parser.JSON;

        return new RequestSpecBuilder()
                .setBaseUri(Config.getBaseUri())
                .setBasePath(Config.getBasePath())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }
}
