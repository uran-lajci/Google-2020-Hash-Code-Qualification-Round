import shared.Data;
import shared.FileReader;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception{
        FileReader reader = new FileReader();
        Data data = reader.readFile(new File("./b_read_on.txt"));
    }
}
