package org.tldb.demo.client;

import io.github.donnie4w.tldb.tlcli.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
        client.newConnect(false, "127.0.0.1", 7100, "mycli=123");

        Map<String, ColumnType> cmap = new HashMap<>();
        cmap.put("classroom", ColumnType.STRING);
        cmap.put("teacher", ColumnType.BINARY);
        cmap.put("level", ColumnType.INT16);
        cmap.put("number", ColumnType.INT64);

        //建表
        client.createTable("school", cmap, new String[]{"classroom", "level"});

        //新增数据
        insert(client, 1);

        //update(client, 1, 33);

        //表当前最大id
        System.out.println("selectId>>" + client.selectId("school"));

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

        //删除数据
//        delete(client, 1);

        Thread.sleep(60 * 1000);
    }

    public static long insert(Client client, int i) throws TlException {
        Map<String, byte[]> m = new HashMap<>();
        m.put("classroom", ("class" + i).getBytes(StandardCharsets.UTF_8));
        m.put("teacher", ("teacher" + i).getBytes(StandardCharsets.UTF_8));
        m.put("level", ByteBuffer.allocate(Short.BYTES).order(ByteOrder.BIG_ENDIAN).putShort((short) (1 << 10)).array());
        m.put("number", ByteBuffer.allocate(Long.BYTES).order(ByteOrder.BIG_ENDIAN).putLong(1L << 33).array());
        AckBean ab = client.insert("school", m);
        return ab.seq;
    }


    public static void update(Client client, long id, int i) throws TlException {
        Map<String, byte[]> m = new HashMap<>();
        m.put("classroom", ("class" + i).getBytes(StandardCharsets.UTF_8));
        m.put("teacher", ("teacher" + i).getBytes(StandardCharsets.UTF_8));
        m.put("student", ("student" + i).getBytes(StandardCharsets.UTF_8));
        AckBean ab = client.update("school", id, m);
    }

    public static void delete(Client client, long id) throws TlException {
        AckBean ab = client.delete("school", id);
        System.out.println(ab.ack.ok);
    }

}