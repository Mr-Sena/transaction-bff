package br.com.blueproject.transactionbff.tratamentodesvio;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String message) {
        super(message);
    }
}
