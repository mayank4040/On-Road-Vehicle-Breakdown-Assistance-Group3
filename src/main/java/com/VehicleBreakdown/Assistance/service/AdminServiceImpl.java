package com.VehicleBreakdown.Assistance.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.VehicleBreakdown.Assistance.model.Admin;
import com.VehicleBreakdown.Assistance.model.Feedback;
import com.VehicleBreakdown.Assistance.model.Mechanic;
import com.VehicleBreakdown.Assistance.model.User;
import com.VehicleBreakdown.Assistance.repository.AdminRepository;
import com.VehicleBreakdown.Assistance.repository.FeedbackRepository;
import com.VehicleBreakdown.Assistance.repository.MechanicRepository;
import com.VehicleBreakdown.Assistance.repository.UserRepository;

@Service
public class AdminServiceImpl implements AdminService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private FeedbackRepository feedbackRepository;
	
	@Autowired
	private MechanicRepository mechanicRepository;
	
	@Autowired
	private MechanicService mechanicService;
	
	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	@Override
	public Admin updateAdmin(Admin admin) {
		return adminRepository.save(admin);
	}

	@Override
	public Optional<Admin> getAdminByUsername(String username) {
		return adminRepository.findByUsername(username);
	}

	@Override
	public List<Feedback> viewFeedback() {
		return feedbackRepository.findAll();
	}
	
	@Override
	public List<Mechanic> getAllMechanics() {
		return mechanicRepository.findAll();
	}

	@Override
	public String allowOrBlockMechanic(long mechanicId) {
		if(mechanicRepository.existsById(mechanicId))
		{
			Mechanic mechanic = mechanicService.getMechanicByMechanicId(mechanicId).orElse(null);
			System.out.println(mechanic);
			long ratingSum = feedbackRepository.sumOfRatings(mechanicId);
			if(ratingSum == 0)
			{
				return "Rating Doesn't exist for the Mechanic with mechanicId:"+mechanicId;
			}
			
			long ratingCount = feedbackRepository.countOfRatings(mechanicId);
			
			double averageRating = ratingSum/ratingCount;
			if(averageRating < 2)
			{
				mechanic.setAllowed(false);
				mechanicRepository.save(mechanic);
				return "Mechanic Blocked Due to Less Average Rating";
			}
			else
				return "Mechanic allowed, Mechanic has average rating above threshold";
		}
			return "Mechanic Doesn't Exist with MechanicId:"+mechanicId;
	}
}
