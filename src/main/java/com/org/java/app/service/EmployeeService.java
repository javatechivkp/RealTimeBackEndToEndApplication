package com.org.java.app.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.org.java.app.dto.EmployeeDto;
import com.org.java.app.entity.Employee;

@Service
public interface EmployeeService {

	EmployeeDto saveEmployeeDetails(EmployeeDto employeeDto);

	Employee updateEmployeeDetails(Employee employee);

	Employee deleteEmployeeDetails(Employee employee);

	List<EmployeeDto> fetchAllEmployeeDetails();

	public List<EmployeeDto> findAllEmployeeDetails();

	public Employee findByIdEmployeeDetails(long empid);

	public List<Employee> findByName(String empName);

	List<Employee> findByEmployeeSalaryAscDeatails();

	List<Employee> findByEmployeeSalaryDscDeatails();

	List<Employee> findByEmployeeIdEvenDeatails();

	List<Employee> findByEmployeeIdOddDeatails();

	Employee findByMaxSalaryDeatails();

	Employee findByMinSalaryDeatails();

	double findBySumSalaryDeatails();

	double findByCountSalaryDeatails();

	List<Employee> findParticularRecordsDeatails();

	List<Employee> findParticularRecordFileter();

	Set<Employee> printDublicateRecordsDeatails();

	Set<Employee> printWithoutDublicateRecordsDeatails();

	List<Employee> findParticularRecordsAscsDeatails();

	List<Employee> findParticularRecordsDscDeatails();

	List<String> mapNamesDeatails();

	List<String> mapNamesToUppercaseDeatails();

	Map<Character, Integer> findStringOccurenceDeatails();

	Map<Object, List<Employee>> groupBySalaryDeatails();

	Map<Object, List<Employee>> groupByNamesDeatails();

	Employee findByNameAndPlatformeDeatails(String empName, String platform);

	Employee findByEmpIdAndNameAndWorkLocationDeatails(long empid, String name, String workLocation);

	String firstnonRepeactedCharacterInStringDeatails();

	String firstRepeactedCharacterInStringDeatails();

	List<String> printDublicatesInStringDeatails();

	List<String> uniquerecordsInStringDeatails();

	String longestStringDeatails();

	String smallestStringDeatails();

	List<String> filterPanNumbersDeatails();

	String stringReverseJava8Deatails();

	Employee secondHigestSalaryDeatails();

	Employee secondListSalaryDeatails();

	List<Employee> indexRangesDeatails(long fromIndex, long toIndex);

	String joiningNamesDeatails();

	Set<Employee> listToSetCoversion();

	Map<Long, Employee> listToMapCoversion();

	List<Employee> setToListConversion();

	Map<Long, Employee> setToMapConversionDetails();

	List<Entry<Long, Employee>> mapToListConversionDetails();

	Set<Entry<Long, Employee>> mapToSetConversionDetails();

	Optional<Employee> findByEmployeeIdDeatails(long empid);

	List<Employee> findByPlatformDetails(String platform);

	String leftRotationStringDeatails();

	String rightRotationStringDeatails();

	List<EmployeeDto> findByEmployeeBetweenSalaryDeatails();

	Map<String, Long> findBygroupCountDeatails();

	// CursorPageResponse<Employee> getEmployees(Integer cursor,int size);

	public Page<Employee> getEmployeesList(long page, long size);

	Employee findByNameAndWorkLocationDeatails(String name, String workLocation);

}
