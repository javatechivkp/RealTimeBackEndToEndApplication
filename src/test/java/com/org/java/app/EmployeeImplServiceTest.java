package com.org.java.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.org.java.app.dto.EmployeeDto;
import com.org.java.app.entity.Employee;
import com.org.java.app.mapper.EmployeeMapper;
import com.org.java.app.repository.EmployeeRepository;
import com.org.java.app.serviceimpl.EmployeeServiceImpl;

@ExtendWith(MockitoExtension.class)
class EmployeeImplServiceTest {

	@Mock
	private EmployeeRepository employeeRepository;

	@InjectMocks
	private EmployeeServiceImpl employeeServiceImpl;

	@Test
	public void saveEmployeeTest() {
		Employee emp = new Employee(1L, "RAJEE", 66, 3550, "RAJEE@gmail.com", "hyderabad", "java", "kms", 748284441532L,
				"DSVPK5008J", 9676919080L);
		EmployeeDto emp1 = EmployeeMapper.INSTANCE.mapToEmployeeToEmployeeDTO(emp);
		when(employeeRepository.save(emp)).thenReturn(emp);
		assertEquals(emp1, employeeServiceImpl.saveEmployeeDetails(emp1));
	}

	@Test
	void updateEmployeeDetails_returnsUpdatedEmployee() {
		Employee employee = new Employee(1L, "RAJEE", 66, 3550, "RAJEE@gmail.com", "hyderabad", "java", "kms",
				748284441532L, "DSVPK5008J", 9676919080L);
		given(employeeRepository.save(any(Employee.class))).willReturn(employee);
		Employee result = employeeServiceImpl.updateEmployeeDetails(employee);
		assertThat(result.getName()).isEqualTo("RAJEE");
		verify(employeeRepository).save(employee);
	}

	@Test
	void deleteEmployeeDetails_whenEmployeeExists_returnsDeletedEmployee() {
		Employee employee = new Employee(1L, "RAJEE", 66, 3550, "RAJEE@gmail.com", "hyderabad", "java", "kms",
				748284441532L, "DSVPK5008J", 9676919080L);
		List<Employee> employees = Arrays.asList(employee);
		given(employeeRepository.findAll()).willReturn(employees);
		Employee result = employeeServiceImpl.deleteEmployeeDetails(employee);
		assertThat(result.getName()).isEqualTo("RAJEE");
		verify(employeeRepository).delete(employee);
	}

	@Test
	void findByIdEmployeeDetails_whenEmployeeExists_returnsEmployee() {
		Employee employee = new Employee(1L, "RAJEE", 66, 3550, "RAJEE@gmail.com", "hyderabad", "java", "kms",
				748284441532L, "DSVPK5008J", 9676919080L);
		given(employeeRepository.findByEmpid(1)).willReturn(Optional.of(employee));
		Employee result = employeeServiceImpl.findByIdEmployeeDetails(1);
		assertThat(result.getName()).isEqualTo("RAJEE");
	}

	@Test
	void findByIdEmployeeDetails_whenEmployeeNotFound_throwsException() {
		given(employeeRepository.findByEmpid(999)).willReturn(Optional.empty());
		assertThatThrownBy(() -> employeeServiceImpl.findByIdEmployeeDetails(999))
				.isInstanceOf(com.org.java.app.exceptions.NoDataAvailableException.class)
				.hasMessageContaining("No data prasent given id::999");
	}

	@Test
	void findAllEmployeeDetails_returnsAllEmployees() {
		List<Employee> employees = Arrays.asList(
				new Employee(1L, "RAJEE", 66, 3550, "RAJEE@gmail.com", "hyderabad", "java", "kms", 748284441532L,
						"DSVPK5008J", 9676919080L),
				new Employee(2L, "srinu", 61, 3540, "srinu@gmail.com", "kmm", "java", "dms", 747854441532L,
						"DSVPK2208J", 9676229080L));
		given(employeeRepository.findAll()).willReturn(employees);
		List<EmployeeDto> result = employeeServiceImpl.findAllEmployeeDetails();
		assertThat(result).hasSize(2);
		assertThat(result.get(0).getName()).isEqualTo("RAJEE");
	}

// Salary Calculation Tests
	@Test
	void findBySumSalaryDeatails_returnsSumOfSalaries() {
		List<Employee> employees = Arrays.asList(
				new Employee(1L, "RAJEE", 66, 200, "RAJEE@gmail.com", "hyderabad", "java", "kms", 748284441532L,
						"DSVPK5008J", 9676919080L),
				new Employee(2L, "srinu", 61, 300, "srinu@gmail.com", "kmm", "java", "dms", 747854441532L,
						"DSVPK2208J", 9676229080L),
				new Employee(3L, "suresh", 36, 100, "suresh@gmail.com", "delhi", "test", "pms", 628284441532L,
						"DSVPK5008J", 8676919080L));
		given(employeeRepository.findAll()).willReturn(employees);
		double sum = employeeServiceImpl.findBySumSalaryDeatails();

		assertThat(sum).isEqualTo(600.00);
	}

	@Test
	void findByCountSalaryDeatails_returnsCountOfEmployees() {
		List<Employee> employees = Arrays.asList(
				new Employee(1L, "RAJEE", 66, 3550, "RAJEE@gmail.com", "hyderabad", "java", "kms", 748284441532L,
						"DSVPK5008J", 9676919080L),
				new Employee(2L, "srinu", 61, 3540, "srinu@gmail.com", "kmm", "java", "dms", 747854441532L,
						"DSVPK2208J", 9676229080L));

		given(employeeRepository.findAll()).willReturn(employees);

		double count = employeeServiceImpl.findByCountSalaryDeatails();

		assertThat(count).isEqualTo(2.0);
	}

	@Test
	void findByMaxSalaryDeatails_returnsEmployeeWithMaxSalary() {
		List<Employee> employees = Arrays.asList(
				new Employee(1L, "RAJEE", 66, 200.5, "RAJEE@gmail.com", "hyderabad", "java", "kms", 748284441532L,
						"DSVPK5008J", 9676919080L),
				new Employee(2L, "srinu", 61, 100.3, "srinu@gmail.com", "kmm", "java", "dms", 747854441532L,
						"DSVPK2208J", 9676229080L));

		given(employeeRepository.findAll()).willReturn(employees);

		Employee result = employeeServiceImpl.findByMaxSalaryDeatails();

		assertThat(result.getSalary()).isEqualTo(200.5);
	}

	@Test
	void findByMinSalaryDeatails_returnsEmployeeWithMinSalary() {
		List<Employee> employees = Arrays.asList(
				new Employee(1L, "RAJEE", 66, 100, "RAJEE@gmail.com", "hyderabad", "java", "kms", 748284441532L,
						"DSVPK5008J", 9676919080L),
				new Employee(2L, "srinu", 61, 200, "srinu@gmail.com", "kmm", "java", "dms", 747854441532L,
						"DSVPK2208J", 9676229080L));

		given(employeeRepository.findAll()).willReturn(employees);

		Employee result = employeeServiceImpl.findByMinSalaryDeatails();

		assertThat(result.getSalary()).isEqualTo(100.0);
	}

// Sorting Tests
	@Test
	void findByEmployeeSalaryAscDeatails_whenEmployeesExist_returnsSortedList() {
		List<Employee> employees = Arrays.asList(
				new Employee(1L, "RAJEE", 66, 100, "RAJEE@gmail.com", "hyderabad", "java", "kms", 748284441532L,
						"DSVPK5008J", 9676919080L),
				new Employee(2L, "srinu", 61, 200, "srinu@gmail.com", "kmm", "java", "dms", 747854441532L,
						"DSVPK2208J", 9676229080L));

		given(employeeRepository.findAll()).willReturn(employees);

		List<Employee> result = employeeServiceImpl.findByEmployeeSalaryAscDeatails();

		assertThat(result.get(0).getSalary()).isEqualTo(100.0);
		assertThat(result.get(1).getSalary()).isEqualTo(200.0);
	}

	@Test
	void findByEmployeeSalaryAscDeatails_whenNoEmployees_throwsException() {
		given(employeeRepository.findAll()).willReturn(Collections.emptyList());

		assertThatThrownBy(() -> employeeServiceImpl.findByEmployeeSalaryAscDeatails())
				.isInstanceOf(com.org.java.app.exceptions.NoDataAvailableException.class);
	}

	@Test
	void findByEmployeeSalaryDscDeatails_whenEmployeesExist_returnsSortedList() {
		List<Employee> employees = Arrays.asList(
				new Employee(1L, "RAJEE", 66, 100, "RAJEE@gmail.com", "hyderabad", "java", "kms", 748284441532L,
						"DSVPK5008J", 9676919080L),
				new Employee(2L, "srinu", 61, 200, "srinu@gmail.com", "kmm", "java", "dms", 747854441532L,
						"DSVPK2208J", 9676229080L));

		given(employeeRepository.findAll()).willReturn(employees);

		List<Employee> result = employeeServiceImpl.findByEmployeeSalaryDscDeatails();

		assertThat(result.get(0).getSalary()).isEqualTo(200.0);
		assertThat(result.get(1).getSalary()).isEqualTo(100.0);
	}

	@Test
	void findByEmployeeSalaryDscDeatails_whenNoEmployees_throwsException() {
		given(employeeRepository.findAll()).willReturn(Collections.emptyList());

		assertThatThrownBy(() -> employeeServiceImpl.findByEmployeeSalaryDscDeatails())
				.isInstanceOf(com.org.java.app.exceptions.NoDataAvailableException.class);
	}

// ID Filtering Tests
	@Test
	void findByEmployeeIdEvenDeatails_returnsEvenIdEmployees() {
		List<Employee> employees = Arrays.asList(
				new Employee(1L, "RAJEE", 66, 3550, "RAJEE@gmail.com", "hyderabad", "java", "kms", 748284441532L,
						"DSVPK5008J", 9676919080L),
				new Employee(2L, "srinu", 61, 3540, "srinu@gmail.com", "kmm", "java", "dms", 747854441532L,
						"DSVPK2208J", 9676229080L),
				new Employee(3L, "ajay", 66, 5550, "ajay@gmail.com", "pune", ".net", "tts", 258284441532L,
						"DSVPK5008J", 7876919080L));

		given(employeeRepository.findAll()).willReturn(employees);

		List<Employee> result = employeeServiceImpl.findByEmployeeIdEvenDeatails();

		assertThat(result).hasSize(1);
		assertThat(result.get(0).getEmpid()).isEqualTo(2);
	}


	@Test
	void findByEmployeeIdOddDeatails_returnsOddIdEmployees() {
		List<Employee> employees = Arrays.asList(
				new Employee(1L, "RAJEE", 66, 3550, "RAJEE@gmail.com", "hyderabad", "java", "kms", 748284441532L,
						"DSVPK5008J", 9676919080L),
				new Employee(2L, "srinu", 61, 3540, "srinu@gmail.com", "kmm", "java", "dms", 747854441532L,
						"DSVPK2208J", 9676229080L),
				new Employee(3L, "ajay", 66, 5550, "ajay@gmail.com", "pune", ".net", "tts", 258284441532L,
						"DSVPK5008J", 7876919080L));

		given(employeeRepository.findAll()).willReturn(employees);

		List<Employee> result = employeeServiceImpl.findByEmployeeIdOddDeatails();

		assertThat(result).hasSize(2);
		assertThat(result.get(0).getEmpid()).isEqualTo(1);
		assertThat(result.get(1).getEmpid()).isEqualTo(3);
	}


// Department Tests

// Pagination Tests
	@Test
	void findParticularRecordsDeatails_returnsSkippedAndLimitedRecords() {
		List<Employee> employees = Arrays.asList(
				new Employee(1L, "RAJEE", 66, 3550, "RAJEE@gmail.com", "hyderabad", "java", "kms", 748284441532L,
						"DSVPK5008J", 9676919080L),
				new Employee(2L, "srinu", 61, 3540, "srinu@gmail.com", "kmm", "java", "dms", 747854441532L,
						"DSVPK2208J", 9676229080L),
				new Employee(3L, "ajay", 66, 5550, "ajay@gmail.com", "pune", ".net", "tts", 258284441532L,
						"DSVPK5008J", 7876919080L),
				new Employee(4L, "RAJEE", 66, 3550, "RAJEE@gmail.com", "hyderabad", "java", "kms", 748284441532L,
						"DSVPK5008J", 9676919080L),
				new Employee(5L, "RAJEE", 66, 3550, "RAJEE@gmail.com", "hyderabad", "java", "kms", 748284441532L,
						"DSVPK5008J", 9676919080L),
				new Employee(6L, "RAJEE", 66, 3550, "RAJEE@gmail.com", "hyderabad", "java", "kms", 748284441532L,
						"DSVPK5008J", 9676919080L));

		given(employeeRepository.findAll()).willReturn(employees);

		List<Employee> result = employeeServiceImpl.findParticularRecordsDeatails();

		assertThat(result).hasSize(4); // skip 2, limit 5
		assertThat(result.get(0).getEmpid()).isEqualTo(3); // first after skip
	}

	@Test
	void findParticularRecordsDeatails_whenNoEmployees_throwsException() {
		given(employeeRepository.findAll()).willReturn(Collections.emptyList());

		assertThatThrownBy(() -> employeeServiceImpl.findParticularRecordsDeatails())
				.isInstanceOf(com.org.java.app.exceptions.NoDataAvailableException.class);
	}

// String Processing Tests
	@Test
	void mapNamesDeatails_returnsNamesInReverseOrder() {
		List<Employee> employees = Arrays.asList(
				new Employee(1L, "RAJEE", 66, 3550, "RAJEE@gmail.com", "hyderabad", "java", "kms", 748284441532L,
						"DSVPK5008J", 9676919080L),
				new Employee(2L, "srinu", 61, 3540, "srinu@gmail.com", "kmm", "java", "dms", 747854441532L,
						"DSVPK2208J", 9676229080L),
				new Employee(3L, "ajay", 66, 5550, "ajay@gmail.com", "pune", ".net", "tts", 258284441532L,
						"DSVPK5008J", 7876919080L));

		given(employeeRepository.findAll()).willReturn(employees);

		List<String> result = employeeServiceImpl.mapNamesDeatails();

		assertThat(result).containsExactly("srinu","ajay", "RAJEE");
	}

	@Test
	void groupBySalaryDeatails_returnsGroupedBySalary() {
		List<Employee> employees = Arrays.asList(
				new Employee(1L, "RAJEE", 66, 100, "RAJEE@gmail.com", "hyderabad", "java", "kms", 748284441532L,
						"DSVPK5008J", 9676919080L),
				new Employee(2L, "srinu", 61, 200, "srinu@gmail.com", "kmm", "java", "dms", 747854441532L,
						"DSVPK2208J", 9676229080L),
				new Employee(3L, "ajay", 66, 200, "ajay@gmail.com", "pune", ".net", "tts", 258284441532L,
						"DSVPK5008J", 7876919080L));

		given(employeeRepository.findAll()).willReturn(employees);

		Map<Object, List<Employee>> result = employeeServiceImpl.groupBySalaryDeatails();

		assertThat(result.get(100.0)).hasSize(1);
		assertThat(result.get(200.0)).hasSize(2);
		
	}

	

// Collection Conversion Tests
	@Test
	void listToSetCoversion_returnsSetOfEmployees() {
		List<Employee> employees = Arrays.asList(
				new Employee(1L, "RAJEE", 66, 3550, "RAJEE@gmail.com", "hyderabad", "java", "kms", 748284441532L,
						"DSVPK5008J", 9676919080L),
				new Employee(2L, "srinu", 61, 3540, "srinu@gmail.com", "kmm", "java", "dms", 747854441532L,
						"DSVPK2208J", 9676229080L),
				new Employee(3L, "ajay", 66, 5550, "ajay@gmail.com", "pune", ".net", "tts", 258284441532L,
						"DSVPK5008J", 7876919080L)); // duplicate

		given(employeeRepository.findAll()).willReturn(employees);

		Set<Employee> result = employeeServiceImpl.listToSetCoversion();

		assertThat(result).hasSize(3); // duplicates removed
	}

// String Processing Tests
	@Test
	void stringReverseJava8Deatails_returnsReversedString() {
		String result = employeeServiceImpl.stringReverseJava8Deatails();

		// "SREENIVASARAO" reversed should be "OARASANIVEERS"
		assertThat(result).isEqualTo("OARASAVINEERS");
	}

	@Test
	void leftRotationStringDeatails_returnsLeftRotatedString() {
		String result = employeeServiceImpl.leftRotationStringDeatails();

		assertThat(result).isEqualTo("nivasaraosree");
	}

	@Test
	void rightRotationStringDeatails_returnsRightRotatedString() {
		String result = employeeServiceImpl.rightRotationStringDeatails();

		assertThat(result).isEqualTo("raosreenivasa");
	}

// Advanced Query Tests
	@Test
	void secondHigestSalaryDeatails_returnsSecondHighestSalaryEmployee() {
		List<Employee> employees = Arrays.asList(
				new Employee(1L, "RAJEE", 66, 500, "RAJEE@gmail.com", "hyderabad", "java", "kms", 748284441532L,
						"DSVPK5008J", 9676919080L),
				new Employee(2L, "srinu", 61, 300, "srinu@gmail.com", "kmm", "java", "dms", 747854441532L,
						"DSVPK2208J", 9676229080L),
				new Employee(3L, "ajay", 66, 400, "ajay@gmail.com", "pune", ".net", "tts", 258284441532L,
						"DSVPK5008J", 7876919080L));

		given(employeeRepository.findAll()).willReturn(employees);

		Employee result = employeeServiceImpl.secondHigestSalaryDeatails();

		assertThat(result.getSalary()).isEqualTo(400.0); // second highest
	}

	@Test
	void secondListSalaryDeatails_returnsSecondLowestSalaryEmployee() {
		List<Employee> employees = Arrays.asList(
				new Employee(1L, "RAJEE", 66, 300, "RAJEE@gmail.com", "hyderabad", "java", "kms", 748284441532L,
						"DSVPK5008J", 9676919080L),
				new Employee(2L, "srinu", 61, 200, "srinu@gmail.com", "kmm", "java", "dms", 747854441532L,
						"DSVPK2208J", 9676229080L),
				new Employee(3L, "ajay", 66, 100, "ajay@gmail.com", "pune", ".net", "tts", 258284441532L,
						"DSVPK5008J", 7876919080L));

		given(employeeRepository.findAll()).willReturn(employees);

		Employee result = employeeServiceImpl.secondListSalaryDeatails();

		assertThat(result.getSalary()).isEqualTo(200.0); // second lowest
	}

// Filtering Tests

	@Test
	void mapNamesToUppercaseDeatails_returnsUppercaseNames() {
		List<Employee> employees = Arrays.asList(
				new Employee(1L, "RAJEE", 66, 3550, "RAJEE@gmail.com", "hyderabad", "java", "kms", 748284441532L,
						"DSVPK5008J", 9676919080L),
				new Employee(2L, "srinu", 61, 3540, "srinu@gmail.com", "kmm", "java", "dms", 747854441532L,
						"DSVPK2208J", 9676229080L),
				new Employee(3L, "ajay", 66, 5550, "ajay@gmail.com", "pune", ".net", "tts", 258284441532L,
						"DSVPK5008J", 7876919080L));

		given(employeeRepository.findAll()).willReturn(employees);

		List<String> result = employeeServiceImpl.mapNamesToUppercaseDeatails();

		assertThat(result).containsExactly("RAJEE","SRINU","AJAY");
	}

// Optional Tests
	@Test
	void findByEmployeeIdDeatails_whenEmployeeExists_returnsOptionalEmployee() {
		Employee employee = new Employee(1L, "RAJEE", 66, 3550, "RAJEE@gmail.com", "hyderabad", "java", "kms",
				748284441532L, "DSVPK5008J", 9676919080L);
		given(employeeRepository.findByEmpid(1)).willReturn(Optional.of(employee));

		Optional<Employee> result = employeeServiceImpl.findByEmployeeIdDeatails(1);

		assertThat(result).isPresent();
		assertThat(result.get().getName()).isEqualTo("RAJEE");
	}

	@Test
	void findByEmployeeIdDeatails_whenEmployeeNotFound_returnsEmptyOptional() {
		given(employeeRepository.findByEmpid(999)).willReturn(Optional.empty());

		Optional<Employee> result = employeeServiceImpl.findByEmployeeIdDeatails(999);

		assertThat(result).isEmpty();
	}

// Department Count Tests

// Name Search Tests
	@Test
	void findByName_whenEmployeeExists_returnsEmployees() {
		List<Employee> employees = Arrays.asList(
				new Employee(1L, "RAJEE", 66, 3550, "RAJEE@gmail.com", "hyderabad", "java", "kms", 748284441532L,
						"DSVPK5008J", 9676919080L),
				new Employee(2L, "srinu", 61, 3540, "srinu@gmail.com", "kmm", "java", "dms", 747854441532L,
						"DSVPK2208J", 9676229080L),
				new Employee(3L, "ajay", 66, 5550, "ajay@gmail.com", "pune", ".net", "tts", 258284441532L,
						"DSVPK5008J", 7876919080L));

		given(employeeRepository.findByName("John")).willReturn(employees);

		List<Employee> result = employeeServiceImpl.findByName("John");

		assertThat(result).hasSize(3);
		assertThat(result.get(0).getName()).isEqualTo("RAJEE");
	}

	@Test
	void findByName_whenEmployeeNotFound_throwsException() {
		given(employeeRepository.findByName("NonExistent")).willReturn(Collections.emptyList());

		assertThatThrownBy(() -> employeeServiceImpl.findByName("NonExistent"))
				.isInstanceOf(com.org.java.app.exceptions.NoDataAvailableException.class);
	}

// String Analysis Tests (with proper null handling)
	@Test
	void firstnonRepeactedCharacterInStringDeatails_returnsFirstNonRepeatedCharacter() {
		List<Employee> employees = Arrays.asList(new Employee(1L, "suresh", 66, 3550, "RAJEE@gmail.com", "hyderabad",
				"java", "kms", 748284441532L, "DSVPK5008J", 9676919080L));

		given(employeeRepository.findAll()).willReturn(employees);

		String result = employeeServiceImpl.firstnonRepeactedCharacterInStringDeatails();

		// In "suresh": s(2), u(1), r(1), e(1), h(1) - first non-repeated is 'u'
		assertThat(result).isEqualTo("u");
	}

	@Test
	void firstRepeactedCharacterInStringDeatails_returnsFirstRepeatedCharacter() {
		List<Employee> employees = Arrays.asList(new Employee(1L, "suresh", 66, 3550, "RAJEE@gmail.com", "hyderabad",
				"java", "kms", 748284441532L, "DSVPK5008J", 9676919080L));

		given(employeeRepository.findAll()).willReturn(employees);

		String result = employeeServiceImpl.firstRepeactedCharacterInStringDeatails();

		assertThat(result).isEqualTo("s"); // first repeated character in "suresh"
	}

	@Test
	void printDublicatesInStringDeatails_returnsDuplicateCharacters() {
		List<Employee> employees = Arrays.asList(new Employee(1L, "suresh", 66, 3550, "RAJEE@gmail.com", "hyderabad",
				"java", "kms", 748284441532L, "DSVPK5008J", 9676919080L));

		given(employeeRepository.findAll()).willReturn(employees);

		List<String> result = employeeServiceImpl.printDublicatesInStringDeatails();

		assertThat(result).contains("s"); // 's' is duplicated in "suresh"
	}

	@Test
	void uniquerecordsInStringDeatails_returnsUniqueCharacters() {
		List<Employee> employees = Arrays.asList(new Employee(1L, "suresh", 66, 3550, "RAJEE@gmail.com", "hyderabad",
				"java", "kms", 748284441532L, "DSVPK5008J", 9676919080L));

		given(employeeRepository.findAll()).willReturn(employees);

		List<String> result = employeeServiceImpl.uniquerecordsInStringDeatails();

		assertThat(result).contains("u", "r", "e", "h"); // unique characters in "suresh"
	}

	@Test
	void longestStringDeatails_returnsLongestName() {
		List<Employee> employees = Arrays.asList(
				new Employee(1L, "RAJEE", 66, 3550, "RAJEE@gmail.com", "hyderabad", "java", "kms", 748284441532L,
						"DSVPK5008J", 9676919080L),
				new Employee(2L, "srinu", 61, 3540, "srinu@gmail.com", "kmm", "java", "dms", 747854441532L,
						"DSVPK2208J", 9676229080L),
				new Employee(3L, "ajay", 66, 5550, "ajay@gmail.com", "pune", ".net", "tts", 258284441532L,
						"DSVPK5008J", 7876919080L));

		given(employeeRepository.findAll()).willReturn(employees);

		String result = employeeServiceImpl.longestStringDeatails();

		assertThat(result).isEqualTo("srinu"); // longest name
	}

	@Test
	void smallestStringDeatails_returnsShortestName() {
		List<Employee> employees = Arrays.asList(
				new Employee(1L, "RAJEE", 66, 3550, "RAJEE@gmail.com", "hyderabad", "java", "kms", 748284441532L,
						"DSVPK5008J", 9676919080L),
				new Employee(2L, "srinu", 61, 3540, "srinu@gmail.com", "kmm", "java", "dms", 747854441532L,
						"DSVPK2208J", 9676229080L),
				new Employee(3L, "ajay", 66, 5550, "ajay@gmail.com", "pune", ".net", "tts", 258284441532L,
						"DSVPK5008J", 7876919080L));
		given(employeeRepository.findAll()).willReturn(employees);

		String result = employeeServiceImpl.smallestStringDeatails();

		assertThat(result).isEqualTo("ajay"); // shortest name
	}

// Additional Utility Tests
	@Test
	void joiningNamesDeatails_returnsCommaSeparatedNames() {
		List<Employee> employees = Arrays.asList(
				new Employee(1L, "RAJEE", 66, 3550, "RAJEE@gmail.com", "hyderabad", "java", "kms", 748284441532L,
						"DSVPK5008J", 9676919080L),
				new Employee(2L, "srinu", 61, 3540, "srinu@gmail.com", "kmm", "java", "dms", 747854441532L,
						"DSVPK2208J", 9676229080L),
				new Employee(3L, "ajay", 66, 5550, "ajay@gmail.com", "pune", ".net", "tts", 258284441532L,
						"DSVPK5008J", 7876919080L));

		given(employeeRepository.findAll()).willReturn(employees);

		String result = employeeServiceImpl.joiningNamesDeatails();

		assertThat(result).isEqualTo("RAJEE,srinu,ajay");
	}


// Filter Department IDs Test
}
