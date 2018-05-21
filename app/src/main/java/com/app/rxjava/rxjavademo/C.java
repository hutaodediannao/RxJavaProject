package com.app.rxjava.rxjavademo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/21.
 */

public class C {

    public static List<Person> PERSON_LIST = new ArrayList<>();

    static {
        PERSON_LIST.add(new Person("hutao", null, 30));
        PERSON_LIST.add(new Person("zhanSan", "武汉", 25));
        PERSON_LIST.add(new Person("baobao", "深圳", 28));
        PERSON_LIST.add(new Person("xiDada", "上海", 50));
    }






}
