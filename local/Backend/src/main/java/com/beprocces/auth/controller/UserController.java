package com.beprocces.auth.controller;


import com.beprocces.auth.authController.ChangePasswordRequest;
import com.beprocces.auth.model.User;
import com.beprocces.auth.model.UserDTO;
import com.beprocces.auth.service.UserService;
import com.beprocces.auth.serviceImpl.GenericSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService ;
    private final GenericSearchService genericSearchService;


    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody UserDTO user) {
      return ResponseEntity.ok(userService.addUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@RequestParam Integer id , @RequestBody User user){
      return ResponseEntity.ok(userService.update(id , user)) ;
    }
    @GetMapping
    public ResponseEntity<List<User>>  findAll() {
     return ResponseEntity.ok(userService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Integer id) {
      return ResponseEntity.ok(userService.findById(id))    ;
    }


    @GetMapping("/getByType/{typeUser}")
    public ResponseEntity<Page<User>> findByTypeUser(
            @PathVariable String typeUser,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int pageNumber
    ) {
        return ResponseEntity.ok(userService.findByTypeUser(typeUser , PageRequest.of(pageNumber,pageSize)));
    }


    @DeleteMapping("/{id}")
    public void delet(@PathVariable Integer id) {
       userService.deleteById(id);
    }


    @PatchMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request , Principal connectedUser){
        userService.changePassword(request , connectedUser);
        return ResponseEntity.ok().build();
    }


    @PutMapping(value = "/accepterOrRefuser/{user_id}/{etat}")
    public ResponseEntity<MessageResponse> accepterOrRefuserConge( @PathVariable("user_id") Integer user_id, @PathVariable("etat") String etat) {
        userService.accepterOrRefuserAccount(user_id, etat);
        if (etat =="Actif" ) {
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("compte accepté"));
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("compte refusè "));
        }

    }

    @DeleteMapping("/roles/{userId}/{roleId}")
    public ResponseEntity<?> removeRoleFromUser(@PathVariable Integer userId , @PathVariable Integer roleId){
        userService.removeRoleFromUser(userId , roleId);
     return ResponseEntity.ok().build();
    }

    @DeleteMapping("/groups/{userId}/{groupId}")
    public ResponseEntity<?> removeGroupFromUser(@PathVariable Integer userId , @PathVariable Integer groupId){
       userService.removeGroupFromUser(userId , groupId) ;
      return ResponseEntity.ok().build();
    }
    @GetMapping("/search/typeUser/{typeUser}")
    public ResponseEntity<Page<User>> searchRoles(
            @PathVariable String typeUser ,
            @RequestParam String columnName,
            @RequestParam String value ,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int pageNumber
    ) {
        Page<User> results = userService.search(columnName, value, typeUser , PageRequest.of(pageNumber ,pageSize) );
        return ResponseEntity.ok(results);
    }
}
