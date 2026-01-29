/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import main.Payable;

/**
 *
 * @author crtit
 */
public class Client extends Person implements Payable {

    private int memberId;
    private Amount balance;
    //Constant
    private final Amount BALANCE = new Amount(50);
    private final int MEMBER_ID = 456;

    public Client(String name) {
        super(name);
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public Amount getBalance() {
        return balance;
    }

    public void setBalance(Amount balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean pay(Amount money) {
        if (balance.getValue() > money.getValue()) {
            balance.setValue(balance.getValue() - money.getValue());
            System.out.println("Puedes pagarlo");
            return true;
        }
        balance.setValue(balance.getValue() - money.getValue());
        System.out.println("Saldo negativo del cliente: " + balance.getValue());
        return false;

    }

}
