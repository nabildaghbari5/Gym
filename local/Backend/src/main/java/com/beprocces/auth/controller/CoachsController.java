package com.beprocces.auth.controller;
import com.beprocces.auth.model.CoachRequest;
import com.beprocces.auth.model.Coachs;
import com.beprocces.auth.model.User;
import com.beprocces.auth.service.CoachsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/coach")
@RequiredArgsConstructor
public class CoachsController {

    private final CoachsService coachsService;
    @PostMapping
    public ResponseEntity<Coachs> addcoachs(@RequestBody CoachRequest coachs) {
        return ResponseEntity.ok(coachsService.add(coachs));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Coachs> update(@PathVariable Integer id, @RequestBody CoachRequest coachs) {
        return ResponseEntity.ok(coachsService.update(id, coachs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coachs> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(coachsService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Coachs>> findAll(
    ) {
        return ResponseEntity.ok(this.coachsService.findAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Coachs>> findPage(
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int pageNumber
    ) {
        return ResponseEntity.ok(this.coachsService.findAll(PageRequest.of(pageNumber, pageSize)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        coachsService.deleteById(id);
    }

    @GetMapping("/coachs/{groupId}")
    public ResponseEntity<Page<Coachs>> findByRole(
            @PathVariable Integer groupId ,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int pageNumber
    ) {
        return ResponseEntity.ok(this.coachsService.getCoachsByGroupId(groupId, PageRequest.of(pageNumber, pageSize)));
    }


    @DeleteMapping("/groups/{userId}/{groupId}")
    public ResponseEntity<?> removeGroupFromUser(@PathVariable Integer userId , @PathVariable Integer groupId){
        coachsService.removeGroupFromUser(userId , groupId) ;
        return ResponseEntity.ok().build();
    }



}    
