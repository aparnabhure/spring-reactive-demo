package com.example.springreactivedemo.repository;

import com.example.springreactivedemo.domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

class PersonRepositoryImplTest {
    PersonRepositoryImpl personRepository;

    @BeforeEach
    void setUp() {
        personRepository = new PersonRepositoryImpl();
    }

    @Test
    void getByIdBlock() {
        Mono<Person> personMono = personRepository.getById(1);
        Person person = personMono.block();
        System.out.println(person.toString());
    }

    @Test
    void getByIdSubscribe(){
        Mono<Person> personMono = personRepository.getById(1);
        StepVerifier.create(personMono).expectNextCount(1).verifyComplete();
        personMono.subscribe(person->System.out.println(person.toString()));
    }

    @Test
    void getByIdSubscribeNotFound(){
        Mono<Person> personMono = personRepository.getById(6);
        StepVerifier.create(personMono).expectNextCount(0).verifyComplete();
        personMono.subscribe(person->System.out.println(person.toString()));
    }

    @Test
    void getByIdMap(){
        Mono<Person> personMono = personRepository.getById(1);
        personMono.map(person -> person.getFirstName()).subscribe(firstName->{
            System.out.println(firstName);
        });

    }

    @Test
    void findAllFluxBlockFirst(){
        Flux<Person> personFlux = personRepository.findAll();
        Person person = personFlux.blockFirst();
        System.out.println(person.toString());
    }

    @Test
    void findAllFluxSubscribe(){
        Flux<Person> personFlux = personRepository.findAll();
        StepVerifier.create(personFlux).expectNextCount(4).verifyComplete();
        personFlux.subscribe(person->{
            System.out.println(person.toString());
        });
    }

    @Test
    void findAllFluxToList(){
        Flux<Person> personFlux = personRepository.findAll();
        Mono<List<Person>> personListMono = personFlux.collectList();
        personListMono.subscribe(list->{
            list.forEach(person->System.out.println(person.toString()));
        });
    }

    @Test
    void findByIdFlux(){
        Flux<Person> personFlux = personRepository.findAll();
        Mono<Person> personMono = personFlux.filter(person -> person.getId() == 2).next();
        personMono.subscribe(person->{
            System.out.println(person.toString());
        });
    }

    @Test
    void findByIdFluxNotFound(){
        Flux<Person> personFlux = personRepository.findAll();
        Mono<Person> personMono = personFlux.filter(person -> person.getId() == 6).single();
        personMono.subscribe(person->{
            System.out.println(person.toString());
        });
    }

    @Test
    void findByIdFluxNotFoundDoOnError(){
        Flux<Person> personFlux = personRepository.findAll();
        Mono<Person> personMono = personFlux.filter(person -> person.getId() == 6).single();
        personMono.doOnError(throwable -> System.out.println("Something went wrong"))
            .onErrorReturn(Person.builder().build())
            .subscribe(person->{
            System.out.println(person.toString());
        });
    }
}