package com.kuehnenagel.citylist.cityManagement.mvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuehnenagel.citylist.features.citymanagement.CityController;
import com.kuehnenagel.citylist.features.citymanagement.CityDto;
import com.kuehnenagel.citylist.features.citymanagement.CityService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = CityController.class)
@WithMockUser(username = "fabius.bile", password = "deathtothefalseemperor",roles = "ALLOW_EDIT")
public class CityControllerTest {

    @MockBean
    private CityService cityService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void successfullyGetListOfCitiesWithDefaultFilterOptions() throws Exception {
        this.mockMvc.perform(get("/api/v1/cities")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void successfullyGetListOfCitiesWithCustomFilterOptions() throws Exception {
        this.mockMvc.perform(get("/api/v1/cities?name=Tokyo&page=0&size=10&sortNy=name")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void successfullyUpdateCityWhenUserHasRoleThatAllowsEditing() throws Exception {
        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/v1/cities")
                                .with(csrf())
                                //.with(httpBasic("fabius.bile", "deathtothefalseemperor"))
                                .content(
                                        new ObjectMapper().writeValueAsString(
                                                CityDto.builder()
                                                        .id(1L)
                                                        .name("Tokyo")
                                                        .photoLink("http://www.photo.com")
                                                        .build()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void failToUpdateCityWhenUserNotAuthenticated() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/cities")
                        .content(
                                new ObjectMapper().writeValueAsString(
                                        CityDto.builder()
                                                .id(1L)
                                                .name("string")
                                                .photoLink("http://www.photo.com")
                                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

}