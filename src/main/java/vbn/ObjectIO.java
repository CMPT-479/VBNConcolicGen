package vbn;

import java.io.*;

public class ObjectIO {

    public static void writeObjectToFile(Object serializableObject, String fileName) {
        try {
            File currentDirectory = new File(System.getProperty("user.dir"));
            File file = new File(currentDirectory, fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(serializableObject);
            System.out.println("Wrote object to " + file.getAbsolutePath());
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object readObjectFromFile(String fileName) {
        try {
            File currentDirectory = new File(System.getProperty("user.dir"));
            File file = new File(currentDirectory, fileName);
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            // read the object from the file
            Object obj = in.readObject();
            in.close();
            fileIn.close();
            return obj;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean deleteFile(String fileName) {
        File currentDirectory = new File(System.getProperty("user.dir"));
        File file = new File(currentDirectory, fileName);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
}
