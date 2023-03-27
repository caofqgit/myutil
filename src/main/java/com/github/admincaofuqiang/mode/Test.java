package com.github.admincaofuqiang.mode;

public class Test {
    public static void main(String[] args) {
        Person1 person1 = new Person1();

        Handler build = new Builder().addHandler(person1).addHandler(new Person2())
                .addHandler(new Person3())
                .build();
        build.handlerRequest("3");
    }
}
