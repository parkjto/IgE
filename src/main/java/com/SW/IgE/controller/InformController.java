package com.SW.IgE.controller;

import com.SW.IgE.entity.Inform;
import com.SW.IgE.service.InformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InformController {

    @Autowired
    private InformService informService;

    @GetMapping("/api/all-informs")
    public List<Inform> getAllInforms() {
        return informService.getAllInformInOrder();
    }
//
//    @GetMapping("/api/random-inform")
//    public Inform getRandomInform() {
//        return informService.getRandomInform();
//    }
}
