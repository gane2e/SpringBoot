package com.shop.repository;

import com.shop.entity.Person;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class PersonRepositoryTest {

    @Autowired /*생성자 자동주입*/
    private PersonRepository personRepository;

    @Test
    public void insertPerson() {
        Person person = Person.builder()
                .userName("길동이")
                .age(25)
                .phone("010-1234-1234")
                .address("Seoul, South Korea")
                .regDate(LocalDateTime.now())
                .build();

        personRepository.save(person);
    }

    @Test
    @DisplayName("조회 테스트")
    public void selectAllTest() {
        List<Person> persons = personRepository.findAll();
        persons.forEach(person -> log.info(person.toString()));
    }

    @Test
    @DisplayName("특정 레코드 조회하기")
    public void selectByUserName() {
        List<Person> personList = personRepository.findByUserName("길");
        personList.forEach(person -> log.info(person.toString()));
    }





}