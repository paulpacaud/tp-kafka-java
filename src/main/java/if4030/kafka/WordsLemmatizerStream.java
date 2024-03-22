package if4030.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class WordsLemmatizerStream {

    private static final String INPUT_TOPIC = "words-stream";
    private static final String OUTPUT_TOPIC = "tagged-words-stream";
    private static final String LEXIQUE_FILE = "/config/workspace/Lexique383.csv";

    private static Map<String, String> wordToLemmaMap = new HashMap<>();
    private static Map<String, String> wordToCategoryMap = new HashMap<>();

    static {
        // Load the lexical database
        try {
            loadLexicalDatabase(LEXIQUE_FILE);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load Lexique database", e);
        }
    }

    static Properties getStreamsConfig() {
        final Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "lemmatize-words");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }

    static void loadLexicalDatabase(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); // Assuming a comma-separated format
                if (parts.length >= 3) {
                    String word = parts[0].trim();
                    String lemma = parts[2].trim();
                    // Add to maps, assuming parts[1] is the category for simplicity
                    wordToLemmaMap.put(word, lemma);
                    wordToCategoryMap.put(word, parts[28].trim());
                }
            }
        }
    }

    static void createLemmatizedWordsStream(final StreamsBuilder builder) {
        KStream<String, String> source = builder.stream(INPUT_TOPIC);
    
        source.mapValues(word -> wordToLemmaMap.getOrDefault(word, word))
              .selectKey((ignoredKey, word) -> wordToCategoryMap.getOrDefault(word, "unknown"))
              // Use peek to print the key (category) and value (lemma)
              .peek((key, value) -> System.out.println("Key (Category): " + key + ", Value (Lemma): " + value))
              .to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.String()));
    }    

    public static void main(final String[] args) {
        final Properties props = getStreamsConfig();

        final StreamsBuilder builder = new StreamsBuilder();
        createLemmatizedWordsStream(builder);
        final KafkaStreams streams = new KafkaStreams(builder.build(), props);

        streams.start();

        // Attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
