package com.example.springreactivedemo.repository;

import com.example.springreactivedemo.domain.Person;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class PersonRepositoryImpl implements PersonRepository{
    Person aparna =  new Person(1, "aparna", "bhure");
    Person medha =  new Person(2, "medha", "patil");
    Person arvind =  new Person(3, "arvind", "bhure");
    Person ruchi =  new Person(4, "ruchi", "bhure");

    @Override
    public Mono<Person> getById(Integer id) {
        switch (id){
            case 1:
                return Mono.just(aparna);
            case 2:
                return Mono.just(medha);
            case 3:
                return Mono.just(arvind);
            case 4:
                return Mono.just(ruchi);
            default:
                return Mono.empty();
        }
    }

    @Override
    public Flux<Person> findAll() {
        return Flux.just(aparna, medha, arvind, ruchi);
    }
}
