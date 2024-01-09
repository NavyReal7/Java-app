package com.example.springsecurityapplication.repositories;

import com.example.springsecurityapplication.models.Order;
import com.example.springsecurityapplication.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//аннотация того, что это репозиторий.
@Repository                      //наследуем          //уазываем, что работаем с моделью Order
                                                              //в качестве первичного ключа тип данных int.
public interface OrderRepository extends JpaRepository<Order, Integer> {

    //метод, получающий лист(список) заказов по объекту person.
    //(будем получать список заказов конкретного пользователя).
    List<Order> findByPerson(Person person);
}
