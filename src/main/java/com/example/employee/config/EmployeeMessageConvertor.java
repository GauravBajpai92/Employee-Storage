package com.example.employee.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.MessageConverter;

import javax.jms.BytesMessage;
import javax.jms.JMSException;

public class EmployeeMessageConvertor implements MessageConverter {




    @Override
    public Object fromMessage(Message<?> message, Class<?> aClass) {
        BytesMessage bytesMessage = (BytesMessage) message;
        int messageLength;
        try {
        messageLength = (int) bytesMessage.getBodyLength();
        byte[] rawEmployee = new byte[messageLength];
            bytesMessage.readBytes(rawEmployee);
        } catch (JMSException e) {
            e.printStackTrace();
        }

        //Employee employee = null;
       /* try {
            //employee = EmployeeProto.parseFrom(rawPerson);
        } catch (InvalidProtocolBufferException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        return new Object();
    }

    @Override
    public Message<?> toMessage(Object o, MessageHeaders messageHeaders) {
        return null;
    }
}
