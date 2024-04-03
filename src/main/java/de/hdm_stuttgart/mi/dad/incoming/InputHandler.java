package de.hdm_stuttgart.mi.dad.incoming;

import de.hdm_stuttgart.mi.dad.ports.ServicePort;

/**
 * Handles user input from command line and executes corresponding methods of service
 */
public class InputHandler {

    final ServicePort service;

    public InputHandler(final ServicePort service) {
        this.service = service;
    }

    public void handleInput(final String[] args) {
        //do stuff with input
        service.handle();
    }
}
