# MailHandler

MailHanlder wysyła maila na podstawie danych pobieranych z serwera Http w formacie Json. Json potrzebuje trzech pól:

- to - adres mailowy odbiorcy
- subject - temat maila
- text - zawartość wiadomości

Dodatkowo można dodać ścieżkę do załącznika w jsonie jako wartość klucza "attachment".

Serwis w responsie na request odsyła "success" bądź "failure" w zależności od wyniku operacji wysyłania maila.

Przykładowy json do wysyłania:

    {
    "to" : "jan_nowak5@gmail.com",
    "subject" : "temat",
    "text" : "Lorem ipsum."
    "attachment" : "1.txt"
    }

Przed zbudowaniem obrazu należy uzupełnić w Dockerfile zmienne środowiskowe odpowiedzalne za adres e-mail i hasło do maila. Są to dane adresata maila.

Jeśli adresatem ma być konto w usłudze Gmail należy wcześniej wejść w `Zarządzanie kontem Google` -> `Bezpieczeństwo` -> `Dostep mniej bezpiecznych aplikacji` i ustawić dostęp na `Włączony`

Komenda do zbudowania:

	docker build -t mail .	

Komenda do wystartowania:

	docker run -d -p 587:587 --name mail1 mail
	
Adres serwisu postawionego lokalnie:

	http://127.0.0.1:587/mail?wsdl


