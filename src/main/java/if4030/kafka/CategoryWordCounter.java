package if4030.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class CategoryWordCounter {

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final List<String> TOPICS = Arrays.asList("tagged-words-stream", "command-topic");
    private static final AtomicBoolean running = new AtomicBoolean(true);
    private static final Map<String, Map<String, Integer>> categoryWordCounts = new HashMap<>();

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        props.put("group.id", "category-word-counter-group");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        props.put("auto.offset.reset", "earliest");

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(TOPICS);

            while (running.get()) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    if ("command-topic".equals(record.topic()) && "END".equals(record.value())) {
                        displayTopWordsByCategory();
                        running.set(false);
                        break;
                    } else if ("tagged-words-stream".equals(record.topic())) {
                        String category = record.key();
                        String word = record.value();
                        // Ignore non-relevant categories if necessary
                        categoryWordCounts.putIfAbsent(category, new HashMap<>());
                        Map<String, Integer> wordCounts = categoryWordCounts.get(category);
                        wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
                    }
                }
            }
        }
    }

    private static void displayTopWordsByCategory() {
        // Aggregate total counts for each category
        Map<String, Integer> categoryTotals = new HashMap<>();
        categoryWordCounts.forEach((category, wordCounts) -> 
            categoryTotals.put(category, wordCounts.values().stream().mapToInt(Integer::intValue).sum()));
    
        // Sort categories by their aggregated counts in descending order and limit to top 20
        List<Map.Entry<String, Integer>> sortedCategories = new ArrayList<>(categoryTotals.entrySet());
        sortedCategories.sort(Map.Entry.<String, Integer>comparingByValue().reversed());
    
        sortedCategories.stream().limit(20).forEach(categoryEntry -> {
            System.out.println("Category: " + categoryEntry.getKey() + ", Total Count: " + categoryEntry.getValue());
            // Additionally, if you want to display the top words in each category as well:
            Map<String, Integer> wordCounts = categoryWordCounts.get(categoryEntry.getKey());
            wordCounts.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(20) // You could adjust this number to show more or fewer top words per category
                    .forEach(wordEntry -> System.out.println("\t" + wordEntry.getKey() + ": " + wordEntry.getValue()));
            System.out.println();
        });
    }
    
}
