package kz.bitlab.G118security.service;

import kz.bitlab.G118security.dto.UserCreate;
import kz.bitlab.G118security.dto.UserUpdate;
import kz.bitlab.G118security.dto.UserView;
import kz.bitlab.G118security.mapper.UserMapper;
import kz.bitlab.G118security.model.Role;
import kz.bitlab.G118security.model.User;
import kz.bitlab.G118security.repository.RoleRepository;
import kz.bitlab.G118security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public String createUser(User newUser, String rePassword) {
        User user = userRepository.findByUsername(newUser.getUsername());
        if (user != null) {
            return "errorEmail";
        }

        if (!newUser.getPassword().equals(rePassword)) {
            return "errorPassword";
        }

        newUser.setPassword(passwordEncoder.encode(rePassword));
        Role userRole = roleRepository.findUserRole();
        newUser.setRoles(List.of(userRole));
        userRepository.save(newUser);
        return "success";
    }

    public UserView createUser(UserCreate userCreate) {
        User user = userRepository.findByUsername(userCreate.getUsername());
        if (user != null) {
            throw new RuntimeException(String.format("%s username is busy", userCreate.getUsername()));
        }

        if (!userCreate.getPassword().equals(userCreate.getRePassword())) {
            throw new RuntimeException("Incorrect passwords");
        }

        User newUser = UserMapper.INSTANCE.toEntity(userCreate);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        Role userRole = roleRepository.findUserRole();
        newUser.setRoles(List.of(userRole));
        newUser = userRepository.save(newUser);
        return UserMapper.INSTANCE.toView(newUser);
    }

    public String changePassword(String currentPassword, String newPassword, String reNewPassword) {
        if (!passwordEncoder.matches(currentPassword, getCurrentUser().getPassword())) {
            return "errorCurrentPassword";
        }

        if (!newPassword.equals(reNewPassword)) {
            return "errorNewPassword";
        }

        getCurrentUser().setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(getCurrentUser());
        return "success";
    }

    public void generateUserReport() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Users");
        List<User> users = userRepository.findAll();
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("ID");

        cell = row.createCell(1);
        cell.setCellValue("USERNAME");

        cell = row.createCell(2);
        cell.setCellValue("PASSWORD");

        cell = row.createCell(3);
        cell.setCellValue("FULL NAME");

        int rowNum = 1;
        int cellNum = 0;
        for (User user : users) {
            row = sheet.createRow(rowNum);
            cell = row.createCell(cellNum);
            cell.setCellValue(user.getId());
            cellNum++;
            cell = row.createCell(cellNum);
            cell.setCellValue(user.getUsername());
            cellNum++;
            cell = row.createCell(cellNum);
            cell.setCellValue(user.getPassword());
            cellNum++;
            cell = row.createCell(cellNum);
            cell.setCellValue(user.getFullName());
            rowNum++;
            cellNum = 0;
        }

        try (FileOutputStream file = new FileOutputStream("UsersResponse.xlsx")) {
            workbook.write(file);
        } finally {
            workbook.close();
        }
    }

    public void checkAndCreateUsers() throws IOException {
        log.info("Check and create user is started!");
        FileInputStream file = new FileInputStream("UsersRequest.xlsx");
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            Cell cell = row.getCell(0);
            String username = cell.getStringCellValue();
            User user = userRepository.findByUsername(username);
            if (user != null) {
                continue;
            }

            cell = row.getCell(1);
            String password = cell.getStringCellValue();

            cell = row.getCell(2);
            String fullName = cell.getStringCellValue();

            User newUser = userRepository.save(User.builder()
                    .username(username)
                    .password(password)
                    .fullName(fullName)
                    .build());
            log.info("New user with id: {}, username: {} is created", newUser.getId(), newUser.getUsername());
        }
    }

    public List<UserView> getUsers() {
        List<User> users = userRepository.findAll();
        return UserMapper.INSTANCE.toViewList(users);
    }

    public UserView changePassword(UserUpdate userUpdate) {
        User user = userRepository.findById(userUpdate.getId()).orElseThrow();

        if (!passwordEncoder.matches(userUpdate.getCurrentPassword(), user.getPassword())) {
            return null;
        }

        if (!Objects.equals(userUpdate.getNewPassword(), userUpdate.getReNewPassword())) {
            return null;
        }

        user.setPassword(passwordEncoder.encode(userUpdate.getNewPassword()));

        userRepository.save(user);
        return UserMapper.INSTANCE.toView(user);
    }
}
