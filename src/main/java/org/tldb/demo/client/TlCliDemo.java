package org.tldb.demo.client;
import io.github.donnie4w.tldb.tlcli.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class TlCliDemo {
    public static Logger logger = Logger.getLogger("TlCliDemo");

    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.newConnect(true, "192.168.2.108", 7100, "mycli=123");
        //建表
        client.createTable("school", new String[]{"classroom", "teacher", "student"}, new String[]{"classroom", "teacher"});
       //新增数据
        for (int i = 0; i < 10; i++) {
            Map<String, byte[]> m = new HashMap<>();
            m.put("classroom", ("class" + i).getBytes(StandardCharsets.UTF_8));
            m.put("teacher", ("teacher" + i).getBytes(StandardCharsets.UTF_8));
            m.put("student", ("student" + i).getBytes(StandardCharsets.UTF_8));
            client.insert("school", m);
        }
        //表当前最大id
        System.out.println("selectId>>"+client.selectId("school"));

        //根据Id值查询
        DataBean db = client.selectById("school", 1);
        System.out.println("id >> " + db.getId());
        for (String k : db.getTBean().keySet()) {
            System.out.println(k + " >> " + new String(db.getTBean().get(k).array(), StandardCharsets.UTF_8));
        }

        //根据索引查询
        List<byte[]> list = new ArrayList<>();
        list.add("teacher0".getBytes(StandardCharsets.UTF_8));
        list.add("teacher1".getBytes(StandardCharsets.UTF_8));
        list.add("teacher2".getBytes(StandardCharsets.UTF_8));
        List<DataBean> dblist = client.selectByIdxLimit("school", "teacher", list, 0, 20);
        if (dblist != null) {
            for (DataBean _db : dblist) {
                StringBuffer sb = new StringBuffer();
                sb.append("id:").append(_db.getId()).append(",");
                for (String key : _db.getTBean().keySet()) {
                    sb.append(key).append(":").append(new String(_db.getTBean().get(key).array(), StandardCharsets.UTF_8)).append(" ");
                }
                logger.info(sb.toString());
            }
        }


        Thread.sleep(60 * 1000);
    }
}