package de.hdm_stuttgart.mi.dad.incoming;

import de.hdm_stuttgart.mi.dad.core.ports.ServicePort;

/**
 * Handles user input from command line and executes corresponding methods of service
 */
public class SearchLevelHandler {

    final ServicePort service;

    public SearchLevelHandler(final ServicePort service) {
        this.service = service;
    }

    public void handleInput(final String[] args) {
        //do stuff with inpu

        //service.handle();
    }
}
