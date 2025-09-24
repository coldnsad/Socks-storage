package org.example.socks.exception;

public class SocksNotFoundException extends RuntimeException {
    public SocksNotFoundException() {
        super("Socks not found");
    }
}
