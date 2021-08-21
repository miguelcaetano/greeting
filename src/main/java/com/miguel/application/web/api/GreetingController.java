package com.miguel.application.web.api;

import com.miguel.application.model.Greeting;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class GreetingController {

    private static BigInteger nextID;
    private static Map<BigInteger, Greeting> greetingMap;

    private static Greeting save(Greeting greeting) {
        if (greetingMap == null) {
            greetingMap = new HashMap<BigInteger, Greeting>();
            nextID = BigInteger.ONE;
        }
        // if update...
        if (greeting.getId() != null) {
            Greeting oldGreeting = greetingMap.get(greeting.getId());
            if (oldGreeting == null) {
                return null;
            }
            greetingMap.remove(greeting.getId());
            greetingMap.put(greeting.getId(), greeting);
            return greeting;
        }
        // if create...
        greeting.setId(nextID);
        nextID = nextID.add(BigInteger.ONE);
        greetingMap.put(greeting.getId(), greeting);
        return greeting;
    }

    private static boolean delete(BigInteger id) {
        Greeting deleteGreeting = greetingMap.remove(id);
        if (deleteGreeting == null) {
            return false;
        }
        return true;
    }


    static {
        Greeting g1 = new Greeting();
        g1.setText("hello world!");
        save(g1);

        Greeting g2 = new Greeting();
        g2.setText("Hola Mundo!");
        save(g2);
    }

    @GetMapping(
            value = "/api/greetings",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Greeting>> getGreetings() {
        Collection<Greeting> greetings = greetingMap.values();
        return new ResponseEntity<Collection<Greeting>>(greetings,
                HttpStatus.OK);
    }

    @GetMapping(value = "/api/greetings/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Greeting> getGreeting(@PathVariable("id") BigInteger id) {

        Greeting greeting = greetingMap.get(id);
        if (greeting == null) {
            return new ResponseEntity<Greeting>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Greeting>(greeting, HttpStatus.OK);

    }

    @PostMapping(value = "/api/greetings",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Greeting> createGreeting(@RequestBody Greeting greeting) {
        Greeting savedGreeting = save(greeting);
        return new ResponseEntity<Greeting>(savedGreeting, HttpStatus.CREATED);
    }

    @PutMapping(value = "/api/greetings/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Greeting> updateGreeting(@RequestBody Greeting greeting) {
        Greeting updatedGreeting = save(greeting);
        if (updatedGreeting == null) {
            return new ResponseEntity<Greeting>(
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Greeting>(updatedGreeting, HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/greetings/{id}")
    public ResponseEntity<Greeting> deleteGreeting
            (@PathVariable("id") BigInteger id) {

        boolean deleted = delete(id);
        if (!deleted) {
            return new ResponseEntity<Greeting>(
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Greeting>(HttpStatus.NO_CONTENT);
    }
}