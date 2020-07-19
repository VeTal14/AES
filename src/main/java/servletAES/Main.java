package servletAES;

import javax.crypto.*;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@WebServlet(urlPatterns = "/aes",
initParams = {
        @WebInitParam(name = "encode", value = "true")
})
public class Main  extends HttpServlet {

    private String msg;
    byte[] encodedBytes;
    byte [] decodedBytes;
    SecretKey key;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        msg = req.getParameter("msg");
        try {
            encrypt();
            decrypt();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        PrintWriter keyWriter = new PrintWriter("Keys.txt","UTF-8");
        keyWriter.write(key.toString());
        keyWriter.close();


        req.setAttribute("text", msg);
        req.setAttribute("dec", new String(decodedBytes));
        req.setAttribute("encod", encodedBytes);
        req.getRequestDispatcher("main.jsp").forward(req, resp);
    }
    public void encrypt() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        Cipher cipher = Cipher.getInstance("AES");
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        key = keyGenerator.generateKey();
        cipher.init(Cipher.ENCRYPT_MODE, key);

        encodedBytes = Base64.getEncoder().encode(cipher.doFinal(msg.getBytes()));

        PrintWriter writer = new PrintWriter("Encrypted.txt", "UTF-8");

        //fileWriter.write(decodedValue);
        for(byte b : encodedBytes){
            writer.write(b);
        }

        writer.write("\n");
        writer.close();


    }
    public void decrypt() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        decodedBytes = cipher.doFinal(Base64.getDecoder().decode(encodedBytes));

        PrintWriter writer = new PrintWriter("Decrypted.txt", "UTF-8");

        //fileWriter.write(decodedValue);
        for(byte b : decodedBytes){
            writer.write((char)b);
        }

        writer.write("\n");
        writer.close();

    }

}
