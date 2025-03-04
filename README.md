# Домашнее задание №14 "Практики надежности"

Реализован класс RetryingHttpClient с методами getData и getDataWithCircuitBreaker согласно приведённой в тексте задания
спецификации.

Для демонстрации работы данного класса в этом же приложении (чтобы не маять себя и проверяющего докером) реализован
http-сервер, эмулирующий работу нестабильного внешнего API.
Сервер имеет единственный эндпоинт /hello, который может:

- с вероятностью successProbability отдать код 200 и текст hello;
- в противном случае с вероятностью blameUserProbability отдать ошибку 40x;
- в противном случае отдать одну из ошибок 500, 502, 503 или 504.

Запуск демонстрационных сценариев можно провести из классов Task1DemoApp и Task2DemoApp для 1 и 2 заданий
соответственно.

Также приложены логи запуска данных сценариев: [первого](/task1demo.log) и [второго](/task2demo.log).

В них из нескольких запусков демонстрационных сценариев я привел наиболее удачные, где показываются различные варианты
развития:

- успех запроса сразу (первый файл, строка 3);
- успех запроса после ретрая (первый файл, строки 169-171);
- неуспех: код ошибки, который нет смысла ретраить (первый файл, строка 7);
- неуспех: исчерпан лимит попыток (первый файл, строка 49);
- срабатывание circuit breaker, т. е. переход в состояние Open (второй файл, строка 4);
- блокировка запросов после срабатывания circuit breaker (второй файл, строка 32);
- переход в состояние Half-Open по таймауту (второй файл, строка 34);
- переход из состояния Half-Open обратно в Open при первой же неудаче (второй файл, строка 36);
- переход из состояния Half-Open в Closed при успехе и дальнейшая нормальная работа (второй файл, строка 70);