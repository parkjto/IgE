package com.SW.IgE.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Board
{
    @Id //프라이머리키 의미
    @GeneratedValue(strategy = GenerationType.IDENTITY) //
    private Integer id;

    private String title;

    private String content; //DB board 스키마의 형식에 맞게 적어준 것

    private String filename;

    private String filepath;
}
