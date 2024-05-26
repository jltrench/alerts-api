package com.jlt.security.alert;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
public class AlertController {
    private final AlertService alertService;

    @PostMapping
    public ResponseEntity<Alert> createAlert(@RequestBody AlertDto newAlertDto, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        Alert newAlert = alertService.save(newAlertDto, email);
        return new ResponseEntity<>(newAlert, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Alert>> getAllAlerts(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        List<Alert> alerts = alertService.findAllByAuthor(email);
        return new ResponseEntity<>(alerts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alert> getAlertById(@PathVariable Integer id, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        Optional<Alert> alert = alertService.findByIdAndAuthor(id, email);
        return alert.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<Alert>> getNearbyAlerts(@RequestParam String coords, @RequestParam double radius, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        List<Alert> alerts = alertService.findNearbyAlerts(email, coords, radius);
        return new ResponseEntity<>(alerts, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alert> updateAlert(@PathVariable Integer id, @RequestBody AlertDto updatedAlert, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        Optional<Alert> alert = alertService.findByIdAndAuthor(id, email);
        if (alert.isPresent()) {
            Alert existingAlert = alert.get();
            if (updatedAlert.getType() != null) {
                existingAlert.setType(updatedAlert.getType());
            }
            if (updatedAlert.getMessage() != null) {
                existingAlert.setMessage(updatedAlert.getMessage());
            }
            if (updatedAlert.getCoords() != null) {
                existingAlert.setCoords(updatedAlert.getCoords());
            }
            alertService.update(existingAlert);
            return new ResponseEntity<>(existingAlert, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteAlert(@PathVariable Integer id, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        boolean deleted = alertService.deleteByIdAndAuthor(id, email);
        return new ResponseEntity<>(deleted ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }
}