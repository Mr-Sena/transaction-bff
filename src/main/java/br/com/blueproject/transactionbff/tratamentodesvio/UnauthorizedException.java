package br.com.blueproject.transactionbff.tratamentodesvio;

public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(String message) {
        super(message);
    }
}
