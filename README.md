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

To test the app, I used the book "La Guerre des Mondes": https://fr.wikisource.org/wiki/La_Guerre_des_mondes
It resulted in this output from the app CategoryWordCounter:

```
Category: "ART:def, Total Count: 7860
        de: 3190
        la: 1877
        le: 1397
        les: 1396

Category: VER, Total Count: 5313
        faire: 210
        pendre: 120
        voir: 116
        trouver: 98
        luire: 94
        sembler: 91
        contrer: 89
        venir: 89
        partir: 81
        rester: 74
        entrer: 73
        passer: 65
        maintenir: 64
        prendre: 64
        mourir: 53
        paraître: 47
        suivre: 46
        former: 44
        tomber: 44
        peiner: 41

Category: NOM, Total Count: 5172
        yu: 124
        gens: 115
        homme: 91
        maison: 85
        route: 71
        travers: 71
        chose: 70
        machine: 63
        moment: 61
        rayon: 57
        jour: 50
        chemin: 50
        coup: 49
        femme: 48
        temps: 48
        oeil: 48
        main: 42
        monde: 42
        pied: 38
        heure: 38

Category: "NOM, Total Count: 3621
        je: 863
        me: 356
        par: 301
        pour: 291
        vers: 224
        pouvoir: 209
        on: 117
        devoir: 96
        sortir: 85
        mes: 85
        sous: 80
        aller: 78
        rien: 74
        moi: 61
        cela: 57
        porter: 56
        savoir: 56
        devenir: 54
        nouvelle: 37
        dire: 36

Category: "ADJ, Total Count: 3306
        sur: 374
        tout: 332
        grand: 109
        bien: 96
        petit: 83
        toute: 75
        noir: 67
        avant: 63
        long: 59
        toutes: 57
        seul: 49
        soudain: 46
        nouveau: 44
        premier: 40
        autre: 40
        fort: 38
        instant: 35
        ardent: 34
        plein: 34
        rouge: 33

Category: "ADJ:num, Total Count: 2252
        un: 883
        une: 672
        il: 585
        quatre: 25
        mille: 22
        ici: 19
        cent: 16
        sept: 6
        cinquante: 6
        onze: 5
        trente: 4
        v: 4
        quarante: 2
        soixante: 2
        midi: 1

Category: CON, Total Count: 2213
        et: 2155
        ni: 47
        donc: 11

Category: "ADV, Total Count: 1906
        en: 657
        mais: 248
        pas: 230
        comme: 174
        si: 142
        peu: 83
        quand: 78
        autour: 54
        moins: 41
        depuis: 37
        milieu: 32
        au-dessus: 26
        mieux: 16
        comment: 16
        pourquoi: 11
        cependant: 11
        lorsque: 11
        tard: 9
        puisque: 5
        jusque: 5

Category: ADV, Total Count: 1453
        ne: 338
        encore: 108
        aussi: 77
        ainsi: 56
        alors: 46
        presque: 46
        loin: 34
        trop: 30
        absolument: 27
        beaucoup: 26
        assez: 25
        jamais: 24
        debout: 22
        seulement: 21
        lentement: 21
        environ: 20
        rapidement: 19
        plupart: 19
        autant: 17
        parce: 16

Category: "PRO:int, Total Count: 1370
        que: 750
        qui: 522
        dont: 34
        lequel: 21
        laquelle: 19
        auquel: 7
        duquel: 4
        lesquels: 4
        auxquels: 3
        lesquelles: 3
        desquels: 2
        desquelles: 1

Category: PRO:per, Total Count: 1265
        se: 492
        nous: 374
        ils: 211
        elle: 69
        vous: 66
        eux: 29
        elles: 23
        te: 1

Category: PRE, Total Count: 1187
        dans: 646
        avec: 267
        sans: 147
        hors: 36
        par-dessus: 32
        chez: 24
        parmi: 24
        selon: 7
        voici: 2
        hormis: 2

Category: "VER, Total Count: 1182
        plus: 323
        puis: 73
        fait: 66
        bruit: 64
        dit: 49
        nuit: 43
        vis: 42
        vu: 39
        vue: 34
        pus: 26
        bois: 22
        mis: 21
        bout: 21
        pris: 18
        produit: 15
        crainte: 14
        point: 14
        venu: 13
        sens: 13
        cours: 12

Category: "AUX, Total Count: 1036
        avoir: 651
        être: 385

Category: ART:def, Total Count: 985
        du: 554
        au: 337
        aux: 94

Category: "ADJ:pos, Total Count: 976
        mon: 241
        son: 152
        sa: 138
        ma: 123
        ses: 120
        leurs: 118
        notre: 70
        ton: 10
        votre: 2
        mien: 1
        mienne: 1

Category: ART:ind, Total Count: 953
        des: 953

Category: unknown, Total Count: 950
        marsiens: 123
        marsien: 43
        cylindrer: 42
        londres: 36
        tandis: 32
        weybridge: 17
        woking: 17
        hill: 14
        chobham: 11
        street: 10
        horsell: 10
        richmond: 9
        chertsey: 9
        leatherhead: 9
        dit-il: 9
        dis-je: 8
        henderson: 8
        st: 7
        surrey: 7
        kingston: 7

Category: ADJ, Total Count: 480
        moindre: 25
        terrible: 22
        vaste: 19
        immense: 18
        obscur: 10
        tumultueux: 10
        gigantesque: 8
        lumineux: 7
        innombrable: 7
        formidable: 6
        capable: 6
        lugubre: 6
        terrestre: 6
        interminable: 5
        lamentable: 5
        lent: 5
        paisible: 5
        immobile: 5
        colossal: 5
        visible: 5

Category: ADJ:dem, Total Count: 387
        cette: 190
        ces: 169
        cet: 28
```