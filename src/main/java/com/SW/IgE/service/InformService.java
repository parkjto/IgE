package com.SW.IgE.service;

import com.SW.IgE.entity.Inform;
import com.SW.IgE.repository.InformRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InformService {

    @Autowired
    private InformRepository informRepository;

    public List<Inform> getAllInformInOrder() {
        return informRepository.findAllByOrderByIdAsc();
    }
//    public Inform getRandomInform() {
//        return informRepository.findRandomInform();
//    }
}
