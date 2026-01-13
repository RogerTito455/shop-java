/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author crtit
 */
public class Amount {

    private double value;
    final private String currency = "euro";

    public Amount(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void add(Amount value) {
        this.value += value.value;
    }

    public void multiply(double value) {
        this.value *= value;
    }

    @Override
    public String toString() {
        return "Amount{" + "value=" + value + ", currency=" + currency + '}';
    }

}
