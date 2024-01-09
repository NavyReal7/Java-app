package com.example.springsecurityapplication.models;


import jakarta.persistence.*;

//КОРЗИНА, РЕАЛИЗОВАННАЯ ЧЕРЕЗ СУЩНОСТЬ БД.
//то, что это уже не класс, а сущность БД.
@Entity
//Это и есть 3 таблица для связи Product.java и Person.java
@Table(name = "product_cart")
public class Cart {

    @Id
    //сущность корзины.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //поле класса(значение сущности БД)
    private int id;

    //аннотация для столбца(поле) в БД.
    @Column(name = "person_id")
    //по этому id пользователя пользователь, войдя в кабинет, сможет подгружать все товары в корзину из БД.
            //целочисленнй тип данных.
    //привязка корзины к пользователю.
    private int personId;

//ДАЛЕЕ БУДЕТ РЕАЛИЗОВАНА связь "многие ко многим".
    //столбец(поле), отвечающий за привязку id(айдишника)пользователя к id(айдишнику) товара. Оба лежат в корзине.
    @Column(name = "product_id")
    //привязка корзины к товару.
    private  int productId;


//constructor
    public Cart(int personId, int productId) {
        this.personId = personId;
        this.productId = productId;
    }

//пустой constructor
    public Cart() {
    }


//getter&setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}


