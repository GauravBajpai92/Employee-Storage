package com.example.employee.service;

import com.example.employee.config.JmsConfig;
import com.example.employee.model.EmployeeDto;
import com.example.employee.proto.EmployeeProto;
import com.example.employee.security.SecurityService;
import com.example.employee.util.ProtoPojoConversionUtil;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    /**
     * @param empId
     * @return
     * @throws SecurityException
     */
    @Override
    public EmployeeDto getEmployeeByID(UUID empId) throws SecurityException {
        String url=storageServiceUrl+empId.toString();
        byte[] securedMessage = restTemplate.getForObject(url,byte[].class);
        byte[] byteMessage = new byte[0];
        EmployeeDto employeeDto = null;
        try {
            byteMessage = securityService.decryptData(securedMessage);
            EmployeeProto.Employee employeeProto = EmployeeProto.Employee.parseFrom(byteMessage);
            employeeDto  = ProtoPojoConversionUtil.toPojo(employeeProto);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException | InvalidKeySpecException | InvalidKeyException | InvalidProtocolBufferException e) {
            throw new SecurityException("Security Exception occurred while getting the Data");
        }

        employeeDto.setEmpId(empId);
        return  employeeDto;
    }

    /**
     * @param employeeDto
     * @param fileType
     * @return
     * @throws SecurityException
     */
    @Override
    public String saveEmployee(EmployeeDto employeeDto, String fileType) throws SecurityException{
        EmployeeDto empDto= EmployeeDto.builder().empId(UUID.randomUUID()).empName(employeeDto.getEmpName()).age(employeeDto.getAge()).salary(employeeDto.getSalary()).build();
        log.info("File Path"+fileType);
        EmployeeProto.Employee employeeProto = ProtoPojoConversionUtil.toProto(empDto);
        log.info("Proto object generated "+employeeProto.getEmpId());
        byte[] message =new byte[0];
        byte[] encryptedMessage = new byte[0];
        try {
            message =employeeProto.toByteArray();
            encryptedMessage = securityService.encryptData(message);
        }catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException | InvalidKeySpecException | InvalidKeyException e) {
            throw new SecurityException("Security Exception occurred while saving the Data");
        }
        log.info("Encrypted Message is being sent");
        sendMessageToQueue(encryptedMessage,fileType);

        return employeeProto.getEmpId();
    }

    /**
     * @param employeeDto
     * @param fileType
     * @param employeeId
     * @return
     * @throws SecurityException
     */
    @Override
    public String editEmployee(EmployeeDto employeeDto, String fileType, String employeeId) throws SecurityException{
        EmployeeDto empDto= EmployeeDto.builder().empName(employeeDto.getEmpName()).age(employeeDto.getAge()).salary(employeeDto.getSalary()).build();
        log.info("File Path"+fileType);
        EmployeeProto.Employee employeeProto = null;

        byte[] message = new byte[0];
        byte[] encryptedMessage =new byte[0];;
        try {
            employeeProto =ProtoPojoConversionUtil.toProto(empDto,employeeId);
            log.info("Editing "+employeeProto.getEmpId());
            message =employeeProto.toByteArray();
            encryptedMessage = securityService.encryptData(message);
        }catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException | InvalidKeySpecException | InvalidKeyException e) {
            throw new SecurityException("Security Exception occurred while editing the Data");
        }
        sendMessageToQueue(encryptedMessage, fileType);
        return employeeProto.getEmpId();
    }

    /** Sends Message to the Queue
     * @param encryptedMessage
     * @param fileType
     */
    private void sendMessageToQueue(byte[] encryptedMessage, String fileType) {
        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE,
                encryptedMessage,
                m -> {
                    log.info("setting standard JMS headers before sending");
                    m.setJMSCorrelationID(UUID.randomUUID().toString());
                    log.info("setting custom JMS headers before sending");
                    m.setStringProperty("filetype",fileType);
                    return m;
                });
    }

}
