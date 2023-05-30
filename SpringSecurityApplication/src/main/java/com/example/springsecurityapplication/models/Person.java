package com.example.springsecurityapplication.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Person")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Логин не может быть пустым")
    @Size(min = 5, max = 100, message = "Логин должен быть от 5 до 100 символов")
    @Column(name = "login")
    private String login;

    @NotEmpty(message = "Пароль не может быть пустым")
    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;


    //PERSON ID для связи(hibernate) в Cart.
    //Связь "многие ко многим(Many to Many) ДЛЯ КОРЗИНЫ(product_cart в Cart.java)!(скопировано из Product.java)
    @ManyToMany()
    //для связи "Many To Many" создается 3-я промежут. эта таблица(product_cart) с двумя колонками.
                                      //это поле 3й таблицы относится к текущей модели Person.java.
    @JoinTable(name = "product_cart", joinColumns = @JoinColumn(name = "person_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> productList;


//СВЯЗЬ С ЗАКАЗОМ(с Order.java)-----------------!--------------------
    //указываем, что будем хранить лист(список) заказов, так как один пользователь может совершать несколько заказов.
    //свзь "один ко многим".
              //связь будет с полем person
    @OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
    private List<Order> orderList;
//вот так настроили связь с точки зрения модели Person---------------.



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id && Objects.equals(login, person.login) && Objects.equals(password, person.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
