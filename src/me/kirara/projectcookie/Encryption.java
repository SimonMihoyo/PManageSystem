// PManageSystem Encryption.java
// Copyright (C) 2025 SimonMihoyo
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.

package me.kirara.projectcookie;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;

public class Encryption
{
    // AES密钥长度（128、192或256位）
    private static final int KEY_SIZE = 128;
    // 加密算法/工作模式/填充方式
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    /**
     * 生成随机密钥
     * @return 密钥
     */
    public static String generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(KEY_SIZE);
        SecretKey secretKey = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /**
     * 生成随机初始化向量(IV)
     * @return IV
     */
    public static String generateIV() {
        byte[] iv = new byte[16]; // AES块大小为16字节
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        return Base64.getEncoder().encodeToString(iv);
    }

    /**
     * 使用AES/CBC/PKCS5Padding加密
     * @param plainText 明文
     * @param key 密钥
     * @param iv IV
     * @return 密文
     * @throws NoSuchPaddingException
     * @throws  NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String encrypt(String plainText, String key, String iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // 转换密钥
        byte[] keyBytes = Base64.getDecoder().decode(key);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        // 转换IV
        byte[] ivBytes = Base64.getDecoder().decode(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        // 加密
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * 使用AES/CBC/PKCS5Padding解密
     * @param cipherText 密文
     * @param key 密钥
     * @param iv IV
     * @return 明文
     */
    public static String decrypt(String cipherText, String key, String iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        // 转换密钥
        byte[] keyBytes = Base64.getDecoder().decode(key);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        // 转换IV
        byte[] ivBytes = Base64.getDecoder().decode(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);

        // 解密
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(cipherText));

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException{
        Scanner input = new Scanner(System.in);
        System.out.println("请输入明文：");
        String plainText = input.nextLine();
        String key = generateKey();
        String iv = generateIV();
        // 调试用，实际使用时请注释掉
        // Begin
        System.out.println("密钥：" + key);
        System.out.println("IV：" + iv);
        // End
        try {
            String cipherText = encrypt(plainText, key, iv);
            System.out.println("加密后密文：" + cipherText);
            String decryptedText = decrypt(cipherText, key, iv);
            System.out.println("解密后明文：" + decryptedText);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                 InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
}