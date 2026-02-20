package com.org.java.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.org.java.app.dto.EmployeeDto;
import com.org.java.app.entity.Employee;

@Mapper
public interface EmployeeMapper {
	
	EmployeeMapper INSTANCE=Mappers.getMapper(EmployeeMapper.class);
	
	EmployeeDto mapToEmployeeToEmployeeDTO(Employee employee);
	Employee mapToEmployeeDTOToEmployee(EmployeeDto employeeDto);

}
