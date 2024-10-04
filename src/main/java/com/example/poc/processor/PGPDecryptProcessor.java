package com.example.poc.processor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import org.apache.camel.Exchange;

public class PGPDecryptProcessor implements org.apache.camel.Processor{
    private Properties pgpProperties;

    public PGPDecryptProcessor(Properties pgpProperties) {
        this.pgpProperties = pgpProperties;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        // Get the input file
        File inputFile = exchange.getIn().getBody(File.class);

        // Read the PGP encrypted data from the file
        FileInputStream fis = new FileInputStream("/output/folder/encrypted_file.pgp");
        PGPUtil load = new PGPUtil();
        PGPEncryptedData encryptedData = load.load(fis);

        // Create a PGP decrypted data generator
        PGPEncryptedDataGenerator decryptedDataGenerator = new PGPEncryptedDataGenerator(
                new JcaPGPObjectFactory(),
                new PGPCompressedData(),
                new PGPEncryptedData(),
                new PGPSignatureList(),
                new PGPSignature()
        );

        // Set the PGP key and passphrase
        decryptedDataGenerator.setProvider("BC");
        decryptedDataGenerator.setKeyStore(pgpProperties.getProperty("pgp.keyring"));
        decryptedDataGenerator.setPassphrase(pgpProperties.getProperty("pgp.passphrase"));

        // Generate the PGP decrypted data
        PGPCompressedData compressedData = decryptedDataGenerator.generate();

        // Write the decrypted data to a file
        FileOutputStream fos = new FileOutputStream("/output/folder/decrypted_file.txt");
        compressedData.write(fos);
        fos.close();
    }

}
