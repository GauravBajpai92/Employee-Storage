package com.example.employee.service;

import com.example.employee.model.EmployeeDto;
import com.google.protobuf.InvalidProtocolBufferException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

public interface EmployeeService {
    EmployeeDto getEmployeeByID(UUID empId) throws SecurityException;
    String saveEmployee(EmployeeDto employeeDto, String fileType) throws SecurityException;

    String editEmployee(EmployeeDto employeeDto, String fileType, String employeeId) throws SecurityException;
}
