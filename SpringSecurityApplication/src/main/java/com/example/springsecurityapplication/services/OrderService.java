package com.example.springsecurityapplication.services;

import com.example.springsecurityapplication.models.Order;
import com.example.springsecurityapplication.models.Product;
import com.example.springsecurityapplication.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


//SERVICE ДЛЯ ЗАКАЗО(тут располагаются методы манипулирования заказом)----------------------------------------------

//данный класс будет сервисом(service)
@Service
//транзакция "только для чтения"(просмотра)
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;


//constructor репозитория
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

//методы.--------------------------------------------------------------------

    //метод для обновления статуса заказа(30.05.23)
    @Transactional                  //объект класса Order
    public void updateOrder(int id, Order order){
        //устанавливаем айдишник заказу для понимания Srping'ом Data JPA
        order.setId(id);
        //обновляем информацию о товаре, на основе объекта product класса Product, пришедшего с формы обновления данных
        //метод сохранения изменений товара
        orderRepository.save(order);
    }

    @Transactional                  //объект класса Order
    public void updateStatusOrder(int id, Order order){
        //IDE ПРЕДЛОЖИЛО УДАЛИТЬ ОДИН ПАРМАМЕТР ДЛЯ КООРЕКТНОГО СОЗДАНИЯ МЕТОДА!!! 30.05.23
        order.setStatus();
        //обновляем информацию о товаре, на основе объекта product класса Product, пришедшего с формы обновления данных
        //метод сохранения изменений товара
        orderRepository.save(order);
    }


}
