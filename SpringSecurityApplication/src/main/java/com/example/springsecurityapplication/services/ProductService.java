package com.example.springsecurityapplication.services;


import com.example.springsecurityapplication.models.Category;
import com.example.springsecurityapplication.models.Product;
import com.example.springsecurityapplication.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//SERVICE ДЛЯ ТОВАРОВ(тут располагаются методы манипулирования товаром)----------------------------------------------

//данный класс будет сервисом(service)
@Service
//транзакция "только для чтения"(просмотра)
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

//constructor репозитория
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

//методы

    //1.метод, который вернёт List(лист, весь список) товаров
    public List<Product> getAllProduct(){
               //обращаемся    и вызываем метод, который вернёт все продукты
        return productRepository.findAll();
    }

    //2.метод позволит получить один товар по id. метод нужен для редактирования и удаления товара, также для открытия страницы полной информации о товаре
    public Product getProductId(int id){
                //лист~список               обращаемся        и вызываем метод, который ведёт поиск товаров по id
        Optional<Product> optionalProduct = productRepository.findById(id);
        //если товар найден по id, он возвращается, а если нет, то null
        return optionalProduct.orElse(null);
    }

//методы для хранения, обновления и удаления товара---------(A,B,C)--------------

    //A.метод для хранения товара
    @Transactional          //объект классов Product и Category
    public void saveProduct(Product product, Category category){

        //ПОМОЩЬ24.05.23-----------------------------------------------!!!ДАТА редактирования
        product.setDateTime(LocalDateTime.now());

        //к товару указываем нужную категорию
        product.setCategory(category);
        //и сохраняем объект товара
        productRepository.save(product);
    }

    //B.метод для обновления товара
    @Transactional                                  //объект класса Product
    public void updateProduct(int id, Product product){
        //устанавливаем айдишник продукту для понимаия Srping'ом Data JPA
        product.setId(id);
        //обновляем информацию о товаре, на основе объекта product класса Product, пришедшего с формы обновления данных
                          //метод сохранения изменений товара
        productRepository.save(product);
    }

    @Transactional
    //C.метод для удаления товара
    public void deleteProduct(int id){
                       //метод удаления товара по id
     productRepository.deleteById(id);
    }


}
