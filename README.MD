# Voice to Text Telegram Bot
## Описание
Производит траскрипцию голосовых сообщений в текст в групповых или персональных чатах

При поступлении `Voice` ообщения, `TelegramBot` ответом на сообщение отправляет текстовую транскрипцию:

![пример](doc/img/img.png)

## Схема работы
![схема работы](doc/img/img_1.png)

## Как запустить
1. клонировать репозиторий
2. сконфигурировать `TokenServer` и `TelegramBotServer`
    1. получить `CLIENT_ID` и `CLIENT_Secret` на сайте [SBER](https://developers.sber.ru/docs/ru/salutespeech/authentication)
    2. получить `IAM-token` и `folder-ID` на сайте [YANDEX](https://cloud.yandex.ru/docs/resource-manager/quickstart)
    3. получить `token` Telegram бота на отца ботов `@BotFather`
    4. задать `JWT_SIGNING_KEY` для подписи сообщей при обмене по `gPRC` между `TelegramBotServer` и `RecognizeServer`
3. собрать проект
```sh
./gradlew clean build
```
4. запустить в `docker`
```sh
docker compose up -d
```



