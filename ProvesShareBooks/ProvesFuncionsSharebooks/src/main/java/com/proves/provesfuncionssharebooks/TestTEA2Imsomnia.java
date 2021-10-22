package com.proves.provesfuncionssharebooks;

import com.jayway.jsonpath.JsonPath;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sergio Prieto Rufián
 */
public class TestTEA2Imsomnia {
    public static void main(String[] args) throws IOException {
        
        TestTEA2Imsomnia test = new TestTEA2Imsomnia();

        //Ha de mostrar el missatge "Error, contrasenya incorrecta.", 
        //ja que la contrasenya vàlida és "sergio_m13"
        test.login("Sergio", "serg3");
        
        //Ha de retornar l'id de l'usuari i un token (UUID).
        //Posibles usuaris vàlids: "Maria", "maria_13" i "Sergio", "sergio_m13"
        String token = JsonPath.read(test.login("Maria", "maria_13"), "token");
        
        //Ha de retornar "Logout correcte.", ja que fem logout amb el token 
        //rebut al pas anterior
        test.logout(token);

        //Ha de retornar "Error, aquest codi no és actiu.", ja que l'usuari ja
        //ha fet logout al pas anterior
        test.logout(token);
        
    }
    
    public String login(String nom, String contrasenya) throws IOException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://spr667.ddns.net:8080/usuari/login"))
                    .header("Content-Type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString("{\n\t\"nom\" : \"" + nom + 
                        "\",\n\t\"contrasenya\" : \"" + contrasenya + "\"\n}\n"))
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());    
            System.out.println(response.body());
            return response.body();
        } catch (InterruptedException ex) {
            Logger.getLogger(TestTEA2Imsomnia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return "Error.";
    }
    
    public String logout(String token){
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://spr667.ddns.net:8080/usuari/logout"))
            .header("Content-Type", "application/json")
            .method("POST", HttpRequest.BodyPublishers.ofString("{\n\t\"token\" : \"" + token + "\"\n}\n"))
            .build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            return response.body();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(TestTEA2Imsomnia.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Error.";
    }
}
