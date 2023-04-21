package com.kuehnenagel.citylist.cityManagement.it;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.kuehnenagel.citylist.features.citymanagement.CityDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CityServiceIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    public static CustomPostgresContainer postgresContainer =
            CustomPostgresContainer.getInstance()
                    .withExposedPorts(5432);

    @Test
    public void successfullyFetchCitiesPageWithDefaultSearchSettings()  {
        ResponseEntity<String> customerResponse = restTemplate
                .withBasicAuth("fabius.bile", "deathtothefalseemperor")
                .getForEntity("/api/v1/cities", String.class);

        assertThat(customerResponse.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void successfullyFetchCitiesPageWithCustomSearchSettingsUserNotAuthorized() {
        ResponseEntity<String> customerResponse = restTemplate
                .withBasicAuth("fabius.bile", "deathtothefalseemperor")
                .getForEntity("/api/v1/cities?page=0&size=5&sortBy=-name", String.class);

        assertThat(customerResponse.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void failToFetchCitiesPageWhenUserNotAuthorized() {
        ResponseEntity<String> customerResponse = restTemplate
                .withBasicAuth("leman.russ", "godblesstheemperor")
                .getForEntity("/api/v1/cities?page=0&size=1", String.class);
        assertThat(customerResponse.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void successfullyFetchCityByNameUserNotAuthorized() {
        ResponseEntity<String> customerResponse = restTemplate
                .getForEntity("/api/v1/cities?name=Barcelona", String.class);
        assertThat(customerResponse.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void successfullyUpdateCityName() {
        HttpEntity<CityDto> request = new HttpEntity<>(CityDto.builder()
                .id(1L)
                .name("Minas Tirith")
                .photoLink("https://upload.wikimedia.org/wikipedia/tirith.jpg")
                .build());


        ResponseEntity<String> responce = restTemplate
                .withBasicAuth("fabius.bile", "deathtothefalseemperor")
                .exchange("/api/v1/cities", HttpMethod.PUT, request, String.class);
        assertThat(responce.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void failToUpdateCityNameWhenUserNotAuthorized() {
        HttpEntity<CityDto> request = new HttpEntity<>(CityDto.builder()
                .id(1L)
                .name("Minas Tirith")
                .photoLink("https://upload.wikimedia.org/wikipedia/tirith.jpg")
                .build());

        ResponseEntity<String> responce = restTemplate
                .withBasicAuth("leman.russ", "godblesstheemperor")
                .exchange("/api/v1/cities", HttpMethod.PUT, request, String.class);
        assertThat(responce.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

}