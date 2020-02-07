package sample.lazyblob.indexation;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Indexation {
    public static String parseMetadata(String filename) {
        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        String result = new String();
        try {
            InputStream stream = new BufferedInputStream(new FileInputStream(filename));
            parser.parse(stream, handler, metadata);
            result = metadata.toString();
            System.out.println(result);
        } catch (IOException | SAXException | TikaException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String parseTextFromImage(String filename) throws IOException {
        InputStream pdf = Files.newInputStream(Paths.get(filename));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        TikaConfig config = TikaConfig.getDefaultConfig();
// TikaConfig fromFile = new TikaConfig("/path/to/file");
        BodyContentHandler handler = new BodyContentHandler(out);
        Parser parser = new AutoDetectParser(config);
        Metadata meta = new Metadata();
        ParseContext parsecontext = new ParseContext();
        PDFParserConfig pdfConfig = new PDFParserConfig();
        pdfConfig.setExtractInlineImages(true);

        TesseractOCRConfig tesserConfig = new TesseractOCRConfig();
        tesserConfig.setLanguage("fra");
        tesserConfig.setTesseractPath(tesserConfig.getTesseractPath());

        parsecontext.set(Parser.class, parser);
        parsecontext.set(PDFParserConfig.class, pdfConfig);
        parsecontext.set(TesseractOCRConfig.class, tesserConfig);
        try {
            parser.parse(pdf, handler, meta, parsecontext);

        } catch (SAXException | TikaException | IOException e) {
            e.printStackTrace();
        }
        return new String(out.toByteArray(), Charset.defaultCharset());
    }


    public static String imageAI(String pathImg) throws Exception {

        String script = "src\\main\\java\\sample\\lazyblob\\indexation\\imageAI\\imageAI.py ";
        String commandToExecute = "python " + script + pathImg;
        String res = "";
        int error = -1;

        try {
            System.out.println(commandToExecute);

            Process pr = Runtime.getRuntime().exec(commandToExecute);
            error = pr.waitFor();
            InputStream stdout = pr.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stdout, StandardCharsets.UTF_8));
            String line;

            try {
                while ((line = reader.readLine()) != null) {
                    if(!line.contains("tracking")) {
                        res = res + line + " ";
                    }
                }
            } catch (IOException e) {
                System.out.println("Exception in reading output" + e.toString());
            }
            System.out.println(error);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(error != 0) {
            throw new Exception("Error during script execution.");
        }

        return res;
    }

    public static String createImagefromByteArray(byte[] data) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        BufferedImage bImage2 = ImageIO.read(bis);
        File f = new File("src/main/java/sample/lazyblob/indexation/tampon.jpg");
        ImageIO.write(bImage2, "jpg",f);
        System.out.println("image created");
        return f.getPath();
    }
}
