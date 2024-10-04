package com.example.poc.processor;

import java.io.FileOutputStream;
import java.util.Properties;

import org.apache.camel.Exchange;

public class PGPEncryptProcessor implements org.apache.camel.Processor{
    private Properties pgpProperties;

    public PGPEncryptProcessor(Properties pgpProperties) {
        this.pgpProperties = pgpProperties;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        // Get the input file
        File inputFile = exchange.getIn().getBody(File.class);

        // Create a PGP encrypted data generator
        PGPEncryptedDataGenerator encryptedDataGenerator = new PGPEncryptedDataGenerator(
                new JcaPGPObjectFactory(),
                new PGPCompressedData(),
                new PGPEncryptedData(),
                new PGPSignatureList(),
                new PGPSignature()
        );

        // Set the PGP key and passphrase
        encryptedDataGenerator.setProvider("BC");
        encryptedDataGenerator.setKeyStore(pgpProperties.getProperty("pgp.keyring"));
        encryptedDataGenerator.setPassphrase(pgpProperties.getProperty("pgp.passphrase"));

        // Generate the PGP encrypted data
        PGPEncryptedData encryptedData = encryptedDataGenerator.generate();

        // Write the PGP encrypted data to a file
        FileOutputStream fos = new FileOutputStream("/output/folder/encrypted_file.pgp");
        encryptedData.write(fos);
        fos.close();
    }


}
