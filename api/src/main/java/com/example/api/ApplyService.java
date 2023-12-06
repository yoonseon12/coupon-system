package com.example.api;

import org.springframework.stereotype.Service;

import com.example.api.domain.Coupon;
import com.example.api.repository.CouponRepository;

@Service
public class ApplyService {
	private final CouponRepository couponRepository;

	public ApplyService(CouponRepository couponRepository) {
		this.couponRepository = couponRepository;
	}

	public void applyV1(Long userId) {
		long count = couponRepository.count();

		if (count > 5) return;

		couponRepository.save(new Coupon(userId));
	}
}
