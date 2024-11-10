package com.SW.IgE.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import com.SW.IgE.converter.IngredientsConverter;

@Entity
@Table(name = "receip500")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "menu_name")
    private String menu_name;

    private String cooking_method;
    private String category;
    private String dish_category;
    private String food_type;
    // IngredientsConverter를 사용하여 JSON 데이터를 List<String>로 자동 변환
    @Convert(converter = IngredientsConverter.class)
    @Column(name = "ingredients", columnDefinition = "json")
    private List<String> ingredients;
    private String serving_size;
}
