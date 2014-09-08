import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        File file = new File("totot");
        List<String> lines = Files.readAllLines(file.toPath());
        lines.set(5, "something");
        Files.write(file.toPath(), lines);
    }
}
