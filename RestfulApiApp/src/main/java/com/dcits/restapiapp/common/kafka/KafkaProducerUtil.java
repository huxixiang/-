package com.dcits.restapiapp.common.kafka;

import com.dcits.restapiapp.config.ConfigFileUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaProducerUtil {

    private static final Properties properties = new Properties();
    private static KafkaProducer<String, String> kafkaProducer;//生产者实例

    static{
//        properties.put("bootstrap.servers","localhost:9092");
//        properties.put("bootstrap.servers","iotdsj1:6667,iotdsj2:6667,iotdsj3:6667");
        properties.put("bootstrap.servers", ConfigFileUtil.configProperties.get("kafka.bootstrap.servers"));
        properties.put("acks", "0");
        properties.put("retries", 3);
        properties.put("batch.size", 26214400);
        properties.put("linger.ms", 0);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    }
    /**
     * @function 获取kafka生产者实例（单例模式）
     * @return
     */
    private static KafkaProducer<String, String> getProducer() {
        if (kafkaProducer == null) {
            synchronized (KafkaProducerUtil.class) {
                kafkaProducer = new KafkaProducer<>(properties);

                Runtime.getRuntime().addShutdownHook(new Thread(){

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        //super.run();
                        if(kafkaProducer!=null)
                            kafkaProducer.close();
                    }

                });
            }
        }
        return kafkaProducer;
    }

    /**
     * @desc 将消息发送到kafka topic
     * @param topic
     * @param key
     * @param message
     */
    public static void sendMessageToKafka(String topic, String key, String message) {
        KafkaProducer<String, String> producer = getProducer();
        producer.send(new ProducerRecord<String, String>(topic, null, message));
    }


}
