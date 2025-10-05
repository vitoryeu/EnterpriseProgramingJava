package lab8.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import lab8.model.OrderEvent;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}") private String bootstrap;
    @Value("${queue.processing.threads}") private int concurrency;

    @Value("${queue.order.topics.urgent}") private String urgentTopic;
    @Value("${queue.order.topics.vip}") private String vipTopic;
    @Value("${queue.order.topics.standard}") private String standardTopic;

    @Bean
    public ProducerFactory<String, OrderEvent> producerFactory(ObjectMapper mapper) {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, OrderEvent> kafkaTemplate(ProducerFactory<String, OrderEvent> pf) {
        return new KafkaTemplate<>(pf);
    }

    private ConsumerFactory<String, OrderEvent> consumerFactory(String groupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
                new JsonDeserializer<>(OrderEvent.class, false));
    }

    private ConcurrentKafkaListenerContainerFactory<String, OrderEvent> baseFactory(String groupId) {
        ConcurrentKafkaListenerContainerFactory<String, OrderEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(groupId));
        factory.setConcurrency(concurrency);
        factory.getContainerProperties().setObservationEnabled(true);
        return factory;
    }

    @Bean(name = "urgentKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, OrderEvent> urgentFactory() {
        return baseFactory("order-processing-group-urgent");
    }
    @Bean(name = "vipKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, OrderEvent> vipFactory() {
        return baseFactory("order-processing-group-vip");
    }
    @Bean(name = "standardKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, OrderEvent> standardFactory() {
        return baseFactory("order-processing-group-standard");
    }

    @Bean public NewTopic urgentTopic() { return new NewTopic(urgentTopic, 3, (short)1); }
    @Bean public NewTopic vipTopic() { return new NewTopic(vipTopic, 3, (short)1); }
    @Bean public NewTopic standardTopic() { return new NewTopic(standardTopic, 3, (short)1); }
}
