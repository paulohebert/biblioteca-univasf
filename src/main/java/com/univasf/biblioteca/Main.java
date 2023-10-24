package com.univasf.biblioteca;

import com.univasf.biblioteca.util.HibernateUtil;

public class Main {
    public static void main(String[] args) {
        HibernateUtil.initialize();
        App.main(args);
    }
}
