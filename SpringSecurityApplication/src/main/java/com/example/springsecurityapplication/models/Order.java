package com.example.springsecurityapplication.models;

import com.example.springsecurityapplication.enumm.Status;
import jakarta.persistence.*;

import java.time.LocalDateTime;


//МОДЕЛЬ СТАТУСА ЗАКАЗА-----------------------------------------------

//аннотация @Entity указывает на то, что данный класс будет моделью.
@Entity
//имя таблицы в БД
              //ORDER-зарезервированное конфликтное слово! меняем на ORDERS.
@Table(name = "orders")
public class Order {
    //первичный ключ(айдишник)таблицы в БД.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //поле, отвечающее за номер заказа.
    private String number;

//Установление связи с сущностями Product и Person(к заказу будут привязываться товар и пользователь, что его совершил):
    //1. устанавливаем связь с сущностью Product.
    //связь "многие ко одному".
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Product product;

    //2. устанавливаем связь с сущностью Person.
    //связь "многие ко одному".
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Person person;
//------------------------------------.

    //укажем количество товаров в заказе.

            //целочисленный тип данных.
    private int count;
            //вещественный тип данных.
    private float price;
            //встроенный класс для дат java.time.
    private LocalDateTime dateTime;

    //объект перечисления
            //"перечисление" из package enumm.
    private Status status;

    //метод инициализации даты(данный метод будет срабатывать при инициализации(создании) объекта класса Order).
    //дата и время будут заполняться засчёт этого метода.
    @PrePersist
    private void init(){
                   //встроенный класс для дат java.time.
        dateTime = LocalDateTime.now();
    }

//constructor     (убран из конструктора localDateTime, так как есть спец. метод по заполнению даты, времени init)
    public Order(String number, Product product, Person person, int count, float price, Status status) {
        this.number = number;
        this.product = product;
        this.person = person;
        this.count = count;
        this.price = price;
        this.status = status;
    }

//constructor пустой
    public Order() {
    }

//getter&setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus() {
        this.status = status;
    }


}
