package engine.utils;

import java.io.*;
import java.util.Scanner;

public class FileUtils {

    //    public static String loadAsString(String path) {
//
//        StringBuilder result = new StringBuilder();
//
//        try {
//
//            //String line = "";
//            File myObj = new File(path);
//            Scanner myReader = new Scanner(myObj);
//            while (myReader.hasNextLine()) {
//                String data = myReader.nextLine();
//                result.append(data).append("\n");
//            }
//            myReader.close();
//        } catch (FileNotFoundException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
//        return result.toString();
//    }
//
//    }
    public static String loadAsString(String path) {
        StringBuilder result = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Class.class.getResourceAsStream(path)))) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Couldn't find the file at " + path);

        }
        return result.toString();
    }
}
