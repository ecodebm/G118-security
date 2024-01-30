package kz.bitlab.G118security;

import kz.bitlab.G118security.model.User;
import kz.bitlab.G118security.repository.UserRepository;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients
public class G118SecurityApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(G118SecurityApplication.class, args);
	}

}
