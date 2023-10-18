package citytech.global.usecase.csv;

import citytech.global.repository.User;
import citytech.global.repository.UserRepository;
import jakarta.inject.Inject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DownloadToCsvUseCase {

    private UserRepository userRepository;

    @Inject
    public DownloadToCsvUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute() {
        try {
            List<User> users = userRepository.findAll();

            String csvDirectoryPath = "csv";

            String csvFileName = csvDirectoryPath + "/user_data.csv";
            try (FileWriter fileWriter = new FileWriter(csvFileName)) {

                fileWriter.append("User ID,Username,Email\n");

                for (User user : users) {
                    fileWriter.append(user.getUserId() + "," + user.getFirstName() + "," + user.getUserType() + "," + user.getEmail() + "\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
