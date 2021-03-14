import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception{
        FileReader reader = new FileReader();
        Data data = reader.readFile(new File("./b_read_on.txt"));
        System.out.println(data.toString());
    }
}
