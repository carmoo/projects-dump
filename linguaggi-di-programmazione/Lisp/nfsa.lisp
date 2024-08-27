;;;;; Ferraro	Carmelo	887447
;;;;; Filomeno	Giuseppe	869506
;;;;; Tessa	Claudio	894601

;;; -----IS-REGEX-----

(defun is-regex (RE)
  (cond ((atom RE) T) ; se e' un atomo ritorna subito T
        ((or (and (equal 'seq (first RE)) 
                  (>= (length (cdr RE)) 1))
             (and (equal 'alt (first RE))
                  (>= (length (cdr RE)) 1))) ; Se e' seq oppure alt
         ;; valuta tutte le sotto-regex e fa l'AND
         (every #'is-regex (cdr RE)))
        ((or (equal 'zom (first RE))
             (equal 'oom (first RE))) ; Se e' * oppure +
         ;; valuta la sotto regex 
         (and (equal (length (cdr RE)) 1) (is-regex(first (cdr RE)))))
        ((equal (length RE) 1) (is-regex (first RE)))
        (T NIL))) ; altrimenti NIL

;;; -----NFSA-REGEX-COMP-----

;;; Nome dello stato (viene poi incrementato ogni volta)
(setf state-name -1) 

(defun next-state-name ()
  (incf state-name)
  state-name)

(defun nfsa-regex-comp (RE)
  (cond ((equal (is-regex RE) NIL) NIL) ; se non e' una regex ritorna NIL
        ((atom RE) (atom-nfsa-regex-comp RE)) ; se è atomo
        ;; se e' una lista di un solo elemento
        ((equal (length RE) 1) (nfsa-regex-comp (first RE)))
        ((equal 'seq (first RE)) (seq-nfsa-regex-comp (cdr RE))) ; se è seq
        ((equal 'alt (first RE)) (alt-nfsa-regex-comp (cdr RE))) ; se è alt
        ((equal 'zom (first RE)) (zom-nfsa-regex-comp (cdr RE))) ; se è *
        ((equal 'oom (first RE)) (oom-nfsa-regex-comp (cdr RE))) ; se è +
        NIL))

(defun atom-nfsa-regex-comp (RE)
  (let* ((initial-state (next-state-name))
         (final-state (next-state-name)))

    (list 
     initial-state
     (list (list initial-state RE final-state))
     final-state)))

(defun seq-nfsa-regex-comp (RE)
  (cond
    ;; PASSO BASE: se RE ha un solo elemento restituisce il suo NFSA
    ((equal (length RE) 1) 
     (nfsa-regex-comp (first RE))) 
    ;; PASSO RICORSIVO: unisce gli NFSA 
    (T (seq-nfsa-merge (seq-nfsa-regex-comp (butlast RE))
                       (nfsa-regex-comp (first (last RE))))))) 

;;; prende 2 nfsa e ne unisce la delta
(defun seq-nfsa-merge (nfsa1 nfsa2)
  (list
   (first nfsa1) ; stato iniziale
   (append 
    (second nfsa1) 
    (second nfsa2)
    ;; epsilon-mossa da stato finale di nfsa1 a stato iniziale di nfsa2
    (list (list (first (last nfsa1)) 'epsilon (first nfsa2))))
   (first (last nfsa2)))) ; stato finale

(defun zom-nfsa-regex-comp (RE)
  (let* ((inner-nfsa (nfsa-regex-comp RE))
         (initial-state (next-state-name))
         (final-state (next-state-name)))

    (list 
     initial-state
     (append
      ;; tutti gli stati gia' esistenti
      (second inner-nfsa)
      ;; epsilon-mossa da initial-state a (first inner-nfsa)
      (list (list initial-state 'epsilon (first inner-nfsa)))
      ;; epsilon-mossa da (last inner-nfsa) a final-state
      (list (list (first (last inner-nfsa)) 'epsilon final-state))
      ;; epsilon-mossa da initial-state a final-state
      (list (list initial-state 'epsilon final-state))
      ;; epsilon-mossa da (last inner-nfsa) a (first inner-nfsa)
      (list (list (first (last inner-nfsa)) 'epsilon (first inner-nfsa))))
     final-state)))

(defun oom-nfsa-regex-comp (RE)
  (let* ((inner-nfsa (nfsa-regex-comp RE))
         (initial-state (next-state-name))
         (final-state (next-state-name)))

    (list 
     initial-state
     (append
      ;; tutti gli stati gia' esistenti
      (second inner-nfsa)
      ;; epsilon-mossa da initial-state a (first inner-nfsa)
      (list (list initial-state 'epsilon (first inner-nfsa)))
      ;; epsilon-mossa da (last inner-nfsa) a final-state
      (list (list (first (last inner-nfsa)) 'epsilon final-state))
      ;; epsilon-mossa da (last inner-nfsa) a (first inner-nfsa)
      (list (list (first (last inner-nfsa)) 'epsilon (first inner-nfsa))))
     final-state)))

(defun alt-nfsa-regex-comp (RE)
  (let* ((initial-state (next-state-name))
         (final-state (next-state-name))
         ;; Compila le regex interne
         (inner-nfsas (mapcar #'nfsa-reg  ex-comp RE)))

    (list 
     initial-state
     (append 
      ;; Concatena le transizioni di ciascun NFSA interno
      (apply #'append (mapcar #'second inner-nfsas))
      ;; Transizioni dall'initial state agli stati interni
      (mapcar (lambda (inner-nfsa)
                (list initial-state 'epsilon (first inner-nfsa)))
              inner-nfsas)
      ;; Transizioni dagli stati finali degli stati interni al final state
      (mapcar (lambda (inner-nfsa)
                (list (first (last inner-nfsa)) 'epsilon final-state))
              inner-nfsas))
     final-state)))


;;; -----NFSA-REC-----

;;; Restituisce gli stati raggiungibili da state tramite symbol
(defun delta (FA state symbol)
  ;; prende il terzo elemento (stato di arrivo)
  (mapcar #'third
          ;; filtra le transizioni
          (remove-if-not 
           (lambda (transition)
             (and (equal (first transition) state)
                  (equal (second transition) symbol)))
           (second FA))))

;; Accetta UN INSIEME di stati
(defun delta-extended (FA states symbol)
  (cond ((null states) NIL)
        (T 
         (reduce #'union 
		 (mapcar (lambda (state) (delta FA state symbol)) states)))))

;;; Funzione di supporto ricorsiva per eclose
(defun eclose-helper (FA state visited)
  (let ((reachable-states (delta FA state 'epsilon)))
    (dolist (s reachable-states visited)
      (unless (member s visited)
        (setq visited (eclose-helper FA s (cons s visited)))))
    visited))

;;; Restituisce gli stati raggiungibili da state tramite epsilon
(defun eclose (FA state)
  (eclose-helper FA state (list state)))

;;; accetta UN INSIEME di stati
(defun eclose-extended (FA states)
  (cond ((null states) NIL)
	(T 
         (reduce #'union 
		 (mapcar (lambda (state) (eclose FA state)) states)))))

;;; accetta UN SOLO stato
(defun delta-cap (FA state string)
  ;; PASSO BASE
  (cond ((equal string 'epsilon) (eclose FA state))
        ((equal string '(epsilon)) (eclose FA state))
        ((equal string NIL) (eclose FA state)) ; NIL e' considerata epsilon
        ((null string) (eclose FA state))
	;; PASSO INDUTTIVO
        ((>= 
          (length string) 1)
         (eclose-extended 
          FA 
          (delta-extended
           FA
           (delta-cap FA state (butlast string))
           (first (last string)))))))

(defun nfsa-rec (FA Input)
  ;; se Input non e' una lista
  (cond ((not (listp Input)) NIL)
	;; se FA e' un atomo
	((atom FA) (format t "Error:  ~A is not a Finite State Automata" FA))
	((not (and (atom (first FA))
		   (listp (second FA)) 
                   (atom (third FA))))
	 (format t "Error: ~A is not a Finite State Automata"  FA))
	;; controlla se negli stati raggiungibili e' presente quello finale
	((equal 
          (first (member (third FA) (delta-cap FA (first FA) Input)))
          (third FA)) 
         T)
	(T NIL)))
