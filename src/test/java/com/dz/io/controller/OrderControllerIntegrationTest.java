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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderControllerIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesOrderController() {
        ServletContext servletContext = wac.getServletContext();

        Assert.assertNotNull(servletContext);
        Assert.assertTrue(servletContext instanceof MockServletContext);
        Assert.assertNotNull(wac.getBean("orderController"));
    }

    @Test
    public void givenPlaceOrder_whenMockMVC_thenVerifyResponse() throws Exception {
        String orderJson = "{\"email\": \"demo2@demo.com\", \"orderItems\" : [ { \"productId\" :1 }] }";
        MvcResult mvcResult = this.mockMvc.perform(post("/orders").content(orderJson).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Success"))
                .andReturn();

        Assert.assertEquals("application/json",
                mvcResult.getResponse().getContentType());
    }

    @Test
    public void givenPlaceOrder_whenProductNonExisting_thenVerifyResponse() throws Exception {
        String orderJson = "{\"email\": \"demo2@demo.com\", \"orderItems\" : [ { \"productId\" :1111 }] }";
        MvcResult mvcResult = this.mockMvc.perform(post("/orders").content(orderJson).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void givenRetrieveOrders_whenMockMVC_thenVerifyResponse() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/orders").param("start","2020-01-01T01:30:00")
                .param("end","2020-02-25T01:30:00")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"))
                .andReturn();

        Assert.assertEquals("application/json",
                mvcResult.getResponse().getContentType());
    }
}
