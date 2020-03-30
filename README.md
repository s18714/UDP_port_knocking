Zadanie 2: UDP port knocking SKJ

Sposób działania aplikacji
1. Aplikacja składa się z dwóch procesów, serwera i klienta.
2. W pierwszym rzędzie należy uruchomić proces serwera. Proces ten otwiera zadaną
parametrem liczbę portów UDP, a następnie zaczyna na nich nasłuchiwać na pakiety od klientów. Jeśli zostanie wykryta odpowiednia sekwencja pakietów UDP wysyłanych z tego jednego adresu (ten sam adres IP i port), otwiera losowo wybrany port i tutaj mamy dwie wersje (piszemy tylko jedną z nich).
• Wersja 1 (łatwiejsza): Otwierany port jest portem TCP.
• Wersja 2 (trudniejsza): Otwierany port jest portem UDP.
2. Numer portu zostaje wysłany do klienta.
3. Niezależnie od wersji, po otwarciu wybranego portu przez klienta, serwer prześle
nazwę pliku i jego długość, a następnie zacznie przesyłać zawartość tego pliku. Klient odbierze i zapisze przesłaną zawartość do pliku w katalogu, w którym uruchomiona jest aplikacja.
4. Po wykonaniu transmisji serwer ponownie zaczyna nasłuchiwać na portach UDP lub w wersji obsługującej wielu klientów od razu przechodzi do nasłuchiwania na pakiety autoryzacyjne od kolejnych klientów.
5. Jeśli sekwencja pakietów UDP jest niepoprawna, odpowiedź od serwera nie przyjdzie. W takim przypadku, po określonym czasie (timeout) klient kończy pracę z komunikatem błędu.
Wymagania i sposób oceny
1. W celu realizacji zadania należy zaprojektować i zaimplementować procesy klienta i serwera
2. implementujące własny protokół komunikacyjny, umożliwiający realizację opisanej powyżej
3. funkcjonalności. Kwestię tego, jaki plik będzie przesyłany pozostawia się sprawdzającemu i to on dostarczy plik.
4. Proces serwera uruchamiany jest parametrami będącymi listą numerów portów UDP, na których ma oczekiwać na kolejne pakiety. Lista może być dowolnej długości i porty na niej mogą się powtarzać (oczywiście port otwieramy tylko raz). Zakładamy, że używane są jedynie porty o numerach powyżej 1024. Jeśli serwer nie może otworzyć danego portu z powodu jego zajętości przez inny proces - kończy pracę z błędem.
5. Proces klienta jako pierwszy swój parametr otrzymuje adres IP serwera, kolejnymi parametrami są numery portów UDP, na które należy wysyłać kolejne pakiety. Do pracy powinien używać losowych (ustalanych przez system) portów UDP i TCP.
6. Poprawny i pełny projekt wart jest 4 punkty (wersja 1) lub 6 punktów (wersja 2). Za zrealizowanie każdej z poniższych funkcjonalności można otrzymać punkty do podanej wartości:
• maksymalnie 2 punkty w wersji 1 i maksymalnie 4 punkty w wersji 2 za uzyskanie powyższej funkcjonalności przy założeniu tylko jednego klienta próbującego uzyskać autoryzację w danym momencie.
• maksymalnie 4 punkty w wersji 1 i maksymalnie 6 punktów w wersji 2 za uzyskanie powyższej funkcjonalności przy założeniu potencjalnie dowolnej liczby klientów próbujących uzyskać autoryzację w danym momencie.
7. Aplikację piszemy w języku Java zgodnie ze standardem Java 8 (JDK 1.8). Do komunikacji przez sieć można wykorzystać jedynie podstawowe klasy do komunikacji z wykorzystaniem protokołu UDP i/lub TCP.
8. Projekty powinny zostać zapisane do odpowiednich katalogów w systemie EDUX w nieprzekraczalnym terminie 30.12.2019 (termin może zostać zmieniony przez prowadzącego grupę).
9. Spakowany plik projektu powinien obejmować:
• Plik Dokumentacja(nr.indeksu)Zad2.pdf, opisujący, co zostało zrealizowane, co się
nie udało, jak zainstalować, gdzie ewentualnie są błędy, których nie udało się poprawić. W szczególności, plik musi zawierać szczegółowy opis zaprojektowanego i zaimplementowanego protokołu (brak opisu protokołu lub jego fragmentaryczność może spowodować znaczące obniżenie oceny rozwiązania zadania).
• Pliki źródłowe (dla JDK 1.8) (włącznie z wszelkimi bibliotekami nie należącymi do standardowej instalacji Javy, których autor użył) - aplikacja musi dać się bez problemu skompilować na komputerach w laboratorium w PJA.
