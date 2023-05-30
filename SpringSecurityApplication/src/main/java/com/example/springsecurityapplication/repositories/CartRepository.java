package com.example.springsecurityapplication.repositories;


import com.example.springsecurityapplication.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//Указываем, как и в сервисных слоях, что тут будут происходит транзакции(типа чтение readonly).
@Transactional
//указываем, что это репозиторий.
@Repository                     //наследуем Jpa репозиторий.
                                                     //указываем, что работаем с сущностью Cart.
                                                            //тип данных айдишника int.
public interface CartRepository extends JpaRepository<Cart, Integer> {

    //1. Метод нахождения пользователя по айдишнику. То есть сформировываем корзину Cart.java на основе айдишника пользователя.
        //Метод берёт айдишник, идёт в сущность Cart.java, ищет какие товары приязаны к данному id  пользователя и возвращает лист этих товаров.       //на вход принимает айдишник.
    List<Cart> findByPersonId(int id);

    //2. Метод не будет ничего возвращать. Он понадобится для удаления из корзины product_cart в Cart.java.

    //модификатор, который изменяет(будет удаление) сущность в рамках БД. То есть не просто чтение, как в транзакциях(где readonly), а будет модификация данных БД.
    @Modifying
                    //удалим запись из корзины, где айдишник товара равняется первому принимаемому параметру в рамках данного метода
                    //также, это SQL запрос в БД на удаление товара по его id из корзины.
    @Query(value = "delete from product_cart where product_id=?1", nativeQuery = true)
                              //на вход принимает айдишник.
    void deleteCartByProductId(int id);



}
