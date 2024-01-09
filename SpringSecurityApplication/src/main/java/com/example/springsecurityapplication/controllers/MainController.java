package com.example.springsecurityapplication.controllers;

import com.example.springsecurityapplication.enumm.Status;
import com.example.springsecurityapplication.models.Cart;
import com.example.springsecurityapplication.models.Order;
import com.example.springsecurityapplication.models.Person;
import com.example.springsecurityapplication.models.Product;
import com.example.springsecurityapplication.repositories.CartRepository;
import com.example.springsecurityapplication.repositories.OrderRepository;
import com.example.springsecurityapplication.repositories.ProductRepository;
import com.example.springsecurityapplication.security.PersonDetails;
import com.example.springsecurityapplication.services.PersonService;
import com.example.springsecurityapplication.services.ProductService;
import com.example.springsecurityapplication.util.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class MainController {

    //добавлено вместе с добавлением метода внизу из ProductController.
    private final ProductRepository productRepository;

    private final PersonValidator personValidator;
    private final PersonService personService;

//внедряем ProductService вместе с скопированными и вставленными сюда методами ниже
    private final ProductService productService; //внедряем его конструктор ниже


//внедряем корзину
    private final CartRepository cartRepository;


//Внедряем OrderRepository.java в контролеер.
    private final OrderRepository orderRepository;




    public MainController(ProductRepository productRepository, PersonValidator personValidator, PersonService personService, ProductService productService, CartRepository cartRepository, OrderRepository orderRepository) {
        //добавлено в конструктор вместе с с добавлением метода внизу из ProductController.
        this.productRepository = productRepository;
        this.personValidator = personValidator;
        this.personService = personService;
        this.productService = productService;
        //добавили конструктор корзины.
        this.cartRepository = cartRepository;
        //добавлено в конструктор вместе с с добавлением метода вверху из OrderRepository.
        this.orderRepository = orderRepository;
    }

    @GetMapping("/person account")
    //метод index
    public String index(Model model){
        // Получаем объект аутентификации из сессии -> с помощью SpringContextHolder обращаемся к контексту и на нем вызываем метод аутентификации. Из сессии текущего пользователя получаем объект, который был положен в данную сессию после аутентификации пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //объект пользователя из объекта аутентификации, который лежит в сессии
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        //проверка на роль админа(на страницу админа) или пользователя(тогда на страницу index)
        //получим отдельно роль пользователя
        String role = personDetails.getPerson().getRole();
        //если роль пользователя равна ROLE_ADMIN, то переход на админ. страницу
        if(role.equals("ROLE_ADMIN")){
            return "redirect:/admin";
        }
//        System.out.println(personDetails.getPerson());
//        System.out.println("ID пользователя: " + personDetails.getPerson().getId());
//        System.out.println("Логин пользователя: " + personDetails.getPerson().getLogin());
//        System.out.println("Пароль пользователя: " + personDetails.getPerson().getPassword());
//        System.out.println(personDetails);

        //кладём в объект модели атрибут, чтобы к нему можно было обратиться на шаблоне.
        //в атрибуте:       ключ;      значение то, что вернёт(лист всех товаров) этот метод из ProductService.
        //т.е. лист товаров кладём в модель по ключу и вызываем метод...
        model.addAttribute("products", productService.getAllProduct());
        //..и к этому ключу возвращать будем
        return "/user/index";
       //шаблон index.html в директории user
    }

    //    @GetMapping("/registration")
//    public String registration(Model model){
//        model.addAttribute("person", new Person());
//        return "registration";
//    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("person") Person person){
        return "registration";
    }

    @PostMapping("/registration")
    public String resultRegistration(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors()){
            return "registration";
        }
        personService.register(person);
        return "redirect:/person account";
    }


    //скопированный метод из ProductController------------------------------------------------------------------------

    //метод, обрабатывающий нажатие на ссылку для вывода информации о товаре для любых пользователей
    //обработали нажатие на ссылку наименования конкретного товара
    @GetMapping("/person account/product/info/{id}")
    //с помощью этого получаем дин. переменную {id}
    //элемент товара, который найдём по id будем класть модель(некий контейнер) = в объект model класса Model
    public String infoProduct(@PathVariable("id") int id, Model model){
        //кладём в объект модели атрибут, чтобы к нему можно было обратиться на шаблоне.
        //в атрибуте:   ключ;      значение то, что вернёт конкретный товар по его id этот метод
        // из ProductService.
        //т.е. id конкретного товара кладём в модель  и вызываем метод по этому id...
        model.addAttribute("product", productService.getProductId(id));
        //..и к этому ключу возвращать будем
        return "/user/infoProduct"; //шаблон в папке user раздела templates
    }

//ВСТАВЛЕН КОПИЕЙ ИЗ ProductController.
//Метод для работы формы "МЕНЮ ФИЛЬТРАЦИИ ТОВАРОВ" на главной странице сайта шаблона "product" в папке "product" "templates"

    @PostMapping("/person account/product/search")  //принимаем все input'ы с формы в данный контроллер
    public String productSearch(@RequestParam("search") String search, @RequestParam("ot") String ot, @RequestParam("do") String Do, @RequestParam(value="price", required = false, defaultValue = "")String price, @RequestParam(value = "contract", required = false, defaultValue = "")String contract, Model model) {
        //значение "по дефолту"(умолчанию)если параметр не придёт.
        //обозначает, что функция кнопки radio может быть как выбрана, так и нет, то есть этот параметр может как прийти в данный метод, так и нет.

        //эти значения положим в объект model класса Model данного метода(класть значения в модель необходимо для того, что страница при перезагрузке очищается и для того, чтобы значения с формы оставались есть такое решение)
        model.addAttribute("products", productService.getAllProduct());
        //ключ      //метод списка всех продуктов из productService.
        //из product.html.
        //внизу
        //СЛОЖНАЯ ЛОГИКА ПРОВЕРОК:
        //проверяем пустоту двух полей input'ов из формы product.html из папки product templates.
        if (!ot.isEmpty() & !Do.isEmpty()) {
            //если не пустые, то далее проверяем значения кнопок radio сортировки цены на пустоту.
            if (!price.isEmpty()) { //значение из product.html, которое будет проверяться тут
                //если в radio сортировки цены выбрано это значение:по возрастанию цены.
                if (price.equals("sorted_by_ascending_price")) {
                    //тогда далее проверяем на пустоту кнопки radio категорий.
                    if (!contract.isEmpty()) {
                        //если выбрана эта категория то: вызываем напрямую(без ProductService(31мин) этот метод из ProductRepository, посылаемый в БД.
                        if (contract.equals("oscillators")) {
                            //в модель кладём атрибут search_product с значением!, полученным из метода productRepository, который сделает запрос в БД!  //ключ
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 1));                 //перевод в мал.буквы. //конвертация в float!
                            //а если выбрана эта категория то: вызываем напрямую(без ProductService(31мин) этот метод из ProductRepository, посылаемый в БД.
                        } else if (contract.equals("trend")) {
                            //в модель кладём атрибут search_product с значением!, полученным из метода productRepository, который сделает запрос в БД!  //ключ
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 2));                 //перевод в мал.буквы. //конвертация в float!
                            //а если выбрана эта категория то: вызываем напрямую(без ProductService(31мин) этот метод из ProductRepository, посылаемый в БД.
                        } else if (contract.equals("blackbox")) {
                            //в модель кладём атрибут search_product с значением!, полученным из метода productRepository, который сделает запрос в БД!   //ключ
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 3));                 //перевод в мал.буквы. //конвертация в float!
                        }
                        //если radio-категория всё же пустая, то сработает данный метод.
                    } else {                            //ключ
                        model.addAttribute("search_product", productRepository.findByTitleOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do)));
                    }
                    //если в radio сортировки цены выбрано это значение:по убыванию цены. И далее идёт тот же порядок действий, что и выше для возрастания.
                } else if (price.equals("sorted_by_descending_price")) {
                    if (!contract.isEmpty()) {

                        System.out.println(contract);

                        if (contract.equals("oscillators")) {
                            //ключ
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 1));
                        } else if (contract.equals("trend")) {
                            //ключ
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 2));

                        } else if (contract.equals("blackbox")) {
                            //ключ
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 3));
                        }
                    }else {                             //ключ
                        model.addAttribute("search_product", productRepository.findByTitleOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do)));
                    }
                }

                //если кнопки radio-сортировка цены и категория пусты, то сработает данный метод.
            } else {                           //ключ
                model.addAttribute("search_product", productRepository.findByTitleAndPriceGreaterThanEqualAndPriceLessThanEqual(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do)));
            }
            //если показатели, что вверху, диапазона цены "(!ot.isEmpty() & !Do.isEmpty())" пустые, то:
        }else{                             //ключ                                                            //search можно не переводить в нижний регистр
            model.addAttribute("search_product", productRepository.findByTitleContainingIgnoreCase(search));

        }



        model.addAttribute("value_search", search);
        //ключ
        model.addAttribute("value_price_ot", ot);
        //ключ
        model.addAttribute("value_price_do", Do);
        //возращаем эту же страницу, но с оставшимися значениями в ней при перезагрузке, благодаря объекту model, куда и лягут значения обратно в форму страницы product.html с помощью шаблонизатора thymeleaf и его метода "th:value".
        //return "/product/product";
        return "/user/index";

    }


//Метод обработки ссылки для нажатия и добавления товара в корзину.
    //get запрос
    @GetMapping("/cart/add/{id}")
    //получаем айдишник товара, который хотим добавть в корзину.
    public String addProductInCart(@PathVariable("id") int id, Model model){
    //Логика по добавлению товара в корзину:
        //получаем продукт("объект продукта") по id товара, добавленного в корзину.
        Product product = productService.getProductId(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //с помощью объекта personDetails получим информацию в виде объекта personDetails класса PersonDetails, который даёт полную информацию об объекте модели Person.java (по сути: из объекта аутентификации взяли объект модели Person.java. Объект аутентификации преобразовали в объект personDetails).
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        //извлечём айдишник пользователя из объекта personDetails(папка security) класса PersonDetails(или из объекта класса Person?).
        int id_person = personDetails.getPerson().getId();
        //формируем новую корзину(состоит из айдишника пользователя и товара, который хочет добавить)
        Cart cart = new Cart(id_person, product.getId());
        //обращаемся к объекту корзины cartRepository, что указали выше.
        //сохраняем корзину в БД.
        //вернём метод save.
        cartRepository.save(cart);
        //и редиректимся на главную страницу.
        return "redirect:/cart";
    }

    //обработаем редирект(return "redirect:/cart") выше.
    //get запрос по url
    @GetMapping("/cart")
                       //добавляем модель.
    public String cart(Model model){
        //извлекаем объект аутентификации пользователя из сессии.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //с помощью объекта personDetails получим информацию в виде объекта personDetails класса PersonDetails, который даёт полную информацию об объекте модели Person.java (по сути: из объекта аутентификации взяли объект модели Person.java. Объект аутентификации преобразовали в объект personDetails).
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        //извлечём айдишник пользователя из объекта personDetails(папка security) класса PersonDetails(или из объекта класса Person?).
        int id_person = personDetails.getPerson().getId();
                              //метод findByPersonId в СartRepository.java, возращающий лист корзины(все записи в корзине) по айдишнику пользователя.
        //'лист(список), что в корзине'.
        List<Cart> cartList = cartRepository.findByPersonId(id_person);

        //таким образом, ищем только ту корзину, которая относится к текущему аутентифицированному пользователю.

        //получим список товаров из корзины, чтобы в ней их можно было отобразить.
        //'лист(список) товаров'      пустой.
        List<Product> productList = new ArrayList<>();

        //цикл foreach, перебираем 'лист(список), что в корзине'.
        for (Cart cart: cartList) {
            //добавляем в ('лист(список) товаров' Arraylist(productList)) товары по id.
                            //метод getProductId в ProductRepository.java,дающий товар айдишнику.
                                                        //айдишник берётся(благодаря методу getProductId() в Cart.java)! из элемента cart, который перебирается в цикле for.
            productList.add(productService.getProductId(cart.getProductId()));
        }

        //Общая цена за все товары в корзине:
        //вещественный тип данных
              //счётсик.
        float price = 0;
        
        //цикл foreach.
        //перебираем          лист(список) тоавров, в котором лежат все товары корзины.
            //класс  объект.
        for (Product product: productList) {
            //в данную переменную price будем суммировать значения цены из метода get.Price.
                     //объект.
            //постфиксный энкримент из price.
            price += product.getPrice();
        }
        //далее суммированую цену товаров корзины кладём в объект класса model;
        model.addAttribute("price", price);




        //далее добавим лист(список) товаров в объект model класса Model.
        model.addAttribute("cart_product", productList);
        //и вернём шаблон корзины и папки user templates.
        return  "/user/cart";

    }

    //метод по удалению товара из корзины.
    //метод будет срабатывать при get запросе.
                                 //динамическая переменная id.
    @GetMapping("/cart/delete/{id}")
                                        //создадим клаас Model  и его объект model.
                                                     //получаем данные(id товара, который хотим удалить) из этой динамической перменной id.
    public String deleteProductFromCart(Model model, @PathVariable("id") int id){
    //логика метода по удалению товара из корзины:

        //извлекаем объект аутентификации пользователя из сессии.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //с помощью объекта personDetails получим информацию в виде объекта personDetails класса PersonDetails, который даёт полную информацию об объекте модели Person.java (по сути: из объекта аутентификации взяли объект модели Person.java. Объект аутентификации преобразовали в объект personDetails).
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        //извлечём айдишник пользователя из объекта personDetails(папка security) класса PersonDetails(или из объекта класса Person?).
        int id_person = personDetails.getPerson().getId();

        //метод findByPersonId в СartRepository.java, возращающий лист корзины(все записи в корзине) по айдишнику пользователя.
        //'лист(список), что в корзине'.
        List<Cart> cartList = cartRepository.findByPersonId(id_person);

        //таким образом, ищем только ту корзину, которая относится к текущему аутентифицированному пользователю.

        //получим список товаров из корзины, чтобы в ней их можно было отобразить.
        //'лист(список) товаров'      пустой.
        List<Product> productList = new ArrayList<>();

        //цикл foreach, перебираем 'лист(список), что в корзине'.
        for (Cart cart: cartList) {
            //добавляем в ('лист(список) товаров' Arraylist(productList)) товары по id.
            //метод getProductId в ProductRepository.java,дающий товар айдишнику.
            //айдишник берётся(благодаря методу getProductId() в Cart.java)! из элемента cart, который перебирается в цикле for.
            productList.add(productService.getProductId(cart.getProductId()));
        }
        //обращаемся к репозиторию и вызываем метод.
        cartRepository.deleteCartByProductId(id);
        //редирект на страницу корзины.
        return "redirect:/cart";
    }


   //Метод для добавления товаров из корзины в заказ(все товары, которые в корзине, будут оформляться в один заказ.)
    //get запрос
    @GetMapping("/order/create")
    public String order(){
        //извлекаем объект аутентификации пользователя из сессии.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //с помощью объекта personDetails получим информацию в виде объекта personDetails класса PersonDetails, который даёт полную информацию об объекте модели Person.java (по сути: из объекта аутентификации взяли объект модели Person.java. Объект аутентификации преобразовали в объект personDetails).
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        //извлечём айдишник пользователя из объекта personDetails(папка security) класса PersonDetails(или из объекта класса Person?).
//АЙДИШНИК(ID) САМОГО ПОЛЬЗОВАТЕЛЯ.
        int id_person = personDetails.getPerson().getId();
        //метод findByPersonId в СartRepository.java, возращающий лист корзины(все записи в корзине) по айдишнику пользователя.
        //'лист(список), что в корзине'.
        List<Cart> cartList = cartRepository.findByPersonId(id_person);

        //таким образом, ищем только ту корзину, которая относится к текущему аутентифицированному пользователю.

        //получим список товаров из корзины, чтобы в ней их можно было отобразить.
        //'лист(список) товаров'      пустой.
        List<Product> productList = new ArrayList<>();

        //цикл foreach, перебираем 'лист(список), что в корзине'.
        for (Cart cart: cartList) {
            //добавляем в ('лист(список) товаров' Arraylist(productList)) товары по id.
            //метод getProductId в ProductRepository.java,дающий товар айдишнику.
            //айдишник берётся(благодаря методу getProductId() в Cart.java)! из элемента cart, который перебирается в цикле for.
//ЛИСТ(СПИСОК) ТОВАРОВ В КОРЗИНЕ, КОТОРЫЙ ХОЧЕТ ОФРМИТЬ ПОЛЬЗОВАТЕЛЬ.
            productList.add(productService.getProductId(cart.getProductId()));
        }

        //Общая цена за все товары в корзине:
        //вещественный тип данных
        //счётсик.
        float price = 0;

        //цикл foreach.
        //перебираем          лист(список) тоавров, в котором лежат все товары корзины.
        //класс  объект.
        for (Product product: productList) {
            //в данную переменную price будем суммировать значения цены из метода get.Price.
            //объект.
            //постфиксный энкримент из price.
//ИТОГОВАЯ ЦЕНА.
            price += product.getPrice();
        }

        //Сгенерируем уникальный номер заказа.
        //(генерируем номер заказа с помощью UUID для уникальности файла=уникальная строка и эту строку будем использовать в качестве номера заказа. В корзине может быть не больше 5 товаров и для них создаётся один уникальный номер(uuid)).
               //uuid, это по сути уникальная строка.
        String uuid = UUID.randomUUID().toString();
        //перебор всех товаров, которые пользователь хочет оформить в заказе.
        //цикл перебора for, перебираются не более 5 товаров:
            //модель Product.
                    //переменная product, куда кладутся нужные товары из листа(списка) productList.
        for(Product product: productList){
            //создание нового заказа согласно конструктору из Order.java:
            Order newOrder = new Order(uuid, product, personDetails.getPerson(), 1, product.getPrice(), Status.Получен);
            //далее, для каждого из товаров генерируем запись в сущности Order БД.
            //потом, можем извлекать заказы конкретного пользователя по его айдишнику(id) и посмотреть какие заказы делал пользователь.

            //обращаемся к репозиторию OrderRepository, вызываем метод save.
                                //и на вход принимаем объек newOrder, который создали выше.
            orderRepository.save(newOrder);

            //Ну и раз уже, все товары, которые лежали в корзине сформированы в заказ, то корзину надо очистить.
            //вызываем репозиторий корзины и метод из неё, который удаляет корзину по айдишнику товара.
            cartRepository.deleteCartByProductId(product.getId());
        }
        //редирект на страницу заказы.
        return "redirect:/orders";
    }

    //Метод обработки кнопки всех заказов конкретного пользователя.
    //public-вариант метода, который возвращает данные, в отличие от void.
                           //создаём класс модели и объект класса
    //get запрос
    @GetMapping("/orders")
    public String orderUser(Model model){
        //извлекаем объект аутентификации пользователя из сессии.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //с помощью объекта personDetails получим информацию в виде объекта personDetails класса PersonDetails, который даёт полную информацию об объекте модели Person.java (по сути: из объекта аутентификации взяли объект модели Person.java. Объект аутентификации преобразовали в объект personDetails).
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        //получеам лист(список) всех заказов конкретного пользователя.
        //к листу(списку) товаров применяется из репозитория заказа метод, позволяющий получить лист(список) всех заказов по объекту person класса Person, где объект person берём из метода personDetails.
        List<Order>  orderList = orderRepository.findByPerson(personDetails.getPerson());

        //таким образом, получим лист(список) всех заказов по конкретному пользователю.

        //далее этот лист(список) положим в модель(в объект model класса Model), чтобы потом к ней обратиться.
        model.addAttribute("orders", orderList);

        //далее возвращаем шаблон orders в папке user templates.
        return "/user/orders";

    }








//    @GetMapping("/orderAdmin")
//    public String orderAdmin(Model model){
//        //извлекаем объект аутентификации пользователя из сессии.
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        //с помощью объекта personDetails получим информацию в виде объекта personDetails класса PersonDetails, который даёт полную информацию об объекте модели Person.java (по сути: из объекта аутентификации взяли объект модели Person.java. Объект аутентификации преобразовали в объект personDetails).
//        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
//
//        //получеам лист(список) всех заказов конкретного пользователя.
//        //к листу(списку) товаров применяется из репозитория заказа метод, позволяющий получить лист(список) всех заказов по объекту person класса Person, где объект person берём из метода personDetails.
//        List<Order> orderList = orderRepository.findByPerson(personDetails.getPerson());
//
//        //таким образом, получим лист(список) всех заказов по конкретному пользователю.
//
//        //далее этот лсит(список) положим в модель(в объект model класса Model), чтобы потом к ней обратиться.
//        model.addAttribute("orders", orderList);
//
//        //далее возвращаем шаблон orders в папке user templates.
//        return "/admin/ordersAdmin";
//
//    }
//
//
//


}
