package com.example.springsecurityapplication.services;

import com.example.springsecurityapplication.models.Person;
import com.example.springsecurityapplication.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


//ПРОПИСАНЫ ВСЕ ОСНОВНЫЕ МЕТОДЫ ПО РАБОТЕ С ТОВАРОМ---------------------------------------------------------------------

@Service
public class PersonService{
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Person findByLogin(Person person){
        Optional<Person> person_db = personRepository.findByLogin(person.getLogin());
        return person_db.orElse(null);
    }

    @Transactional
    public void register(Person person){
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        personRepository.save(person);
    }



//пытался добавить 2 метода в персонсервис для изменения роли пользователя.---(МОЯ ПОПЫТКА!)

    //B.метод для обновления пользователя для имзенения роли(добавлен мной)

    @Transactional                                  //объект класса Product
    public void updatePerson(int id, Person person){
        //устанавливаем айдишник пользователю для понимания Srping'ом Data JPA
        person.setId(id);
        //обновляем информацию о товаре, на основе объекта person класса Person, пришедшего с формы обновления данных
        //метод сохранения изменений товара
        personRepository.save(person);
    }

    //B.метод для получения id пользователя(добавлен мной)!

    //2.метод позволит получить один товар по id. метод нужен для редактирования и удаления товара, также для открытия страницы полной информации о товаре
//    public Person getPersonId(int id){
//        //лист~список               обращаемся        и вызываем метод, который ведёт поиск товаров по id
//        Optional<Person> optionalPerson = personRepository.findByLogin(id);
//        //если товар найден по id, он возвращается, а если нет, то null
//        return optionalPerson.orElse(null);
//        {

}
