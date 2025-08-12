package com.beprocces.auth.controller;

import com.beprocces.auth.model.Adherent;
import com.beprocces.auth.model.Adherent;
import com.beprocces.auth.model.User;
import com.beprocces.auth.repository.AdherentRepository;
import com.beprocces.auth.service.AdherentService;
import com.beprocces.auth.serviceImpl.AdherentServiceImpl;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/adherent")
@RequiredArgsConstructor
public class AdherentController {

       private final AdherentService adherentService ;
       private final AdherentServiceImpl  service ;
    @PostMapping
    public ResponseEntity<Adherent> addAdherent(@RequestBody Adherent Adherent) {
        return ResponseEntity.ok(adherentService.create(Adherent));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Adherent> update(@PathVariable Integer id , @RequestBody Adherent adherent){
     return ResponseEntity.ok(adherentService.update(id,adherent));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Adherent> findById(@PathVariable Integer id ){
        return ResponseEntity.ok(adherentService.findById(id))  ;
    }

    @GetMapping
    public ResponseEntity<List<Adherent>> findAll(
    ){
        return  ResponseEntity.ok(this.adherentService.findAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Adherent>> findPage(
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int pageNumber
    ){
        return  ResponseEntity.ok(this.adherentService.findAll(PageRequest.of(pageNumber,pageSize)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id ) {
        adherentService.deleteById(id) ;
    }


    @GetMapping("/getByStatus/{status}")
    public ResponseEntity<Page<Adherent>> findByTypeUser(
            @PathVariable String status,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int pageNumber
    ) {
        return ResponseEntity.ok(adherentService.findByStatus(status , PageRequest.of(pageNumber,pageSize)));
    }

   @PatchMapping("/renouvellAbonnement/{idAdherent}")
   public ResponseEntity<Adherent> updateAbonnement(@PathVariable Integer idAdherent ,@RequestBody Adherent adherent) {
    return ResponseEntity.ok(adherentService.updateAbonnement(idAdherent , adherent)) ;
   }

   @PostMapping("/sendEmail")
    public void sendEmailTest(@RequestBody Adherent adherent) throws MessagingException {
      service.sendEmail(adherent);
   }

    @GetMapping("/search/adherent")
    public ResponseEntity<Page<Adherent>> searchRoles(
            @RequestParam String columnName,
            @RequestParam String value ,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int pageNumber
    ) {
        Page<Adherent> results = adherentService.search(columnName, value , PageRequest.of(pageNumber ,pageSize) );
        return ResponseEntity.ok(results);
    }


}
