package com.beprocces.auth.controller;

import com.beprocces.auth.model.Groups;
import com.beprocces.auth.model.Groups;
import com.beprocces.auth.model.Role;
import com.beprocces.auth.model.User;
import com.beprocces.auth.service.GroupsService;
import com.beprocces.auth.serviceImpl.GenericSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/groups")
@RequiredArgsConstructor
public class GroupsController {
    private final GroupsService groupsService ;
    private final GenericSearchService genericSearchService ;
    @PostMapping
    public ResponseEntity<Groups> addGroups(@RequestBody Groups groups) {
        return ResponseEntity.ok(groupsService.addGroups(groups));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Groups> findById(@PathVariable Integer id ){
        return ResponseEntity.ok(groupsService.findById(id))  ;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Groups> update(@PathVariable Integer id , @RequestBody Groups groups) {
        return ResponseEntity.ok(groupsService.update(id , groups));
    }

    @GetMapping
    public ResponseEntity<List<Groups>> findAll(
    ){
        return  ResponseEntity.ok(this.groupsService.findAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Groups>> findPage(
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int pageNumber
    ){
        return  ResponseEntity.ok(this.groupsService.findAll(PageRequest.of(pageNumber,pageSize)));
    }

    @GetMapping("/users/{groupId}")
    public ResponseEntity<Page<User>> findByRole(
            @PathVariable Integer groupId ,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int pageNumber
    ) {
        return ResponseEntity.ok(this.groupsService.getUsersByGroupId(groupId, PageRequest.of(pageNumber, pageSize)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id ) {
        groupsService.deleteById(id) ;
    }

   // search dans la liste de user par group id
    @GetMapping("/search/user/{groupId}")
    public ResponseEntity<Page<User>> searchRoles(
            @RequestParam String columnName,
            @RequestParam String value ,
            @PathVariable Integer groupId ,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int pageNumber
       ) {
        Page<User> results = groupsService.search(columnName, value, groupId , PageRequest.of(pageNumber ,pageSize) );
        return ResponseEntity.ok(results);
    }

    // search dans la table groups
    @GetMapping("/search")
    public ResponseEntity<List<Groups>> searchRoles(
            @RequestParam String columnName,
            @RequestParam String value
    ) {
        List<Groups> results = genericSearchService.search(columnName, value, Groups.class);
        return ResponseEntity.ok(results);
    }




}
