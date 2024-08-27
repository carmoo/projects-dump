Progetto "Espressioni Regolari per Automi Nondeterministici"

Progetto in Lisp realizzato da
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

Per ogni fase, è stata aggiunta una sezione EXTRA in cui vengono spiegate
tutte le funzioni aggiunte da noi.
Descrizione delle funzioni:

_______________________________________________

1. (is-regex RE)
Verifica se una data espressione è una regex valida.
Restituisce T (vero) se l'espressione è valida, altrimenti NIL.
Controlla se l'espressione è un atomo, una sequenza (seq), 
un'alternativa (alt), zero o più (zom) o uno o più (oom).
SOLO GLI ATOMI VENGONO CONSIDERATI PARTE DELL'ALFABETO.

_______________________________________________

2. nfsa-regex-comp(RE)
Valuta il tipo di espressione regolare e richiama 
la funzione appropriata per costruire il corrispondente NFSA.
retiruisce l'automa corrispondente oppure NIL se
RE non è un'espressione regolare valida.

EXTRA

next-state-name()
Incrementa il contatore degl nome degli stati e ritorna il nuovo valore.
Viene utilizzata per generare nomi univoci per gli stati dell'automa.

atom-nfsa-regex-comp(RE)
Costruisce un NFSA per una regex atomica.
Crea uno stato iniziale e uno finale collegati 
dalla transizione che rappresenta l'atomo.

seq-nfsa-regex-comp(RE)
Costruisce un NFSA per una regex di tipo sequenza.
Concatenando gli NFSA delle sotto-espressioni, 
unisce le transizioni in modo sequenziale.

alt-nfsa-regex-comp(RE)
Costruisce un NFSA per una regex di tipo alternanza.
Unisce gli NFSA delle sotto-espressioni in modo che
rappresentino una scelta tra più alternative.

zom-nfsa-regex-comp(RE)
Costruisce un NFSA per una regex di tipo zero or more (zom).
Crea gli stati e le transizioni necessarie per rappresentare
che l'espressione può ripetersi zero o più volte.

oom-nfsa-regex-comp(RE)
Costruisce un NFSA per una regex di tipo one or more (oom). 
Crea gli stati e le transizioni necessarie per rappresentare che
l'espressione deve ripetersi almeno una volta.

seq-nfsa-merge(nfsa1 nfsa2)
Unisce due NFSA in sequenza. Collega lo stato finale del primo 
NFSA con lo stato iniziale del secondo NFSA tramite una transizione epsilon.

_______________________________________________

3. nfsa-rec(FA Input)
Verifica se un NFSA accetta una data stringa Input.
Utilizza delta-cap per determinare se lo stato finale dell'automa
è raggiungibile dopo aver processato l'intera stringa di input.
controlla se lo stato finale è presente nella lista di statiraggiungibili
partendo dallo stato iniziale e consumando la stringa.

EXTRA

delta(FA state symbol)
Restituisce la lista degli stati raggiungibili da state tramite symbol.
Filtra le transizioni dell'automa per trovare quelle che partono
dallo stato specificato e usano il simbolo specificato.

delta-extended(FA states symbol)
Restituisce l'insieme di stati raggiungibili da states tramite symbol.
Applica la funzione delta a ciascuno stato nell'insieme di stati
e unisce i risultati.

eclose(FA state)
Restituisce l'insieme di stati raggiungibili da state tramite epsilon-mosse.
Utilizza eclose-helper per tenere traccia degli stati già visitati
ed evitare loop infiniti.

eclose-helper(FA state visited)
Funzione di supporto ricorsiva per eclose.
Trova tutti gli stati raggiungibili dallo stato specificato tramite 
epsilon-mosse, evitando stati già visitati.

eclose-extended(FA states)
Restituisce l'insieme di stati raggiungibili da states 
tramite epsilon-mosse. Applica la funzione eclose a 
ciascuno stato nell'insieme di stati e unisce i risultati.

delta-cap(FA state string)
Accetta un singolo stato e una stringa, restituendo tutti i
possibili stati raggiungibili partendo da state e consumando
la stringa.

