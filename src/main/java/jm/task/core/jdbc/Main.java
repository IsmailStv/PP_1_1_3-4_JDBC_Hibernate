package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    private final static UserService userService = new UserServiceImpl();

    public static void main(String[] args) {
        userService.createUsersTable();

        userService.saveUser("Джордж", "Клуни", (byte) 62);
        userService.saveUser("Майли", "Сайрус", (byte) 30);
        userService.saveUser("Брэд", "Питт", (byte) 59);
        userService.saveUser("Ариана", "Гранде", (byte) 30);

        userService.removeUserById(2);

        userService.getAllUsers();

        userService.cleanUsersTable();

        userService.dropUsersTable();
    }
}
