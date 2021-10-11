//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package nel.marco;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Password extends JFrame {
    final String DECRYPT = "./decrypted-v2.txt";
    final String ENCRYPT = "./encrypted-v2.txt";
    String password = "10910";

    public Password() throws IOException, InterruptedException {
        System.setProperty("headless", "true");
        File file = new File(DECRYPT);
        File file2 = new File(ENCRYPT);

        if ((!file.isFile() || !file.exists()) && (!file2.isFile() || !file2.exists())) {
            file.createNewFile();
            return;
        }

        if ((new File(DECRYPT)).exists()) {
            this.encryptFile();
        } else {
            this.decryptFile();
        }

    }

    private void encryptFile() {
        File file = new File(DECRYPT);
        if (file.isFile() && file.exists()) {
            String encrypt = EncryptUtil.encrypt(FileHandlerUtil.readFile(DECRYPT).toString());
            FileHandlerUtil.writeFile(ENCRYPT, encrypt);
        }
        new File(DECRYPT).delete();

        System.exit(0);
    }

    private void decryptFile() throws InterruptedException {

        if (!this.password.equals(JOptionPane.showInputDialog("enter password"))) {
            JOptionPane.showMessageDialog(this, "Wrong password!");
            System.exit(0);
        }

        String decrypt = EncryptUtil.decrypt(FileHandlerUtil.readFile(ENCRYPT).toString());
        FileHandlerUtil.writeFile(DECRYPT, decrypt);
        new File(ENCRYPT).delete();

        JOptionPane.showMessageDialog(this, "File will auto encrypt in 1 min!");
        TimeUnit.SECONDS.sleep(60);

        encryptFile();

    }


    public static void main(String[] args) {
        try {
            new Password();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }
}
