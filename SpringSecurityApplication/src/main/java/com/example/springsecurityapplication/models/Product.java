package com.example.springsecurityapplication.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity //указание именно модели, а не обычного класса
//модель для взаимодействия с товарами
public class Product {

//поля класса(модели)

    //1.создание первичного ключа(айдишник) данной модели
    @Id
    //поле модели
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //2.наименование товара
    //создание определенных настроек для поля в сущности модели БД
                           //поле не пустое   //тип данных "текст"       //уникальность значений true
    @Column(name = "title", nullable = false, columnDefinition = "text", unique = true)
    //валидация поля класса
    @NotEmpty(message = "наименование товара не может быть пустым")
    private String title;

    //3.описание товара
    //создание определенных настроек для поля в сущности модели БД
                                  //поле не пустое  //тип данных "текст"
    @Column(name = "description", nullable = false, columnDefinition = "text")
    //валидация поля класса
    @NotEmpty(message = "описание не может быть пустым")
    private String description;

    //4.описание цены
    //создание определенных настроек для поля в сущности модели БД
                            //поле не пустое
    @Column(name = "price", nullable = false)
    //валидация минимального значения
    @Min(value = 1, message = "цена товара не может быть отрицательной или нулевой")
            //вещественный тип данных float
    private float price;

    //5.описание склада
    //создание определенных настроек для поля в сущности модели БД
                                //поле не пустое
    @Column(name = "warehouse", nullable = false)
    //валидация поля класса
    @NotEmpty(message = "склад по нахождению товара не может быть пустым")
    private String warehouse;

    //6.размещение товара продавцом на площадке
    //создание определенных настроек для поля в сущности модели БД
                              //поле не пустое
    @Column(name = "seller", nullable = false)
    //валидация поля класса
    @NotEmpty(message = "информация о продавце товара не может быть пустой")
    private String seller;


//связь двух таблиц(Product и Category)------------------------------------------СВЯЗЬ
    //аннотация ManyToOne связь(много товаров может относится к одной категории)
    @ManyToOne(optional = false)
    //объект категории
    private Category category;

    //7.хранение даты и времени при создании нового товара(объект класса LocalDateTime)
    private LocalDateTime dateTime;

    //фото может быть много, поэтому нужен лист(List) названий(наименований) этих фотографий(файлов), которые будут относиться к объекту класса Product(по-простому, "5 фото в листе к одному товару")----ВАЖНО--------------СВЯЗЬ
    //аннотация OneToMany связь(один продукт относится ко многим фото)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product") //виды связей HYBERNATE?
    private List<Image> imageList = new ArrayList<>(); //пустой constructor из модели Image нужен тут.

    //метод, который вызывается в нужном месте и позволяет добавить фото в лист(List) Image к текущему товару
    public void addImageToProduct(Image image){
        //обращаемся к объекту Image, где идёт привязка к текущему продукту(экземпляру класса Product) и далее фото идёт в List Image
        image.setProduct(this);
        imageList.add(image);
    }

    //метод, позволющий инициализировать дату и время
    //данный метод будет заполнять поле даты и времени при создании объекта класса
    @PrePersist
    private void init(){
        dateTime = LocalDateTime.now(); //с помощью синтаксиса now присвоим дату и время на момент создания экземпляра dateTime
    }


    //PRODUCT ID для связи(hibernate) в Cart.
//Сязь "многие ко многим(Many to Many) ДЛЯ КОРЗИНЫ(product_cart в Cart.java)!
    @ManyToMany()
    //для связи "Many To Many" создается 3-я промежут. эта таблица(product_cart) с двумя колонками.
                                      //это поле 3й таблицы  относится к текущей модели Product.java
    @JoinTable(name = "product_cart", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "person_id"))
    private List<Person> personList;



//СВЯЗЬ С ЗАКАЗОМ(с Order.java)-----------------!--------------------
    //указываем, что будем хранить лист(список) заказов, так как один пользователь может совершать несколько заказов.
    //свзь "один ко многим".
              //связь будет с полем person
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<Order> orderList;
//вот так настроили связь с точки зрения модели Person---------------.





//constructor
    public Product(String title, String description, float price, String warehouse, String seller, Category category, LocalDateTime dateTime, List<Image> imageList) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.warehouse = warehouse;
        this.seller = seller;
        this.category = category;
        this.dateTime = dateTime;
        this.imageList = imageList;
    }

//пустой constructor
    public Product() {
    }

//getter&setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }


}
