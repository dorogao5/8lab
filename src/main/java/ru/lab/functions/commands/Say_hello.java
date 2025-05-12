package ru.lab.functions.commands;

import ru.lab.functions.Command;


public class Say_hello implements Command {


    @Override
    public void execute(String[] args) {
        System.out.println("Hello");

    }

    @Override
    public String getDescription() {
        return "say_hello - вывести hello";
    }
}
