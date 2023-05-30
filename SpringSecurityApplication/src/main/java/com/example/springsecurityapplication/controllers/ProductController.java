package com.example.springsecurityapplication.controllers;


import com.example.springsecurityapplication.repositories.ProductRepository;
import com.example.springsecurityapplication.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

//ЛОГИКА ПО РАБОТЕ С ТОАВРАМИ ПОЛЬЗОВАТЕЛЕЙ, КОТОРЫЕ НЕ ВОШЛИ В ЛИЧНЫЙ КАБИНЕТ------------------------------------------

//уже контроллер, а не просто класс
@Controller
//указываем, что данный контроллер будет срабатывать, если в URL будет строка "/product"
@RequestMapping("/product")
public class ProductController {

    //внедрение productRepository так как, его методы не прописаны в ProductService и напрямую обращаемся в контроллер для сортировки, поиска и фильтрации товаров
    private final ProductRepository productRepository;
    private final ProductService productService;

//constructor для работы с объектом productService
    public ProductController(ProductRepository productRepository, ProductService productService) {
        //добавлен productRepository в конструктор
        this.productRepository = productRepository;
        this.productService = productService;
    }

    //реализация в рамках данного контролера--------------------------------------------

    //метод, получающий список всех товаров, где далее вернём шаблон, на котором отобразим все товары
                                //создаём класс(контейнер) модели Model и её объект(экземплр) model, в который положим значения всех товаров и далее к этим значениям обратимся на контроллер
    @GetMapping("")
    public String getAllProduct(Model model){
        //кладём в объект модели атрибут, чтобы к нему можно было обратиться на шаблоне.
                       //в атрибуте:    ключ;      значение то, что вернёт(лист всех товаров) этот метод из ProductService.
        //т.е. лист товаров кладём в модель по ключу и вызываем метод...
        model.addAttribute("products", productService.getAllProduct());
        //..и к этому ключу возвращать будем
        return "/product/product"; //шаблон в папке product раздела templates
    }


    //метод, обрабатывающий нажатие на ссылку для вывода информации о товаре для любых пользователей
                    //обработали нажатие на ссылку наименования конкретного товара
    @GetMapping("/info/{id}")
                              //с помощью этого получаем дин. переменную {id}
                                                          //элемент товара, который найдём по id будем класть модель(некий контейнер) = в объект model класса Model
    public String infoProduct(@PathVariable("id") int id, Model model){
        //кладём в объект модели атрибут, чтобы к нему можно было обратиться на шаблоне.
                        //в атрибуте:   ключ;      значение то, что вернёт конкретный товар по его id этот метод
                                                  // из ProductService.
        //т.е. id конкретного товара кладём в модель  и вызываем метод по этому id...
        model.addAttribute("product", productService.getProductId(id));
        //..и к этому ключу возвращать будем
        return "/product/infoProduct";
    }


//Метод для работы формы "МЕНЮ ФИЛЬТРАЦИИ ТОВАРОВ" на главной страницее сайта шаблона "product" в папке "product" "templates"

                    //поскольку вверху уже обозначена первая половина url /product, то здесь пишем вторую часть, а не полностью /product/search
    @PostMapping("/search")  //принимаем все input'ы с формы в данный контроллер
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
      return "/product/product";

    }
}
