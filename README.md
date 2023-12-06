# coupon-system
[실습으로 배우는 선착순 이벤트 시스템](https://www.inflearn.com/course/%EC%84%A0%EC%B0%A9%EC%88%9C-%EC%9D%B4%EB%B2%A4%ED%8A%B8-%EC%8B%9C%EC%8A%A4%ED%85%9C-%EC%8B%A4%EC%8A%B5/dashboard) 학습 리포지토리

---

### 프로젝트 환경세팅

#### docker 설치
```
brew install docker
brew link docker

docker version
```

#### docker mysql 실행 명령어
```
docker pull mysql
docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=1234 --name mysql mysql
docker ps
docker exec -it mysql bash
```

>docker: no matching manifest for linux/arm64/v8 in the manifest list entries.
오류가 발생 시
<br>
docker pull --platform linux/x86_64 mysql
>

#### mysql 명령어
```
mysql -u root -p
create database coupon_example;
use coupon_example;
```

---

### Redis 환경설정
```
docker pull redis
docker run --name myredis -d -p 6379:6379 redis
```

---

### Kafka 환경설정
Kafak 디렉터리를 만들고 docker-compose.yml 파일을 생성한다.
```
vim docker-compose.yml
```

docker-compose.yml
```yaml
version: '2'
services:
  zookeeper:
    image: wurstmeister/zookeeper
    container_name: zookeeper
    ports:
      - "2181:2181"
  kafka:
    image: wurstmeister/kafka:2.12-2.5.0
    container_name: kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_ADVERTISED_HOST_NAME: 127.0.0.1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
```

생성 후 kafka 실행
```
docker-compose up -d
```

kafka, zookeeper 실행 확인
```
docker ps
```

docker-compose 종료
```
docker-compose down
```

---

### Kafka 토픽 생성 및 프로듀서, 컨슈머 실행
토픽 생성
```
docker exec -it kafka kafka-topics.sh --bootstrap-server localhost:9092 --create --topic coupon_create
```

컨슈머 실행
```
docker exec -it kafka kafka-console-consumer.sh --topic coupon_create --bootstrap-server localhost:9092 --key-deserializer "org.apache.kafka.common.serialization.StringDeserializer" --value-deserializer "org.apache.kafka.common.serialization.LongDeserializer"
```


