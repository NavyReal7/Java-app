package com.example.springsecurityapplication.repositories;

import com.example.springsecurityapplication.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

//РЕПОЗИТОРИЙ ПОД ТОВАРЫ

//интерфейс становится репозиторием
@Repository                        //наследует          //модель Product и первичный ключ с тип данных int
public interface ProductRepository extends JpaRepository<Product, Integer> {
                                                        //сущность Product БД



//Пишем порядок(6 МЕТОДОВ) для параметров из формы в шаблоне index.html для запросов в БД "DBS" тут. Эти 6 методов будут обрабатываться в ProductController:

//1. Метод поиска всех товаров по части наименования(поиск по одному ключевому слову!) вне зависимости от регистрации пользователей
    List<Product> findByTitleContainingIgnoreCase(String name);

//2. Метод поиска по наименованию товара и фильтрации по диапазону цены:
                                                       //title, это значение первого поля(наименование) формы в index.html
    //запрос в БД                               //нижний регистр.            //нижний регистр.            //нижний регистр.
                                                               //title в центре.           //в начале.                //в конце.
                 //диапазон цены.
    @Query(value = "select * from product where (lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1') and (price >= ?2 and price <= ?3)", nativeQuery = true)              //1
    List<Product> findByTitleAndPriceGreaterThanEqualAndPriceLessThanEqual(String title, float ot, float Do);

//3. Метод поиска по наименованию товара и фильтрации по диапазону, и сортировке по возрастанию цены:
    @Query(value = "select * from product where (lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1') and (price >= ?2 and price <= ?3) order by price", nativeQuery = true)
    //команду Asc в запрос SQL ставить не нужно, так как она идёт по умолчанию.
                                        //Asc отвечает за возрастание.
    List<Product> findByTitleOrderByPriceAsc(String title, float ot, float Do);

//4. Метод поиска по наименованию товара и фильтрации по диапазону и сортировке по убыванию цены:
    @Query(value = "select * from product where (lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1') and (price >= ?2 and price <= ?3) order by price desc", nativeQuery = true)
                                                             //команду desc в запрос SQL ставить нужно.
                                         //desc отвечает за убывание.
    List<Product> findByTitleOrderByPriceDesc(String title, float ot, float Do);

//5. Метод поиска по наименованию товара и фильтрации по диапазону, и сортировке по возрастанию цены, а ткаже фильтрации по   категории товара:                              //category_id значение из таблицы product.
                                                              //4й параметр в методе.
    @Query(value = "select * from product where category_id = ?4 and(lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1') and (price >= ?2 and price <= ?3) order by price", nativeQuery = true)
    //команду Asc в запрос SQL ставить не нужно, так как она идёт по умолчанию
                                                    //Asc отвечает за возрастание.         //4й параметр в методе=айдишник категорий
    List<Product> findByTitleAndCategoryOrderByPriceAsc(String title, float ot, float Do, int category);

//6. Метод поиска по наименованию товара и фильтрации по диапазону, и сортировке по убыванию цены, а ткаже фильтрации по   категории товара:                              //category_id значение из таблицы product.
                                                              //4й параметр в методе.
    @Query(value = "select * from product where category_id = ?4 and(lower(title) LIKE %?1%) or (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1') and (price >= ?2 and price <= ?3) order by price desc", nativeQuery = true)
                                                                                //команду desc в запрос SQL ставить нужно
                                                    //desc отвечает за убывание.            //4й параметр в методе=айдишник категорий
    List<Product> findByTitleAndCategoryOrderByPriceDesc(String title, float ot, float Do, int category);

}

//В дальнейшем, эти методы идут в обработку на контроллеры через созданные методы в ProductService(31 минута 22 урока), но пока пойдут напрямую в контроллер.
