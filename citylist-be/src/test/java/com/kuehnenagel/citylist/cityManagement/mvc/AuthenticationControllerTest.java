package com.kuehnenagel.citylist.cityManagement.mvc;

import com.kuehnenagel.citylist.features.security.AuthenticationController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@WebMvcTest(controllers = AuthenticationController.class)
@WithMockUser(username = "fabius.bile", password = "deathtothefalseemperor", roles = "ALLOW_EDIT")
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUserValidateSuccess() throws Exception {
        MvcResult result = mockMvc.perform(
                get("/api/v1/authentication/validateLogin")
                    .with(csrf())
                    .with(httpBasic("fabius.bile", "deathtothefalseemperor")))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString();
        assertEquals(content,"{\"status\":\"User successfully authenticated\",\"roles\":[\"ROLE_ALLOW_EDIT\"]}");
    }

    @Test
    public void testUserValidateFail() throws Exception {
        mockMvc.perform(
                get("/api/v1/authentication/validateLogin")
                    .with(csrf())
                    .with(httpBasic("fabiu1s.bile", "deathto1thefalseemperor")))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
