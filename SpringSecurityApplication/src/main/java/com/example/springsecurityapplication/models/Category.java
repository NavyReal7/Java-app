package com.example.springsecurityapplication.models;

import jakarta.persistence.*;

import java.util.List;


@Entity //указание именно модели, а не обычного класса
public class Category {

//поля класса(модели)

    //1.создание первичного ключа(айдишник) данной модели
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //2.наименование категории
    private String name;

//связь двух таблиц(Product и Category)

    //аннотация OneToMany связь(одна категория относится ко многим продуктам(и один продукт к одной категории))
              //поле связи с названием category
    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
            //лист(в одной категории лежит целый лист продуктов)
    private List<Product> product;

//getter&setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }





}
