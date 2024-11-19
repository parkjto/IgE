package com.SW.IgE.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "inform")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inform {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String inform;
}
