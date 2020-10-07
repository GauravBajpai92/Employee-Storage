package com.example.employee.service;

import com.example.employee.config.JmsConfig;
import com.example.employee.model.EmployeeDto;
import com.example.employee.proto.EmployeeProto;
import com.example.employee.security.SecurityService;
import com.example.employee.security.SecurityServiceImpl;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import util.ProtoPojoConversionUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private static Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    @Value("${storageService.url}")
    private String storageServiceUrl;
    private final JmsTemplate jmsTemplate;
    private final RestTemplate restTemplate;
    private final SecurityService securityService;

    @Override
    public EmployeeDto getEmployeeByID(UUID empId) {
        String url=storageServiceUrl+empId.toString();
        EmployeeDto empDto = restTemplate.getForObject(url,EmployeeDto.class);
        empDto.setEmpId(empId);
        return  empDto;
    }

    @Override
    public String saveEmployee(EmployeeDto employeeDto, String fileType) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException, InvalidProtocolBufferException {
        EmployeeDto empDto= EmployeeDto.builder().empId(UUID.randomUUID()).empName(employeeDto.getEmpName()).age(employeeDto.getAge()).salary(employeeDto.getSalary()).build();
        log.info("File Path"+fileType);
        EmployeeProto.Employee employeeProto = ProtoPojoConversionUtil.toProto(empDto);
        log.info("Proto object generated "+employeeProto.getEmpId());
        byte[] message =employeeProto.toByteArray();
        byte[] encryptedmessage = encryptedmessage =securityService.encryptData(message);
            byte[] message1 =securityService.decryptData(encryptedmessage);
            EmployeeProto.Employee emp1 = EmployeeProto.Employee.parseFrom(message1);
            System.out.println("secured msg "+emp1);

        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE,
                encryptedmessage,
                m -> {
                    log.info("setting standard JMS headers before sending");
                    m.setJMSCorrelationID(UUID.randomUUID().toString());
                    log.info("setting custom JMS headers before sending");
                    m.setStringProperty("filetype",fileType);
                    return m;
                });
        return employeeProto.getEmpId();
    }

    @Override
    public String editEmployee(EmployeeDto employeeDto, String fileType, String employeeId) {
        EmployeeDto empDto= EmployeeDto.builder().empName(employeeDto.getEmpName()).age(employeeDto.getAge()).salary(employeeDto.getSalary()).build();
        log.info("File Path"+fileType);
        EmployeeProto.Employee employeeProto = ProtoPojoConversionUtil.toProto(empDto,employeeId);
        log.info("Edited "+employeeProto.getEmpId());
        byte[] message =employeeProto.toByteArray();
        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE,
                message,
                m -> {
                    log.info("setting standard JMS headers before sending");
                    m.setJMSCorrelationID(UUID.randomUUID().toString());
                    log.info("setting custom JMS headers before sending");
                    m.setStringProperty("filetype",fileType);
                    return m;
                });
        return employeeProto.getEmpId();
    }

}
