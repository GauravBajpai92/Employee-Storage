package com.example.employee.controller;

import com.example.employee.model.EmployeeDto;
import com.example.employee.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
@RunWith(SpringRunner.class)
@WebMvcTest(value = EmployeeController.class)
@AutoConfigureMockMvc
public class EmployeeControllerTest {
    @Autowired
    private EmployeeController controller;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService service;

    @Test
    public void testPost() throws Exception {

        EmployeeDto empDto= EmployeeDto.builder().empId(null).empName("Dummy").age(26).salary(10000.27).build();

        ObjectMapper objectMapper = new ObjectMapper();
        String empJson= objectMapper.writeValueAsString(empDto);

        Mockito.when(
                service.saveEmployee(Mockito.any(),Mockito.any())).thenReturn("abc");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/v1/employee/?fileType=xml").content(empJson.getBytes()).accept(
                MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertNotEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

}