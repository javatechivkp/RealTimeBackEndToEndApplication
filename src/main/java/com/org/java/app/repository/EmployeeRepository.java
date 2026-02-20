package com.org.java.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.java.app.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	
Optional<Employee> findByEmpid(long empid);
	
	List<Employee> findByName(String name);
	
	List<Employee> findByPlatform(String platform);
	
	@Query("SELECT e FROM Employee e WHERE e.empid=:empid and e.name=:name and e.workLocation=:workLocation")
	Employee findByEmpIdAndNameAndWorkLocation(@Param("empid") long empid,@Param("name") String name, @Param("deptName") String workLocation);
	

    @Query("SELECT p FROM Employee p WHERE (:cursor IS NULL OR p.id > :cursor) ORDER BY p.id ASC ")
	List<Employee> findByFeatchAllRecords(@Param("cursor") Integer cursor,Pageable pageable);

    @Query("SELECT e FROM Employee e WHERE e.name=:name and e.platform=:platform")
	Employee findByNameAndPlatform(@Param("name")String name,@Param("platform") String platform);

    @Query("SELECT e FROM Employee e WHERE e.name=:name and e.workLocation=:workLocation")
	Employee findByNameAndWorkLocation(String name, String workLocation);

	

}
