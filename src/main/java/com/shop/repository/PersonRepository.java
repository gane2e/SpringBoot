package com.shop.repository;

import com.shop.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    /* 인자값으로 넘긴 userName을 @Param으로 받을것으로 명시함*/
    @Query("select p from Person p where p.userName like %:userName%")
    List<Person> findByUserName(@Param("userName") String userName);
}
