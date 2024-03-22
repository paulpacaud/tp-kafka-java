# tp-kafka-java

### Set Up

Make sure to run ``` mvn package ``` before beginning, and make sure to include a book in plain text named "fichier.txt" at the root of the project.

Open a first terminal, name it "kafka server" and run:

```
kafka-server-start.sh /opt/kafka/config/kraft/server.properties
```

Open a 2nd terminal, name it "producer lines-stream" and run:
```
kafka-console-producer.sh --bootstrap-server localhost:9092 \
        --topic lines-stream
```

Open a 3rd terminal, name it "producer command-topic" and run:
```
kafka-console-producer.sh --bootstrap-server localhost:9092 \
        --topic command-topic
```

Open a 4th terminal, name it "TextLinesToWordsStream", and run:
```
java -cp target/tp-kafka-0.0.1-SNAPSHOT.jar if4030.kafka.TextLinesToWordsStream
```

Open a 5th terminal, name it "WordsLemmatizerStream", and run:
```
java -cp target/tp-kafka-0.0.1-SNAPSHOT.jar if4030.kafka.WordsLemmatizerStream
```

Open a 6th terminal, name it "CategoryWordCounter", and run:
```
java -cp target/tp-kafka-0.0.1-SNAPSHOT.jar if4030.kafka.CategoryWordCounter
```

### Test and Play

In the terminal "producer lines-stream", run the command:

```
cat fichier.txt | kafka-console-producer.sh --bootstrap-server localhost:9092 \
        --topic lines-stream
```

wait a few seconds, and go in the terminal called "producer command-topic", and run:
```
END
```

It should output in the terminal of "CategoryWordCounter" the 20 most used categories of the book "fichier.txt", along with the 20 most used lemmas within each of these 20 categories.