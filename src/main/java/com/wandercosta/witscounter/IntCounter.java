package com.wandercosta.witscounter;

/**
 * The class to provide shared counter.
 *
 * @author Wander Costa (www.wandercosta.com)
 */
class IntCounter {

    private transient int counter;

    void set(int counter) {
        this.counter = counter;
    }

    int get() {
        return this.counter;
    }

}
