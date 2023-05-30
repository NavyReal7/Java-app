package com.example.springsecurityapplication.models;

import jakarta.persistence.*;

//МОДЕЛЬ(сущность~путь хранения фото) ДЛЯ ХРАНЕНИЯ ФОТО, ПРИВЯЗАННЯ К Product модели-----------------------------------

@Entity //указание именно модели, а не обычного класса
public class Image {

//поля класса(модели)

    //1.создание первичного ключа(айдишник) данной модели
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    //2.название фото
    private String fileName;

    //связь с Product моделью
    //объект продуктов
    //аннотация ManyToOne(много фото может относится к одному продукту)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Product product;

//constructor
    public Image(int id, String fileName, Product product) {
        this.id = id;
        this.fileName = fileName;
        this.product = product;
    }
// пустой constructor. Он нужен для листа(List) Image в модели Product
    public Image() {

    }

    //getter&setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }







}
