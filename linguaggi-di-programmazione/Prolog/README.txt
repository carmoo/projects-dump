Progetto "Espressioni Regolari per Automi Nondeterministici"

Progetto in Prolog realizzato da
Ferraro	Carmelo	887447
Filomeno	Giuseppe	869506
Tessa	Claudio	894601

Le espressioni regolari – regular expressions, o, abbreviando 
regexs – sono tra gli strumenti più utilizzati (ed abusati) 
in Informatica. 
Un’espressione regolare rappresenta in maniera finita un 
linguaggio (regolare), ossia un insieme potenzialmente infinito 
di sequenze di “simboli”, o stringhe, dove i “simboli” sono 
tratti da un alfabeto che indicheremo con S.

Lo scopo del progetto è implementare in Prolog e in Common Lisp
un compilatore da regexs a NFSA. Le regexs sono espresse in un 
opportuno formato che verrà dettagliato in seguito.

Per ogni fase, è stata aggiunta una sezione EXTRA in cui vengono spiegati
tutti i predicati aggiunti da noi.
Spiegazione dei predicati utilizzati:

_______________________________________________

1. is_regex

:- is_regex(RE).
Restituisce vero quando RE è un’espressione regolare. 
Numeri e atomi(in genere anche ciò che soddisfa atomic/1)
sono le espressioni regolari più semplici;
Se l'espressione non risulta valida perché non presente
Nell'alfabeto, restituisce errore via schermo.

EXTRA

:- is_regex_list([]).
Predicato ausiliario nei casi seq e alt.

_______________________________________________

2.nfsa_regex_comp

:- nfsa_regex_comp(FA_Id, RE).
Restituisce vero quando RE è compilabile in un'automa
Che viene inserito nella base dati del Prolog. 
FA_Id diventa un identificatore per l’automa.

EXTRA

:- is_ID_unique(FA_Id).
Controlla se l'identificatore è già presente o meno.
In caso positivo, segnala errore su schermo e fallisce.

:- nfsa_regex_comp_seq(FA_Id, [RE], Begin, End).
Predicato ausiliario per il corretto funzionamento
Nel caso seq.

:- nfsa_regex_comp_alt(FA_Id, [RE], Begin, End).
Predicato ausiliario per il corretto funzionamento
Nel caso alt.

_______________________________________________

3.nfsa_rec

:- nfsa_rec(FA_Id, Input).
Vero quando l’input per l’automa identificato da FA_Id 
Viene consumato completamente e l’automa si trova in uno 
Stato finale. Input è una lista Prolog di simboli 
dell’alfabeto S sopra definito.

_______________________________________________

4. nfsa_clear

:- nfsa_clear().
Rimuove tutti gli automi dalla base di dati.

:- nfsa_clear(FA_Id).
Rimuove un'automa specifico FA_Id dalla base di dati.
