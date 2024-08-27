package com.example.gamesuniverse.model;

public abstract class Result {
    private Result(){ }

    public boolean isSuccess(){
        return this instanceof Success;
    }

    public boolean isSuccessSingleGame(){
        return this instanceof SuccessSingleGame;
    }
    public boolean isSuccessGenres(){
        return this instanceof SuccessGenres;
    }
    public boolean isSuccessUser(){
        return this instanceof SuccessUser;
    }

    public static final class Success extends Result{
        private final GamesResponse gamesResponse;
        public Success(GamesResponse gamesResponse){
            this.gamesResponse = gamesResponse;
        }
        public GamesResponse getData(){
            return gamesResponse;
        }
    }

    public static final class SuccessSingleGame extends Result{
        private final GameDetail singleGameResponse;
        public SuccessSingleGame(GameDetail singleGameResponse){
            this.singleGameResponse = singleGameResponse;
        }
        public GameDetail getData(){
            return singleGameResponse;
        }
    }

    public static final class SuccessGenres extends Result{
        private final GenreResponse genreResponse;
        public SuccessGenres(GenreResponse genreResponse){
            this.genreResponse = genreResponse;
        }
        public GenreResponse getData(){
            return genreResponse;
        }
    }

    public static final class SuccessUser extends Result {
        private final User user;

        public SuccessUser(User user) {
            this.user = user;
        }

        public User getData() {
            return user;
        }
    }


    public static final class Error extends Result {
        private final String message;
        public Error(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
}
