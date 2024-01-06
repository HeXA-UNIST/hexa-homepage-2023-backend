package pro.hexa.backend.config;

import java.security.Key;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;

@Converter
public class StringCryptoConverter implements AttributeConverter<String, String> {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    @Value("${hexa-page-jwt-secret-key}")
    private String secretKey;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }

        Key key = new SecretKeySpec(secretKey.getBytes(), "AES");
        IvParameterSpec secretIv = getSecretIv();
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, secretIv);
            byte[] encrypted = Base64.getEncoder().encode(cipher.doFinal(attribute.getBytes()));

            return new String(encrypted);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if ("null".equals(dbData) || dbData == null) {
            return null;
        }

        Key key = new SecretKeySpec(secretKey.getBytes(), "AES");

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, getSecretIv());

            return decodeFromData(dbData, cipher);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private String decodeFromData(String dbData, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException {
        try {
            final byte[] decode = Base64.getDecoder().decode(dbData);
            return new String(cipher.doFinal(decode));
        } catch (IllegalArgumentException e) {
            return dbData;
        }
    }

    private IvParameterSpec getSecretIv() {
        byte[] iv = secretKey.substring(0,16).getBytes();
        return new IvParameterSpec(iv);
    }
}
