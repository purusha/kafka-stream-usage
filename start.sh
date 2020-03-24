docker rm test
docker run --name test -p 2181:2181 -p 9092:9092 -e ADVERTISED_HOST=127.0.0.1 -e NUM_PARTITIONS=1 johnnypark/kafka-zookeeper
java -jar /home/osboxes/.m2/repository/com/homeadvisor/kafka/kafdrop/2.1.0/kafdrop-2.1.0.jar --zookeeper.connect=localhost:2181 --kafka.brokers=localhost:9092
