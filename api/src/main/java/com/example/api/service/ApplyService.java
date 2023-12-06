package com.example.api.service;

import org.springframework.stereotype.Service;

import com.example.api.domain.Coupon;
import com.example.api.producer.CouponCreateProducer;
import com.example.api.repository.AppliedUserRepository;
import com.example.api.repository.CouponCountRepository;
import com.example.api.repository.CouponRepository;

@Service
public class ApplyService {
	private final CouponRepository couponRepository;
	private final CouponCountRepository couponCountRepository;
	private final CouponCreateProducer couponCreateProducer;
	private final AppliedUserRepository appliedUserRepository;

	public ApplyService(
			CouponRepository couponRepository,
			CouponCountRepository couponCountRepository,
			CouponCreateProducer couponCreateProducer,
			AppliedUserRepository appliedUserRepository) {
		this.couponRepository = couponRepository;
		this.couponCountRepository = couponCountRepository;
		this.couponCreateProducer = couponCreateProducer;
		this.appliedUserRepository = appliedUserRepository;
	}

	public void applyV1(Long userId) {
		long count = couponRepository.count();

		if (count > 5) return;

		couponRepository.save(new Coupon(userId));
	}

	public void applyV2(long userId) {
		Long count = couponCountRepository.increment();

		if (count > 5) return;

		couponRepository.save(new Coupon(userId));
	}

	public void applyV3(long userId) {
		Long count = couponCountRepository.increment();

		if (count > 5) return;

		couponCreateProducer.create(userId);
	}

	public void applyV4(long userId) {
		Long apply = appliedUserRepository.add(userId);

		if (apply != 1) return; // 발급 가능한 쿠폰 개수를 1개로 제한

		Long count = couponCountRepository.increment();

		if (count > 5) return;

		couponCreateProducer.create(userId);
	}
}
