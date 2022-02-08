package com.assignment.service.users;

import com.assignment.dto.users.AffiliateDto;
import com.assignment.dto.users.EmployeeDto;
import com.assignment.dto.users.LoginDto;
import com.assignment.dto.users.UserDto;
import com.assignment.entity.products.Product;
import com.assignment.entity.users.Affiliate;
import com.assignment.entity.users.Employee;
import com.assignment.entity.users.User;
import com.assignment.exceptions.BadRequestException;
import com.assignment.helpers.ActionResponse;
import com.assignment.helpers.ErrorConstants;
import com.assignment.helpers.RecordHolder;
import com.assignment.helpers.utils.JwtUtils;
import com.assignment.repositories.UserRepository;
import com.assignment.service.AuditingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImplV1 implements IUserService{

    private final UserRepository userRepository;
    private final AuditingService auditingService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    @Autowired
    public UserServiceImplV1(
            UserRepository userRepository,
            AuditingService auditingService,
            BCryptPasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.auditingService = auditingService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }


    @Override
    public ActionResponse createUser(UserDto userDto) {
        userDto.isValid();
        userRepository.findByUsername(userDto.getUsername()).ifPresent(user -> {
            throw new BadRequestException(String.format(ErrorConstants.ENTITY_ALREADY_EXISTS, User.class.getSimpleName(), "Username"));
        });
        userRepository.findByEmail(userDto.getEmail()).ifPresent(user -> {
            throw new BadRequestException(String.format(ErrorConstants.ENTITY_ALREADY_EXISTS, User.class.getSimpleName(), "Email"));
        });
        User user = userDto.toUserEntity();
        auditingService.stamp(user);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        try {
            user.setEmployee(userDto.getEmployee() != null ? toEmployee(userDto.getEmployee()): null);
            user.setAffiliate(userDto.getAffiliate() != null ? toAffiliate(userDto.getAffiliate()) : null);
        } catch (ParseException exception) {
            exception.printStackTrace();
        }
        return ActionResponse.builder().resourceId(userRepository.save(user).getId()).build();
    }

    @Override
    public UserDto authenticateUser(LoginDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword())
        );
        User user = userRepository.findByUsername(loginDto.getUsername()).orElseThrow(() -> new UsernameNotFoundException(
                String.format(ErrorConstants.ENTITY_DOES_NOT_EXISTS, Product.class.getSimpleName(), "id")));
        String token = jwtUtils.generateToken(loginDto.getUsername());
        UserDto userDto = user.toUserDto();
        userDto.setToken(token);
        return userDto;
    }

    @Override
    public RecordHolder<List<UserDto>> getUsers() {
        List<UserDto> users = userRepository.findAll().stream()
                .map(User::toUserDto)
                .collect(Collectors.toList());
        return new RecordHolder(users.size(), users);
    }

    private Employee toEmployee(EmployeeDto employeeDto) throws ParseException {
        Employee employee = Employee.builder()
                .employeeNumber(employeeDto.getEmployeeNumber())
                .lineManager(employeeDto.getLineManager())
                .employmentDate(formatter.parse(employeeDto.getEmploymentDate()))
                .contractExpiryDate(formatter.parse(employeeDto.getContractExpiryDate()))
                .build();
        auditingService.stamp(employee);
        return employee;
    }

    private Affiliate toAffiliate(AffiliateDto affiliateDto) throws ParseException {
        Affiliate affiliate = Affiliate.builder()
                .affiliateNumber(affiliateDto.getAffiliateNumber())
                .affiliateCommission(affiliateDto.getAffiliateCommission())
                .lineManager(affiliateDto.getLineManager())
                .contractStartDate(formatter.parse(affiliateDto.getContractStartDate()))
                .contractEndDate(formatter.parse(affiliateDto.getContractEndDate()))
                .build();
        auditingService.stamp(affiliate);
        return affiliate;
    }
}
