package com.dhbw.secure_pic.coder;

import com.dhbw.secure_pic.auxiliary.exceptions.IllegalLengthException;
import com.dhbw.secure_pic.auxiliary.exceptions.IllegalTypeException;
import com.dhbw.secure_pic.auxiliary.exceptions.InsufficientCapacityException;
import com.dhbw.secure_pic.data.ContainerImage;
import com.dhbw.secure_pic.data.Information;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

// TODO comment

/**
 * @author Frederik Wolter
 */
public class TestLeastSignificantBit {

    @Test
    public void testEncodeDecode() throws IllegalTypeException, InsufficientCapacityException, IllegalLengthException {
        // send
        String in = "This is a Test 123456öäü?0§";
        Information info = Information.getInformationFromString(in);
        ContainerImage image = new ContainerImage("test/com/dhbw/secure_pic/data/PNG_Test.png");

        LeastSignificantBit coder = new LeastSignificantBit(image);
        ContainerImage encoded = coder.encode(info);
        // encoded.copyToClipboard();

        // receive
        LeastSignificantBit coder2 = new LeastSignificantBit(encoded);
        Information info2 = coder2.decode();

        String out = info2.toText();

        assertEquals(in, out);
    }

    @Test
    public void testEncodeDecodeFileText() throws IllegalTypeException, InsufficientCapacityException, IOException, IllegalLengthException {
        // send
        String in = "This is a Test 123456öäü?0§";
        Information info = Information.getInformationFromString(in);
        ContainerImage image = new ContainerImage("test/com/dhbw/secure_pic/data/PNG_Test.png");

        LeastSignificantBit coder = new LeastSignificantBit(image);
        ContainerImage encoded = coder.encode(info);
        encoded.exportImg("./test/com/dhbw/secure_pic/encoded_test.png");

        // receive
        ContainerImage image2 = new ContainerImage("./test/com/dhbw/secure_pic/encoded_test.png");
        LeastSignificantBit coder2 = new LeastSignificantBit(image2);
        Information info2 = coder2.decode();

        assertNull(info2.toImage());
        String out = info2.toText();

        assertEquals(in, out);
    }

    @Test
    public void testEncodeDecodeFileIMG() throws IllegalTypeException, InsufficientCapacityException, IOException, IllegalLengthException {
        // send
        Information info = Information.getInformationFromImage("test/com/dhbw/secure_pic/data/cat_small.jpg");
        ContainerImage image = new ContainerImage("test/com/dhbw/secure_pic/data/PNG_Test.png");

        LeastSignificantBit coder = new LeastSignificantBit(image);
        ContainerImage encoded = coder.encode(info);
        encoded.exportImg("./test/com/dhbw/secure_pic/encoded_test.png");

        // receive
        ContainerImage image2 = new ContainerImage("./test/com/dhbw/secure_pic/encoded_test.png");
        LeastSignificantBit coder2 = new LeastSignificantBit(image2);
        Information info2 = coder2.decode();

        assertNull(info2.toText());
        info2.copyToClipboard();

        // TODO automatic test?
    }

    @Test
    public void testGetCapacity() throws IllegalTypeException {
        ContainerImage image = new ContainerImage("test/com/dhbw/secure_pic/data/PNG_Test.png");
        LeastSignificantBit coder = new LeastSignificantBit(image);

        assertEquals(123456, coder.getCapacity());

        // part 2
        Information info = Information.getInformationFromImage("test/com/dhbw/secure_pic/data/PNG_Test.png");
        assertThrows(InsufficientCapacityException.class, () -> coder.encode(info));
    }
}