package com.SW.IgE.repository;

import com.SW.IgE.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


//public interface MenuRepository extends JpaRepository<Menu, Integer> {
//
//    @Query(value = "SELECT * FROM receip500 WHERE id = (SELECT FLOOR(RAND() * (MAX(id) - MIN(id) + 1)) + MIN(id) FROM receip500) LIMIT 1", nativeQuery = true)
//    Menu findRandomMenu();
//}

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    // 최소 id를 조회하는 쿼리
    @Query("SELECT MIN(m.id) FROM Menu m")
    Integer findMinId();

    // 최대 id를 조회하는 쿼리
    @Query("SELECT MAX(m.id) FROM Menu m")
    Integer findMaxId();

    // 특정 ID에 해당하는 메뉴 조회
    @Query("SELECT m FROM Menu m WHERE m.id = :id")
    Menu findMenuById(@Param("id") Integer id);  // @Param("id")를 추가하여 파라미터 이름을 명시

    @Query("SELECT m FROM Menu m WHERE m.category IN :categories ORDER BY RAND() LIMIT 1")
    Menu findRandomMenuByCategories(@Param("categories") List<String> categories);

    List<Menu> findByCategory(String category);
}
