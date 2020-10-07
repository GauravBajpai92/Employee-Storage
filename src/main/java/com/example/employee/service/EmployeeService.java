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
    EmployeeDto getEmployeeByID(UUID empId);
    String saveEmployee(EmployeeDto employeeDto, String fileType) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException, InvalidProtocolBufferException;

    String editEmployee(EmployeeDto employeeDto, String fileType, String employeeId);
}
