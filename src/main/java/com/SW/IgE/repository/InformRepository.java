package com.SW.IgE.repository;

import com.SW.IgE.entity.Inform;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InformRepository extends JpaRepository<Inform, Integer> {

//    @Query(value = "SELECT * FROM inform ORDER BY RAND() LIMIT 1", nativeQuery = true)
//    Inform findRandomInform();

    List<Inform> findAllByOrderByIdAsc();
}
