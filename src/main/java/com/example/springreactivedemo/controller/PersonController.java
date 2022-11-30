package com.example.springreactivedemo.controller;

import com.example.springreactivedemo.domain.ActionInstanceModel;
import com.example.springreactivedemo.domain.EntityInfo;
import com.example.springreactivedemo.domain.Person;
import com.example.springreactivedemo.repository.PersonRepository;
import com.example.springreactivedemo.service.AssetGroupResolverService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/persons")
public class PersonController {
    private final PersonRepository personRepository;
    private final AssetGroupResolverService assetGroupResolverService;
    @Value("${purge.id:0}")
    private Long lastPurgeScopeId;
    Random random = new Random();

    @GetMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<String> getPersonById(@PathVariable final Integer id){
        Mono<Person> personMono = personRepository.getById(id);
        Person person = personMono.block();
        return ResponseEntity.ok(person==null?"not found":person.toString());
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<String> getAll(){
        personRepository.findAll();
        return ResponseEntity.ok("success");
    }

    @PostMapping(value = "/execute")
    @ResponseBody
    public ResponseEntity<String> execute(){
        assetGroupResolverService.process(ActionInstanceModel.builder().id(123L).build(), generateDummyEntities());
        return ResponseEntity.accepted().body("Request expected");
    }

    @PostMapping(value = "/execute/insync")
    @ResponseBody
    public ResponseEntity<String> executeInSequence(){
        assetGroupResolverService.processWithOutThread(ActionInstanceModel.builder().id(123L).build(), generateDummyEntities());
        return ResponseEntity.accepted().body("Request expected");
    }

    List<EntityInfo> generateDummyEntities(){
        List<EntityInfo> entityInfos = new ArrayList<>();
        int min = 20;
        for(int i=0; i<10; i++){
            Map<String, Object> attr = new HashMap<>();
            attr.put("device_group_name", "group "+i);
            attr.put("device_count", (min+random.nextInt(10)));
            entityInfos.add(EntityInfo.builder().entityId(UUID.randomUUID().toString()).additionalAttributes(attr).build());
        }
        return entityInfos;
    }

}
