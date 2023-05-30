package com.example.springsecurityapplication.controllers;

import com.example.springsecurityapplication.models.*;
import com.example.springsecurityapplication.repositories.CategoryRepository;
import com.example.springsecurityapplication.repositories.OrderRepository;
import com.example.springsecurityapplication.repositories.PersonRepository;
import com.example.springsecurityapplication.security.PersonDetails;
import com.example.springsecurityapplication.services.OrderService;
import com.example.springsecurityapplication.services.PersonService;
import com.example.springsecurityapplication.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class AdminController {

    //поле класса(уже контроллера), где обозначен объект класса Service
    private final ProductService productService;

    //внедрим путь под хранение фото
    //создадим поле контроллера(а уже не поле класса)
    @Value("${upload.path}")//внедрим значение пути папка для фото, которое лежит в application.properties
    private String uploadPath;

    //внедрим поле под ссылку на CategoryRepository
    private final CategoryRepository categoryRepository;


    //-----------------------------------------------------------------------------------------------------------------
//Внедряем PersonRepository.java в контроллер.
    private final PersonRepository personRepository;

    private final PersonService personService;

    //Внедряем  в контроллер 30.05.23.
    private final OrderService orderService;



//-----------------------------------------------------------------------------------------------------------------
//Внедряем OrderRepository.java в контроллер.
private final OrderRepository orderRepository;



//constructor
    public AdminController(ProductService productService, CategoryRepository categoryRepository, PersonService personService, PersonRepository personRepository, PersonService personService1, OrderService orderService, OrderRepository orderRepository) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
        //добавлен
        this.personRepository = personRepository;
        this.personService = personService;
        //добавлено в конструктор 30.05.23
        this.orderService = orderService;
        //добавлено в конструктор вместе с с добавлением метода вверху из OrderRepository.
        this.orderRepository = orderRepository;
    }

    //ссылка на добавление товара админом в admin.html
    @GetMapping("admin/product/add")
    public String addProduct(Model model){       //объект товара будет привязан к форме
        model.addAttribute("product", new Product());
        //положен объект категории
        model.addAttribute("category", categoryRepository.findAll()); //правильный ЛИСТ?????????????????
        return "product/addProduct";
    }

    //метод добавления фото
                             //принимаем объект модели Product и под хранение создается новый экземпляр класса Product
                                                       //валидация значений  и !сюда кладутся ошибки после валидации
                                                                                                           //принятие фото с input на контроллер
    @PostMapping("/admin/product/add")
    public String addProduct(@ModelAttribute("product")@Valid Product product, BindingResult bindingResult, @RequestParam("file_1")MultipartFile file_1, @RequestParam("file_2")MultipartFile file_2, @RequestParam("file_3")MultipartFile file_3, @RequestParam("file_4")MultipartFile file_4, @RequestParam("file_5")MultipartFile file_5, @RequestParam("category") int category, Model model)throws IOException {
                //доп. категория            //"ВЫБРОС" эксепшн "для различных случаев с файлами"

        Category category_db = categoryRepository.findById(category).orElseThrow();
        //sout вызов в панель
        System.out.println(category_db.getName());
        //проверка ошибок
        if(bindingResult.hasErrors()){
            //если есть  ошибки то: 1.обратно вернём список категорий("положим в модель")
            model.addAttribute("category", categoryRepository.findAll());
            //2.вернём  начальный шаблон добавления товара
            return "product/addProduct";
        }
//1.фото
        //проверка на пустые фото(файлы)-----------------------------------------------------
        //если первый файл не равен пустому значению
        if(file_1 != null){
            //преобразуем ссылку на файл в объек файла
            File uploadDir = new File(uploadPath);
            //далее проверяем существует ли директория
            if(!uploadDir.exists()){
                //если не существует, то создаём
                uploadDir.mkdir();
            }
            //чтобы файлы не имели одинакового наименования генерерируем уникальный UID
            String uuidFile = UUID.randomUUID().toString();
            //сформируем название файла, которое состоит из UID и наименования файла
            String resultFileName = uuidFile + "." + file_1.getOriginalFilename();
            //отправляем файл с созданным названием по созданной или указанной директроии
                                                //папка файлов
            file_1.transferTo(new File(uploadPath + "/" + resultFileName));
            //привязка фото к товау---------------------------------------------------------("связка фото+товар")
            //создаём объект image класса(модели) Image
            Image image = new Image();
            //сама привязка фото к товару
            image.setProduct(product);
            //после уже указываем наименование фото, привязанное к товару
            image.setFileName(resultFileName);
            //далее это добавляем фото, которое привязано к товару, в лист товаров
                    //метод из модели Product, который добавляет такое фото в лист(список) ImageList
            product.addImageToProduct(image);
        }
 //2.фото
        //проверка на пустые фото(файлы)-----------------------------------------------------
        //если первый файл не равен пустому значению
        if(file_2 != null){
            //преобразуем ссылку на файл в объек файла
            File uploadDir = new File(uploadPath);
            //далее проверяем существует ли директория
            if(!uploadDir.exists()){
                //если не существует, то создаём
                uploadDir.mkdir();
            }
            //чтобы файлы не имели одинакового наименования генерерируем уникальный UID
            String uuidFile = UUID.randomUUID().toString();
            //сформируем название файла, которое состоит из UID и наименования файла
            String resultFileName = uuidFile + "." + file_2.getOriginalFilename();
            //отправляем файл с созданным названием по созданной или указанной директроии
            //папка файлов
            file_2.transferTo(new File(uploadPath + "/" + resultFileName));
            //привязка фото к товау---------------------------------------------------------("связка фото+товар")
            //создаём объект image класса(модели) Image
            Image image = new Image();
            //сама привязка фото к товару
            image.setProduct(product);
            //после уже указываем наименование фото, привязанное к товару
            image.setFileName(resultFileName);
            //далее это добавляем фото, которое привязано к товару, в лист товаров
            //метод из модели Product, который добавляет такое фото в лист(список) ImageList
            product.addImageToProduct(image);
        }

//3.фото
        //проверка на пустые фото(файлы)-----------------------------------------------------
        //если первый файл не равен пустому значению
        if(file_3 != null){
            //преобразуем ссылку на файл в объек файла
            File uploadDir = new File(uploadPath);
            //далее проверяем существует ли директория
            if(!uploadDir.exists()){
                //если не существует, то создаём
                uploadDir.mkdir();
            }
            //чтобы файлы не имели одинакового наименования генерерируем уникальный UID
            String uuidFile = UUID.randomUUID().toString();
            //сформируем название файла, которое состоит из UID и наименования файла
            String resultFileName = uuidFile + "." + file_3.getOriginalFilename();
            //отправляем файл с созданным названием по созданной или указанной директроии
            //папка файлов
            file_3.transferTo(new File(uploadPath + "/" + resultFileName));
            //привязка фото к товау---------------------------------------------------------("связка фото+товар")
            //создаём объект image класса(модели) Image
            Image image = new Image();
            //сама привязка фото к товару
            image.setProduct(product);
            //после уже указываем наименование фото, привязанное к товару
            image.setFileName(resultFileName);
            //далее это добавляем фото, которое привязано к товару, в лист товаров
            //метод из модели Product, который добавляет такое фото в лист(список) ImageList
            product.addImageToProduct(image);
        }

//4.фото
        //проверка на пустые фото(файлы)-----------------------------------------------------
        //если первый файл не равен пустому значению
        if(file_4 != null){
            //преобразуем ссылку на файл в объек файла
            File uploadDir = new File(uploadPath);
            //далее проверяем существует ли директория
            if(!uploadDir.exists()){
                //если не существует, то создаём
                uploadDir.mkdir();
            }
            //чтобы файлы не имели одинакового наименования генерерируем уникальный UID
            String uuidFile = UUID.randomUUID().toString();
            //сформируем название файла, которое состоит из UID и наименования файла
            String resultFileName = uuidFile + "." + file_4.getOriginalFilename();
            //отправляем файл с созданным названием по созданной или указанной директроии
            //папка файлов
            file_4.transferTo(new File(uploadPath + "/" + resultFileName));
            //привязка фото к товау---------------------------------------------------------("связка фото+товар")
            //создаём объект image класса(модели) Image
            Image image = new Image();
            //сама привязка фото к товару
            image.setProduct(product);
            //после уже указываем наименование фото, привязанное к товару
            image.setFileName(resultFileName);
            //далее это добавляем фото, которое привязано к товару, в лист товаров
            //метод из модели Product, который добавляет такое фото в лист(список) ImageList
            product.addImageToProduct(image);
        }

//5.фото
        //проверка на пустые фото(файлы)-----------------------------------------------------
        //если первый файл не равен пустому значению
        if(file_5 != null){
            //преобразуем ссылку на файл в объек файла
            File uploadDir = new File(uploadPath);
            //далее проверяем существует ли директория
            if(!uploadDir.exists()){
                //если не существует, то создаём
                uploadDir.mkdir();
            }
            //чтобы файлы не имели одинакового наименования генерерируем уникальный UID
            String uuidFile = UUID.randomUUID().toString();
            //сформируем название файла, которое состоит из UID и наименования файла
            String resultFileName = uuidFile + "." + file_5.getOriginalFilename();
            //отправляем файл с созданным названием по созданной или указанной директроии
            //папка файлов
            file_5.transferTo(new File(uploadPath + "/" + resultFileName));
            //привязка фото к товау---------------------------------------------------------("связка фото+товар")
            //создаём объект image класса(модели) Image.
            Image image = new Image();
            //сама привязка фото к товару.
            image.setProduct(product);
            //после уже указываем наименование фото, привязанное к товару.
            image.setFileName(resultFileName);
            //далее это добавляем фото, которое привязано к товару, в лист товаров.
            //метод из модели Product, который добавляет такое фото в лист(список) ImageList.
            product.addImageToProduct(image);
        }
                       //вызываем метод и передаём в него объект класса Product и объект категории.
        productService.saveProduct(product, category_db);
        return "redirect:/admin";
    }


     //контроллер главной страницы администратора.
    @GetMapping("/admin")
    public String admin(Model model){
        //вывод всех существующих продуктов.                      вывод всех продуктов из листа-списка List <Product> в                                                            объект model модели(класса) Model.
        model.addAttribute("products", productService.getAllProduct());
        return "admin";
    }

    //метод удаления товара(href в admin.html)
                                   //принимаем динамическую переменную id.
    @GetMapping("admin/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id){
                       //вызываем метод, который удаляет товар по id.
        productService.deleteProduct(id);
        //редирект на главную страницу админа.
        return "redirect:/admin";
    }

    //метод редактирования товара(href в admin.html)
    @GetMapping("admin/product/edit/{id}")
                             //инициализация класса и объекта модели
    public String editProduct(Model model, @PathVariable("id") int id){

        //найдем товар по айдишнику, положим его в объект модели и далее привяжем к форме и благодаря этому заполним все поля со значениями данного объекта.
        //кладём в объект модели два атрибута.                   вызываем метод, поместив туда айдишник.
        model.addAttribute("product", productService.getProductId(id));

        //список категорий
        model.addAttribute("category", categoryRepository.findAll());
        return "product/editProduct";
    }


//    //добавление товара админом
//    @GetMapping("admin/product/add")       //добавлен объект класса модели
//    public String addProduct(Model model){
//        //привязываем к форме объект товара
//                          //объект товара кладём в модель
//        model.addAttribute("product", new Product());
//                          //объект категории кладём в модель
//        model.addAttribute("category");
//
//    }

    //обработка валидации формы редактирования товара
    @PostMapping("admin/product/edit/{id}")
                              //принимаем объект product, валидируем его и если есть ошибки кладём их в объект bindingResult класса BindingResult                                                   и принимаем айдишник
    public String editProduct(@ModelAttribute("product")@Valid Product product, BindingResult bindingResult, @PathVariable("id") int id, Model model){ //добавляем объект model и класс Model модели для списка категории
        //проверка на наличие ошибок
        //если есть возвращаем шаблон редактирования
        if(bindingResult.hasErrors()){
            //передаём на шаблон список категорий после того, как прошла валидация(@Valid)
            model.addAttribute("category", categoryRepository.findAll());
            return "product/editProduct";
        }
        //если нет ошибок, то с помощью метода обновления передаем айдишник и объект товара.
        productService.updateProduct(id, product);
        return "redirect:/admin";
    }










//Метод вывода заказов для администратора.---------------------------------------------мои методы добавленные-----------

    //1. Метод обработки кнопки всех заказов конкретного пользователя.-------------------------------------1.
    //public-вариант метода, который возвращает данные, в отличие от void.
    //создаём класс модели и объект класса
    //get запрос
    @GetMapping("/orderAdmin")
    public String orderUser(Model model){
        //извлекаем объект аутентификации пользователя из сессии.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //с помощью объекта personDetails получим информацию в виде объекта personDetails класса PersonDetails, который даёт полную информацию об объекте модели Person.java (по сути: из объекта аутентификации взяли объект модели Person.java. Объект аутентификации преобразовали в объект personDetails).
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        //получеам лист(список) всех заказов конкретного пользователя.
        //к листу(списку) товаров применяется из репозитория заказа метод, позволяющий получить лист(список) всех заказов по объекту person класса Person, где объект person берём из метода personDetails.
        List<Order> orderList = orderRepository.findAll();

        //таким образом, получим лист(список) всех заказов по конкретному пользователю.

        //далее этот лист(список) положим в модель(в объект model класса Model), чтобы потом к ней обратиться.
        model.addAttribute("orders", orderList);

        //далее возвращаем шаблон orders в папке user templates.
        return "/admin/ordersAdmin";
    }


    //метод редактирования(смены) статуса у заказа(30.05.23)
    @PostMapping("/orderAdmin")
    //принимаем объект product, валидируем его и если есть ошибки кладём их в объект bindingResult класса BindingResult                                                   и принимаем айдишник
    public String orderUser(@ModelAttribute("orders")@Valid Order order, BindingResult bindingResult, @PathVariable("id") int id, Model model){ //добавляем объект model и класс Model модели для списка категории
        //проверка на наличие ошибок
        //если есть возвращаем шаблон редактирования
        if(bindingResult.hasErrors()){
            //передаём на шаблон список категорий после того, как прошла валидация(@Valid)
            model.addAttribute("orders", categoryRepository.findAll());
            return "/admin/ordersAdmin";
        }
        //если нет ошибок, то с помощью метода обновления передаем айдишник и объект товара.
        orderService.updateStatusOrder(id, order );
        return "/admin/ordersAdmin";
    }




    //2. метод вывода пользователей у админа.-------------------------------------------------------2.
    @GetMapping("listPersonAdmin")
    public String adminPerson(Model model){
        //вывод всех существующих продуктов                       вывод всех продуктов из листа-списка List <Person> в                                                            объект model модели(класса) Model.
        model.addAttribute("persons", personRepository.findAll());
        return "/admin/listPersonAdmin";
    }

   //метод редактирования(смены) роли у пользователя(30.05.23)
    @PostMapping("listPersonAdmin")
    //принимаем объект product, валидируем его и если есть ошибки кладём их в объект bindingResult класса BindingResult                                                   и принимаем айдишник
    public String adminPerson(@ModelAttribute("persons")@Valid Person person, BindingResult bindingResult, @PathVariable("id") int id, Model model){ //добавляем объект model и класс Model модели для списка категории
        //проверка на наличие ошибок
        //если есть возвращаем шаблон редактирования
        if(bindingResult.hasErrors()){
            //передаём на шаблон список категорий после того, как прошла валидация(@Valid)
            model.addAttribute("persons", personRepository.findAll());
            return "/admin/listPersonAdmin";
        }
        //если нет ошибок, то с помощью метода обновления передаем айдишник и объект товара.
        personService.updatePerson(id, person);
        return "/admin/listPersonAdmin";
    }











//пытался добавить метод в котроллер для изменения роли пользователя.---(МОЯ ПОПЫТКА!)


//-------------------- Метод зменения роли пользователя.


//    @GetMapping("admin/product/edit/{id}")
////инициализация класса и объекта модели
//    public String editPersonRole(Model model, @PathVariable("id") int id){
//
//        model.addAttribute("persons", personService.getPersonId(id);
//
//        model.addAttribute("persons", personRepository.findAll());
//        return "product/editProduct";
//    }

//------

//    @PostMapping("admin/product/edit/{id}")
//    //принимаем объект product, валидируем его и если есть ошибки кладём их в объект bindingResult класса BindingResult                                                   и принимаем айдишник
//    public String editPersonRole(@ModelAttribute("persons")@Valid Person person, BindingResult bindingResult, @PathVariable("id") int id, Model model){ //добавляем объект model и класс Model модели для списка категории
//
//        productService.updatePerson(id, person);
//        return "redirect:/admin";
//    }


}
