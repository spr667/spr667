package com.sharebooks.controllers;

import com.sharebooks.models.UsuarisModel;
import com.sharebooks.repositories.UsuarisRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 
 * @author Sergio Prieto Rufián
 */
@RestController
@RequestMapping("/usuari")
public class UsuarisController {
    @Autowired
    UsuarisRepository usuariRepository;
    ArrayList<UsuarisModel> usuaris = new ArrayList<>();

    /**
     * 
     * @return Llistat de tots els usuaris de la bdD.
     */
    @GetMapping( path = "/tots")
    public ArrayList<UsuarisModel> obtenirUsuaris(){
            return (ArrayList<UsuarisModel>) usuariRepository.findAll();
    }

    /**
     * 
     * @param usuari a afegir
     * @return "Usuari afegit a la bdD" si no hi ha cap error.
     */
    @PostMapping( path = "/afegir")
    public String afegirUsuari(@RequestBody UsuarisModel usuari){
        usuari.setToken(null);
        usuariRepository.save(usuari);
        return "Usuari afegit a la bdD";
    }

    /**
     * 
     * @param usuari a actualitzar
     * @return "Usuari actualitzat correctament" o "Error"
     */
    @PutMapping( path = "/actualitzar")
    public String actualitzarUsuari(@RequestBody UsuarisModel usuari) {
        try{
            UsuarisModel user = usuariRepository.findById(usuari.getId()).get();
            user.setNom(usuari.getNom());
            user.setContrasenya(usuari.getContrasenya());
            user.setEsadmin(usuari.isEsadmin());
            user.setToken(null);
            usuariRepository.save(user);
            return "Usuari " + usuari.getNom() + " actualitzat correctament.";
        }catch(Exception err) {
            return "Error, usuari " + usuari.getNom() + " no actualitzat correctament.";
        }
    }

    /**
     * Elimina un usuari de la bdD
     * @param usuari id de l'usuari a esborrar
     * @return "Usuari eliminat correctament" o "Error al eliminar l'usuari"
     */
    @DeleteMapping( path = "/eliminar")
    public String eliminarPerId(@RequestBody UsuarisModel usuari){
        try{
            usuariRepository.deleteById(usuari.getId());
            return "Usuari eliminat correctament.";
        }catch(Exception err){
            return "Error, usuari no eliminat.";
        }
    }

    /**
     * Login d'usuari
     * @param usuari usuari (nom + contrasenya) a comprovar la contrasenya per fer login
     * @return id + token aleatori i login a true si la contrasenya és correcta
     *         Error si la contrasenya és incorrecta
     * @exemple http://spr667.ddns.net:8080/usuari/login
     *          {"nom" : "Sergio", "contrasenya" : "sergio_m13"}
     */
    @PostMapping(value = "/login")
    public String ferLogin(@RequestBody UsuarisModel usuari) {
        List <UsuarisModel> llistaUsuaris = (List <UsuarisModel>) usuariRepository.findAll();
        for (UsuarisModel u : llistaUsuaris){
            if (usuari.getNom().equals(u.getNom())) {
                if (usuari.getContrasenya().equals(u.getContrasenya())) {
                    eliminarUsuariActiu(usuari.getNom());
                    UsuarisModel nouUsuari = new UsuarisModel();
                    nouUsuari.setId(u.getId());
                    nouUsuari.setNom(u.getNom());
                    nouUsuari.setContrasenya(u.getContrasenya());
                    nouUsuari.setEsadmin(u.isEsadmin());
                    nouUsuari.setToken(generarToken());
                    usuaris.add(nouUsuari);
                    return "{\"id\": " + nouUsuari.getId() + ", \"token\": \"" + nouUsuari.getToken() + "\"}";
                }
            }
        }
        return "Error, contrasenya incorrecta.";
    }

    /**
     * Fa logout de l'aplicació: Elimina l'usuari que ha fet login amb el token especificat 
     * @param usuari Usuari a fer logout. Només necessari el camp "token"
     * @return "Correcte" si el codi existeix, Error si no existeix
     * @exemple http://spr667.ddns.net:8080/usuari/logout
     *          {"token" : ""595499fe-d876-4fa2-87e5-7c27778ff51b"}
}     */
    @PostMapping(value = "/logout")
    public String ferLogout(@RequestBody UsuarisModel usuari) {
        for (UsuarisModel u : usuaris){
            if (u.getToken().compareTo(usuari.getToken()) == 0) {
                eliminarUsuariActiu(u.getNom());
                return "Logout correcte.";
            }
        }
        return "Error, aquest codi no és actiu.";
    }


    /*
    FUNCIONS
    */

    
    /**
     * Elimina l'usuari de la memòria
     * @param nomUsuari nom d'usuari a eliminar
     * @return true si el troba, false si no el troba
     */    
    public boolean eliminarUsuariActiu(String nomUsuari){
        Predicate<UsuarisModel> condicio = user -> user.getNom().equals(nomUsuari);
        return usuaris.removeIf(condicio);
    }

    /**
     * Comprova que el token de l'usuari és correcte
     * @param usuari
     * @return true si el token és correcte, false si no ho és.
     */
    public boolean comprovarToken(UsuarisModel usuari){
        for (UsuarisModel user : usuaris) {
            if (usuari.getToken() == user.getToken()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Crea un token nou
     * @return token creat
     */
    public UUID generarToken(){
        UUID uuid = UUID.randomUUID();
        return uuid;
    }

}
