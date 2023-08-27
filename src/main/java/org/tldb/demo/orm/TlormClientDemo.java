/*
 * Copyright 2023 tldb Author. All Rights Reserved.
 * email: donnie4w@gmail.com
 * https://githuc.com/donnie4w/tldb
 * https://githuc.com/donnie4w/tlorm-java
 */

package org.tldb.demo.orm;

import io.github.donnie4w.tldb.tlcli.TlException;
import io.github.donnie4w.tlorm.Orm;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class TlormClientDemo {
    public static void main(String[] args) throws TlException {
        Orm.registerDefaultResource(false, "127.0.0.1", 7100, "mycli=123");
        UserInfo u = new UserInfo();
        u.createTable();//创建表userinfo

        u.age = 22;
        u.achi = 1.22f;
        u.char1 = (char)222;
        u.desc = "aaaa".getBytes(StandardCharsets.UTF_8);
        u.gender = 1;
        u.achi = 2.33f;
        u.name = "tom";
        long seq = u.insert();//新增数据

        System.out.println("seq--->"+seq);
        u.name = "jerry";
        u.desc = "bbbbb".getBytes(StandardCharsets.UTF_8);
        u.update();//更新数据

        System.out.println(u.selectById(1));
        System.out.println("————————————————————————————————————————————————");
        List<UserInfo> uis =u.selectsByIdLimit(1,10);
        for (UserInfo v:uis){
            System.out.println(v);
        }
        System.out.println("————————————————————————————————————————————————");
        System.out.println(u.selectByIdx("name","tom"));
        System.out.println("————————————————————————————————————————————————");
        List<UserInfo> uis2 =u.selectAllByIdx("name","jerry");
        for (UserInfo v:uis2){
            System.out.println(v);
        }
        System.out.println("————————————————————————————————————————————————");
        List<UserInfo> uis3 =u.selectByIdxLimit(0,2,"name","jerry");
        for (UserInfo v:uis3){
            System.out.println(v);
        }
    }
}


