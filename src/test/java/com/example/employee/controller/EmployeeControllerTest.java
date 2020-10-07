package com.example.employee.controller;

import com.example.employee.model.EmployeeDto;
import com.example.employee.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
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
import org.springframework.test.util.ReflectionTestUtils;
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
    private EmployeeDto empDto;

    @Before
    public void setup(){
      //  ReflectionTestUtils.setField(service, "storageServiceUrl", "http://localhost:7779/v1/employee/");
       // empDto = EmployeeDto.builder().empId(null).empName("Dummy").age(26).salary(10000.27).build();
    }
    @Test
    public void testPostCreated() throws Exception {
        EmployeeDto empDto = EmployeeDto.builder().empId(null).empName("Dummy").age(26).salary(10000.27).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String empJson= objectMapper.writeValueAsString(empDto);
        Mockito.when(
                service.saveEmployee(Mockito.any(),Mockito.any())).thenReturn(empJson);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/v1/employee").param("fileType","xml").content(empJson).contentType("application/json");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }
    @Test
    public void testPostBadRequest() throws Exception {
        EmployeeDto empDto = EmployeeDto.builder().empId(null).empName("Dummy").age(16).salary(10000.27).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String empJson= objectMapper.writeValueAsString(empDto);
        Mockito.when(
                service.saveEmployee(Mockito.any(),Mockito.any())).thenReturn(empJson);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/v1/employee").param("fileType","xml").content(empJson).contentType("application/json");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
    @Test
    public void testPutOk() throws Exception {
        EmployeeDto empDto = EmployeeDto.builder().empId(null).empName("Dummy").age(26).salary(10000.27).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String empJson= objectMapper.writeValueAsString(empDto);
        Mockito.when(
                service.editEmployee(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(empJson);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(
                "/v1/employee").param("fileType","csv").param("employeeId","399387ec-9bed-41cf-bbed-0d1273f2b1de").content(empJson).contentType("application/json");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
    @Test
    public void testPutBadRequest() throws Exception {
        EmployeeDto empDto = EmployeeDto.builder().empId(null).empName("Dummy").age(26).salary(111110000.27).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String empJson= objectMapper.writeValueAsString(empDto);
        Mockito.when(
                service.editEmployee(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(empJson);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(
                "/v1/employee").param("fileType","csv").param("employeeId","399387ec-9bed-41cf-bbed-0d1273f2b1de").content(empJson).contentType("application/json");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    public void testGetOk() throws Exception {
        EmployeeDto empDto = EmployeeDto.builder().empId(null).empName("Dummy").age(26).salary(10000.27).build();
        Mockito.when(
                service.getEmployeeByID(Mockito.any())).thenReturn(empDto);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/v1/employee/399387ec-9bed-41cf-bbed-0d1273f2b1de");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

}