package com.example.api;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import com.example.api.repository.CouponRepository;

@SpringBootTest
class ApplyServiceTest {

	@Autowired
	private ApplyService applyService;

	@Autowired
	private CouponRepository couponRepository;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	/**
	 * 만약 멀티스레드 환경에서 여러 스레드가 병렬로 테스트를 실행하는 경우,
	 * 각 스레드에 대해 별도의 테스트 메서드가 실행되고,
	 * 각 테스트 메서드 실행이 끝날 때마다 해당 스레드에 대한 @AfterEach 메서드가 호출된다.
	 */
	@AfterEach
	void tearDown() {
		couponRepository.deleteAll();
		redisTemplate.delete("coupon_count");
	}

	@Test
	public void 한번만응모() {
		applyService.applyV1(1L);

		long count = couponRepository.count();

		assertThat(count).isEqualTo(1);
	}

	@Test
	@DisplayName("멀티스레드 환경에서 쿠폰 발급을 진행 : 단순 count 값을 비교 -> 레이스 컨디션이 발생하여 테스트에 실패한다.")
	public void 여러번응모V1() throws InterruptedException {
		int threadCount = 10;

		//ExecutorService : 병렬 작업을 간단하게 할 수 있게 도와주는 Java API
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		// CountDownLatch : 다른 Thread에서 수행하는 작업을 기다리도록 도와주는 클래스
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i=0; i<threadCount; i++) {
			long userId = i;
			executorService.submit(() -> {
				try {
					applyService.applyV1(userId);
				} finally {
					latch.countDown(); // 하나의 스레드가 종료되면 count를 내림.
				}
			});
		}

		latch.await(); // 모든 스레드의 작업이 완료될 때 까지 대기

		long count = couponRepository.count();

		assertThat(count).isGreaterThan(5); // 레이스 컨디션 발생하여 기대했던 5보다 큰 값으로 설정된다.
	}

	@Test
	@DisplayName("멀티스레드 환경에서 쿠폰 발급을 진행 : 레디스 increment 적용 -> 레디스는 싱글스레드 기반으로 동작하기 때문에 레이스 컨디션이 발생하지 않는다")
	public void 여러번응모V2() throws InterruptedException {
		int threadCount = 10;

		//ExecutorService : 병렬 작업을 간단하게 할 수 있게 도와주는 Java API
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		// CountDownLatch : 다른 Thread에서 수행하는 작업을 기다리도록 도와주는 클래스
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i=0; i<threadCount; i++) {
			long userId = i;
			executorService.submit(() -> {
				try {
					applyService.applyV2(userId);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		long count = couponRepository.count();

		assertThat(count).isEqualTo(5);
	}

	@Test
	public void 여러번응모V3() throws InterruptedException {
		int threadCount = 1000;

		//ExecutorService : 병렬 작업을 간단하게 할 수 있게 도와주는 Java API
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		// CountDownLatch : 다른 Thread에서 수행하는 작업을 기다리도록 도와주는 클래스
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i=0; i<threadCount; i++) {
			long userId = i;
			executorService.submit(() -> {
				try {
					applyService.applyV3(userId);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		Thread.sleep(10000); // 데이터가 전송이 완료된 시점을 기준으로 쿠폰의 개수를 가져오고 컨슈머에서는 그 시점에 아직 모든 쿠폰을 생성하지 않았기 때문에 Thread Slip 사용

		long count = couponRepository.count();

		assertThat(count).isEqualTo(5);
	}
}