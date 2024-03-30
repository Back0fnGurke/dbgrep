package de.hdm_stuttgart.mi.dad.incoming;

import de.hdm_stuttgart.mi.dad.ports.ServicePort;

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
