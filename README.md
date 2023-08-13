<!-- REST Service -->
<br />
<div align="center">
  <h3 align="center">REST Service</h3>
</div>


<!-- ABOUT THE PROJECT -->
## О проекте

Проект представляет REST Service. Используется для общения с базой данных MySQL и получения результатов запросов.
База данных состоит из двух таблиц которые связаны OneToMany отношением: 
- employee с полями id, name, surname birthday, salary, department_id;
- departments с полями id, departmentName, minSalary, maxSalary.

Проект позволяет осуществлять CRUD операции над сущностями базы данных, а так же доплнительные операцие, такие как:
поиск работников, рождённых в определённый период или в определённую дату, просмотр средней запрлаты по департаментам,
сортировка работников по департаментам. Используется формат JSON.

Операции по созданию и изменению сущностей наделены всяческими проверками, валидацией на корректность ввода данных. К примеру, в компании не может работать сотрудник младше 18 лет и старше 60 лет, а имена могут использовать только символы латинского алфавита. Подробнее можно узнать в комментариях по коду.



### Built With

- Spring
- Spring Hibernnate
- Spring MVC
- JACKSON
