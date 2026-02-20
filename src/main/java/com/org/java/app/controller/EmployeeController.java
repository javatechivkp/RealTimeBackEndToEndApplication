package com.org.java.app.controller;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.org.java.app.dto.EmployeeDto;
import com.org.java.app.entity.Employee;
import com.org.java.app.mapper.EmployeeMapper;
import com.org.java.app.service.EmployeeService;
import com.org.java.app.util.EmailSender;
import com.org.java.app.util.ExcelGenerator;
import com.org.java.app.util.PdfGenerator;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/employee")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private EmailSender emailSender;

	@PostMapping("/save")
	public ResponseEntity<String> saveEmployee(@Valid @RequestBody EmployeeDto employeeDto, BindingResult result) {
		EmployeeDto emp = employeeService.saveEmployeeDetails(employeeDto);
		 if (result.hasErrors()) {
		        return ResponseEntity.badRequest()
		                .body(result.getAllErrors().get(0).getDefaultMessage());
		    }
		return new ResponseEntity(emp, HttpStatus.CREATED);
	}

	@PutMapping("/update")
	public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee) {
		Employee emp = employeeService.updateEmployeeDetails(employee);
		return new ResponseEntity<Employee>(emp, HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Employee> deletEmployee(@RequestBody Employee employee) {
		Employee emp = employeeService.deleteEmployeeDetails(employee);
		return new ResponseEntity("Sucessfully deleted from DB", HttpStatus.NO_CONTENT);

	}

	@GetMapping("/findAll")
	public ResponseEntity<List<EmployeeDto>> findAllEmployees() {
		List<EmployeeDto> list = employeeService.fetchAllEmployeeDetails();
		return new ResponseEntity<List<EmployeeDto>>(list, HttpStatus.OK);

	}

	@GetMapping("/report/pdf")
	public ResponseEntity<byte[]> downloadEmployeesPdf() {
		// Reuse existing service to fetch all employees as DTOs, then map to entity
		// shape used by PdfGenerator
		var dtos = employeeService.findAllEmployeeDetails();
		var employees = dtos.stream().map(dto -> EmployeeMapper.INSTANCE.mapToEmployeeDTOToEmployee(dto))
				.collect(Collectors.toList());
		byte[] pdf = PdfGenerator.generateEmployeesPdf(employees);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employees.pdf")
				.contentType(MediaType.APPLICATION_PDF).body(pdf);
	}

	@GetMapping("/report/excel")
	public ResponseEntity<byte[]> downloadEmployeesExcel() {
		var dtos = employeeService.findAllEmployeeDetails();
		var employees = dtos.stream().map(dto -> EmployeeMapper.INSTANCE.mapToEmployeeDTOToEmployee(dto))
				.collect(Collectors.toList());
		byte[] xlsx = ExcelGenerator.generateEmployeesExcel(employees);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employees.xlsx")
				.contentType(
						MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
				.body(xlsx);
	}

	@PostMapping("/report/email")
	public ResponseEntity<String> emailEmployeesPdf(String to) {
		try {
			var dtos = employeeService.findAllEmployeeDetails();
			var employees = dtos.stream().map(dto -> EmployeeMapper.INSTANCE.mapToEmployeeDTOToEmployee(dto))
					.collect(Collectors.toList());
			byte[] pdf = PdfGenerator.generateEmployeesPdf(employees);
			emailSender.sendPdf(to, "Employees Report", "<p>Please find attached the latest employees report.</p>", pdf,
					"employees.pdf");
			return ResponseEntity.ok("Email sent to " + to);
		} catch (MessagingException ex) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Email sending failed: " + ex.getMessage());
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + ex.getMessage());
		}
	}

	@GetMapping("/findById/{empid}")
	public ResponseEntity<?> findByIdData(@PathVariable("empid") int empid,
			@RequestHeader(name = "x-Request-Source") String sourceSystem) {

		return switch (sourceSystem.toLowerCase()) {

		case "udemy" -> (empid == 2) ? fetchEmployees(empid)
				: ResponseEntity.badRequest()
						.body("Udemy does not have a course with ID " + empid + ". Please provide a valid ID.");

		case "coursera" -> (empid == 1) ? fetchEmployees(empid)
				: ResponseEntity.badRequest()
						.body("coursera does not have a course with ID " + empid + ". Please provide a valid ID.");

		case "unacademy" -> fetchEmployees(empid);

		default -> ResponseEntity.internalServerError().body("Unknown source system: " + sourceSystem);
		};
	}

	private ResponseEntity<Employee> fetchEmployees(long empid) {
		return ResponseEntity.ok(employeeService.findByIdEmployeeDetails(empid));
	}

	@GetMapping("/getEmployeeByName/{name}")
	public ResponseEntity<Employee> getEmployeeByName(@PathVariable("name") String empName) {
		List<Employee> emp = employeeService.findByName(empName);
		return new ResponseEntity(emp, HttpStatus.OK);
	}

	public ResponseEntity<List<EmployeeDto>> findAllEmployee() {
		return ResponseEntity.ok(employeeService.findAllEmployeeDetails());
	}

	@GetMapping("/findByPlatform/{platform}")
	public ResponseEntity<Employee> findByPlatformDetails(@PathVariable("deptName") String platform) {
		List<Employee> deptDetails = employeeService.findByPlatformDetails(platform);
		return new ResponseEntity(deptDetails, HttpStatus.OK);

	}

	@GetMapping("/findByNameAndWorkLocation/{name}/{workLocation}")
	public ResponseEntity<Employee> findByNameAndDeptName(@PathVariable("name") String name,
			@PathVariable("workLocation") String workLocation) {
		Employee emplist = employeeService.findByNameAndWorkLocationDeatails(name, workLocation);
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/findByEmpIdAndNameAndWorkLocation/{empid}/{name}/{workLocation}")
	public ResponseEntity<Employee> findByEmpIdAndNameAndWorkLocation(@PathVariable("empid") long empid,
			@PathVariable("name") String name, @PathVariable("workLocation") String workLocation) {
		Employee emplist = employeeService.findByEmpIdAndNameAndWorkLocationDeatails(empid, name, workLocation);
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/findBySalaryAsc")
	public ResponseEntity<Employee> findByEmployeeAscSalary() {
		List<Employee> emplist = employeeService.findByEmployeeSalaryAscDeatails();
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/findBySalaryDsc")
	public ResponseEntity<Employee> findByEmployeeDscSalary() {
		List<Employee> emplist = employeeService.findByEmployeeSalaryDscDeatails();
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/findByBetwenSalary")
	public ResponseEntity<Employee> findByEmployeeBetweenSalary() {
		List<EmployeeDto> emplist = employeeService.findByEmployeeBetweenSalaryDeatails();
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/groupCount")
	public ResponseEntity<Employee> findBygroupCount() {
		Map<String, Long> emplist = employeeService.findBygroupCountDeatails();
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/findByEvenNumber")
	public ResponseEntity<Employee> findByEmployeeIdEven() {
		List<Employee> emplist = employeeService.findByEmployeeIdEvenDeatails();
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/findByOddNumber")
	public ResponseEntity<Employee> findByEmployeeIdOdd() {
		List<Employee> emplist = employeeService.findByEmployeeIdOddDeatails();
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/MaxSalary")
	public ResponseEntity<Employee> findByMaxSalary() {
		Employee emplist = employeeService.findByMaxSalaryDeatails();
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/SecondHigestSalary")
	public ResponseEntity<Employee> SecondHigestSalary() {
		Employee secondHigestSalary = employeeService.secondHigestSalaryDeatails();
		return new ResponseEntity(secondHigestSalary, HttpStatus.OK);
	}

	@GetMapping("/SecondListSalary")
	public ResponseEntity<Employee> SecondListSalary() {
		Employee secondListSalary = employeeService.secondListSalaryDeatails();
		return new ResponseEntity(secondListSalary, HttpStatus.OK);
	}

	@GetMapping("/MinSalary")
	public ResponseEntity<Employee> findByMinSalary() {
		Employee emplist = employeeService.findByMinSalaryDeatails();
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/SumSalary")
	public ResponseEntity<Employee> findBySumSalary() {
		double emplist = employeeService.findBySumSalaryDeatails();
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/CountSalary")
	public ResponseEntity<Employee> findByCountSalary() {
		double emplist = employeeService.findByCountSalaryDeatails();
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/findParticularRecords")
	public ResponseEntity<Employee> findParticularRecords() {
		List<Employee> emplist = employeeService.findParticularRecordsDeatails();
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/findParticularFilters")
	public ResponseEntity<Employee> findParticularfilters() {
		List<Employee> empFilterlist = employeeService.findParticularRecordFileter();
		return new ResponseEntity(empFilterlist, HttpStatus.OK);
	}

	@GetMapping("/findParticularRecordsAsc")
	public ResponseEntity<Employee> findParticularRecordsAsc() {
		List<Employee> emplist = employeeService.findParticularRecordsAscsDeatails();
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/findParticularRecordsDsc")
	public ResponseEntity<Employee> findParticularRecordsDsc() {
		List<Employee> emplist = employeeService.findParticularRecordsDscDeatails();
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/printDublicateRecordsInList")
	public ResponseEntity<Employee> printDublicateRecords() {
		Set<Employee> emplist = employeeService.printDublicateRecordsDeatails();
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/printDublicatesInString")
	public ResponseEntity<Employee> printDublicatesInString() {
		List<String> dublicates = employeeService.printDublicatesInStringDeatails();
		return new ResponseEntity(dublicates, HttpStatus.OK);
	}

	@GetMapping("/printwithoutDublicateRecordsInList")
	public ResponseEntity<Employee> printWithoutDublicateRecords() {
		Set<Employee> emplist = employeeService.printWithoutDublicateRecordsDeatails();
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/uniquerecordsInString")
	public ResponseEntity<Employee> uniquerecordsInString() {
		List<String> dublicates = employeeService.uniquerecordsInStringDeatails();
		return new ResponseEntity(dublicates, HttpStatus.OK);
	}

	@GetMapping("/mapNames")
	public ResponseEntity<Employee> mapNames() {
		List<String> emplist = employeeService.mapNamesDeatails();
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/mapNamesToUpperCase")
	public ResponseEntity<Employee> mapNamesToUpperCase() {
		List<String> emplist = employeeService.mapNamesToUppercaseDeatails();
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/findStringOccurence")
	public ResponseEntity<Employee> findStringOccurence() {
		Map<Character, Integer> emplist = employeeService.findStringOccurenceDeatails();
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/groupBySalary")
	public ResponseEntity<Employee> groupBySalary() {
		Map<Object, List<Employee>> emplist = employeeService.groupBySalaryDeatails();
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/groupByNames")
	public ResponseEntity<Employee> groupByNames() {
		Map<Object, List<Employee>> emplist = employeeService.groupByNamesDeatails();
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/indexRanges/{fromIndex}/{toIndex}")
	public ResponseEntity<Employee> indexRanges(@PathVariable("fromIndex") int fromIndex,
			@PathVariable("toIndex") int toIndex) {
		List<Employee> emplist = employeeService.indexRangesDeatails(fromIndex, toIndex);
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/firstnonRepeactedCharacterInString")
	public ResponseEntity<Employee> firstnonRepeactedCharacterInString() {
		String emplist = employeeService.firstnonRepeactedCharacterInStringDeatails();
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/firstRepeactedCharacterInString")
	public ResponseEntity<Employee> firstRepeactedCharacterInString() {
		String emplist = employeeService.firstRepeactedCharacterInStringDeatails();
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/longestString")
	public ResponseEntity<Employee> longestString() {
		String emplist = employeeService.longestStringDeatails();
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/leftRotationString")
	public ResponseEntity<Employee> leftRotationString() {
		String emplist = employeeService.leftRotationStringDeatails();
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/rightRotationString")
	public ResponseEntity<Employee> rightRotationString() {
		String emplist = employeeService.rightRotationStringDeatails();
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/smallestString")
	public ResponseEntity<Employee> smallestString() {
		String emplist = employeeService.smallestStringDeatails();
		return new ResponseEntity(emplist, HttpStatus.OK);
	}

	@GetMapping("/filterPanNumbersDeatails")
	public ResponseEntity<Employee> filterPanNumbersDeatails() {
		List<String> deptIds = employeeService.filterPanNumbersDeatails();
		return new ResponseEntity(deptIds, HttpStatus.OK);
	}

	@GetMapping("/stringReverseJava8")
	public ResponseEntity<Employee> stringReverseJava8() {
		String reverse = employeeService.stringReverseJava8Deatails();
		return new ResponseEntity(reverse, HttpStatus.OK);
	}

	@GetMapping("/joiningNames")
	public ResponseEntity<Employee> joiningNames() {
		String names = employeeService.joiningNamesDeatails();
		return new ResponseEntity(names, HttpStatus.OK);
	}

	@GetMapping("/listToSetConversion")
	public ResponseEntity<Employee> listToSetConversion() {
		Set<Employee> listToSetConversion = employeeService.listToSetCoversion();
		return new ResponseEntity(listToSetConversion, HttpStatus.OK);
	}

	@GetMapping("/listToMapConversion")
	public ResponseEntity<Employee> listToMapConversion() {
		Map<Long, Employee> listToMapConversion = employeeService.listToMapCoversion();
		return new ResponseEntity(listToMapConversion, HttpStatus.OK);
	}

	@GetMapping("/setToListConversion")
	public ResponseEntity<Employee> setToListConversion() {
		List<Employee> setToListConversion = employeeService.setToListConversion();
		return new ResponseEntity(setToListConversion, HttpStatus.OK);
	}

	@GetMapping("/setToMapConversion")
	public ResponseEntity<Employee> setToMapConversion() {
		Map<Long, Employee> setToMapConversion = employeeService.setToMapConversionDetails();
		return new ResponseEntity(setToMapConversion, HttpStatus.OK);
	}

	@GetMapping("/mapToListConversion")
	public ResponseEntity<Employee> mapToListConversion() {
		List<Entry<Long, Employee>> mapToListConversion = employeeService.mapToListConversionDetails();
		return new ResponseEntity(mapToListConversion, HttpStatus.OK);
	}

	@GetMapping("/mapToSetConversion")
	public ResponseEntity<Employee> mapToSetConversion() {
		Set<Entry<Long, Employee>> mapToSetConversion = employeeService.mapToSetConversionDetails();
		return new ResponseEntity(mapToSetConversion, HttpStatus.OK);
	}

	@GetMapping("/pagination")
	public ResponseEntity<Page<Employee>> listProducts(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {

		Page<Employee> products = employeeService.getEmployeesList(page, size);
		System.out.println(products);
		return ResponseEntity.ok(products);

	}

}
