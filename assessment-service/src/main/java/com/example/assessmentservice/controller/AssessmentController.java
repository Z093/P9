package com.example.assessmentservice.controller;

import com.example.assessmentservice.model.AssessmentResult;
import com.example.assessmentservice.service.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/assess")
public class AssessmentController {

    private final AssessmentService service;

    public AssessmentController(AssessmentService service) {
        this.service = service;
    }

    @GetMapping("/{patientId}")
    public AssessmentResult assessPatient(@PathVariable int patientId) {
        return service.assessRisk(patientId);
    }
}





