package com.beprocces.auth.controller;

import com.beprocces.auth.model.Role;
import com.beprocces.auth.model.User;
import com.beprocces.auth.service.RoleService;
import com.beprocces.auth.serviceImpl.GenericSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/role")
@RequiredArgsConstructor
public class RoleController {
   private final RoleService roleService ;
   private final GenericSearchService genericSearchService;


    @PostMapping
    public ResponseEntity<Role> addRole(@RequestBody Role role) {
     return ResponseEntity.ok(roleService.addRole(role));
   }

   @GetMapping("/{id}")
   public ResponseEntity<Role> findById(@PathVariable Integer id ){
     return ResponseEntity.ok(roleService.findById(id))  ;
   }

   @PostMapping("addRoleToUser")
   public void addRoleToUser(@RequestParam String email , @RequestParam String roleName) {
      roleService.addRoleToUser(email , roleName);
   }

   @PutMapping("/{id}")
   public ResponseEntity<Role> update(@PathVariable Integer id , @RequestBody Role role) {
    return ResponseEntity.ok(roleService.update(id , role));
   }

    @GetMapping
    public ResponseEntity<List<Role>> findAll(
    ){
        return  ResponseEntity.ok(this.roleService.findAll());
    }

   @GetMapping("/page")
    public ResponseEntity<Page<Role>> findPage(
            @RequestParam(defaultValue = "10") int pageSize,
             @RequestParam(defaultValue = "0") int pageNumber
   ){
      return  ResponseEntity.ok(this.roleService.findAll(PageRequest.of(pageNumber,pageSize)));
   }

  @GetMapping("/users/{roleId}")
  public ResponseEntity<Page<User>> findByRole(
          @PathVariable Integer roleId ,
          @RequestParam(defaultValue = "10") int pageSize,
          @RequestParam(defaultValue = "0") int pageNumber
          ) {
      return ResponseEntity.ok(this.roleService.getUsersByRoleId(roleId, PageRequest.of(pageNumber, pageSize)));
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Integer id ) {
       roleService.deleteById(id) ;
  }

    @GetMapping("/search")
    public ResponseEntity<List<Role>> searchRoles(
            @RequestParam String columnName,
            @RequestParam String value
               ) {
        List<Role> results = genericSearchService.search(columnName, value, Role.class);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/search/user/{roleId}")
    public ResponseEntity<Page<User>> searchRoles(
            @RequestParam String columnName,
            @RequestParam String value ,
            @PathVariable Integer roleId ,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int pageNumber
    ) {
        Page<User> results = roleService.search(columnName, value, roleId , PageRequest.of(pageNumber ,pageSize) );
        return ResponseEntity.ok(results);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> deleteRoles(@RequestBody List<Role> roles ) {
        roleService.deleteAllRoles(roles);
      return ResponseEntity.noContent().build();
    }



}
