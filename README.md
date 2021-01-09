# SpringDemoProject
Это мой демонстрационный проект в спринге. Допустим, у нас есть магазин и мы учитываем наших клиентов. У каждого клиента есть 4 поля. Name = имя клиента, Address = город проживания, isVIPclient = является ли клиент держателем фирменной карты, purchasesMadeSoFar = количество покупок, совершенных клиентом. Допустим, мы хотим создать RESTful API, который имеет возможность:
1) добавить нового клиента с помощью POST запроса
2) получить список всех клиентов в формате JSON 
3) получить конкретного клиента по id
4) найти всех клиентов по параметрам (либо указано имя, либо адресс, либо и то и другое и тд)
5) Допустим, клиенты с вип карточкой в среднем приносят 2000 рублей за покупку, а клиенты без неё -- 1000. Мы хотим рассчитать нашу выручку в рублях, долларах и евро. Для конвертации валюты, мы будем обращаться к внешнему API для получения курса. (то есть мы создаем @Service, а потом нджектим его в наш RestController класс). 
6) Все Endpoints должны быть грамотно задокументированы с помощью SWAGGER 2. 
# Features:
1) MongoDb database (No sql)
2) JPA repository
3) Swagger docs
4) RestContoller (Post and Get requests)
5) Синхронный запрос https://www.cbr-xml-daily.ru/daily_json.js для получения данных о курсе валют 
6) Работа с json (атрибуты, небольшая кастомизация)
