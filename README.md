# kafka
Setting Up Kafka
===============================================================
1. Download kafka and extract it to <kafka_dir>
2. Install Java to a path without spaces (If path has spaces, kafka startup fails)
3. Setup JAVA_HOME
4. Modify <kafka_dir>\config\zookeeper.properties
	dataDir=F:/tmp/kafka/zookeeper
5. Modify <kafka_dir>\config\server.properties
	log.dirs=F:/tmp/kafka/logs
6. Execute following commands:
start call <kafka_dir>\bin\windows\zookeeper-server-start.bat  <kafka_dir>\config\zookeeper.properties
start call <kafka_dir>\bin\windows\kafka-server-start.bat  <kafka_dir>\config\server.properties 