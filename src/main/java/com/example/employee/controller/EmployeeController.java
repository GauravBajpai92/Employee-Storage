package com.example.employee.controller;

import com.example.employee.constants.EmployeeConstants;
import com.example.employee.service.EmployeeService;
import com.example.employee.model.EmployeeDto;
import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/${application.url}")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/{empId}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable("empId") UUID empId){
        return new ResponseEntity<EmployeeDto>(employeeService.getEmployeeByID(empId), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity handlePost(@RequestBody @Valid EmployeeDto employeeDto,@RequestParam String fileType) throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidProtocolBufferException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        HttpHeaders header = new HttpHeaders();
        if(EmployeeConstants.ALLOWED_FILE_TYPES.contains(fileType)){
        String employeeId = employeeService.saveEmployee(employeeDto,fileType);
        header.add("getLocation","/v1/employee/"+employeeId);
        }else{
            throw new UnsupportedOperationException();
        }
        return new ResponseEntity(header,HttpStatus.CREATED);
    }

    @PutMapping
        public ResponseEntity handlePut(@RequestBody @Valid EmployeeDto employeeDto,@RequestParam String fileType, @RequestParam String employeeId){
        HttpHeaders header = new HttpHeaders();
        if(EmployeeConstants.ALLOWED_FILE_TYPES.contains(fileType)){
            String empId = employeeService.editEmployee(employeeDto,fileType,employeeId);
            header.add("getLocation","/v1/employee/"+empId);
        }else{
            throw new UnsupportedOperationException();
        }
        return new ResponseEntity(header,HttpStatus.CREATED);
    }

}
