package com.org.java.app.serviceimpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import com.org.java.app.dto.EmployeeDto;
import com.org.java.app.entity.Employee;
import com.org.java.app.exceptions.NoDataAvailableException;
import com.org.java.app.mapper.EmployeeMapper;
import com.org.java.app.repository.EmployeeRepository;
import com.org.java.app.service.EmployeeService;

@Component
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public EmployeeDto saveEmployeeDetails(EmployeeDto employeeDto) {
		Employee employee = EmployeeMapper.INSTANCE.mapToEmployeeDTOToEmployee(employeeDto);
		employeeRepository.save(employee);
		return employeeDto;
	}

	@Override
	public Employee updateEmployeeDetails(Employee employee) {
		return employeeRepository.save(employee);
	}

	@Override
	public Employee deleteEmployeeDetails(Employee employee) {
		// TODO Auto-generated method stub
		List<Employee> list = employeeRepository.findAll();
		List<Long> empIds = list.stream().map(s1 -> s1.getEmpid()).collect(Collectors.toList());
		if (!empIds.contains(employee.getEmpid())) {
			throw new RuntimeException();
		}
		employeeRepository.delete(employee);
		return employee;

	}

	@Override
	public List<EmployeeDto> fetchAllEmployeeDetails() {
		List<Employee> list = employeeRepository.findAll();
		return list.stream().map((user) -> EmployeeMapper.INSTANCE.mapToEmployeeToEmployeeDTO(user))
				.collect(Collectors.toList());
	}

	@Override
	public List<EmployeeDto> findAllEmployeeDetails() {
		List<Employee> list = employeeRepository.findAll();
		return list.stream().map((user) -> EmployeeMapper.INSTANCE.mapToEmployeeToEmployeeDTO(user))
				.collect(Collectors.toList());
	}

	@Override
	public Employee findByIdEmployeeDetails(long empid) {
		Employee findIds = employeeRepository.findByEmpid(empid)
				.orElseThrow(() -> new NoDataAvailableException("No data prasent given id::" + empid));
		return findIds;
	}

	@Override
	public List<Employee> findByEmployeeSalaryAscDeatails() {
		List<Employee> list = employeeRepository.findAll();
		if (list.isEmpty()) {
			throw new NoDataAvailableException("No Data available" + list);
		}
		List<Employee> ascsort = list.stream()
				.sorted((s1, s2) -> s1.getSalary() < s2.getSalary() ? -1 : s2.getSalary() < s2.getSalary() ? 1 : 0)
				.collect(Collectors.toList());
		/*
		 * findByEmpIdAndNameAndDeptName List<Employee> ByIDAscSorted =
		 * list.stream().sorted(Comparator.comparing(Employee::getEmpId)).collect(
		 * Collectors.toList()); return ByIDAscSorted;
		 */
		return ascsort;
	}

	@Override
	public List<Employee> findByEmployeeSalaryDscDeatails() {
		List<Employee> list = employeeRepository.findAll();
		if (list.isEmpty()) {
			throw new NoDataAvailableException("No Data available" + list);
		}
		List<Employee> dscsort = list.stream()
				.sorted((s1, s2) -> s1.getSalary() > s2.getSalary() ? -1 : s2.getSalary() > s1.getSalary() ? 1 : 0)
				.collect(Collectors.toList());

		return dscsort;
		/*
		 * List<Employee> ByIdDscSorted =
		 * list.stream().sorted(Comparator.comparing(Employee::getEmpId).reversed()).
		 * collect(Collectors.toList()); return ByIdDscSorted;
		 */
	}

	@Override
	public List<Employee> findByEmployeeIdEvenDeatails() {
		List<Employee> list = employeeRepository.findAll();
		List<Employee> evenIds = list.stream().filter(s1 -> s1.getEmpid() % 2 == 0).collect(Collectors.toList());
		if (evenIds.isEmpty()) {
			throw new NoDataAvailableException("No Data available" + list);
		}
		return evenIds;
	}

	@Override
	public List<Employee> findByEmployeeIdOddDeatails() {
		List<Employee> list = employeeRepository.findAll();
		List<Employee> oddIds = list.stream().filter(s1 -> s1.getEmpid() % 2 != 0).collect(Collectors.toList());
		if (oddIds.isEmpty()) {
			throw new NoDataAvailableException("No Data available" + oddIds);
		}
		return oddIds;
	}

	@Override
	public Employee findByMaxSalaryDeatails() {
		List<Employee> list = employeeRepository.findAll();
		Employee maxSalary11 = list.stream()
				.max((s1, s2) -> s1.getSalary() < s2.getSalary() ? -1 : s1.getSalary() < s2.getSalary() ? 1 : 0).get();
		Employee maxSalaryEmployee = list.stream().max(Comparator.comparingDouble(s1 -> s1.getSalary())).get();
		return maxSalaryEmployee;
		// return maxSalary;
	}

	@Override
	public Employee findByMinSalaryDeatails() {
		List<Employee> list = employeeRepository.findAll();
		// Employee
		// minSalary=list.stream().min((s1,s2)->s2.getSalary()>s1.getSalary()?-1:s1.getSalary()>s2.getSalary()?1:0).get();
		Employee minSalaryEmployee = list.stream().min(Comparator.comparingDouble(s1 -> s1.getSalary())).get();
		return minSalaryEmployee;
		// return minSalary;
	}

	@Override
	public double findBySumSalaryDeatails() {
		List<Employee> list = employeeRepository.findAll();
		double sumSalary = list.stream().mapToDouble(s1 -> s1.getSalary()).summaryStatistics().getSum();
		return sumSalary;
	}

	@Override
	public double findByCountSalaryDeatails() {
		List<Employee> list = employeeRepository.findAll();
		double countSalary = list.stream().mapToDouble(s1 -> s1.getSalary()).summaryStatistics().getCount();
		return countSalary;
	}

	@Override
	public List<Employee> findParticularRecordsDeatails() {
		List<Employee> list = employeeRepository.findAll();
		List<Employee> records = list.stream().skip(2).limit(5).collect(Collectors.toList());
		if (records.isEmpty()) {
			throw new NoDataAvailableException("No Data available" + records);
		}
		return records;
	}

	@Override
	public Set<Employee> printDublicateRecordsDeatails() {
		Set<Double> set = new HashSet<Double>();
		List<Employee> list = employeeRepository.findAll();
		Set<Employee> dublicates = list.stream().filter(s1 -> !set.add(s1.getSalary())).collect(Collectors.toSet());
		if (dublicates.isEmpty()) {
			throw new NoDataAvailableException("No Data available" + dublicates);
		}
		return dublicates;
	}

	@Override
	public Set<Employee> printWithoutDublicateRecordsDeatails() {
		Set<Double> set = new HashSet<Double>();
		List<Employee> list = employeeRepository.findAll();
		Set<Employee> withoutdublicates = list.stream().filter(s1 -> set.add(s1.getSalary()))
				.collect(Collectors.toSet());
		if (withoutdublicates.isEmpty()) {
			throw new NoDataAvailableException("No Data available" + withoutdublicates);
		}
		return withoutdublicates;
	}

	@Override
	public List<Employee> findParticularRecordsAscsDeatails() {
		List<Employee> list = employeeRepository.findAll();
		if (list.isEmpty()) {
			throw new NoDataAvailableException("No Data available" + list);
		}
		List<Employee> ascrecords = list.stream()
				.sorted((s1, s2) -> s1.getSalary() < s2.getSalary() ? -1 : s2.getSalary() < s1.getSalary() ? 1 : 0)
				.skip(1).limit(4).collect(Collectors.toList());
		return ascrecords;
	}

	@Override
	public List<Employee> findParticularRecordsDscDeatails() {
		List<Employee> list = employeeRepository.findAll();
		List<Employee> dscrecords = list.stream()
				.sorted((s1, s2) -> s2.getSalary() < s1.getSalary() ? -1 : s1.getSalary() < s2.getSalary() ? 1 : 0)
				.skip(1).limit(4).collect(Collectors.toList());
		return dscrecords;
	}

	@Override
	public List<String> mapNamesDeatails() {
		List<Employee> list = employeeRepository.findAll();
		List<String> names = list.stream().map(s1 -> s1.getName()).sorted(Comparator.reverseOrder())
				.collect(Collectors.toList());
		return names;
		// List<Employee> ascNamesSorted =
		// list.stream().sorted(Comparator.comparing(Employee::getEmpName)).collect(Collectors.toList());
		// return ascNamesSorted;
		// List<Employee> dscNamesSorted =
		// list.stream().sorted(Comparator.comparing(Employee::getEmpName).reversed()).collect(Collectors.toList());
		// return dscNamesSorted;
	}

	@Override
	public Map<Character, Integer> findStringOccurenceDeatails() {
		String str = null;
		List<Employee> list = employeeRepository.findAll();
		List<String> names = list.stream().map(s1 -> s1.getName()).sorted().collect(Collectors.toList());
		System.out.println(names);
		for (String string : names) {
			if (string.equals("naveenkumar")) {
				str = "naveenkumar";
				break;
			}
		}
		if (str == null) {
			return new HashMap<>();
		}
		Map<String, Long> map = Arrays.stream(str.split(""))
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		System.out.println(map);
		char[] ch = str.toCharArray();
		Map<Character, Integer> map1 = new HashMap<Character, Integer>();
		for (Character char1 : ch) {
			if (map1.containsKey(char1)) {
				map1.put(char1, map1.get(char1) + 1);
			} else {
				map1.put(char1, +1);
			}
		}
		System.out.println(map);
		return map1;
	}

	@Override
	public Map<Object, List<Employee>> groupBySalaryDeatails() {
		List<Employee> list = employeeRepository.findAll();
		Map<Object, List<Employee>> groupSalaries = list.stream()
				.collect(Collectors.groupingBy(s1 -> s1.getSalary(), TreeMap::new, Collectors.toList()));
		return groupSalaries;
	}

	@Override
	public Map<Object, List<Employee>> groupByNamesDeatails() {
		List<Employee> list = employeeRepository.findAll();
		Map<Object, List<Employee>> groupNames = list.stream()
				.collect(Collectors.groupingBy(s1 -> s1.getName(), TreeMap::new, Collectors.toList()));
		return groupNames;
	}

	@Override
	public Employee findByNameAndPlatformeDeatails(String name, String platform) {
		Employee list = employeeRepository.findByNameAndPlatform(name, platform);
		if (list == null || list.getName() == null || list.getName().isEmpty()) {
			throw new NoDataAvailableException("No Data available" + list);
		}
		return list;
	}

	@Override
	public Employee findByEmpIdAndNameAndWorkLocationDeatails(long empid, String name, String workLocation) {
		Employee list = employeeRepository.findByEmpIdAndNameAndWorkLocation(empid, name, workLocation);
		if (list == null || list.getName() == null || list.getName().isEmpty()) {
			throw new NoDataAvailableException("No Data available" + list);
		}
		return list;
	}

	@Override
	public String firstnonRepeactedCharacterInStringDeatails() {
		String str = null;
		List<Employee> list = employeeRepository.findAll();
		List<String> names = list.stream().map(s1 -> s1.getName()).sorted().collect(Collectors.toList());
		for (String string : names) {
			if (string.equals("suresh")) {
				str = "suresh";
				break;
			}

		}
		if (str == null) {
			return null;
		}
		String firstnonRepeated = Arrays.stream(str.split(""))
				.collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()))
				.entrySet().stream().filter(s1 -> s1.getValue() == 1).findFirst().get().getKey();
		return firstnonRepeated;
	}

	@Override
	public String firstRepeactedCharacterInStringDeatails() {
		String str = null;
		List<Employee> list = employeeRepository.findAll();
		List<String> names = list.stream().map(s1 -> s1.getName()).sorted().collect(Collectors.toList());
		for (String string : names) {
			if (string.equals("suresh")) {
				str = "suresh";
				break;
			}
		}
		if (str == null) {
			return null;
		}
		String firstRepeatedCharcater = Arrays.stream(str.split(""))
				.collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()))
				.entrySet().stream().filter(s1 -> s1.getValue() > 1).findFirst().get().getKey();
		return firstRepeatedCharcater;
	}

	@Override
	public List<String> printDublicatesInStringDeatails() {
		String str = null;
		List<Employee> list = employeeRepository.findAll();
		List<String> names = list.stream().map(s1 -> s1.getName()).sorted().collect(Collectors.toList());
		for (String string : names) {
			if (string.equals("suresh")) {
				str = "suresh";
				break;
			}

		}
		if (str == null) {
			return Collections.emptyList();
		}
		List<String> dublicates = Arrays.stream(str.split(""))
				.collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()))
				.entrySet().stream().filter(s1 -> s1.getValue() > 1).map(Map.Entry::getKey)
				.collect(Collectors.toList());
		return dublicates;
	}

	@Override
	public List<String> uniquerecordsInStringDeatails() {
		String str = null;
		List<Employee> list = employeeRepository.findAll();
		List<String> names = list.stream().map(s1 -> s1.getName()).sorted().collect(Collectors.toList());
		for (String string : names) {
			if (string.equals("suresh")) {
				str = "suresh";
				break;
			}

		}
		if (str == null) {
			return Collections.emptyList();
		}
		List<String> uniq = Arrays.stream(str.split(""))
				.collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()))
				.entrySet().stream().filter(s1 -> s1.getValue() == 1).map(Map.Entry::getKey)
				.collect(Collectors.toList());
		return uniq;
	}

	@Override
	public String longestStringDeatails() {
		List<Employee> list = employeeRepository.findAll();
		List<String> stringnames = list.stream().map(s1 -> s1.getName()).collect(Collectors.toList());
		String longestString = stringnames.stream()
				.reduce((word1, word2) -> word1.length() > word2.length() ? word1 : word2).get();

		return longestString;
	}

	@Override
	public String smallestStringDeatails() {
		List<Employee> list = employeeRepository.findAll();
		List<String> stringnames = list.stream().map(s1 -> s1.getName()).collect(Collectors.toList());
		String smallestString = stringnames.stream()
				.reduce((word1, word2) -> word2.length() > word1.length() ? word1 : word2).get();
		return smallestString;
	}

	@Override
	public List<String> filterPanNumbersDeatails() {
		List<Employee> list = employeeRepository.findAll();
		List<String> deptIds = list.stream().map(s1 -> s1.getPanNumber()).collect(Collectors.toList());
		List<String> depts = deptIds.stream().map(s1 -> s1 + "").filter(s2 -> s2.startsWith("2"))
				.collect(Collectors.toList());
		return depts;
	}

	@Override
	public String stringReverseJava8Deatails() {
		String str = "SREENIVASARAO";
		String reverse = Arrays.asList(str).stream().map(s -> new StringBuilder(s).reverse().toString())
				.collect(Collectors.toList()).get(0);
		return reverse;
	}

	@Override
	public Employee secondHigestSalaryDeatails() {
		List<Employee> list = employeeRepository.findAll();
		Employee secondHigestSalary = list.stream().sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
				.skip(1).findFirst().get();
		return secondHigestSalary;
	}

	@Override
	public Employee secondListSalaryDeatails() {
		List<Employee> list = employeeRepository.findAll();
		Employee secondListSalary = list.stream().sorted(Comparator.comparingDouble(Employee::getSalary)).skip(1)
				.findFirst().get();
		return secondListSalary;
	}

	@Override
	public List<Employee> indexRangesDeatails(long fromIndex, long toIndex) {
		List<Employee> list = employeeRepository.findAll();
		List<Employee> ranges = list.subList(0, 0);
		return ranges;
	}

	@Override
	public String joiningNamesDeatails() {
		List<Employee> list = employeeRepository.findAll();
		String joinNames = list.stream().map(s1 -> s1.getName()).collect(Collectors.joining(","));
		return joinNames;
	}

	@Override
	public Set<Employee> listToSetCoversion() {
		List<Employee> list = employeeRepository.findAll();
		Set<Employee> set = list.stream().collect(Collectors.toSet());
		return set;
	}

	@Override
	public Map<Long, Employee> listToMapCoversion() {
		List<Employee> list = employeeRepository.findAll();
		Map<Long, Employee> listToMapConversion = list.stream()
				.collect(Collectors.toMap(Employee::getEmpid, Function.identity()));
		return listToMapConversion;
	}

	@Override
	public List<Employee> setToListConversion() {
		List<Employee> list = employeeRepository.findAll();
		Set<Employee> set = list.stream().collect(Collectors.toSet());
		List<Employee> setToList = set.stream().collect(Collectors.toList());
		return setToList;
	}

	@Override
	public Map<Long, Employee> setToMapConversionDetails() {
		List<Employee> list = employeeRepository.findAll();
		Set<Employee> set = list.stream().collect(Collectors.toSet());
		Map<Long, Employee> setToMap = set.stream().collect(Collectors.toMap(Employee::getEmpid, Function.identity()));
		return setToMap;
	}

	@Override
	public List<Entry<Long, Employee>> mapToListConversionDetails() {
		List<Employee> list = employeeRepository.findAll();
		Map<Long, Employee> listToMapConversion = list.stream()
				.collect(Collectors.toMap(Employee::getEmpid, Function.identity()));
		List<Entry<Long, Employee>> mapToList = listToMapConversion.entrySet().stream().collect(Collectors.toList());
		return mapToList;
	}

	@Override
	public Set<Entry<Long, Employee>> mapToSetConversionDetails() {
		List<Employee> list = employeeRepository.findAll();
		Map<Long, Employee> listToMapConversion = list.stream()
				.collect(Collectors.toMap(Employee::getEmpid, Function.identity()));
		Set<Entry<Long, Employee>> mapToSet = listToMapConversion.entrySet().stream().collect(Collectors.toSet());
		return mapToSet;
	}

	@Override
	public Optional<Employee> findByEmployeeIdDeatails(long empid) {
		Optional<Employee> emp = employeeRepository.findByEmpid(empid);
		// TODO Auto-generated method stub
		return emp;
	}

	public List<Employee> findByPlatformDetails(String platform) {
		List<Employee> deptNames = employeeRepository.findByPlatform(platform);
		return deptNames;

	}

	@Override
	public String leftRotationStringDeatails() {
		String originalString = "sreenivasarao";
		int rotateCharacters = 4;
		String leftRotation = originalString.substring(rotateCharacters)
				+ originalString.substring(0, rotateCharacters);
		return leftRotation;
	}

	@Override
	public String rightRotationStringDeatails() {
		String originalString = "sreenivasarao";
		int rotatechar = 3;
		int partion = originalString.length() - rotatechar;
		String rightRotation = originalString.substring(partion) + originalString.substring(0, partion);
		return rightRotation;
	}

	@Override
	public List<EmployeeDto> findByEmployeeBetweenSalaryDeatails() {
		List<Employee> list = employeeRepository.findAll();
		List<Employee> empList = list.stream().filter(s1 -> s1.getSalary() > 65000 & s1.getSalary() < 90000)
				.collect(Collectors.toList());
		List<EmployeeDto> empDtoList = empList.stream()
				.map(s1 -> EmployeeMapper.INSTANCE.mapToEmployeeToEmployeeDTO(s1)).collect(Collectors.toList());
		return empDtoList;
	}

	@Override
	public Map<String, Long> findBygroupCountDeatails() {
		List<Employee> list = employeeRepository.findAll();
		List<EmployeeDto> dtoList = list.stream().map(s1 -> EmployeeMapper.INSTANCE.mapToEmployeeToEmployeeDTO(s1))
				.collect(Collectors.toList());
		Map<String, Long> deptDeatils = dtoList.stream()
				.collect(Collectors.groupingBy(EmployeeDto::getProjectName, Collectors.counting()));
		return deptDeatils;

	}

	@Override
	public List<Employee> findParticularRecordFileter() {
		List<Employee> list = employeeRepository.findAll();
		List<Employee> particularDetails = list.stream().filter(s1 -> s1.getEmpid() > 3).collect(Collectors.toList());
		return particularDetails;
	}

	@Override
	public List<String> mapNamesToUppercaseDeatails() {
		List<Employee> list = employeeRepository.findAll();
		List<String> Names = list.stream().map(s1 -> s1.getName()).collect(Collectors.toList());
		List<String> namesList = Names.stream().map(s1 -> s1.toUpperCase()).collect(Collectors.toList());
		return namesList;
	}

	@Override
	public List<Employee> findByName(String name) {
		List<Employee> list = employeeRepository.findByName(name);
		if (list.isEmpty()) {
			throw new NoDataAvailableException("No Data available" + list);
		}

		return list;
	}

	@Override
	public Page<Employee> getEmployeesList(long page, long size) {
		return employeeRepository.findAll(PageRequest.of(0, 0));
	}

	@Override
	public Employee findByNameAndWorkLocationDeatails(String name, String workLocation) {
		Employee list = employeeRepository.findByNameAndWorkLocation(name, workLocation);
		if (list == null || list.getName() == null || list.getName().isEmpty()) {
			throw new NoDataAvailableException("No Data available" + list);
		}
		return list;
	}

}
