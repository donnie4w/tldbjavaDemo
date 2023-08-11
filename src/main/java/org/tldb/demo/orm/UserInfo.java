package org.tldb.demo.orm;

import io.github.donnie4w.tlorm.DefName;
import io.github.donnie4w.tlorm.Index;
import io.github.donnie4w.tlorm.Orm;

import java.nio.charset.StandardCharsets;

public class UserInfo extends Orm<UserInfo> {
    public long id;
    @Index
    public String name;
    public int age;
    public byte[] desc;
    @DefName(name = "Achi")
    public float achi;
    public byte gender;
    public char char1;

    public UserInfo() {
    }

    public UserInfo(int id, String name, int age, byte[] desc, float achi, byte gender,char char1) {
        super();
        this.id = id;
        this.name = name;
        this.age = age;
        this.desc = desc;
        this.achi = achi;
        this.gender = gender;
        this.char1 = char1;
    }

    public String toString() {
        return id + "," + name + "," + age + "," + new String(desc, StandardCharsets.UTF_8) + "," + achi + "," + gender+","+(char1==(char)222);
    }
}