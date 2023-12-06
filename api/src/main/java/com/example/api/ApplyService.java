package com.example.api;

import org.springframework.stereotype.Service;

import com.example.api.domain.Coupon;
import com.example.api.producer.CouponCreateProducer;
import com.example.api.repository.CouponCountRepository;
import com.example.api.repository.CouponRepository;

@Service
public class ApplyService {
	private final CouponRepository couponRepository;
	private final CouponCountRepository couponCountRepository;
	private final CouponCreateProducer couponCreateProducer;

	public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository, CouponCreateProducer couponCreateProducer) {
		this.couponRepository = couponRepository;
		this.couponCountRepository = couponCountRepository;
		this.couponCreateProducer = couponCreateProducer;
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
}
