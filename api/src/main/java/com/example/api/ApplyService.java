package com.example.api;

import org.springframework.stereotype.Service;

import com.example.api.domain.Coupon;
import com.example.api.repository.CouponCountRepository;
import com.example.api.repository.CouponRepository;

@Service
public class ApplyService {
	private final CouponRepository couponRepository;
	private final CouponCountRepository couponCountRepository;

	public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository) {
		this.couponRepository = couponRepository;
		this.couponCountRepository = couponCountRepository;
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
}
