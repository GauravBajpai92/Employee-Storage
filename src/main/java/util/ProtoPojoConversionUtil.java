package util;

import com.example.employee.exception.ProtoPojoConversionException;
import com.example.employee.model.EmployeeDto;
import com.example.employee.proto.EmployeeProto;
import com.google.protobuf.Message;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

public class ProtoPojoConversionUtil {
    public static EmployeeDto toPojo(EmployeeProto.Employee empDto) {
        return EmployeeDto.builder().empId(UUID.fromString(empDto.getEmpId())).empName(empDto.getEmpName()).age(empDto.getAge()).salary(empDto.getSalary()).build();
    }

    public static EmployeeProto.Employee toProto(EmployeeDto empDto) {
        return EmployeeProto.Employee.newBuilder().setEmpId(UUID.randomUUID()
                .toString()).setEmpName(empDto.getEmpName()).setAge(empDto.getAge())
                .setSalary(empDto.getSalary()).build();
    }
    public static EmployeeProto.Employee toProto(EmployeeDto empDto,String empId) {
        return EmployeeProto.Employee.newBuilder().setEmpId(empId).setEmpName(empDto.getEmpName()).setAge(empDto.getAge())
                .setSalary(empDto.getSalary()).build();
    }
}
