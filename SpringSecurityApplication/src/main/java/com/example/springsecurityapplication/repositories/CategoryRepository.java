package com.example.springsecurityapplication.repositories;

import com.example.springsecurityapplication.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//ФУНКЦИОНАЛ ПО РАБОТЕ С БАЗОЙ ДАННЫХ-------------------------------------------------------------------------------

//интерфейс является репозиторием
@Repository               //наследуем          //работа с моделью категории и с типом данных Int
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    //метод будет возвращать категорию товаров по её наименованию, которое будет использовано для нахождения нужной категории в сущности(hybernate)базы данных
    com.example.springsecurityapplication.models.Category findByName(String name);
}
