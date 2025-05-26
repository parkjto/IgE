package com.SW.IgE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.ArrayList;

@Data
@Builder
@Entity
@Table(name = "menus")
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String imageUrl;

    @ElementCollection
    @CollectionTable(name = "menu_allergens", joinColumns = @JoinColumn(name = "menu_id"))
    @Column(name = "allergen")
    private List<String> allergens = new ArrayList<>();
}
