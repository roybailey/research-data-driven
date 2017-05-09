package me.roybailey.data.schema;


import com.fasterxml.jackson.dataformat.avro.AvroMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.assertj.core.api.JUnitBDDSoftAssertions;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.File;
import java.io.IOException;

@Slf4j
public class UserTest {

    @Rule
    public TestName name = new TestName();

    @Rule
    public final JUnitBDDSoftAssertions softly = new JUnitBDDSoftAssertions();

    @Before
    public void banner() {
        log.info("------------------------------------------------------------");
        log.info(this.getClass().getSimpleName() + "." + name.getMethodName() + "()");
        log.info("------------------------------------------------------------");
    }


    @Test
    public void testDtoBuilders() throws IOException {

        UserDto user1 = new UserDto();
        user1.setName("Alyssa");
        user1.setFavoriteNumber(256);
        // Leave favorite color null

        // Alternate constructor
        UserDto user2 = new UserDto("Ben", 7, "red");

        // Construct via builder
        UserDto user3 = UserDto.newBuilder()
                .setName("Charlie")
                .setFavoriteColor("blue")
                .setFavoriteNumber(null)
                .build();

        File file = new File("users.avro");
        // Serialize user1, user2 and user3 to disk
        DatumWriter<UserDto> userDatumWriter = new SpecificDatumWriter<UserDto>(UserDto.class);
        DataFileWriter<UserDto> dataFileWriter = new DataFileWriter<UserDto>(userDatumWriter);
        dataFileWriter.create(user1.getSchema(), file);
        dataFileWriter.append(user1);
        dataFileWriter.append(user2);
        dataFileWriter.append(user3);
        dataFileWriter.close();

        // Deserialize Users from disk
        DatumReader<UserDto> userDatumReader = new SpecificDatumReader<UserDto>(UserDto.class);
        DataFileReader<UserDto> dataFileReader = new DataFileReader<UserDto>(file, userDatumReader);
        UserDto user = null;
        while (dataFileReader.hasNext()) {
            // Reuse user object by passing it to next(). This saves us from
            // allocating and garbage collecting many objects for files with
            // many items.
            user = dataFileReader.next(user);
            System.out.println(user);
        }
    }


    @Ignore("~Cant get Avro to serialize through Jackson")
    @Test
    public void testJacksonAvroCapability() throws IOException {

        UserDto user1 = new UserDto();
        user1.setName("Alyssa");
        user1.setFavoriteNumber(256);

        AvroMapper mapper = new AvroMapper();
        System.out.println(mapper.writerFor(UserDto.class).writeValueAsString(user1));
    }
}
