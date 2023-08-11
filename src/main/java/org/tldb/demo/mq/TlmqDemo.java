package org.tldb.demo.mq;

import io.github.donnie4w.tlmq.cli.MqClient;
import io.github.donnie4w.tlmq.cli.SimpleClient;
import io.github.donnie4w.tlmq.tldb.bean.JMqBean;
import io.github.donnie4w.tlmq.tldb.bean.MqBean;

import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class TlmqDemo {
    public static Logger logger = Logger.getLogger("tlmq");

    public static void main(String[] args) throws Exception {
        logger.info("java mqcli demo run");
        MqClient mc = new SimpleClient("ws://127.0.0.1:5100", "mymq=123");
        mc.pubByteHandler((mb) -> {
            logger.info(new String(mb.getMsg(), StandardCharsets.UTF_8));
        });
        mc.pullByteHandler((mb) -> {
            logger.info(new String(mb.getMsg(), StandardCharsets.UTF_8));
        });
        mc.pubJsonHandler((mb) -> {
            logger.info(mb.toString());
        });
        mc.pubMemHandler((mb) -> {
            logger.info(mb.toString());
        });
        mc.pullJsonHandler((mb) -> {
            logger.info(mb.toString());
        });
        mc.errHandler((errCode) -> {
            System.out.println("err code >> " + errCode);
        });
        mc.ackHandler((ackId) -> {
            System.out.println("ack id >> " + ackId);
        });
        //连接服务器
        mc.connect();

        //设定服务器重发数据的时间，默认60秒
        //sc.recvAck((byte) 60);

        //设定服务器压缩原数据大小上限 10M
        mc.mergeOn((byte) 10);

        //压缩协议，可能导致服务器分配大量内存
//        sc.setZlib(true);
        Thread.sleep(1000);
        long v = mc.sub("usertable"); //订阅 topic “usertable”
//        cd.subCancel("usertable"); //订阅 topic “usertable”
        logger.info("sub ackId:" + v);
        mc.sub("usertable2");  //订阅 topic “usertable”
        mc.sub("usertable3");  //订阅 topic “usertable”

        mc.pubMem("usertable", "this is java pubmem"); // 只内存发布，不存数据

        mc.pubJson("usertable", "this is java pubJson"); //发布 topic usertable2  及信息

        long id = mc.pullIdSync("usertable");
        logger.info("pullIdSync >>" + id);

        JMqBean jmb = mc.pullJsonSync("usertable", 1);
        logger.info(jmb == null ? "null" : "pullJsonSync>>" + jmb.toString());

        MqBean mb = mc.pullByteSync("usertable", 1);
        logger.info(mb == null ? "null" : "pullByteSync>>" + mb.toString());
        Thread.sleep(600000);
    }
}
