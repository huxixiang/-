package com.dcits.restapiapp.common.kafka;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;


import java.util.Arrays;
import java.util.Properties;

public class KafkaConsumerUtil {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "api_test_groupid01");
        props.put("enable.auto.commit", "true");
        props.put("auto.offset.reset", "latest");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 62428800);
        @SuppressWarnings("unchecked")
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("TOPIC_INTERFACE_ZHLD_DEVINFO"));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records){
//                System.out.println(record.offset()+"|===========|:"+record.value());
                //                System.out.printf("partition = %d,offset = %d, key = %s, value = %s%n",record.partition(), record.offset(), record.key(), record.value());

            }

        }
    }


}
