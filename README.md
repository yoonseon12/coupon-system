# coupon-system
[실습으로 배우는 선착순 이벤트 시스템] 학습 리포지토리

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