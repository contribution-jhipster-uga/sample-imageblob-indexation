package sample.lazyblob.indexation.imageAI;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Indexation {
    public static String parseMetadata(String filename){
        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        String result = new String();
        try{
            InputStream stream = new BufferedInputStream(new FileInputStream(filename));
            parser.parse(stream,handler,metadata);
            result = metadata.toString();
            System.out.println(result);
        } catch (IOException | SAXException | TikaException e) {
            e.printStackTrace();
        }
        return result;
    }
}
