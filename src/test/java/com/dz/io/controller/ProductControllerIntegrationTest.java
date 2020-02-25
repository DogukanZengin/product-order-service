package com.dz.io.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductControllerIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesProductController() {
        ServletContext servletContext = wac.getServletContext();

        Assert.assertNotNull(servletContext);
        Assert.assertTrue(servletContext instanceof MockServletContext);
        Assert.assertNotNull(wac.getBean("productController"));
    }

    @Test
    public void givenAddProduct_whenMockMVC_thenVerifyResponse() throws Exception {
        String productJson = "{\"name\" : \"demo\", \"price\" : 5 }";
        MvcResult mvcResult = this.mockMvc.perform(post("/products").content(productJson).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Success"))
                .andReturn();

        Assert.assertEquals("application/json",
                mvcResult.getResponse().getContentType());
    }

    @Test
    public void givenRetrieveProducts_whenMockMVC_thenVerifyResponse() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/products").contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"))
                .andReturn();

        Assert.assertEquals("application/json",
                mvcResult.getResponse().getContentType());
    }

    @Test
    public void givenUpdateProduct_whenMockMVC_thenVerifyResponse() throws Exception {
        String productJson = "{\"name\" : \"demo-2\", \"price\" : 5 }";
        MvcResult mvcResult = this.mockMvc.perform(put("/products/1").content(productJson).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isAccepted())
                .andExpect(jsonPath("$.message").value("Success"))
                .andReturn();

        Assert.assertEquals("application/json",
                mvcResult.getResponse().getContentType());
    }

    @Test
    public void givenUpdateProduct_whenProductNonExisting_thenVerifyResponse() throws Exception {
        String productJson = "{\"name\" : \"demo-2\", \"price\" : 5 }";
        MvcResult mvcResult = this.mockMvc.perform(put("/products/1111").content(productJson).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isNotFound())
                .andReturn();
    }
}
