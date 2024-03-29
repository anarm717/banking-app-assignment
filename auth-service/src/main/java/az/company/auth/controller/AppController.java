package az.company.auth.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import az.company.auth.service.app.AppService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/App")
@SecurityRequirement(name = "bearerAuth")
public class AppController {

    private final AppService service;

    /**
     * Retrieves all applications.
     *
     * @return a ResponseEntity containing a list of all applications
     */
    @GetMapping
    ResponseEntity<?> getAllApp() {
        return ResponseEntity.ok(service.getAll());
    }
}
