package com.company;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.BitSet;

public class Main {

    public static final int M = 32;
    private static  BitSet register = new BitSet(M);

    public static void main(String[] args) {
        register.set(0, M);
        try (FileInputStream fIn = new FileInputStream("text.txt");
             FileOutputStream fOut = new FileOutputStream("encryptedText.txt")) {
            byte[] buffer = new byte[fIn.available()];
            fIn.read(buffer, 0, fIn.available());

            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = (byte) (buffer[i] ^ generateKey());
            }

            fOut.write(buffer);
            System.out.println("Данные успешно закодированы");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        register.set(0, M);
        try (FileOutputStream fOut = new FileOutputStream("decryptedText.txt");
             FileInputStream fIn = new FileInputStream("encryptedText.txt")) {

            byte[] bufferEnc = new byte[fIn.available()];
            fIn.read(bufferEnc, 0, fIn.available());

            for (int i = 0; i < bufferEnc.length; i++) {
                bufferEnc[i] = (byte) (bufferEnc[i] ^ generateKey());
            }

            fOut.write(bufferEnc);
            System.out.println("Данные успешно декодированы");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static final byte generateKey() {
        BitSet resultKey = new BitSet(0);

        for (int i = 0; i < 8; i++) {
            resultKey.set(i, register.get(M - 1));
            BitSet tmp = (BitSet) register.clone();
            for (int j = 1; j <= M - 1; j++) {
                register.set(j, tmp.get(j - 1));
            }
            register.set(0, (register.get(31) ^ register.get(27) ^ register.get(26) ^ register.get(0)));
        }
        return resultKey.toByteArray()[0];
    }
}
