% -*- Mode: Prolog -*- 
% "Espressioni regolari per Automi Nondeterministici" %

% Progetto in Prolog realizzato da %
% Ferraro	Carmelo	887447 %
% Filomeno	Giuseppe	869506 %
% Tessa	Claudio	894601 %

:- dynamic nfsa_initial/2.
:- dynamic nfsa_delta/4.
:- dynamic nfsa_final/2.

%1. is_regex(RE) %

% Caso base con unico simbolo
is_regex(RE) :-
    atomic(RE),
    !.

% Caso base con epsilon
is_regex(epsilon).

% Caso ricorsivo con seq
is_regex(RE) :-
    RE =.. [seq | REs],
    is_regex_list(REs),
    !.

% Caso ricorsivo con alt
is_regex(RE) :-
    RE =.. [alt | REs],
    !,
    is_regex_list(REs).

% Caso ricorsivo con zom
is_regex(RE) :-
    RE =.. [zom, REs],
    is_regex(REs),
    !.

% Caso ricorsivo con oom
is_regex(RE) :-
    RE =.. [oom, REs],
    is_regex(REs),
    !.

% Gestione errori
is_regex(RE) :-
    write(RE),
    write(' non è una RE!'),
    fail.

% Predicato ausiliario nei casi seq e alt
is_regex_list([]). 

is_regex_list([RE|REs]) :-
    is_regex(RE),
    is_regex_list(REs).

%2. nfsa_regex_comp(FA_Id, Re) %

% Caso base
nfsa_regex_comp(FA_Id, RE) :-
    nonvar(FA_Id),
    is_regex(RE),
    !,
    is_ID_unique(FA_Id),
    gensym(q, Begin),
    assert(nfsa_initial(FA_Id, Begin)),
    gensym(q, End),
    assert(nfsa_final(FA_Id, End)),
    nfsa_regex_comp(FA_Id, RE, Begin, End).

% Controllo se ID è unico
is_ID_unique(FA_Id) :-
    nfsa_initial(FA_Id, _),
    write('Esiste già un NFSA con questo ID!'),
    !,
    fail.

is_ID_unique(_) :-
    !.

% Caso RE unica
nfsa_regex_comp(FA_Id, RE, Begin, End) :-
    atomic(RE),
    assert(nfsa_delta(FA_Id, Begin, RE, End)),
    !.

% Caso seq
nfsa_regex_comp(FA_Id, RE, Begin, End) :-
    RE =.. [seq | REs],
    nfsa_regex_comp_seq(FA_Id, REs, Begin, End),
    !.

% Caso alt
nfsa_regex_comp(FA_Id, RE, Begin, End) :-
    RE =.. [alt | REs],
    nfsa_regex_comp_alt(FA_Id, REs, Begin, End),
    !.

% Caso zom
nfsa_regex_comp(FA_Id, RE, Begin, End) :-
    RE =.. [zom, REs],
    gensym(q, QI),
    assert(nfsa_delta(FA_Id, Begin, epsilon, QI)),
    gensym(q, QF),
    assert(nfsa_delta(FA_Id, QF, epsilon, End)),
    assert(nfsa_delta(FA_Id, QF, epsilon, QI)),
    assert(nfsa_delta(FA_Id, Begin, epsilon, End)),
    nfsa_regex_comp(FA_Id, REs, QI, QF),
    !.

% Caso oom
nfsa_regex_comp(FA_Id, RE, Begin, End) :-
    RE =.. [oom, REs],
    nfsa_regex_comp(FA_Id, seq(REs, zom(REs)), Begin, End),
    !.

% Predicato ausiliario seq
nfsa_regex_comp_seq(FA_Id, [RE], Begin, End) :-
    nfsa_regex_comp(FA_Id, RE, Begin, End),
    !.

nfsa_regex_comp_seq(FA_Id, [RE | REs], Begin, End) :-
    gensym(q, F1),
    nfsa_regex_comp_seq(FA_Id, [RE], Begin, F1),
    gensym(q, I2),
    assert(nfsa_delta(FA_Id, F1, epsilon, I2)),
    nfsa_regex_comp_seq(FA_Id, REs, I2, End),
    !.

% Predicato ausiliario alt
nfsa_regex_comp_alt(FA_Id, [RE], Begin, End) :-
    gensym(q, QI),
    assert(nfsa_delta(FA_Id, Begin, epsilon, QI)),
    gensym(q, QF),
    assert(nfsa_delta(FA_Id, QF, epsilon, End)),
    nfsa_regex_comp(FA_Id, RE, QI, QF),
    !.

nfsa_regex_comp_alt(FA_Id, [RE | REs], Begin, End) :-
    nfsa_regex_comp_alt(FA_Id, [RE], Begin, End),
    nfsa_regex_comp_alt(FA_Id, REs, Begin, End).

%3. nfsa_rec(FA_Id, Input) %

% Caso base
nfsa_rec(FA_Id, Input) :-
    nfsa_initial(FA_Id, Begin),
    !,
    nfsa_rec(FA_Id, Input, Begin),
    !.

% Caso no input 
nfsa_rec(FA_Id, [], State) :-
    nfsa_final(FA_Id, State).

nfsa_rec(FA_Id, [], State) :-
    nfsa_delta(FA_Id, State, epsilon, Ns),
    nfsa_rec(FA_Id, [], Ns).

% Caso ricorsivo
nfsa_rec(FA_Id, [Input | Inputs], State) :-
    nfsa_delta(FA_Id, State, epsilon, Ns),
    nfsa_rec(FA_Id, [Input | Inputs], Ns).

nfsa_rec(FA_Id, [Input | Inputs], State) :-
    nfsa_delta(FA_Id, State, Input, Ns),
    nfsa_rec(FA_Id, Inputs, Ns).

%4. nfsa_clear() e nfsa_clear(FA, Id) %

% Rimuove tutti gli automi dalla base di dati
nfsa_clear() :-
    retractall(nfsa_initial(_, _)),
    retractall(nfsa_final(_, _)),
    retractall(nfsa_delta(_, _, _, _)).

% Rimuove un automa specifico FA_Id dalla base di dati
nfsa_clear(FA_Id) :-
    retractall(nfsa_initial(FA_Id, _)),
    retractall(nfsa_final(FA_Id, _)),
    retractall(nfsa_delta(FA_Id, _, _, _)).
