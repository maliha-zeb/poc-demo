package com.example.poc;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.FileComponent;

public class PGPExample extends RouteBuilder{

        @Override
    public void configure() throws Exception {
        // Define the file component
        FileComponent fileComponent = new FileComponent();

        // Define the PGP settings
        Properties pgpProperties = new Properties();
        pgpProperties.load(new FileInputStream("pgp.properties"));

        // Encrypt the file using PGP
        from("file:/input/folder?noop=true")
                .to("direct:encrypt");

        // Define the encrypt route
        from("direct:encrypt")
                .process(new PGPEncryptProcessor(pgpProperties));

        // Decrypt the file using PGP
        from("file:/output/folder?noop=true")
                .to("direct:decrypt");

        // Define the decrypt route
        from("direct:decrypt")
                .process(new PGPDecryptProcessor(pgpProperties));
    }
}
